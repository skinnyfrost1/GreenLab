// $(document).ready(function(){
// console.log("Ready!!");
function ahhhhh(studentEmail) {
    console.log("on click!!");
    $.ajax({
        type: "POST",
        url: "/course/edit/deleteStu",
        data: {
            courseId: $('#courseId').val(),
            studentEmail: studentEmail.id
        },
        cache: false,
        timeout: 600000,
        success: function (result) {
            var success = result['bool'];
            console.log(result);
            var someId = 'div_' + studentEmail.id;
            document.getElementById(someId).style.display = 'none';
            // location.reload();
            if (success===true){
                // window.location.replace("/course/edit");
            } else{
                $('#errorMessage').html(result['message']);
                $('#errorMessage').css("color","red");
            }
        },
        error: function (e) {
// 那我们打字也可以 哈哈哈哈哈哈
            console.log("Error")
            $('#errorMessage').html(result['message']);
            $('#errorMessage').css("color","red");
        }
    });
}
//)
//   });

