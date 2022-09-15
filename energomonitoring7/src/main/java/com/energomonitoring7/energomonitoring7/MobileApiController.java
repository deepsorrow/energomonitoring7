package com.energomonitoring7.energomonitoring7;

import com.energomonitoring7.energomonitoring7.apiUtils.ApiUtils;
import com.energomonitoring7.energomonitoring7.domain.*;
import com.energomonitoring7.energomonitoring7.domain.arshin.*;
import com.energomonitoring7.energomonitoring7.repos.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;

@RestController
@RequestMapping("/api/v1")
public class MobileApiController {

    @Autowired
    ResultDataRepo resultDataRepo;
    @Autowired
    MobileDataBundleRepo mobileDataBundleRepo;
    @Autowired
    ClientInfoRepo clientInfoRepo;
    @Autowired
    ServingOrganizationsRepo servingOrganizationsRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    OtherInfoRepo otherInfoRepo;
    @Autowired
    FlowTransducerCheckLengthResultsRepo flowTransducerCheckLengthResultsRepo;
    @Autowired
    RefDocRepo refDocRepo;

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @GetMapping(path = "/getAvailableClientInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<ClientInfo> getAvailableClientInfo() {
        ArrayList<ClientInfo> clientInfos = new ArrayList<>();
        for (MobileDataBundle mobileDataBundle : mobileDataBundleRepo.findAll()) {
            ClientInfo clientInfo = clientInfoRepo.findById(mobileDataBundle.clientId);
            clientInfo.dataId = mobileDataBundle.id;
            clientInfos.add(clientInfo);
        }

        return clientInfos;
    }

    @GetMapping(path = "/getAllAgreements", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<ClientInfo> getAllAgreements() {
        ArrayList<ClientInfo> clientInfos = new ArrayList<>();
        clientInfoRepo.findAll().forEach(clientInfos::add);

        return clientInfos;
    }

    @GetMapping(path = "/getDetailedClientBundle", produces = MediaType.APPLICATION_JSON_VALUE)
    public MobileDataBundle getDetailedClientBundle(@RequestParam int id) {
        Optional<MobileDataBundle> optionalData = mobileDataBundleRepo.findById(id);
        if (optionalData.isPresent()) {
            MobileDataBundle mobileDataBundle = optionalData.get();
            mobileDataBundle.setClientInfo(clientInfoRepo.findById(mobileDataBundle.clientId));
            mobileDataBundle.organizationInfo = servingOrganizationsRepo.findById(mobileDataBundle.organizationId).get();
            return mobileDataBundle;
        } else {
            throw new IllegalArgumentException("Not exists");
        }
    }

    @PostMapping(path = "/authorize", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorizeResponse authorize(@RequestBody AuthorizeBody body) {
        UserData user = userRepo.getByLoginAndPassword(body.getLogin(), body.getPassword());
        return new AuthorizeResponse(user != null, user);
    }

    @GetMapping(path = "/getDueDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public DueDateResponse getDueDate(@RequestParam String deviceName, @RequestParam String arshinId) throws IOException {
        Gson gson = new Gson(); // Or use new GsonBuilder().create();

        HttpGet request = new HttpGet("https://fgis.gost.ru/fundmetrology/cm/xcdb/vri/select?fq=" + arshinId + "&q=*&fl=mi.mitnumber,valid_date");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);

                ModelDetailed objResult = gson.fromJson(result, ModelDetailed.class);

                int count = objResult.getResponse().getNumFound();
                if (count == 0)
                    return new DueDateResponse(false, "Не найдено");
                else if (count > 1) {
                    return new DueDateResponse(false, "Найдено " + count);
                } else {
                    String validDate = objResult.getResponse().getDocs().get(0).getValidDate();
                    validDate = validDate.replace("T", " ");
                    validDate = validDate.replace("Z", "");
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(validDate);
                    String formattedDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
                    return new DueDateResponse(true, formattedDate);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new DueDateResponse(false, "");
    }

    @PostMapping(path = "/sendResults", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean sendResults(@RequestBody ResultData resultData) {
        //ApiUtils.resetDeviceIds(resultData);

        resultData.otherInfo.date = new Date(); // set current date
        otherInfoRepo.save(resultData.otherInfo);
        if (resultData.checkLengthResults != null) {
            flowTransducerCheckLengthResultsRepo.saveAll(resultData.checkLengthResults);
        }

        resultDataRepo.save(resultData);
        // create new instance of docx template
        Docx docx = new Docx("template2.docx");

        // set the variable pattern. In this example the pattern is as follows: #{variableName}
        docx.setVariablePattern(new VariablePattern("${", "}"));

        // find all variables satisfying the pattern #{...}
        List<String> findVariables = docx.findVariables();

        Date today = new Date();

        // prepare map of variables for template
        Variables var = new Variables();
        var.addTextVariable(new TextVariable("${ServeCompany}", resultData.organizationInfo.getOrganizationName()));
        var.addTextVariable(new TextVariable("${ServeCompanyAddress}", resultData.organizationInfo.getAddress()));
        var.addTextVariable(new TextVariable("${Chief}",       resultData.organizationInfo.chiefName));
        var.addTextVariable(new TextVariable("${Day}",         new SimpleDateFormat("dd").format(today)));
        var.addTextVariable(new TextVariable("${Month}",       ApiUtils.getMonthNameByNumber(today.getMonth())));
        var.addTextVariable(new TextVariable("${Year}",        new SimpleDateFormat("yy").format(today)));
        var.addTextVariable(new TextVariable("${CompanyName}",     resultData.clientInfo.getName()));
        var.addTextVariable(new TextVariable("${AgreementNumber}", resultData.clientInfo.getAgreementNumber()));
        var.addTextVariable(new TextVariable("${Address}",         resultData.clientInfo.getAddressUUTE()));

        int maxRows = 19;
        int lastRow = ApiUtils.fillDeviceTemperatureCounters(maxRows, resultData, var);
        lastRow = ApiUtils.fillDeviceFlowTransducers(maxRows, resultData, var, lastRow);
        lastRow = ApiUtils.fillDeviceTemperatureTransducers(maxRows, resultData, var, lastRow);
        lastRow = ApiUtils.fillDevicePressureTransducers(maxRows, resultData, var, lastRow);
        ApiUtils.fillImpulseWeights(resultData, var);
        ApiUtils.fillSealNumbersEmpty(var, maxRows);
        ApiUtils.fillRestEmpty(var, lastRow, maxRows);

        String username = userRepo.findById(mobileDataBundleRepo.findById(resultData.otherInfo.dataId).get().userId).get().getName();
        var.addTextVariable(new TextVariable("${User}", username));
        // fill template by given map of variables
        docx.fillTemplate(var);

        // save filled document
        boolean success = new File(String.format("reports/%1d/", resultData.otherInfo.dataId)).mkdirs();
//        if(!success) {
//            System.out.println("Couldn't create new directory!");
//            return false;
//        }
        docx.save(String.format("reports/%1d/report.docx", resultData.otherInfo.dataId));

        return true;
    }

    @GetMapping(path = "/refDocsWithoutData", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RefDoc> refDocsWithoutData(){
        if (refDocRepo.findById(0).isEmpty())
            ApiUtils.initRefDocs(refDocRepo);

        List<RefDoc> result = new ArrayList<>();
        refDocRepo.findAll().iterator().forEachRemaining(result::add);

        return result;
    }

    @GetMapping(path = "/refDocById", produces = MediaType.APPLICATION_JSON_VALUE)
    RefDoc getRefDocById(@RequestParam int id) throws IOException {
        return ApiUtils.getRefDocData(id, refDocRepo);
    }

    @GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean ping()  {
        return true;
    }
}