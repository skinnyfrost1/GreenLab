

 


$(document).ready(function () {
  var mousex = 0, mousey = 0; //这个..我也不知道是什么,网上找回来的代码原本就有了,要来做拖动用的.
  var divLeft, divTop;        //这个..我也不知道是什么,网上找回来的代码原本就有了,要来做拖动用的.
  let equips = [];            //所有的equipoment的id都会放在这里
  let selected;               //你正在拖动的equip的id
  let associated;             //将要和seleted发生一些什么的equip的id


  //当这个叫but的按钮被按下去以后,就会生成一些图片.
  $(".equipmentContainer").click(function () {
    var ws = document.getElementById('workspace').innerHTML;
    var id = "equip_" + equips.length;
    console.log(this.src);
    ws += '<div class="workspaceEquipment" id="' + id + '" ><img class="workspaceEquipment_img" id="' + id +'_img" src="'+this.src+'" style="position:absolute"><div>';
    document.getElementById('workspace').innerHTML = ws;
    var imgWidth = 2+$("#" + id+"_img").width();
    var imgHeight = 2+$("#"+ id+"_img").height();
    $("#"+id).css("width",imgWidth+"px");
    $("#"+id).css("height",imgHeight+"px");
    var el = document.getElementById(id);
    el.style.zIndex = equips.length;
    equips.push(id);

    //建立一个mousedown 的 event handler
    $(".workspaceEquipment").mousedown(function (e) {
      var id = $(this).attr('id'); //获取被点击的图片的id
      unAssociated();
      unSelected();
      doSelected(id);

      //当鼠标按下某个equipment, 会自动将他放到最顶,所以不会被其他东西盖住. 
      //图片在在第几层是与equips这个array有关, array里面的index是等于equipment在网页上CSS样式的z-index;
      var currentZ = equips.indexOf(id);
      if (currentZ != -1) {
        equips.splice(currentZ, 1);
        equips.push(id);
        for (var i = currentZ; i < equips.length; i++) {
          var element = document.getElementById(equips[i]);
          element.style.zIndex = i;
        }
      }

      var offset = $(this).offset();//鼠标按下后获得图片第一个像素点的坐标位置，此时offset是一个对象
      divLeft = parseInt(offset.left, 10);//将offset.left返回的字符串解析为十进制整数
      divTop = parseInt(offset.top, 10);
      mousey = e.pageY;//鼠标指针的纵坐标
      mousex = e.pageX;
      $(this).bind("mousemove", dragElement);//当按下后的鼠标移动时，调用dragElement函数
    });
  })

  function dragElement(event) {
    var left = divLeft + (event.pageX - mousex);//图片第一个像素当前x坐标=鼠标按下后第一个像素点的x坐标+（目前鼠标指针的x坐标-移动前鼠标指针的x坐标）
    var top = divTop + (event.pageY - mousey);
    $(this).css( //在css中设置图片的新位置
      {
        "top": top + "px",
        "left": left + "px",
        "position": "absolute"
      });
    return false;//如果不这样，鼠标松开时图片也会随着鼠标的移动而移动
  }

  $(document).mouseup(function (event)
  {
    $(".workspaceEquipment").unbind("mousemove");//当鼠标松开时，删除鼠标移动的处理程序
    
    
    if (selected != null) {
      for (var i = equips.length - 1; i >= 0; i--) {
        if (equips[i] != selected) {
          var overlap = isOverlap(event,equips[i]);
          if (overlap == true) {
            doAssociated(equips[i]);
            console.log("overlap!")
            break;
          }
          else
            console.log("not overlap");
        }
      }
    }
  });

    function isOverlap(event, b) {
    var bLeft, bRight, bTop, bBot;
    bLeft = $("#" + b).offset().left;
    bRight = bLeft + $("#" + b).width();
    bTop = $("#" + b).offset().top;
    bBot = bTop + $("#" + b).height();

    //check if event x,y in b
    if (event.pageX >= bLeft && event.pageX <= bRight
      && event.pageY >= bTop && event.pageY <= bBot)
      return true;

    // if ()
    //   return true;
    return false
  }

  //
  function unSelected() {
    $("#" + selected).css("border", "");
    selected = null;
  }

  function doSelected(id) {
    $("#" + id).css("border", "2px");
    $("#" + id).css("border-style", "dashed");
    selected = id;
  }


  //
  function doAssociated(id) {
    $("#" + id).css("border", "2px");
    $("#" + id).css("border-style", "dashed");
    $("#" + id).css("border-color", "#378ca3");
    associated=id;
  }

  function unAssociated() {
    $("#" + associated).css("border", "");
    associated=null;
  }

  function cleanSelectedAndAssociated(){
    unSelected();
    unAssociated();
  }



  

});