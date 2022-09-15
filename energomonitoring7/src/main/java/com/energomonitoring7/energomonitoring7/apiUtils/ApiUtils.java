package com.energomonitoring7.energomonitoring7.apiUtils;

import com.energomonitoring7.energomonitoring7.RefDoc;
import com.energomonitoring7.energomonitoring7.domain.*;
import com.energomonitoring7.energomonitoring7.repos.RefDocRepo;
import org.apache.commons.codec.binary.Base64;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ApiUtils {
    public static void resetDeviceIds(ResultData resultData){
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

        if (resultData.checkLengthResults != null) {
            for (CheckLengthResult checkLengthResult : resultData.checkLengthResults)
                checkLengthResult.resetId();
        }

        resultData.otherInfo.setId(0);
    }

    public static int fillDeviceTemperatureCounters(int maxRows, ResultData resultData, Variables var){
        int i = 0;
        for(; i < resultData.deviceTemperatureCounters.size(); ++i){
            DeviceTemperatureCounter device = resultData.deviceTemperatureCounters.get(i);

            var.addTextVariable(getDeviceName(i+1, device.typeId, device.deviceName.value));
            var.addTextVariable(getDeviceNumber(i+1, device.deviceNumber.value));
            var.addTextVariable(getLastCheckDate(i+1, device.lastCheckDate.value));
            var.addTextVariable(getInstallationPlace(i+1, ""));
        }

        return i+1;
    }

    public static int fillDeviceFlowTransducers(int maxRows, ResultData resultData, Variables var, int lastRow){
        for(int i = 0; i < resultData.deviceFlowTransducers.size(); ++i){
            DeviceFlowTransducer device = resultData.deviceFlowTransducers.get(i);

            var.addTextVariable(getDeviceName(lastRow, device.getTypeId(), device.deviceName.value));
            var.addTextVariable(getDeviceNumber(lastRow, device.deviceNumber.value));
            var.addTextVariable(getInstallationPlace(lastRow, device.installationPlace.value));
            var.addTextVariable(getLastCheckDate(lastRow, device.lastCheckDate.value));
            lastRow += 1;
        }

        return lastRow;
    }

    public static int fillDeviceTemperatureTransducers(int maxRows, ResultData resultData, Variables var, int lastRow) {
        for(int i = 0; i < resultData.deviceTemperatureTransducers.size(); ++i){
            DeviceTemperatureTransducer device = resultData.deviceTemperatureTransducers.get(i);

            var.addTextVariable(getDeviceName(lastRow, device.typeId, device.deviceName.value));
            var.addTextVariable(getDeviceNumber(lastRow, device.deviceNumber.value));
            var.addTextVariable(getInstallationPlace(lastRow, device.installationPlace.value));
            var.addTextVariable(getLastCheckDate(lastRow, device.lastCheckDate.value));
            lastRow += 1;
        }

        return lastRow;
    }

    public static int fillDevicePressureTransducers(int maxRows, ResultData resultData, Variables var, int lastRow) {
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

                var.addTextVariable(getDeviceName(lastRow, device.typeId, device.deviceName.value));
                var.addTextVariable(getDeviceNumber(lastRow, device.deviceNumber.value));
                var.addTextVariable(getInstallationPlace(lastRow, device.installationPlace.value));
                var.addTextVariable(getLastCheckDate(lastRow, device.lastCheckDate.value));
            }
            lastRow += 1;
        }

        return lastRow;
    }

    public static void fillRestEmpty(Variables var, int lastRow, int maxRows){
        for( ; lastRow <= maxRows; lastRow++) {
            var.addTextVariable(getDeviceName(lastRow, 0, ""));
            var.addTextVariable(getDeviceNumber(lastRow, ""));
            var.addTextVariable(getInstallationPlace(lastRow, ""));
            var.addTextVariable(getLastCheckDate(lastRow, ""));
        }
    }

    public static void fillSealNumbersEmpty(Variables var, int maxRows){
        for(int i = 1; i <= maxRows; ++i)
            var.addTextVariable(getSealNumber(i, ""));
    }

    public static void fillImpulseWeights(ResultData resultData, Variables var){
        for(int i = 0; i <= 4; ++i) {
            boolean fillEmpty = i >= resultData.deviceFlowTransducers.size();
            if(fillEmpty) {
                var.addTextVariable(getImpulseWeight(i + 1, ""));
            } else {
                DeviceFlowTransducer deviceFlowTransducer = resultData.deviceFlowTransducers.get(i);
                var.addTextVariable(getImpulseWeight(i + 1, deviceFlowTransducer.impulseWeight.value));
            }
        }
    }

    public static String getMonthNameByNumber(int num){
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

    public static TextVariable getDeviceName(int ind, int type, String name){
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

    public static TextVariable getDeviceNumber(int ind, String deviceNumber){
        String variableName = String.format("${Number%1d}", ind);
        return new TextVariable(variableName, deviceNumber);
    }

    public static TextVariable getInstallationPlace(int ind, String installationPlace){
        String variableName = String.format("${InstallationPlace%1d}", ind);
        return new TextVariable(variableName, installationPlace);
    }

    public static TextVariable getLastCheckDate(int ind, String date){
        String variableName = String.format("${LastCheckDate%1d}", ind);
        return new TextVariable(variableName, date);
    }

    public static TextVariable getSealNumber(int ind, String sealNumber){
        String variableName = String.format("${SealNumber%1d}", ind);
        return new TextVariable(variableName, sealNumber);
    }

    public static TextVariable getImpulseWeight(int ind, String impulseWeight){
        String variableName = String.format("${ImpulseWeight%1d}", ind);
        return new TextVariable(variableName, impulseWeight);
    }

    public static int fileId = 1;

    public static void initRefDocs(RefDocRepo repo) {
        repo.deleteAll();

        try {
            fileId = 1;
            ArrayList<File> files = new ArrayList<File>(List.of(new File("refDocs").listFiles()));
            for (File file : files) {
                int id = parseFile(file, 0, repo);
                traverseFolder(id, file, repo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void traverseFolder(int parentId, File folder, RefDocRepo repo) throws IOException {
        List<File> allFiles = List.of(folder.listFiles());
        for (File file : allFiles) {
            int id = parseFile(file, parentId, repo);

            if (file.isDirectory()) {
                traverseFolder(id, file, repo);
            }
        }
    }

    public static int parseFile(File file, int parentId, RefDocRepo repo) throws IOException {
        File parentFile = file.getParentFile();

        String parentFolderName = parentFile.getName();
        if (parentFolderName.equals("refDocs"))
            parentFolderName = "";

        String path = file.getPath().replace("refDocs\\", "");
        String size = getStringSizeLengthFile(Files.size(file.toPath()));

        RefDoc refDoc = new RefDoc(fileId, parentId, file.isDirectory(), path, parentFolderName, file.getName(), size);
        repo.save(refDoc);
        fileId += 1;
        return refDoc.id;
    }

    public static String getStringSizeLengthFile(long size) {
        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;

        if(size < sizeMb)
            return df.format(size / sizeKb)+ " Кб";
        else if(size < sizeGb)
            return df.format(size / sizeMb) + " Мб";
        else if(size < sizeTerra)
            return df.format(size / sizeGb) + " Гб";

        return "";
    }

    public static RefDoc getRefDocData(int id, RefDocRepo repo) throws IOException {
        RefDoc refDoc = repo.findById(id).orElse(null);
        if (refDoc == null) {
            throw new NullPointerException("Такого документа по id" + id + " не было найдено!");
        }

        File file = new File("refDocs/" + refDoc.path);
        if (!file.exists()) {
            throw new NullPointerException("Такого документа по пути " + refDoc.path + " не было найдено!");
        }

        refDoc.dataString = Base64.encodeBase64String(Files.readAllBytes(file.toPath()));
        return refDoc;
    }
}
