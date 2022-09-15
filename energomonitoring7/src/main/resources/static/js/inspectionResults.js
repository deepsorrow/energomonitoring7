function openResultById(thElement){
    let xmlHttp = new XMLHttpRequest();
    let url = "http://172.16.0.4:8080/result?id=" + thElement.getAttribute("data-id");

    location.href = url;
}