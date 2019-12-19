// $(document).ready(function(){
// console.log("Ready!!");
$(document).ready(function(e) {
    $("#editCourseEditLabs").show().hide().removeClass("show");
    $("#submitEditBtnContainer").show().hide().removeClass("show");
    $("#previousEditBtnContainer").show().hide().removeClass("show");
    $("#nextEditBtnContainer").show().hide().removeClass("show");
    $("#editCourseInfoContainer").show().hide().removeClass("show");
    $("#deleteCourseBtnContainer").show().hide().removeClass("show");
    $("#studentRosterContainer").show().hide().removeClass("show");

    $("#editCourseInfoContainer").show().addClass("show");
    $("#studentRosterContainer").show().addClass("show");
    $("#deleteCourseBtnContainer").show().addClass("show");
    $("#nextEditBtnContainer").show().addClass("show");

    $(".labToAdd").click(function (e) {
        if (e.target.tagName != 'INPUT') {
            $(this).find("input").toggleCheckbox();
            return false;
        }
    });

    $(".labExisted").click(function (e) {
        if (e.target.tagName != 'INPUT') {
            $(this).find("input").toggleCheckbox();
            return false;
        }
    });

    $("#courseDeleteBtn").click(function(){
        if (confirm("Do you really want to delete this course?")){
            $('form#deleteForm').submit();
        }
    });




})

function valthisform() {
    console.log("changed")
    var checkboxs=document.getElementsByName("editLabSelected");
    console.log("checkboxs")
    var okay=false;
    for(var i=0,l=checkboxs.length;i<l;i++) {
        if(checkboxs[i].checked) {
            okay=true;
            break;
        }
    }
    if(okay){
        console.log("ok")
        $('#saveEditBtn').removeAttr("disabled", 'disabled');
        $('#saveEditBtn').show().removeClass(".save-button-disabled")
    }
    else{
        console.log("not ok")
        $('#saveEditBtn').attr('disabled');
        $('#saveEditBtn').show().addClass(".save-button-disabled")
    }
}

function deleteStudent(studentEmail) {
    console.log("on click!!");
    $.ajax({
        type: "POST",
        url: "/course/edit/deleteStu",
        data: {
            courseId: $('#courseId').val(),
            studentEmail: studentEmail.id
        },
        cache: false,
        timeout: 600000,
        success: function (result) {
            var success = result['bool'];
            var someId = 'div_' + studentEmail.id;
            document.getElementById(someId).style.display = 'none';
            // location.reload();
            if (success===true){
                console.log(result);
                // window.location.replace("/course/edit");
            } else{
                $('#errorMessage').html("Error");
                $('#errorMessage').css("color","red");
            }
        },
        error: function (e) {
            console.log("Error")
            $('#errorMessage').html(result['message']);
            $('#errorMessage').css("color","red");
        }
    });
}
//)
//   });

function test() {
    document.getElementById("displayStudent").innerHTML = "123"
}


function uploadRoster() {
    console.log("on click!!");
    // document.getElementById("displayStudent").innerHTML = "123";
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        url: "/course/edit/uploadRoster2",
        data: {
            courseId: $('#courseId').val(),
            file: $('#file').file
        },
        cache: false,
        timeout: 600000,
        success: function (result) {
            document.getElementById("displayStudent").innerHTML = result;
        },
        error: function (e) {
            console.log("Error")
            $('#errorMessage').html("Error");
            $('#errorMessage').css("color","red");
        }
    });
}

function uploadRoster2() {
    console.log("on click!!");
    // document.getElementById("displayStudent").innerHTML = "123";
    var form = $('#uploadRosterForm')[0];

    // Create an FormData object
    var data = new FormData(form);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        url: "/course/edit/uploadRoster2",
        data: data,
        cache: false,
        timeout: 600000,
        success: function (result) {
            document.getElementById("displayStudent").innerHTML = result;
        },
        error: function (e) {
            console.log("Error")
            $('#errorMessage').html("Error");
            $('#errorMessage').css("color","red");
        }
    });
}
$("#nextEditBtn").click(function (e) {
    $("#studentRosterContainer").show().hide().removeClass("show");

    $("#editCourseInfoContainer").show().hide().removeClass("show");
    $("#deleteCourseBtnContainer").show().hide().removeClass("show");
    $("#nextEditBtnContainer").show().hide().removeClass("show");

    $("#editCourseEditLabs").show().addClass("show");
    $("#previousEditBtnContainer").show().addClass("show");
    $("#submitEditBtnContainer").show().addClass("show");
    valthisform()
})

$("#previousEditBtn").click(function (e) {
    $("#editCourseEditLabs").show().hide().removeClass("show");
    $("#previousEditBtnContainer").show().hide().removeClass("show");
    $("#submitEditBtnContainer").show().hide().removeClass("show");

    $("#editCourseInfoContainer").show().addClass("show");
    $("#studentRosterContainer").show().addClass("show");
    $("#deleteCourseBtnContainer").show().addClass("show");
    $("#nextEditBtnContainer").show().addClass("show");
})

$("#editCourseEditLabs").change(function (e) {
    valthisform()
})


$.fn.toggleCheckbox = function() {
    this.attr('checked', !this.attr('checked'));
}
//})