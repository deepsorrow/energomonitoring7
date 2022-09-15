package com.energomonitoring7.energomonitoring7;


import com.energomonitoring7.energomonitoring7.config.Settings;
import com.energomonitoring7.energomonitoring7.domain.*;
import com.energomonitoring7.energomonitoring7.domain.utils.TemperatureCounterCharacteristicsParameter;
import com.energomonitoring7.energomonitoring7.repos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Controller
@SessionAttributes({"mobileDataBundle", "name"})
public class MainController {

    @Autowired
    ClientInfoRepo clientInfoRepo;
    @Autowired
    ServingOrganizationsRepo organizationsRepo;
    @Autowired
    MobileDataBundleRepo mobileDataBundleRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ResultDataRepo resultDataRepo;
    @Autowired
    ProjectRepo projectDescriptionRepo;

    @GetMapping("/")
    public String greeting(Model model) {

        ArrayList<ClientInfo> clientInfos = new ArrayList<>();
        Iterator<ClientInfo> clientInfoIterator = clientInfoRepo.findAll().iterator();
        while(clientInfoIterator.hasNext()){
            clientInfos.add(clientInfoIterator.next());
        }
        model.addAttribute("clientsInfo", clientInfos);

        ArrayList<OrganizationInfo> organizationInfos = new ArrayList<>();
        Iterator<OrganizationInfo> servingOrganizationIterator = organizationsRepo.findAll().iterator();
        while(servingOrganizationIterator.hasNext()){
            OrganizationInfo newOrgInfo = servingOrganizationIterator.next();
            if(newOrgInfo.servingObjectString != null)
                newOrgInfo.servingObjects = List.of(newOrgInfo.servingObjectString.split(";"));

            organizationInfos.add(newOrgInfo);
        }
        model.addAttribute("organizationsInfo", organizationInfos);
        model.addAttribute("name", getCurrentUsername());

        return "actOfEntry";
    }

    private String getCurrentUsername(){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.getNameByLogin(currentUser.getUsername());
    }

    @PostMapping("/checkProject")
    public String checkProject(@RequestParam String agreementNumber, @RequestParam String clientName,
                               @RequestParam String knotEnergyAddress, @RequestParam String clientRepresentativePhone,
                               @RequestParam String clientEmail, @RequestParam String clientRepresentative,
                               @RequestParam String selectedOrganization, Model model){

        ClientInfo clientInfo = clientInfoRepo.findByAgreementNumber(agreementNumber);
        OrganizationInfo servingOrganization = organizationsRepo.findByOrganizationName(selectedOrganization);

        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userRepo.getByLogin(currentUser.getUsername()).getId();

        MobileDataBundle mobileDataBundle = new MobileDataBundle(userId, clientInfo);
        mobileDataBundle.organizationInfo = servingOrganization;
        mobileDataBundle.organizationId = servingOrganization.id;

        model.addAttribute("mobileDataBundle", mobileDataBundle);
        model.addAttribute("name", getCurrentUsername());
        return "checkProject";
    }

    @PostMapping("/checkVerificationDocuments")
    public void checkVerificationDocuments(@RequestBody ProjectDescription projectDescription, Model model){
        MobileDataBundle mobileDataBundle = (MobileDataBundle) model.getAttribute("mobileDataBundle");
        mobileDataBundle.setProject(projectDescription);

        model.addAttribute("mobileDataBundle", mobileDataBundle);
    }

    @GetMapping("/checkVerificationDocuments")
    public String checkVerificationDocumentsGet(Model model){
        model.addAttribute("server_ip",   Settings.server_ip);
        model.addAttribute("server_port", Settings.server_port);
        model.addAttribute("name", getCurrentUsername());

        return "checkVerificationDocuments";
    }

    @PostMapping("/finish")
    public void finish(@RequestBody String jsonBody, Model model){
        Gson gson = new Gson();

        List<DeviceTemperatureCounter> deviceTemperatureCounters = new ArrayList<>();
        List<DeviceFlowTransducer> deviceFlowTransducers = new ArrayList<>();
        List<DeviceTemperatureTransducer> deviceTemperatureTransducers = new ArrayList<>();
        List<DevicePressureTransducer> devicePressureTransducers = new ArrayList<>();
        List<DeviceCounter> deviceCounters = new ArrayList<>();

        DeviceInfo[] devicesInfoToSend = gson.fromJson(jsonBody, DeviceInfo[].class);
        for(DeviceInfo deviceInfo : devicesInfoToSend){
            switch(deviceInfo.getDeviceType()) {
                case "1":
                    DeviceTemperatureCounter newDevice1 =
                            new DeviceTemperatureCounter(deviceInfo.getDeviceName(), deviceInfo.getDeviceNumber(), 1);
                    deviceTemperatureCounters.add(newDevice1);
                    break;
                case "2":
                    DeviceFlowTransducer newDevice2 =
                            new DeviceFlowTransducer(deviceInfo.getDeviceName(), deviceInfo.getDeviceNumber(),
                                    deviceInfo.getImpulseWeight(), deviceInfo.getDiameter(), 2);
                    deviceFlowTransducers.add(newDevice2);
                    break;
                case "3":
                    DeviceTemperatureTransducer newDevice3 =
                            new DeviceTemperatureTransducer(deviceInfo.getDeviceName(), deviceInfo.getDeviceNumber(),
                                    deviceInfo.getLength(), 3);
                    deviceTemperatureTransducers.add(newDevice3);
                    break;
                case "4":
                    DevicePressureTransducer newDevice4 =
                            new DevicePressureTransducer(deviceInfo.getDeviceName(), deviceInfo.getDeviceNumber(),
                                    deviceInfo.getSensorRange(), 4);
                    devicePressureTransducers.add(newDevice4);
                    break;
                case "5":
                    DeviceCounter newDevice5 =
                            new DeviceCounter(deviceInfo.getDeviceName(), deviceInfo.getDeviceNumber(),
                                    deviceInfo.getImpulseWeight(), deviceInfo.getDiameter(), 5);
                    deviceCounters.add(newDevice5);
                    break;
            }
        }

        MobileDataBundle mobileDataBundle = (MobileDataBundle) model.getAttribute("mobileDataBundle");
        assert mobileDataBundle != null;
        mobileDataBundle.setDevices(deviceTemperatureCounters, deviceFlowTransducers, deviceTemperatureTransducers,
                devicePressureTransducers, deviceCounters);

        mobileDataBundleRepo.save(mobileDataBundle);
    }

    @GetMapping("/finish")
    public String finish(Model model){

        return "finish";
    }

    @GetMapping("/inspectionResults")
    public String inspectionResults(Model model){

        ArrayList<String> inspectorNames = new ArrayList<>();
        ArrayList<ResultData> results = new ArrayList<>();
        for (ResultData resultData : resultDataRepo.findAll()) {
            resultData.clientInfo = clientInfoRepo.findById(resultData.otherInfo.clientId);
            results.add(resultData);
            inspectorNames.add(userRepo.findById(resultData.otherInfo.userId).get().getName());
        }

        model.addAttribute("name", getCurrentUsername());
        model.addAttribute("inspectorNames", inspectorNames);
        model.addAttribute("results", results);

        return "inspectionResults";
    }

    @GetMapping("/result")
    public String inspectionResult(@RequestParam int id, Model model){

        ResultData resultData = resultDataRepo.findById(id).get();
        resultData.clientInfo = clientInfoRepo.findById(resultData.otherInfo.clientId);
        resultData.project = projectDescriptionRepo.findById(resultData.otherInfo.projectId).get();

        Gson gson = new Gson();
        Type jsonType = new TypeToken<ArrayList<ArrayList<TemperatureCounterCharacteristicsParameter>>>(){}.getType();
        ArrayList<ArrayList<TemperatureCounterCharacteristicsParameter>> counterCharacteristicts
                = gson.fromJson(resultData.otherInfo.counterCharacteristicts, jsonType);

        for(int i = 0; i < resultData.deviceCounters.size(); ++i)
            resultData.deviceCounters.get(i).parameters = counterCharacteristicts.get(i);

        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("name", userRepo.getNameByLogin(currentUser.getUsername()));
        model.addAttribute("result", resultData);
        model.addAttribute("counterCharacteristics", counterCharacteristicts);

        return "result";
    }

    @GetMapping(
            value = "/getWord",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @ResponseBody
    public ResponseEntity<Resource> getFile(@RequestParam String id) throws IOException {
        // 1
        FileSystemResource resource = new FileSystemResource(String.format("reports/%1s/report.docx", id));
        // 2.
        MediaType mediaType = MediaTypeFactory
                .getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        // 3
        ContentDisposition disposition = ContentDisposition
                // 3.2
                .inline() // or .attachment()
                // 3.1
                .filename(resource.getFilename())
                .build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
