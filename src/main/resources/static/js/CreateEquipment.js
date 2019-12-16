$(document).ready(function () {
  document.getElementById("image").addEventListener("change", readFile);
  function readFile() {
  
    if (this.files && this.files[0]) {
      
      var FR= new FileReader();
      
      FR.addEventListener("load", function(e) {
        document.getElementById("img").src= e.target.result;
      }); 
      
      FR.readAsDataURL( this.files[0] );
    }
  }

  $("#unitText").hide().removeClass("show");

  $('.isMaterial').click(function() {
    if($('#isMaterial_yes').is(':checked')){
      $("#unitText").show().addClass("show");
      $('#textareaName').val('');
      $(".isSolutionContainer").hide().removeClass("show");
    }
    else{
      $("#unitText").hide().removeClass("show");
      $(".isSolutionContainer").show().addClass("show");
    }
  });


});
