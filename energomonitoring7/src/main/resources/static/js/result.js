function downloadWord(button) {
    window.open("http://192.168.6.19:8080/getWord?id=" + button.getAttribute("data-id"));
}

$(function() {
    $('img').on('click', function() {
        $('.imagepreview').attr('src', $(this).attr('src'));
        $('#imagemodal').modal('show');
    });
});