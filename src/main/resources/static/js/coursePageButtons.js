var addLabToCourseButton
var flag = 0
function ButtonMenu(){
    addLabToCourseButton = document.getElementById("addLabToCourse")
    addLabToCourseButton.onclick = addLabToCourseButtonFunction()
}

function addLabToCourseButtonFunction() {
    console.log("11111111111111111111111111111111111")
    addLabPageContainer = document.getElementById("addLabPage")
    labMenuContainer = document.getElementById("labMenu")
    if(flag == 0){
        $("addLabPage").style.visibility="hidden"
        $("labMenu").style.visibility=""
        flag=1;
    }
    else{
        $("addLabPage").style.visibility=""
        $("labMenu").style.visibility="hidden"
        flag=1;
    }
}