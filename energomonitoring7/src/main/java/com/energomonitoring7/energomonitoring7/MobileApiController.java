package com.energomonitoring7.energomonitoring7;

import com.energomonitoring7.energomonitoring7.domain.*;
import com.energomonitoring7.energomonitoring7.domain.arshin.*;
import com.energomonitoring7.energomonitoring7.repos.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.w3c.dom.Text;
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


    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @GetMapping(path = "/getAvailableClientInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<ClientInfo> getAvailableClientInfo(@RequestParam int userId) {
        ArrayList<ClientInfo> clientInfos = new ArrayList<>();
        for (MobileDataBundle mobileDataBundle : mobileDataBundleRepo.getByUserIdAndCompleted(userId, false)) {
            ClientInfo clientInfo = clientInfoRepo.findById(mobileDataBundle.clientId);
            clientInfo.dataId = mobileDataBundle.id;
            clientInfos.add(clientInfo);
        }

        return clientInfos;
    }

    @GetMapping(path = "/getDetailedClientBundle", produces = MediaType.APPLICATION_JSON_VALUE)
    public MobileDataBundle getDetailedClientBundle(@RequestParam int id) {
        MobileDataBundle mobileDataBundle = mobileDataBundleRepo.findById(id).get();
        mobileDataBundle.setClientInfo(clientInfoRepo.findById(mobileDataBundle.clientId));
        mobileDataBundle.organizationInfo = servingOrganizationsRepo.findById(mobileDataBundle.organizationId).get();

        return mobileDataBundle;
    }

    @PostMapping(path = "/authorize", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorizeResponse authorize(@RequestBody AuthorizeBody body) {
        UserData user = userRepo.getByLoginAndPassword(body.getLogin(), body.getPassword());
        return new AuthorizeResponse(user != null, user);
    }

    @GetMapping(path = "/getDueDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public DueDateResponse getDueDate(@RequestParam String deviceName, @RequestParam String arshinId) throws IOException {

        Gson gson = new Gson(); // Or use new GsonBuilder().create();

        String parameters = URLEncoder.encode(deviceName + " " + arshinId, "UTF-8");
        HttpGet request = new HttpGet("https://fgis.gost.ru/fundmetrology/eapi/mit?search=" + parameters);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);

                ArshinResultResponse objResult = gson.fromJson(result, ArshinResultResponse.class);

                int count = objResult.getResult().getCount();
                if (count == 0)
                    return new DueDateResponse(false, "Не найдено");
                else if (count > 1) {
                    HashMap<String, String> variants = new HashMap<>();
                    for (Item item : objResult.getResult().getItems()) {
                        String variant = item.getNumber() + " " + item.getTitle() + " " + item.getManufactorer()
                                + " " + item.getNotation();
                        variants.put(item.getMit_id(), variant);
                    }

                    return new DueDateResponse(variants);
                } else {
                    String mitId = objResult.getResult().getItems().get(0).getMit_id();
                    return getDueDateByMit(mitId);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new DueDateResponse(false, "");
    }

    @GetMapping(path = "/getDueDateByMit", produces = MediaType.APPLICATION_JSON_VALUE)
    public DueDateResponse getDueDateByMit(@RequestParam String mitId) throws ParseException {
        Gson gson = new Gson();

        try {
            ModelDetailed objResult = new ModelDetailed();
            boolean result = requestDueDateByMit(mitId, objResult);
            if (!result) // 2nd try
                requestDueDateByMit(mitId, objResult);

            Mit mit = objResult.getMit();
            if (mit == null)
                return new DueDateResponse(false, "Нет срока");

            String validFor = mit.getValidFor();
            if (validFor == null || validFor.isEmpty())
                return new DueDateResponse(false, "Не указано");

            Date dueDate = new SimpleDateFormat("dd.MM.yyyy").parse(validFor);
            Date currentDate = new Date();
            if (currentDate.before(dueDate))
                return new DueDateResponse(true, validFor);
            else
                return new DueDateResponse(false, validFor);
        } catch (Exception e) {
            return new DueDateResponse(false, e.toString());
        }
    }

    @PostMapping(path = "/sendResults", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean sendResults(@RequestBody ResultData resultData) {
        resetDeviceIds(resultData);

        resultData.otherInfo.date = new Date(); // set current date
        otherInfoRepo.save(resultData.otherInfo);
        flowTransducerCheckLengthResultsRepo.saveAll(resultData.flowTransducerCheckLengthResults);

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
        var.addTextVariable(new TextVariable("${Month}",       getMonthNameByNumber(today.getMonth())));
        var.addTextVariable(new TextVariable("${Year}",        new SimpleDateFormat("yy").format(today)));
        var.addTextVariable(new TextVariable("${CompanyName}",     resultData.clientInfo.getName()));
        var.addTextVariable(new TextVariable("${AgreementNumber}", resultData.clientInfo.getAgreementNumber()));
        var.addTextVariable(new TextVariable("${Address}",         resultData.clientInfo.getAddressUUTE()));

        int maxRows = 19;
        int lastRow = fillDeviceTemperatureCounters(maxRows, resultData, var);
        lastRow = fillDeviceFlowTransducers(maxRows, resultData, var, lastRow);
        lastRow = fillDeviceTemperatureTransducers(maxRows, resultData, var, lastRow);
        lastRow = fillDevicePressureTransducers(maxRows, resultData, var, lastRow);
        fillImpulseWeights(resultData, var);
        fillSealNumbersEmpty(var, maxRows);
        fillRestEmpty(var, lastRow, maxRows);

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

        MobileDataBundle mobileDataBundle = mobileDataBundleRepo.findById(resultData.otherInfo.dataId).get();
        mobileDataBundle.completed = true;
        mobileDataBundleRepo.save(mobileDataBundle);

        return true;
    }

    public void resetDeviceIds(ResultData resultData){
        for(DevicePressureTransducer devicePressureTransducer : resultData.devicePressureTransducers)
            devicePressureTransducer.resetId();
        for(DeviceTemperatureCounter deviceTemperatureCounter : resultData.deviceTemperatureCounters)
            deviceTemperatureCounter.resetId();
        for(DeviceFlowTransducer deviceFlowTransducer : resultData.deviceFlowTransducers)
            deviceFlowTransducer.resetId();
        for(DeviceTemperatureTransducer deviceTemperatureTransducer : resultData.deviceTemperatureTransducers)
            deviceTemperatureTransducer.resetId();
        for(DeviceCounter deviceCounter : resultData.deviceCounters)
            deviceCounter.resetId();

        for(FlowTransducerCheckLengthResult flowTransducerCheckLengthResult : resultData.flowTransducerCheckLengthResults)
            flowTransducerCheckLengthResult.resetId();

        resultData.otherInfo.setId(0);
    }

    private int fillDeviceTemperatureCounters(int maxRows, ResultData resultData, Variables var){
        int i = 0;
        for(; i < resultData.deviceTemperatureCounters.size(); ++i){
                DeviceTemperatureCounter device = resultData.deviceTemperatureCounters.get(i);

                var.addTextVariable(getDeviceName(i+1, device.getTypeId(), device.getDeviceName()));
                var.addTextVariable(getDeviceNumber(i+1, device.getDeviceNumber()));
                var.addTextVariable(getLastCheckDate(i+1, device.lastCheckDate));
                var.addTextVariable(getInstallationPlace(i+1, ""));
        }

        return i+1;
    }

    private int fillDeviceFlowTransducers(int maxRows, ResultData resultData, Variables var, int lastRow){
        for(int i = 0; i < resultData.deviceFlowTransducers.size(); ++i){
                DeviceFlowTransducer device = resultData.deviceFlowTransducers.get(i);

                var.addTextVariable(getDeviceName(lastRow, device.getTypeId(), device.getDeviceName()));
                var.addTextVariable(getDeviceNumber(lastRow, device.getDeviceNumber()));
                var.addTextVariable(getInstallationPlace(lastRow, device.installationPlace));
                var.addTextVariable(getLastCheckDate(lastRow, device.lastCheckDate));
            lastRow += 1;
        }

        return lastRow;
    }

    private int fillDeviceTemperatureTransducers(int maxRows, ResultData resultData, Variables var, int lastRow) {
        for(int i = 0; i < resultData.deviceTemperatureTransducers.size(); ++i){
                DeviceTemperatureTransducer device = resultData.deviceTemperatureTransducers.get(i);

                var.addTextVariable(getDeviceName(lastRow, device.getTypeId(), device.getDeviceName()));
                var.addTextVariable(getDeviceNumber(lastRow, device.getDeviceNumber()));
                var.addTextVariable(getInstallationPlace(lastRow, device.installationPlace));
                var.addTextVariable(getLastCheckDate(lastRow, device.lastCheckDate));
            lastRow += 1;
        }

        return lastRow;
    }

    private int fillDevicePressureTransducers(int maxRows, ResultData resultData, Variables var, int lastRow) {
        for(int i = 0; i <= maxRows - lastRow; ++i){
            boolean fillEmpty = i >= resultData.devicePressureTransducers.size();
            if(fillEmpty){
                if(i < lastRow)
                    continue;
                var.addTextVariable(getDeviceName(lastRow, 0, ""));
                var.addTextVariable(getDeviceNumber(lastRow, ""));
                var.addTextVariable(getInstallationPlace(lastRow, ""));
                var.addTextVariable(getLastCheckDate(lastRow, ""));
            } else {
                DevicePressureTransducer device = resultData.devicePressureTransducers.get(i);

                var.addTextVariable(getDeviceName(lastRow, device.getTypeId(), device.getDeviceName()));
                var.addTextVariable(getDeviceNumber(lastRow, device.getDeviceNumber()));
                var.addTextVariable(getInstallationPlace(lastRow, device.installationPlace));
                var.addTextVariable(getLastCheckDate(lastRow, device.lastCheckDate));
            }
            lastRow += 1;
        }

        return lastRow;
    }

    private void fillRestEmpty(Variables var, int lastRow, int maxRows){
        for( ; lastRow <= maxRows; lastRow++) {
            var.addTextVariable(getDeviceName(lastRow, 0, ""));
            var.addTextVariable(getDeviceNumber(lastRow, ""));
            var.addTextVariable(getInstallationPlace(lastRow, ""));
            var.addTextVariable(getLastCheckDate(lastRow, ""));
        }
    }

    private void fillSealNumbersEmpty(Variables var, int maxRows){
        for(int i = 1; i <= maxRows; ++i)
            var.addTextVariable(getSealNumber(i, ""));
    }

    private void fillImpulseWeights(ResultData resultData, Variables var){
        for(int i = 0; i <= 4; ++i) {
            boolean fillEmpty = i >= resultData.deviceFlowTransducers.size();
            if(fillEmpty) {
                var.addTextVariable(getImpulseWeight(i + 1, ""));
            } else {
                DeviceFlowTransducer deviceFlowTransducer = resultData.deviceFlowTransducers.get(i);
                var.addTextVariable(getImpulseWeight(i + 1, deviceFlowTransducer.getImpulseWeight()));
            }
        }
    }

    private String getMonthNameByNumber(int num){
        switch(num){
            case 1: return "января";
            case 2: return "февраля";
            case 3: return "марта";
            case 4: return "апреля";
            case 5: return "мая";
            case 6: return "июня";
            case 7: return "июля";
            case 8: return "августа";
            case 9: return "сентября";
            case 10: return "октября";
            case 11: return "ноября";
            case 12: return "декабря";
        }
        return "" + num;
    }

    private TextVariable getDeviceName(int ind, int type, String name){
        String deviceType = "";
        if(type == 1)
            deviceType = "Тепловычислитель";
        else if(type == 2)
            deviceType = "Преобразователь расхода";
        else if(type == 3)
            deviceType = "Преобразователь температуры";
        else if(type == 4)
            deviceType = "Преобразователь давления";
        
        String variableName  = String.format("${DeviceName%1d}", ind);
        String variableValue = deviceType + " " + name;
        
        return new TextVariable(variableName, variableValue);
    }

    private TextVariable getDeviceNumber(int ind, String deviceNumber){
        String variableName = String.format("${Number%1d}", ind);
        return new TextVariable(variableName, deviceNumber);
    }

    private TextVariable getInstallationPlace(int ind, String installationPlace){
        String variableName = String.format("${InstallationPlace%1d}", ind);
        return new TextVariable(variableName, installationPlace);
    }

    private TextVariable getLastCheckDate(int ind, String date){
        String variableName = String.format("${LastCheckDate%1d}", ind);
        return new TextVariable(variableName, date);
    }

    private TextVariable getSealNumber(int ind, String sealNumber){
        String variableName = String.format("${SealNumber%1d}", ind);
        return new TextVariable(variableName, sealNumber);
    }

    private TextVariable getImpulseWeight(int ind, String impulseWeight){
        String variableName = String.format("${ImpulseWeight%1d}", ind);
        return new TextVariable(variableName, impulseWeight);
    }

    public boolean requestDueDateByMit(String mitId, ModelDetailed objResult) {
        Gson gson = new Gson();

        HttpGet request = new HttpGet("https://fgis.gost.ru/fundmetrology/eapi/mit/" + mitId);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                try {
                    objResult = gson.fromJson(result, ModelDetailed.class);
                } catch (Exception e) {
                    return false;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}