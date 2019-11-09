$(document).ready(function(){
  $('input[name=confirmPassword],input[name=password]').on("input propertychange",function() {
      var password = $('input[name=password]').val();
      var confirmPassord = $('input[name=confirmPassword]').val();
      if(password == confirmPassord)
      {
          $('#confirmPasswordLabel').css("color","green");
          $('#submit').attr("disabled",false);
      }
      else {
          $('#confirmPasswordLabel').css("color","red");
          $('#submit').attr("disabled",true);
      }
  });

  // $('input[name=username]').on("input propertychange",function() {
  //     $.ajax({
  //         type : "GET",
  //         url : 'checkusername',
  //         data:{ username: $('input[name=username]').val()},
  //         cache : false,
  //         success : function(data) {
  //             $('#usernameError').text(data);
  //         },
  //         error: function(){
  //             alert(arguments[1]);
  //         }
  //     });
  // })
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