function confirmOrganizationSelect() {
	let selectOrganization = document.getElementById("selectOrganization");
	let newOrganization = selectOrganization.options[selectOrganization.selectedIndex].text;
	document.getElementsByClassName("clientServiceOrganization")[0].value = newOrganization;

	showHideOrganizationSelect(false);
	enableDisableContinueButton();
}

function showHideOrganizationSelect(show = false){
	let organizationPage = document.getElementsByClassName("bg-modal-enter-organization")[0];
	if(show) {
		organizationPage.style.display = "flex";
	} else {
		organizationPage.style.display = "none";
	}
}

function enableDisableContinueButton() {
	let inputAgreementNum = document.getElementsByName("agreementNumber")[0];
	let inputOrganization = document.getElementsByName("selectedOrganization")[0];
	let buttonContinue = document.getElementsByClassName("submit-button")[0];
	if(!inputAgreementNum.classList.contains("not-found") && inputAgreementNum.value != "" && inputOrganization.value != "")
		buttonContinue.disabled = false;
	else
		buttonContinue.disabled = true;
}




