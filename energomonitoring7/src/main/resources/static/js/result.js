function downloadWord(button) {
    window.open("http://172.16.0.4:8080/getWord?id=" + button.getAttribute("data-id"));
}

$(function() {
    $('img').on('click', function() {
        $('.imagepreview').attr('src', $(this).attr('src'));
        $('#imagemodal').modal('show');
    });
});