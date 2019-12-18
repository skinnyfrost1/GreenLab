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
      $(".isblanderContainer").hide().removeClass("show");
      $(".isblandableContainer").hide().removeClass("show");
      $(".isHeaterContainer").hide().removeClass("show");
      $(".isHeatableContainer").hide().removeClass("show");
      $(".isSolutionContainer").hide().removeClass("show");
    }
    else{
      $("#unitText").hide().removeClass("show");
      $('#textareaName').val('unitDefault');
      $(".isblanderContainer").show().addClass("show");
      $(".isblandableContainer").show().addClass("show");
      $(".isHeaterContainer").show().addClass("show");
      $(".isHeatableContainer").show().addClass("show");
      $(".isSolutionContainer").show().addClass("show");
      $('#solution').prop('checked',true);
      $('#solution_yes').prop('checked',false);
    }
  });


});
