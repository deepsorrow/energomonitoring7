var files = [];
var clickedId = 0

function addNewFileUploadCell(){
    return document.getElementsByClassName("files")[0].innerHTML += getFileUploadCell()
        .replace('imgupload', "imgupload" + files.length)
        .replace("photoPreview", "photoPreview" + files.length)
        .replace("emptyLabel", "emptyLabel" + files.length)
}

function getFileUploadCell(){
    return "<input type=\"file\" id=\"imgupload\" style=\"display:none\" onchange=\"readURL(this);\"/>" +
        "<div id=\"" + files.length + "\" class=\"attached-image\" onClick=\"uploadPhoto(this.id)\">\n" +
        "<img id=\"photoPreview\" />\n" +
        "<span id=\"emptyLabel\">Нажмите, чтобы загрузить</span>\n" +
        "</div>\n" +
        "</div>"
}

$(document)
    .ready(function() {
        addNewFileUploadCell()
    })
    .one('focus.textarea', '.autoExpand', function(){
        var savedValue = this.value;
        this.value = '';
        this.baseScrollHeight = this.scrollHeight;
        this.value = savedValue;
    })
    .on('input.textarea', '.autoExpand', function(){
        var minRows = this.getAttribute('data-min-rows')|0,
            rows;
        this.rows = minRows;
        rows = Math.ceil((this.scrollHeight - this.baseScrollHeight) / 16);
        this.rows = minRows + rows;
    });
	
function checkProject(){
	var label = document.getElementById("label");
	if (label.classList.contains("btn-outline-success")){		
		label.innerHTML = "Не соответствует";
		label.className = "btn btn-outline-danger";
	}
	else{
		label.innerHTML = "Соответствует";
		label.className = "btn btn-outline-success";
	}
}

function uploadPhoto(id){
    clickedId = id
	document.getElementById("imgupload" + id).click();
}

function updateTextbox(text) {
	$('#commentBox').val(text);
}

function readURL(input) {

    if (input.files && input.files[0]) {
        const extension = input.files[0].name.split('.').pop().toLowerCase();
        var reader = new FileReader();

        reader.onload = function (e) {
            const photoPreview = $('#photoPreview' + clickedId)[0]
            const imageExists = photoPreview.src !== ""
            if (extension === "pdf") {
                photoPreview.src = pdfIconLink
                photoPreview.width = 100
                photoPreview.height = 100
            } else {
                photoPreview.src = e.target.result
                photoPreview.width = 120
                photoPreview.height = 120
            }

            document.getElementById("emptyLabel" + clickedId).innerHTML = "";

            for(index = 0; index < files.length; ++index){
                if (files[index].clickedId === clickedId) {
                    files.splice(index, 1)
                    break
                }
            }
            files.push({clickedId: clickedId, title: input.files[0].name, dataBase64: e.target.result, extension: extension})

            if (!imageExists) {
                addNewFileUploadCell()
            }
        };

        reader.readAsDataURL(input.files[0]);
    }
}

function saveProjectInfo(){
    let projectOk = document.getElementById("label").innerHTML === "Соответствует";
    let projectComment = document.getElementById("projectComment").value;

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/checkVerificationDocuments", false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        "isOk": projectOk,
        "comment": projectComment,
        "files": files
    }));

    redirectToNext();
}

function redirectToNext(){
    window.location.href = "/checkVerificationDocuments"
}

updateTextbox("");

const pdfIconLink = "https://i.imgur.com/nwUiNVi.png"