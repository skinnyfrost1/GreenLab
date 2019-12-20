$(document).ready(function () {
    $("#labDelete").click(function(){
        if (confirm("Do you really want to delete these labs?")){
            console.log("going to delete")
            $('form#deleteForm').submit();
        }
    });


    $(".notVeryBigContainer").click(function (e) {
        if (e.target.tagName != 'INPUT') {
            var checkBoxes = $(this).find('input')
            checkBoxes.prop("checked", !checkBoxes.prop("checked"));
            return false;
        }
    });
});