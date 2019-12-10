$(document).ready(function () {
  var equipmentContainer = document.getElementById("equipmentsContainer");
  var posting = {};
  posting['str'] = "give me equipments"
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/equipments",
    data: JSON.stringify(posting),
    dataType: 'json',
    cache: false,

    timeout: 600000,
    success: function (result) {
      console.log(result);
      console.log(result.length)
      for (var i = 0; i < result.length; i++) {
        var _id = result[i]._id;
        var equipmentName = result[i].equipmentName;
        var data = result[i].image;
        var str = equipmentContainer.innerHTML;
        equipmentContainer.innerHTML = str + '<div class="equipment viewEquipContainer" id="' + _id + '">' + equipmentName +
          '<img src = "data:image/png;base64,' + data + '">' + '</div>'
      }
    },
    error: function (e) {

    }
  });

});