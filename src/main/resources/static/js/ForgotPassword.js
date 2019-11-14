$(document).ready(function(){
console.log("Ready!!");
$('input[name=confirmPassword],input[name=password]').on("input propertychange",function() {
    var password = $('input[name=password]').val();
    var confirmPassord = $('input[name=confirmPassword]').val();
    if(password == confirmPassord)
    {
        $('#confirmPasswordLabel').css("color","green");
        $('#submitButton').attr("disabled",false);
    }
    else {
        $('#confirmPasswordLabel').css("color","red");
        $('#submitButton').attr("disabled",true);
    }
});


$('#submitButton').click(function() {
    console.log("on click!!");
  var posting = {};
  posting['email']=$("#email").val();
  posting['password']=$("#password").val();
  posting['uid']=$("#uid").val();
    $.ajax({
      type: "POST",
      contentType: "application/json",
      url: "/forgotpassword",
      data: JSON.stringify(posting),
      dataType: 'json',
      cache: false,
      timeout: 600000,
      success: function (result) {
          var success = result['bool'];
          console.log(result);
          if (success==true){
            window.location.replace("/index");
          } else{
            $('#errorMessage').html(result['message']);
            $('#errorMessage').css("color","red");
          }
      },
      error: function (e) {

      }
  });
})
  });

