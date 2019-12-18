

function GetCheckBoxListValues()
{
    var listOfEquipId = [];
    var numberOfEquipments = document.getElementById('numberOfEquipments').innerText;
    numberOfEquipments = parseInt(numberOfEquipments);
    for(var i = 0; i < numberOfEquipments; i++){
        var chkBox = document.getElementById('equipSelected_' + i);
        if(chkBox.checked){
            listOfEquipId.push(chkBox.value.toString());
        }
    }
    $.ajax({
        type: "POST",
        url: "/equipment/add",
        data: {
            listOfEquipId: listOfEquipId
        },
        traditional: true,
        dataType: "json",
        cache: false,
        timeout: 600000,
        success: function (result) {
            console.log("Add Equipment success");
            window.location.replace("/equipments");
            // var success = result['bool'];
            // var someId = 'div_' + studentEmail.id;
            // document.getElementById(someId).style.display = 'none';
            // // location.reload();
            // if (success===true){
            //     console.log(result);
            //     // window.location.replace("/course/edit");
            // } else{
            //     $('#errorMessage').html("Error");
            //     $('#errorMessage').css("color","red");
            // }
        },
        error: function (e) {
            window.location.replace("/equipments");
            console.log("Error")
            // $('#errorMessage').html(result['message']);
            // $('#errorMessage').css("color","red");
        }
    });


}

// $(document).ready(function () {
//   var equipmentContainer = document.getElementById("equipmentsContainer");
//   var posting = {};
//   posting['str'] = "give me search equipments"
//   $.ajax({
//     type: "POST",
//     contentType: "application/json",
//     url: "/equipment/search",
//     data: JSON.stringify(posting),
//     dataType: 'json',
//     cache: false,
//
//     timeout: 600000,
//     success: function (result) {
//       console.log(result);
//       console.log(result.length)
//       for (var i = 0; i < result.length; i++) {
//         var _id = result[i]._id;
//         var equipmentName = result[i].equipmentName;
//         var data = result[i].image;
//         var str = equipmentContainer.innerHTML;
//         equipmentContainer.innerHTML = str + '<div class="equipment viewEquipContainer webfont" id="' + _id + '">' + equipmentName +
//           '<img class="imageContainer" src = "data:image/png;base64,' + data + '">' + '</div>'
//       }
//     },
//     error: function (e) {
//
//     }
//   });
//
// });
