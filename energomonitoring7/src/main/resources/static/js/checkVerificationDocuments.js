var deviceSpecs = function(id, name, value) {

    var private = {
        id: id,
        name: name,
        value: value,
    }

    return {
        get: function (prop) {
            if(private.hasOwnProperty(prop))
                return private[prop];
        }
    }
};

var deviceModel = function(id, deviceSpecs) {

    var privateData = {
        id: id,
        deviceSpecs: deviceSpecs,
    }

    return {
        get: function (prop) {
            if(privateData.hasOwnProperty(prop)) {
                return privateData[prop];
            }
        },
        setDevicesSpecs: function(prop){
            privateData.deviceSpecs = prop;
        }
    }
};

var modelsWithSpecs = [];

let currentModel = "";

function saveCurrentFields() {

}

function addModel(){

    let modelId = String(document.getElementById("list-tab").children.length + 1);

    let newDeviceModel = new deviceModel(modelId);

    let newContent = document.createElement('a');
    newContent.id = modelId;
    newContent.className = "list-group-item list-group-item-action active";
    newContent.innerHTML =  "Новый прибор (" + modelId + ")";
    newContent.addEventListener("click", selectDeviceHandler.bind(newContent));

    document.getElementsByClassName("list-group")[0].appendChild(newContent);

    markAllOtherDevicesInactive(modelId);
    addSpecsToNewDevice(modelId);
    showDeviceSpecsForCurrentDevice(modelId);
}

function markAllOtherDevicesInactive(modelId){
    let items = document.getElementsByClassName("list-group-item");
    for(let i=0; i < items.length; ++i){
        if(items[i].id !== modelId)
            items[i].classList.remove("active");
    }
}

function addSpecsToNewDevice(deviceId){

    let htmlElement = document.createElement('div');
    htmlElement.className = "deviceSpecs";
    htmlElement.setAttribute("data_id",  deviceId);
    htmlElement.innerHTML = "<div class=\"input-field\">Тип прибора:\n" +
        "<select class=\"form-select form-select-type\" onchange='updateCustomFields(this)'>\n" +
        "<option value=\"0\" selected disabled hidden>Выберите тип...</option>\n" +
        "<option value=\"1\">Тепловычислитель</option>\n" +
        "<option value=\"2\">Преобразователь расхода</option>\n" +
        "<option value=\"3\">Преобразователь температуры</option>\n" +
        "<option value=\"4\">Преобразователь давления</option>\n" +
        "<option value=\"5\">Счетчик</option>\n" +
        "</select>\n" +
        "</div>\n" +
        "<div class=\"custom-fields\"> " +
        "</div>\n" +
        "<div class=\"input-field device-arshin-number\"><label>Номер свидетельства: </label>" +
        "<input type=\"text\"><button class=\"btn btn-secondary\" onclick=\"checkDeviceDueDate(this)\">Проверить</button></div>\n" +
        "<div class=\"input-field device-arshin-date\"><label>Поверка прибора действительна до:" +
        "<select id='due-date' class=\"input-arshin-date hide-bg form-select\" onchange='getDueDateByMit(this)'></select></div>";

    document.getElementById("deviceSpecsContainer").appendChild(htmlElement);
}

function getDueDateByMit(selectorElement){

    selectorElement.innerHtml = ""; // clear options
    let url = "http://192.168.6.19:8080/api/v1/getDueDateByMit?mitId=" + selectorElement.value;

    let xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            let response = JSON.parse(xmlHttp.responseText);
            if(response['success']) {
                selectorElement.classList.remove("invalidate-date");
                selectorElement.classList.add("validate-date");
            }
            else {
                selectorElement.classList.remove("validate-date");
                selectorElement.classList.add("invalidate-date");
            }
            putDueDate(selectorElement, response["result"]);
        }
    }
    xmlHttp.open("GET", url, true); // true for asynchronous
    xmlHttp.send(null);
}

function updateCustomFields(selectorElement) {
    let deviceSpecs = selectorElement.parentElement.parentElement;
    document.getElementById(deviceSpecs.getAttribute("data_id")).innerHTML = "Новый прибор";

    let customFields = deviceSpecs.getElementsByClassName("custom-fields")[0];
    customFields.innerHTML = "";

    let modelElement = document.createElement('div');
    modelElement.className = "input-field";
    modelElement.innerHTML = "<label>Модель прибора: </label><input class=\"input-deviceName\" type=\"text\">";
    modelElement.addEventListener("change", updateDeviceSummaryName.bind(modelElement));
    customFields.appendChild(modelElement);

    if(selectorElement.value === "1"){
        let newElement = document.createElement('div');
        newElement.className = "input-field";
        newElement.innerHTML = "<label>Номер прибора: </label><input class=\"input-deviceNumber\" type=\"text\">";
        newElement.addEventListener("change", updateDeviceSummaryName.bind(newElement));
        customFields.appendChild(newElement);
    } else if(selectorElement.value === "2" || selectorElement.value === "5") {
        let newElement1 = document.createElement('div');
        newElement1.className = "input-field";
        newElement1.innerHTML = "<label>Номер прибора: </label><input class=\"input-deviceNumber\" type=\"text\">";
        newElement1.addEventListener("change", updateDeviceSummaryName.bind(newElement1));
        customFields.appendChild(newElement1);

        let newElement2 = document.createElement('div');
        newElement2.className = "input-field";
        newElement2.innerHTML = "<label>Диаметр: </label><input class=\"input-diameter\" type=\"text\">";
        newElement2.addEventListener("change", updateDeviceSummaryName.bind(newElement2));
        customFields.appendChild(newElement2);

        let newElement3 = document.createElement('div');
        newElement3.className = "input-field";
        newElement3.innerHTML = "<label>Вес импульса: </label><input class=\"input-impulseWeight\" type=\"text\">";
        newElement3.addEventListener("change", updateDeviceSummaryName.bind(newElement3));
        customFields.appendChild(newElement3);
    }
    else if(selectorElement.value === "3"){
        let newElement1 = document.createElement('div');
        newElement1.className = "input-field";
        newElement1.innerHTML = "<label>Номер датчика: </label><input class=\"input-deviceNumber\" type=\"text\">";
        newElement1.addEventListener("change", updateDeviceSummaryName.bind(newElement1));
        customFields.appendChild(newElement1);

        let newElement2 = document.createElement('div');
        newElement2.className = "input-field";
        newElement2.innerHTML = "<label>Длина: </label><input class=\"input-length\" type=\"text\">";
        newElement2.addEventListener("change", updateDeviceSummaryName.bind(newElement2));
        customFields.appendChild(newElement2);
    }
    else if(selectorElement.value === "4"){
        let newElement1 = document.createElement('div');
        newElement1.className = "input-field";
        newElement1.innerHTML = "<label>Номер датчика: </label><input class=\"input-deviceNumber\" type=\"text\">";
        newElement1.addEventListener("change", updateDeviceSummaryName.bind(newElement1));
        customFields.appendChild(newElement1);

        let newElement2 = document.createElement('div');
        newElement2.className = "input-field";
        newElement2.innerHTML = "<label>Диапазон датчика: </label><input class=\"input-sensorRange\" type=\"text\">";
        newElement2.addEventListener("change", updateDeviceSummaryName.bind(newElement2));
        customFields.appendChild(newElement2);
    }

    // document.getElementsByClassName("row")[0].style.height =
    //     document.getElementsByClassName("container2")[0].style.height;
}

function showDeviceSpecsForCurrentDevice(currentDevice) {
    let deviceSpecs = document.getElementsByClassName("deviceSpecs");
    for(let i=0; i<deviceSpecs.length; ++i){
        if(deviceSpecs[i].getAttribute("data_id") === currentDevice)
            deviceSpecs[i].style.display = "block";
        else
            deviceSpecs[i].style.display = "none";
    }
}

function selectDeviceHandler(){
    let deviceId = this.id;
    this.classList.add("active");
    markAllOtherDevicesInactive(deviceId)
    showDeviceSpecsForCurrentDevice(deviceId);
}

function updateDeviceSummaryName() {
    let customFields = this.parentElement.parentElement;
    let inputs = this.parentElement.getElementsByTagName("input");

    let newName = "";
    for(let i=0; i<inputs.length; ++i){
        newName += inputs[i].value;

        if(i+1 !== inputs.length && inputs[i+1].value !== "")
            newName += ", ";
    }

    document.getElementById(customFields.getAttribute("data_id")).innerHTML = newName;
}

function checkDeviceDueDate(checkButton){

    let deviceName = "";

    let deviceNames = checkButton.parentElement.parentElement.getElementsByClassName("input-deviceName");
    if(deviceNames.length > 0)
        deviceName = checkButton.parentElement.parentElement.getElementsByClassName("input-deviceName")[0].value;

    let arshinId = checkButton.parentElement.getElementsByTagName('input')[0].value;

    let inputDate = checkButton.parentElement.parentElement.getElementsByClassName("input-arshin-date")[0];
    inputDate.classList.remove("invalidate-date");
    inputDate.classList.remove("validate-date");

    let url = "http://" + server_ip + ":" + server_port + "/api/v1/getDueDate?deviceName="
        + deviceName + "&arshinId=" + arshinId;

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            let response = JSON.parse(xmlHttp.responseText);
            if(response['success']) {
                if(response['variantsCount'] > 0) {
                    inputDate.classList.remove('hide-bg');
                    inputDate.innerHTML = "";

                    let opt = document.createElement("option");
                    opt.selected  = true;
                    opt.disabled  = true;
                    opt.hidden    = true;
                    opt.innerHTML = "Выберите..";

                    inputDate.appendChild(opt);
                    for(let variantKey in response['variants'])
                    {
                        let opt = document.createElement("option");
                        opt.value = variantKey;
                        opt.innerHTML = response['variants'][variantKey];

                        inputDate.appendChild(opt);
                    }
                } else {
                    inputDate.classList.remove("invalidate-date");
                    inputDate.classList.add("validate-date");

                    putDueDate(inputDate, response["result"]);
                }
            }
            else {
                inputDate.classList.remove("validate-date");
                inputDate.classList.add("invalidate-date");

                putDueDate(inputDate, response["result"]);
            }
        }
    }
    xmlHttp.open("GET", url, true); // true for asynchronous
    xmlHttp.send(null);

}

function saveDevices() {

    let deviceSpecs = document.getElementsByClassName("deviceSpecs");
    for(let i=0; i < deviceSpecs.length; ++i) {
        let data_id = parseInt(deviceSpecs[i].getAttribute("data_id"));
        let specs = deviceSpecs[i].getElementsByClassName("custom-fields")[0];
        let specsInputs = specs.getElementsByTagName("input")

        let inputValues = {};
        for (let i = 0; i < specsInputs.length; ++i) {
            let specName = specsInputs[i].className;
            let specValue = specsInputs[i].value;
            inputValues[specName.replace("input-", "")] = specValue;
        }

        inputValues["deviceType"] = deviceSpecs[i].getElementsByTagName("select")[0].value;

        modelsWithSpecs.push(inputValues);
    }

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/finish", false);
    xhr.setRequestHeader('Content-Type', 'application/json');

    // let jsonResult = "";
    // for(let i = 0; i < modelsWithSpecs.length; ++i){
    //     jsonResult += JSON.stringify(Object.fromEntries(modelsWithSpecs[0]));
    // }
    xhr.send(JSON.stringify(modelsWithSpecs));

    redirectToFinish();
}

function redirectToFinish(){
    window.location.href = "/finish"
}

function putDueDate(inputDate, value){
    inputDate.innerHTML = "";
    inputDate.classList.add('hide-bg');
    let opt = document.createElement("option");
    opt.innerHTML = value;
    opt.selected  = true;
    inputDate.appendChild(opt);
}