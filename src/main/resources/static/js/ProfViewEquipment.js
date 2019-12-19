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

        // equipmentContainer.innerHTML = str + '<div class="equipment viewEquipContainer webfont" id="' + _id + '">' + equipmentName +
        //   '<img class="imageContainer" src = "data:image/png;base64,' + data + '">' + '</div>'

        equipmentContainer.innerHTML = str +
            '                    <div class="bigLabContainer">\n' +
            '                    <div class="ViewEquipmentContainer">\n' +
            '                        <table class="labContainer" >\n' +
            '                            <col width="30%"/>\n' +
            '                            <col width="40%"/>\n' +
            '                            <col width="30%"/>\n' +
            '                            <!-- <col width="20%"/> -->\n' +
            '                            <!-- <tbody style="padding: 0; margin: 0;"> -->\n' +
            '                            <tr>\n' +
            '                                <td class="courseCellPadding">\n' +
            '                                    <div class="equipment">\n' +
            '                                       <img src = "data:image/png;base64,' + data + '" style="width:80px;height:80px">' +
            '                                    </div>\n' +
            '                                </td>\n' +
            '                                <td class="courseCellPadding">\n' +
            '                                    <div class="webfont" >' + equipmentName + '</div>\n' +
            '                                </td>\n' +
            '                                <td class="courseCellPadding">\n' +
            '                                    <!--                            <div class="checkBoxDiv">-->\n' +
            '                                    <input class="webfont" name="viewEquipmentsList" type="checkbox"\n' +
            '                                          value="' + _id + '" id="' + _id + '" style="width:20px;height:20px"></input>\n' +
            '                                    <!--                            </div>-->\n' +
            '                                </td>\n' +
            '                            </tr>\n' +
            '                        </table>\n' +
            '                    </div>\n' +
            '                    </div>'

        $(".bigLabContainer").click(function (e) {
          if (e.target.tagName != 'INPUT') {
            var checkBoxes = $(this).find('input')
            checkBoxes.prop("checked", !checkBoxes.prop("checked"));
            return false;
          }
        });



      }
    },
    error: function (e) {

    }
  });

  $("#equipDelete").click(function(){
    if (confirm("Do you really want to delete these items?")){
      $('form#deleteForm').submit();
    }
  });

});