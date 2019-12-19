
$(document).ready(function () {
    $(".createLabInfoPicture").click(function (e) {
        if (e.target.tagName != 'INPUT') {
            var checkBoxes = $(this).find('input')
            checkBoxes.prop("checked", !checkBoxes.prop("checked"));
            return false;
        }
    });

    // $(".bigLabContainer").click(function (e) {
    //     if (e.target.tagName != 'INPUT') {
    //         $(this).find("input").toggleCheckbox();
    //         return false;
    //     }
    // });
    // $('.equipsToAdd').on('click', function(){
    //     var checkbox = $(this).children('input[type="checkbox"]');
    //     console.log(checkbox)
    //     checkbox.prop('checked', !checkbox.prop('checked'));
    // });

});

// var str={};
// str['str']=$("input[name=courseId]").val();;
// // console.log("lab name is "+str);
// $.ajax({
//     type: "POST",
//     contentType: "application/json",
//     url: "/course/create/requestlabmenu",
//     data: JSON.stringify(str),
//     dataType: 'json',
//     cache: false,
//     timeout: 600000,
//     success: function (data) {
//         console.log("data is "+data)
//         labNameList = data['labNameList']
//         console.log("lab name list is "+labNameList)
//         console.log("its size is "+labNameList.length)
//         for(var i=0;i<labNameList.length;i++){
//             console.log("success")
//             htmlStr+=   '            <div class="bigLabContainer" >\n' +
//                 '              <div class="createCourseLabSelection">\n' +
//                 '              <table class="labContainer" >\n' +
//                 '                <col width="30%"/>\n' +
//                 '                <col width="40%"/>\n' +
//                 '                <col width="30%"/>\n' +
//                 '                  <tr>\n' +
//                 '                    <td class="courseCellPadding">\n' +
//                 '                        <div>'+labNameList[i]+'</div>\n' +
//                 '                    </td>\n' +
//                 '                    <td>\n' +
//                 '                      <input class="webfont" type="checkbox" id="select' + i + '" name="select" value="'+labNameList[i]+'">'  +
//                 '                    </td>' +
//                 '                  </tr>\n' +
//                 '                </table>\n' +
//                 '                </div>\n' +
//                 '              </div>\n'
//         }
//         // CreateCourseLoadLabListener(labNameList.length)
//         $("#addLabPage").append(htmlStr);
//         $("#addLabPage").show().addClass("show");
//         $("#addLabBigContainer").show().addClass("show");
//         $(".createCourseLabSelection").click(function (e) {
//             if (e.target.tagName != 'INPUT') {
//                 var checkBoxes = $(this).find('input')
//                 checkBoxes.prop("checked", !checkBoxes.prop("checked"));
//                 return false;
//             }
//         });
//     },
// });


//
// for( var i  = 0 ; i< Object.keys(labs1).length ; i++ ){
//
//     var item1 =  labs1[Object.keys(labs1)[i]];
//     var itemKeyArr = Object.keys(item1);
//
//
//     // console.log(  labs1[Object.keys(labs1)[i]] );
//     str+=   '            <div class="bigLabContainer" >\n' +
//         '              <table class="labContainer" >\n' +
//         '                <col width="30%"/>\n' +
//         '                <col width="40%"/>\n' +
//         '                <col width="30%"/>\n' +
//         '                  <tr>\n' +
//         '                    <td class="courseCellPadding">\n' +
//         '                        <div>'+item1[itemKeyArr[0]]+'</div>\n' +
//         '                    </td>\n' +
//         '                    <td class="courseCellPadding">\n' +
//         '                      <div class="webfont">[ '+item1[itemKeyArr[1]]+' ]</div>\n' +
//         '                    </td>\n' +
//         '                    <td class="courseCellPadding">\n' +
//         '                      <div class="webfont">'+item1[itemKeyArr[2]]+'</div>\n' +
//         '                    </td>\n' +
//         '                    <td>\n' +
//         '                      <input type="checkbox" name="select1" value="lab03">'  +
//         '                    </td>' +
//         '                  </tr>\n' +
//         '                </table>\n' +
//         '              </div>\n'
// }

