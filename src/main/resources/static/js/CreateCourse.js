
$("#addLabBigContainer").hide().removeClass("show");
console.log("1");
$("#saveBtnContainer").hide().removeClass("show");
console.log("2");
$("#previousBtnContainer").hide().removeClass("show");  
console.log("3");
var labs={
    "course1":{
        "courseName": "niubi",
        "courseTerm": "fall 1111",
        "labName": "lab001",
    },
    "course2":{
        "courseName": "wudi",
        "courseTerm": "fall 2222",
        "labName": "lab002",
    },
    "course3":{
        "courseName": "shuai",
        "courseTerm": "fall 3333",
        "labName": "lab003",
    }

};

$(document).ready(function(e) {
    // $("#addLabBtn").click(function(e) {
    //     if( $("#labMenu").hasClass("show") ){
    //         // 执行隐藏
    //         $("#labMenu").hide().removeClass("show");
    //         $("#addLabBtn").hide().removeClass("show");
    //
    //         $("#addLabPage").empty();
    //         var str = '';
    //         $.each(labs, function (value) {
    //             console.log(value)
    //             str+=   '            <div class="bigLabContainer" >\n' +
    //                 '              <table class="labContainer" >\n' +
    //                 '                <col width="30%"/>\n' +
    //                 '                <col width="40%"/>\n' +
    //                 '                <col width="30%"/>\n' +
    //                 '                  <tr>\n' +
    //                 '                    <td class="courseCellPadding">\n' +
    //                 '                        <div>'+value.courseName+'</div>\n' +
    //                 '                    </td>\n' +
    //                 '                    <td class="courseCellPadding">\n' +
    //                 '                      <div class="webfont">[ '+value.courseTerm+' ]</div>\n' +
    //                 '                    </td>\n' +
    //                 '                    <td class="courseCellPadding">\n' +
    //                 '                      <div class="webfont">'+value.labName+'</div>\n' +
    //                 '                    </td>\n' +
    //                 '                    <td>\n' +
    //                 '                      <input type="checkbox" name="select1" value="lab03">'  +
    //                 '                    </td>' +
    //                 '                  </tr>\n' +
    //                 '                </table>\n' +
    //                 '              </div>\n'
    //         })
    //         str+='<div class="webfont" style="text-align:left; margin: 0.5em 0;">hello world!!!!!</div>\n';
    //         $("#addLabPage").append(str);
    //
    //         $("#addLabPage").show().addClass("show");
    //         $("#addLabBtn").hide().removeClass("show");
    //         $("#confirmAddCourseBtn").show().addClass("show");
    //     }

        // $.ajax({
        //     type: "POST",
        //     contentType: "application/json",
        //     url: "/requestLabMenu",
        //     data: JSON.stringify("CHE 133"),
        //     dataType: 'json',
        //     cache: false,
        //     timeout: 600000,
        //     success: function (data) {
        //         var isExist = data['exist'];
        //     },
        // });
    // });
    // $("#confirmAddCourseBtn").click(function(e) {
    //     $("#confirmAddCourseBtn").hide().removeClass("show");
    //     $("#labMenu").show().addClass("show");
    //     $("#addLabBtn").show().addClass("show");
    //     $("#addLabPage").hide().removeClass("show");
    //
    // });
    $("#nextBtn").click(function (e) {
        $("#addLabBigContainer").show().addClass("show");
        $("#labMenu").show().hide().removeClass("show");

        $("#saveBtnContainer").show().addClass("show");
        $("#previousBtnContainer").show().addClass("show");
        $("#inputInfoPage").hide().removeClass("show");
        $("#addLabPage").empty();
        var str='';



        // var labs={
        //     "course1":{
        //         "courseName": "niubi",
        //         "courseTerm": "fall 1111",
        //         "labName": "lab001",
        //     },
        //     "course2":{
        //         "courseName": "wudi",
        //         "courseTerm": "fall 2222",
        //         "labName": "lab002",
        //     },
        //     "course3":{
        //         "courseName": "shuai",
        //         "courseTerm": "fall 3333",
        //         "labName": "lab003",
        //     }
        //
        // };

        var labs1 = {};
        var course1 = {};
        course1.courseName = "niubi";
        course1.courseTerm = "fall 111";
        course1.labName = "lab001";
        labs1["course1"] = course1;
        var course2 = {};
        course2.courseName = "2222";
        course2.courseTerm = "fall 2222";
        course2.labName = "lab002";
        labs1["course2"] = course2;
        var course3 = {};
        course3.courseName = "3333";
        course3.courseTerm = "fall 3333";
        course3.labName = "lab003";
        labs1["course3"] = course3;

        for( var i  = 0 ; i< Object.keys(labs1).length ; i++ ){

            var item1 =  labs1[Object.keys(labs1)[i]];
            var itemKeyArr = Object.keys(item1);


            console.log(  labs1[Object.keys(labs1)[i]] );
            str+=   '            <div class="bigLabContainer" >\n' +
                '              <table class="labContainer" >\n' +
                '                <col width="30%"/>\n' +
                '                <col width="40%"/>\n' +
                '                <col width="30%"/>\n' +
                '                  <tr>\n' +
                '                    <td class="courseCellPadding">\n' +
                '                        <div>'+item1[itemKeyArr[0]]+'</div>\n' +
                '                    </td>\n' +
                '                    <td class="courseCellPadding">\n' +
                '                      <div class="webfont">[ '+item1[itemKeyArr[1]]+' ]</div>\n' +
                '                    </td>\n' +
                '                    <td class="courseCellPadding">\n' +
                '                      <div class="webfont">'+item1[itemKeyArr[2]]+'</div>\n' +
                '                    </td>\n' +
                '                    <td>\n' +
                '                      <input type="checkbox" name="select1" value="lab03">'  +
                '                    </td>' +
                '                  </tr>\n' +
                '                </table>\n' +
                '              </div>\n'
        }

        // console.log(labs1)
        // var b = JSON.parse(JSON.stringify(labs));
        // $.each(labs1, function (value) {
        //     console.log(value)
        //     str+=   '            <div class="bigLabContainer" >\n' +
        //         '              <table class="labContainer" >\n' +
        //         '                <col width="30%"/>\n' +
        //         '                <col width="40%"/>\n' +
        //         '                <col width="30%"/>\n' +
        //         '                  <tr>\n' +
        //         '                    <td class="courseCellPadding">\n' +
        //         '                        <div>'+value[0]+'</div>\n' +
        //         '                    </td>\n' +
        //         '                    <td class="courseCellPadding">\n' +
        //         '                      <div class="webfont">[ '+value.courseTerm+' ]</div>\n' +
        //         '                    </td>\n' +
        //         '                    <td class="courseCellPadding">\n' +
        //         '                      <div class="webfont">'+value.labName+'</div>\n' +
        //         '                    </td>\n' +
        //         '                    <td>\n' +
        //         '                      <input type="checkbox" name="select1" value="lab03">'  +
        //         '                    </td>' +
        //         '                  </tr>\n' +
        //         '                </table>\n' +
        //         '              </div>\n'
        // })
        // str+='<div class="webfont" style="text-align:left; margin: 0.5em 0;">hello world!!!!!</div>\n';
        $("#addLabPage").append(str);
        $("#addLabPage").show().addClass("show");
        $("#addLabBigContainer").show().addClass("show");


    })
    $("#previousBtnContainer").click(function (e){
        $("#inputInfoPage").show().addClass("show");
        $("#nextBtn").show().addClass("show");
        $("#addLabBigContainer").hide().removeClass("show");
        $("#saveBtnContainer").hide().removeClass("show");
        $("#previousBtnContainer").hide().removeClass("show");
    })
    // $("#confirmAddCourse").click(function (e) {
    //     console.log("asdasdasdasd")
    //     $("#addLabPage").html('');
    //     $("#addLabPage").toggle();
    //     $("#labMenu").toggle();
    //     $("#addLabBtn").toggle();
    // })
});