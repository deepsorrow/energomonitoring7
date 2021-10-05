function openResultById(thElement){
    let xmlHttp = new XMLHttpRequest();
    let url = "http://192.168.6.19:8080/result?id=" + thElement.getAttribute("data-id");

    location.href = url;
}