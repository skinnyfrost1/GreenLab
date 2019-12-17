$(document).ready(function () {
    $(".bigLabContainer").click(function (e) {
        if (e.target.tagName != 'INPUT') {
            var checkBoxes = $(this).find('input')
            checkBoxes.prop("checked", !checkBoxes.prop("checked"));
            return false;
        }
    });
    // $('.equipsToAdd').on('click', function(){
    //     var checkbox = $(this).children('input[type="checkbox"]');
    //     console.log(checkbox)
    //     checkbox.prop('checked', !checkbox.prop('checked'));
    // });

});