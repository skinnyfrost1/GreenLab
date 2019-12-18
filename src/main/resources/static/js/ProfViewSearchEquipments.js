

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
