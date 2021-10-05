var projectBase64;

$(document)
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

function uploadPhoto(){
	document.getElementById("imgupload").click();
}

function updateTextbox(text) {
	$('#commentBox').val(text);
	//$('#cagetextbox').attr("rows", Math.max.apply(null, text.split("\n").map(function(a){alert(a.length);return a.length;})));
};

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#photoPreview')
                .attr('src', e.target.result)
                .width(120)
                .height(120);

            document.getElementById("emptyLabel").innerHTML = "";
        };

        reader.readAsDataURL(input.files[0]);
        projectBase64 = reader.result;
    }
}

function saveProjectInfo(){
    let projectOk = document.getElementById("label").innerHTML === "Соответствует";
    let projectComment = document.getElementById("projectComment").value;
    let projectPhotoBase64 = document.getElementById("photoPreview").src;

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/checkVerificationDocuments", false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        "isOk": projectOk,
        "comment": projectComment,
        "photoBase64": projectPhotoBase64
    }));

    redirectToNext();
}

function redirectToNext(){
    window.location.href = "/checkVerificationDocuments"
}

updateTextbox("");