function checkConfirmPassword() {
    var password=document.getElementById("password").value;
    var confirmPassord=document.getElementById("confirmPassword").value;
    console.log(password);
    console.log(confirmPassword);
    if(password!=confirmPassord){
      document.getElementById("errorMessage").innerHTML = "password and confirm password should be the same";
      return
    }

    


  }