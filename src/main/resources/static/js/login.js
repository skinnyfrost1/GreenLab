$(document).ready(function () {
  console.log("Ready!!");
  $('#submitButton').click(function () {
    console.log("on click!!");
    var posting = {};
    posting['email'] = $("#email").val();
    posting['password'] = $("#password").val();
    $.ajax({
      type: "POST",
      contentType: "application/json",
      url: "/index",
      data: JSON.stringify(posting),
      dataType: 'json',
      cache: false,
      timeout: 600000,
      success: function (result) {
        var success = result['bool'];
        console.log(result);
        if (success == true) {
          window.location.replace("/courses");
        } else {
          $('#errorMessage').html(result['message']);
          $('#errorMessage').css("color", "red");
        }
      },
      error: function (e) {

      }
    });
  })
  $("#email").on('change keyup paste',function(){
    $(this).val($(this).val().toLowerCase());
  })
});




