$(document).ready(function(e) {

    $(".createLabInfoPicture").click(function (e) {
        console.log("11111111111")
        if (e.target.tagName != 'INPUT') {
            $(this).find("input").toggleCheckbox();
            return false;
        }
    });
})