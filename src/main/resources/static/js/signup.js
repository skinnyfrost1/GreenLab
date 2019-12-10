$(document).ready(function () {
    $('input[name=confirmPassword],input[name=password]').on("input propertychange", function () {
        var password = $('input[name=password]').val();
        var confirmPassord = $('input[name=confirmPassword]').val();
        if (password == confirmPassord) {
            $('#confirmPasswordLabel').css("color", "green");
            // $('#submit').attr("disabled", false);
        }
        else {
            $('#confirmPasswordLabel').css("color", "red");
            $('#submit').attr("disabled", true);
        }
    });

    $('input[name=email]').on("input propertychange", function () {
        var email = {};
        email['email'] = $("#email").val();
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/checkemail",
            data: JSON.stringify(email),
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (data) {
                var isExist = data['exist'];
                if (isExist == true) {
                    $('#emailExist').html(data['message']);
                    $('#emailExist').css("color", "red");
                    $('#submit').attr("disabled", true);

                } else {
                    $('#emailExist').html('');
                    $('#submit').attr("disabled", false);
                }
                console.log("SUCCESS : ", data);
            },
            error: function (e) {

            }
        });
    });
    $("#uid").on("input propertychange",function () {
        var str = {};
        if ($('uid') != null) {
            str['str'] = $('#uid').val();
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/checkuid",
                data: JSON.stringify(str),
                dataType: 'json',
                cache: false,
                timeout: 600000,
                success: function (data) {
                    var isExist = data['bool'];
                    if (isExist == true) {
                        $('#uidExist').html(data['message']);
                        $('#uidExist').css("color", "red");
                        $('#submit').attr("disabled", true);
                    } else {
                        $('#uidExist').html('');
                        $('#submit').attr("disabled", false);
                    }
                    console.log("SUCCESS : ", data);
                },
                error: function (e) {
                    console.log(e);
                }
            });
        }


    });

});


// function checkConfirmPassword() {
//     var password=document.getElementById("password").value;
//     var confirmPassord=document.getElementById("confirmPassword").value;
//     console.log(password);
//     console.log(confirmPassword);
//     if(password!=confirmPassord){
//       document.getElementById("errorMessage").innerHTML = "password and confirm password should be the same";
//       return
//     }
//   }