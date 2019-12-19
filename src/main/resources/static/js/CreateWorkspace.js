//(╯°Д°)╯︵ ┻━┻
$(document).ready(function () {
  var mousex = 0, mousey = 0; //这个..我也不知道是什么,网上找回来的代码原本就有了,要来做拖动用的.
  var divLeft, divTop;        //这个..我也不知道是什么,网上找回来的代码原本就有了,要来做拖动用的.

  let equips = [];            //所有的equipoment的id都会放在这里
  let equipsData = [];
  let stepnumber = 0;

  let selected;               //你正在拖动的equip的id
  let associated;             //将要和seleted发生一些什么的equip的id

  let selectedData;
  let associatedData;
  let solutionMaterialsS = [];
  let solutionMaterialsA = [];
  let solutionEquipments = [];
  let NewLookS_id;
  let NewLookA_id;
  let hint;






  //(╯°Д°)╯︵ ┻━┻

  let solutionMaterialCounter = 1;


  var lab_id = document.getElementById("lab_id").innerHTML;
  console.log("lab_id  = " + lab_id);

  // function LabEquipment(equipment_id, htmlid, nickname, material, blandable, blander, heatable, heater, tempreature, material) {
  function LabEquipment(equipment_id, htmlid, nickname, material, blandable, blander, heatable, heater, materials) {

    this.equipment_id = equipment_id;
    this.htmlid = htmlid;
    this.nickname = nickname;
    this.material = material;
    this.blandable = blandable;
    this.blander = blander;
    this.heatable = heatable;
    this.heater = heater;
    // this.tempreature = tempreature;
    this.materials = materials;

  }

  function Material(material, quantity, unit) {
    this.material = material;
    this.quantity = quantity;
    this.unit = unit;
  }


  //当这个叫but的按钮被按下去以后,就会生成一些图片.
  $(".equipmentContainer").click(function () {
    var ws = document.getElementById('workspace').innerHTML;
    var id = "equip_" + equips.length;
    // console.log(this.src);
    ws += '<div class="workspaceEquipment" id="' + id + '" ><img class="workspaceEquipment_img" id="' + id + '_img" src="' + this.src + '" style="position:absolute"><div>';
    document.getElementById('workspace').innerHTML = ws;
    var imgWidth = 2 + $("#" + id + "_img").width();
    var imgHeight = 2 + $("#" + id + "_img").height();
    $("#" + id).css("width", imgWidth + "px");
    $("#" + id).css("height", imgHeight + "px");
    var el = document.getElementById(id);
    el.style.zIndex = equips.length;
    equips.push(id);


    //ajax.create a labequipment on server.
    var equipment_id = this.id;
    var post = {};
    post['equipment_id'] = equipment_id;
    post['htmlid'] = id;
    post['nickname'] = id;

    console.log(post);
    $.ajax({
      type: "POST",
      contentType: "application/json",
      url: "/workspace/addequipment/" + lab_id,
      data: JSON.stringify(post),
      dataType: 'json',
      cache: false,
      timeout: 600000,
      success: function (result) {
        //id is html id
        //equipment_id is object id in server
        console.log(result.message);
        var data = result.labEquipment;

        equipment_id = data.equipment_id;
        var htmlid = data.htmlid;
        var nickname = data.nickname;

        var material = data.material;
        var blandable = data.blandable;
        var blander = data.blander;
        var heatable = data.heatable;
        var heater = data.heater;
        // var tempreature = data.tempreature;
        var materials = data.materials;

        var labequipment = new LabEquipment(equipment_id, htmlid, nickname, material, blandable, blander, heatable, heater, materials);
        equipsData.push(labequipment);
        console.log(equipsData);
      },
      error: function (e) {
        console.log(e);
      }
    });

    //建立一个mousedown 的 event handler
    $(".workspaceEquipment").mousedown(function (e) {
      var id = $(this).attr('id'); //获取被点击的图片的id
      var selectedOrAssociated = "selectedEquipment";
      unAssociated();
      unSelected();
      doSelected(id);
      showProperties(id, selectedOrAssociated);

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

  $(document).mouseup(function (event) {
    $(".workspaceEquipment").unbind("mousemove");//当鼠标松开时，删除鼠标移动的处理程序


    if (selected != null) {
      for (var i = equips.length - 1; i >= 0; i--) {
        if (equips[i] != selected) {
          var overlap = isOverlap(event, equips[i]);
          if (overlap == true) {
            doAssociated(equips[i]);
            showInteractionForm(selected, equips[i]);
            console.log("overlap!")
            break;
          }
          else
            console.log("not overlap");
        }
      }
    }
  });




  function showInteractionForm(selected, associated) {
    var materials;
    var selectedTitle;
    var selectedMaterialsHtml = "";
    var associatedTitle;
    var associatedMaterialsHtml = "";

    var id = selected;
    for (var i = 0; i < equipsData.length; i++) {
      if (id == equipsData[i].htmlid) {
        associatedTitle = '<div>Get from ' + equipsData[i].nickname + '</div></br>';
        materials = equipsData[i].materials;
        selectedData = equipsData[i];

        break;
      }
    }
    if (materials) {
      for (var i = 0; i < materials.length; i++) {
        var materialName = materials[i].material;
        var unit = materials[i].unit;
        selectedMaterialsHtml = selectedMaterialsHtml + '<div>' +
          '<a class="smn" id="smn_' + (i + 1) + '">' + materialName + '</a>' +
          ':' +
          '<input id="smq_' + (i + 1) + '" type="number" name="smq_' + (i + 1) + '">' +
          '<a class="smu" id="smu_' + (i + 1) + '">' + unit + '</a>' +
          '</div></br>';
      }
    }

    id = associated
    for (var i = 0; i < equipsData.length; i++) {
      if (id == equipsData[i].htmlid) {
        selectedTitle = '<div>Add to ' + equipsData[i].nickname + '</div></br>';
        materials = equipsData[i].materials;
        associatedData = equipsData[i];
        break;
      }
    }
    if (materials) {
      for (var i = 0; i < materials.length; i++) {
        var materialName = materials[i].material;
        var unit = materials[i].unit;
        associatedMaterialsHtml = associatedMaterialsHtml + '<div>' +
          '<a class="amn" id="amn_' + (i + 1) + '">' + materialName + '</a>' +
          ':' +
          '<input id="amq_' + (i + 1) + '" type="number" name="amq_' + (i + 1) + '">' +
          '<a class="amu" id="amu_' + (i + 1) + '">' + unit + '</a>' +
          '</div></br>';
      }
    }
    $('#addToAssociated').html(selectedTitle + selectedMaterialsHtml);
    $('#getFromAssociated').html(associatedTitle + associatedMaterialsHtml);
    $("#actionPopUp").css("visibility", "visible");
    $("#interactionButton").css("visibility", "visible");

  }

  $("#interactionButton").click(function () {
    if (!selectedData) {
      console.log("selectedData = null")
      return false;
    }
    if (!associatedData) {
      console.log("associatedData = null")
      return false;
    }
    var am = associatedData.materials;
    var amBuffer = cloneMaterials(associatedData.materials);
    var sm = selectedData.materials;
    var smBuffer = cloneMaterials(selectedData.materials);

    //check if the out going quantity is more then itself.
    // if (selectedData) {
    for (var i = 0; i < selectedData.materials.length; i++) {
      var smq = $('input[name=smq_' + (i + 1) + ']').val();
      smq = parseFloat(smq);
      var smn = $('#smn_' + (i + 1)).text();
      var smu = $('#smu_' + (i + 1)).text();
      //要注意如果smq input没有输入任何东西的时候,下面这个 if 能否吃到这个case
      if (!smq) {
        continue;
      }
      //error.
      if (smq > selectedData.materials[i].quantity) {
        console.log("Error: You don't have enought " + smn);
        alert("Error: You don't have enought " + smn);
        $('#addToAssociated').html('');
        $('#getFromAssociated').html('');
        $('#interactionButton').css('visibility','hidden');
        return false;
      }
      //if am is empty
      if (!amBuffer) {
        amBuffer = [];
        var temp = {};
        temp.material = smn;
        temp.quantity = smq;
        smBuffer[i].quantity -= smq;
        temp.unit = smu
        amBuffer.push(temp);
      }
      //if am has something 
      else {
        var hasSameMaterial = false
        for (var j = 0; j < amBuffer.length; j++) {
          console.log("test1");
          console.log(amBuffer[j].material);
          console.log(smn);
          if (amBuffer[j].material == smn) {
            amBuffer[j].quantity += smq;
            smBuffer[i].quantity -= smq;
            hasSameMaterial = true;
            break;
          }
        }
        if (!hasSameMaterial) {
          var temp = {};
          temp.material = smn;
          temp.quantity = smq;
          temp.unit = smu
          smBuffer[i].quantity -= smq;
          amBuffer.push(temp);
        }
      }
    }
    // }
    //associated object
    for (var i = 0; i < associatedData.materials.length; i++) {
      var amq = $('input[name=amq_' + (i + 1) + ']').val();
      amq = parseFloat(amq);
      //要注意如果amq input没有输入任何东西的时候,下面这个 if 能否吃到这个case
      if (!amq) {
        continue;
      }
      //error.
      if (amq > associatedData.materials[i].quantity) {
        console.log("Error: You don't have enought " + amn);
        alert("Error: You don't have enought " + amn);
        $('#addToAssociated').html('');
        $('#getFromAssociated').html('');
        $('#interactionButton').css('visibility','hidden');
        return false;
      }
      var amn = $('#amn_' + (i + 1)).text();
      var amu = $('#amu_' + (i + 1)).text();
      //if am is empty
      if (!smBuffer) {
        smBuffer = [];
        var temp = {};
        temp.material = amn;
        temp.quantity = amq;
        amBuffer[i].quantity -= amq;
        temp.unit = amu
        smBuffer.push(temp);
      }
      //if am has something 
      else {
        var hasSameMaterial = false
        for (var j = 0; j < smBuffer.length; j++) {
          if (smBuffer[j].material == amn) {
            smBuffer[j].quantity += amq;
            amBuffer[i].quantity -= amq;
            hasSameMaterial = true;
            break;
          }
        }
        if (!hasSameMaterial) {
          var temp = {};
          temp.material = amn;
          temp.quantity = amq;
          amBuffer[i].quantity -= amq;
          temp.unit = amu
          smBuffer.push(temp);
        }
      }
    }

    selectedData.materials = smBuffer;
    associatedData.materials = amBuffer;
    showProperties(selected, "selectedEquipment");
    showProperties(associated, "associatedEquipment")
    $("#interactionButton").css("visibility", "hidden");
    $("#addToAssociated").html("");
    $("#getFromAssociated").html("");



    // $("#solutionS").css("visibility", "visible");
    // $("#solutionSButton").css("visibility", "visible");

    showSolutionPanel(selectedData);

  });

  function showSolutionPanel(selectedData) {
    //建立一个框架for solution
    solutionMaterialCounter = 1;
    solutionMaterialsS = [];
    var htmlDOM = '<div id="solutionSPart1"></div>' +
      '<div id="solutionSDetails"></div>' +
      '<button id="solutionSSubmit" style="visibility:hidden;">Next</button>';
    $("#solutionS").html(htmlDOM);

    //第一个yes no 问题
    htmlDOM = '<div id="solutionSTitle1">Do you want to edit the solution of <a id="solutionSNickname">' + selectedData.nickname + '</a>?</div>' +
      '<div id="solutionSButton" style="visibility: visible;">' +
      '<button id="solutionSButton_yes">Yes</button>' +
      '<button id="solutionSButton_no">no</button>' +
      '</div>';
    $("#solutionSPart1").html(htmlDOM);


    //如果yes的话 建立下面细节问题
    // $("#solutionSButton_yes").click(function (selectedData) {
    $("#solutionSButton_yes").click(function () {
      var solutionSNickname = $("#solutionSNickname").text();
      $("#solutionSSubmit").css("visibility", "visible");
      $('#solutionSPart1').html("");

      var htmlDOM = '' +
        '<div id="solutionSTitle2">What <a>' + solutionSNickname + '</a> should contain?</div>' +
        '<div id="solutionSM"></div>' +
        '<button id="solutionSMoreBut">Add a new material</button>' +
        '<button id="solutionSNewLookBut">Choose a new look</button>' +
        '<div id="solutionSNewLooks"></div>';
      $('#solutionSDetails').html(htmlDOM);

      //增加一条material
      $("#solutionSMoreBut").click(function () {
        // var solutionSMDOM = $("#solutionSM").html();
        var solutionSMDOM = '' +
          '<div id="solutionSM_' + solutionMaterialCounter + '"></div>' +
          '<div id="solutionSMN_' + solutionMaterialCounter + '">Name: <input type="text" name="solutionSMNI_' + solutionMaterialCounter + '" required></div>' +
          '<div id="solutionSMQ_' + solutionMaterialCounter + '">Quantity: <input type="number" name="solutionSMQI_' + solutionMaterialCounter + '" required></div>' +
          '<div id="solutionSMU_' + solutionMaterialCounter + '">Unit: <input type="text" name="solutionSMUI_' + solutionMaterialCounter + '" required></div>' +
          '</br>';
        $("#solutionSM").append(solutionSMDOM);
        solutionMaterialCounter++;
      });
      //read solution requipment
      $('#solutionSNewLookBut').click(function () {
        var posting = {};
        posting['str'] = lab_id;
        $.ajax({
          type: "POST",
          contentType: "application/json",
          url: "/lab/create/workspace/getnewlooks/",
          data: JSON.stringify(posting),
          dataType: 'json',
          cache: false,
          timeout: 600000,
          success: function (result) {
            console.log(result.message);
            var htmlDOM = "";
            solutionEquipments = result.resEquipments;
            for (var i = 0; i < solutionEquipments.length; i++) {
              htmlDOM += '' +
                '<div id="solutionNewLook_' + i + '">' +
                '<img src="' + solutionEquipments[i].image + '" style="width:80px; height:80px">' +
                '<a>' + solutionEquipments[i].equipmentName + '</a>' +
                '<input type="radio" name="Newlook_id" value="' + solutionEquipments[i]._id + '">' +
                '</div></br>';
            }
            $("#solutionSNewLooks").html(htmlDOM);
            $("#solutionSNewLookBut").css("visibility", "hidden");
          },
          error: function (e) {
          }
        });
      });

      $("#solutionSSubmit").click(function () {
        // solutionSSubmit
        NewLookS_id = $('input[name=Newlook_id]:checked').val()
        for (var i = 1; i < solutionMaterialCounter; i++) {
          var ssmn = $('input[name=solutionSMNI_' + i + ']').val();
          var ssmq = $('input[name=solutionSMQI_' + i + ']').val();
          var ssmu = $('input[name=solutionSMUI_' + i + ']').val();
          if (!ssmn) {
            alert("Material name can not be empty.");
            $('#solutionSMN_' + i).css("color", "red");
            solutionMaterialsS = [];
            break;
          }
          if (!ssmq) {
            alert("Quantity can not be empty.");
            $('#solutionSMQ_' + i).css("color", "red");
            solutionMaterialsS = [];
            break;
          }
          if (!ssmu) {
            alert("Unit can not be empty.");
            $('#solutionSMU_' + i).css("color", "red");
            solutionMaterialsS = [];
            break;
          }
          var ssm = new Material(ssmn, ssmq, ssmu);
          solutionMaterialsS.push(ssm);
          console.log("ssmn=" + ssmn)
        }
        console.log("newlooks_id" + NewLookS_id);
        console.log("solutionMaterialsS=" + solutionMaterialsS);
        $("#solutionS").html("");
        showSolutionPanel2();
      });
    });
    $("#solutionSButton_no").click(function () {
      // $("#solutionSSubmit").css("visibility","visible");
      $("#solutionSSubmit").css("visibility", "visible");
      $("#solutionSPart1").html("");
      $("#solutionSSubmit").click(function () {
        solutionMaterialsS = [];
        NewLookS_id = "";
        $("#solutionS").html('');
        showSolutionPanel2();
      })
      // $("#solutionSDetails").css("visibility","visible");
    });
  }

  function showSolutionPanel2() {
    solutionMaterialCounter = 1;
    solutionMaterialsA = [];
    var htmlDOM = '<div id="solutionAPart1"></div>' +
      '<div id="solutionADetails"></div>' +
      '<button id="solutionASubmit" style="visibility:hidden;">Done</button>';
    $("#solutionA").html(htmlDOM);


    //第一个yes no 问题
    htmlDOM = '<div id="solutionATitle1">Do you want to edit the solution of <a id="solutionANickname">' + associatedData.nickname + '</a>?</div>' +
      '<div id="solutionAButton" style="visibility: visible;">' +
      '<button id="solutionAButton_yes">Yes</button>' +
      '<button id="solutionAButton_no">no</button>' +
      '</div>';

    $("#solutionAPart1").html(htmlDOM);


    //如果yes的话 建立下面细节问题
    $("#solutionAButton_yes").click(function () {
      var solutionANickname = $("#solutionANickname").text();
      $("#solutionASubmit").css("visibility", "visible");
      $('#solutionAPart1').html("");

      var htmlDOM = '' +
        '<div id="solutionATitle2">What <a>' + solutionANickname + '</a> should contain?</div>' +
        '<div id="solutionAM"></div>' +
        '<button id="solutionAMoreBut">Add a new material</button>' +
        '<button id="solutionANewLookBut">Choose a new look</button>' +
        '<div id="solutionANewLooks"></div>';
      $('#solutionADetails').html(htmlDOM);


      //增加一条material
      $("#solutionAMoreBut").click(function () {
        // var solutionAMDOM = $("#solutionAM").html();
        var solutionAMDOM = '' +
          '<div id="solutionAM_' + solutionMaterialCounter + '"></div>' +
          '<div id="solutionAMN_' + solutionMaterialCounter + '">Name: <input type="text" name="solutionAMNI_' + solutionMaterialCounter + '" required></div>' +
          '<div id="solutionAMQ_' + solutionMaterialCounter + '">Quantity: <input type="number" name="solutionAMQI_' + solutionMaterialCounter + '" required></div>' +
          '<div id="solutionAMU_' + solutionMaterialCounter + '">Unit: <input type="text" name="solutionAMUI_' + solutionMaterialCounter + '" required></div>' +
          '</br>';
        $("#solutionAM").append(solutionAMDOM);
        solutionMaterialCounter++;
      });

      //read solution requipment
      $('#solutionANewLookBut').click(function () {
        var posting = {};
        posting['str'] = lab_id;
        $.ajax({
          type: "POST",
          contentType: "application/json",
          url: "/lab/create/workspace/getnewlooks/",
          data: JSON.stringify(posting),
          dataType: 'json',
          cache: false,
          timeout: 600000,
          success: function (result) {
            console.log(result.message);
            var htmlDOM = "";
            solutionEquipments = result.resEquipments;
            for (var i = 0; i < solutionEquipments.length; i++) {
              htmlDOM += '' +
                '<div id="solutionNewLook_' + i + '">' +
                '<img src="' + solutionEquipments[i].image + '" style="width:80px; height:80px">' +
                '<a>' + solutionEquipments[i].equipmentName + '</a>' +
                '<input type="radio" name="Newlook_id" value="' + solutionEquipments[i]._id + '">' +
                '</div></br>';
            }
            $("#solutionANewLooks").html(htmlDOM);
            $("#solutionANewLookBut").css("visibility", "hidden");
          },
          error: function (e) {
          }
        });
      });


      $("#solutionASubmit").click(function () {
        // solutionASubmit
        NewLookA_id = $('input[name=Newlook_id]:checked').val()
        for (var i = 1; i < solutionMaterialCounter; i++) {
          var samn = $('input[name=solutionAMNI_' + i + ']').val();
          var samq = $('input[name=solutionAMQI_' + i + ']').val();
          var samu = $('input[name=solutionAMUI_' + i + ']').val();
          if (!samn) {
            alert("Material name can not be empty.");
            $('#solutionAMN_' + i).css("color", "red");
            solutionMaterialsA = [];
            break;
          }
          if (!samq) {
            alert("Quantity can not be empty.");
            $('#solutionSMQ_' + i).css("color", "red");
            solutionMaterialsA = [];
            break;
          }
          if (!samu) {
            alert("Unit can not be empty.");
            $('#solutionSMU_' + i).css("color", "red");
            solutionMaterialsA = [];
            break;
          }
          var asm = new Material(samn, samq, samu);
          solutionMaterialsA.push(asm);
          console.log("ssmn=" + samn)
        }
        console.log("newlooks_id" + NewLookA_id);
        console.log("solutionMaterialsA=" + solutionMaterialsA);
        $("#solutionA").html("");
        setStepInfo();
        //debug 
        console.log("check S data" + NewLookS_id);
        if (!solutionMaterialsS || solutionMaterialsS.length == 0) {
          console.log("solutionMaterialsS is empty");
        } else {
          for (var i = 0; i < solutionMaterialsS.length; i++) {
            console.log(solutionMaterialsS[i].material);
          }
        }
        console.log("check A data" + NewLookA_id);
        if (!solutionMaterialsA || solutionMaterialsA.length == 0) {
          console.log("solutionMaterialsA is empty");
        } else {
          for (var i = 0; i < solutionMaterialsA.length; i++) {
            console.log(solutionMaterialsA[i].material);
          }
        }
      });
    });

    $("#solutionAButton_no").click(function () {
      // $("#solutionASubmit").css("visibility","visible");
      $("#solutionASubmit").css("visibility", "visible");
      $("#solutionAPart1").html("");
      $("#solutionASubmit").click(function () {
        $('#solutionA').html('');
        solutionMaterialsA = [];
        NewLookA_id = "";
        setStepInfo();
        
        //debug 
        console.log("check S data" + NewLookS_id);
        if (!solutionMaterialsS || solutionMaterialsS.length == 0) {
          console.log("solutionMaterialsS is empty");
        } else {
          for (var i = 0; i < solutionMaterialsS.length; i++) {
            console.log(solutionMaterialsS[i].material);
          }
        }
        console.log("check A data" + NewLookA_id);
        if (!solutionMaterialsA || solutionMaterialsA.length == 0) {
          console.log("solutionMaterialsA is empty");
        } else {
          for (var i = 0; i < solutionMaterialsA.length; i++) {
            console.log(solutionMaterialsA[i].material);
          }
        }
      })
      // $("#solutionADetails").css("visibility","visible");
    });

    function setStepInfo() {
      var htmlDOM = 'Write a hint for this step.</br>' +
        '<input type="text" id = "stepInfoInput" name="stepInfoInput"></br>' +
        '<button id="stepInfoSubmit">Submit</button> '
      $("#stepInfo").html(htmlDOM);
      $('#stepInfoSubmit').click(function () {
        hint = $('input[name=stepInfoInput]').val();
        console.log("hint=" + hint);
        //post step

        //post factory

        //                           (╯°Д°)╯︵ ┻━┻ 

        stepnumber = stepnumber + 1;
        var posting = {};



        ///////////////////////
        // posting['selectedData_htmlid'] = selectedData.htmlid
        posting['selectedData'] = selectedData

        var selectedData_material = [];
        var selectedData_quantity = [];
        var selectedData_unit = [];
        for (var i = 0; i < selectedData.materials.length; i++) {
          selectedData_material.push(selectedData.materials[i].material);
          selectedData_quantity.push(selectedData.materials[i].quantity);
          selectedData_unit.push(selectedData.materials[i].unit);
        }
        posting['selectedData_material'] = selectedData_material;
        posting['selectedData_quantity'] = selectedData_quantity;
        posting['selectedData_unit'] = selectedData_unit;

        /////////////////////////////////
        // posting['associatedData_htmlid'] = associatedData.htmlid
        posting['associatedData'] = associatedData
        var associatedData_material = [];
        var associatedData_quantity = [];
        var associatedData_unit = [];
        for (var i = 0; i < associatedData.materials.length; i++) {
          associatedData_material.push(associatedData.materials[i].material);
          associatedData_quantity.push(associatedData.materials[i].quantity);
          associatedData_unit.push(associatedData.materials[i].unit);
        }
        posting['associatedData_material'] = associatedData_material;
        posting['associatedData_quantity'] = associatedData_quantity;
        posting['associatedData_unit'] = associatedData_unit;

        /////////////////////////////////
        // posting['solutionMaterialsS'] = solutionMaterialsS
        var solutionMaterialsS_material = [];
        var solutionMaterialsS_quantity = [];
        var solutionMaterialsS_unit = [];
        for (var i = 0; i < solutionMaterialsS.length; i++) {
          solutionMaterialsS_material.push(solutionMaterialsS[i].material);
          solutionMaterialsS_quantity.push(solutionMaterialsS[i].quantity);
          solutionMaterialsS_unit.push(solutionMaterialsS[i].unit);
        }
        posting['solutionMaterialsS_material'] = solutionMaterialsS_material;
        posting['solutionMaterialsS_quantity'] = solutionMaterialsS_quantity;
        posting['solutionMaterialsS_unit'] = solutionMaterialsS_unit;

        ///////////////////////////////////////////
        // posting['solutionMaterialsA'] = solutionMaterialsA
        var solutionMaterialsA_material = [];
        var solutionMaterialsA_quantity = [];
        var solutionMaterialsA_unit = [];
        for (var i = 0; i < solutionMaterialsA.length; i++) {
          solutionMaterialsA_material.push(solutionMaterialsA[i].material);
          solutionMaterialsA_quantity.push(solutionMaterialsA[i].quantity);
          solutionMaterialsA_unit.push(solutionMaterialsA[i].unit);
        }
        posting['solutionMaterialsA_material'] = solutionMaterialsA_material;
        posting['solutionMaterialsA_quantity'] = solutionMaterialsA_quantity;
        posting['solutionMaterialsA_unit'] = solutionMaterialsA_unit;
        posting['newLookS_id'] = NewLookS_id
        posting['newLookA_id'] = NewLookA_id
        posting['stepnumber'] = stepnumber
        posting['hint'] = hint;
        posting['_id'] = lab_id;

        $.ajax({
          type: "POST",
          contentType: "application/json",
          url: "/lab/create/workspace/addstep/",
          data: JSON.stringify(posting),
          dataType: 'json',
          cache: false,
          timeout: 600000,
          success: function (result) {
            console.log(result)
            if (result.labEquipS) {
              selectedData.equipmnet_id = result.labEquipS.equipment_id
              selectedData.htmlid = result.labEquipS.htmlid;
              selectedData.nickname = result.labEquipS.nickname;
              selectedData.material = result.labEquipS.material;
              selectedData.blandable = result.labEquipS.blandable;
              selectedData.blander = result.labEquipS.blander;
              selectedData.heatable = result.labEquipS.heatable;
              selectedData.heater = result.labEquipS.heater;
              selectedData.materials = result.labEquipS.materials;
            }
            if (result.labEquipA) {
              associatedData.equipmnet_id = result.labEquipA.equipment_id
              associatedData.htmlid = result.labEquipA.htmlid;
              associatedData.nickname = result.labEquipA.nickname;
              associatedData.material = result.labEquipA.material;
              associatedData.blandable = result.labEquipA.blandable;
              associatedData.blander = result.labEquipA.blander;
              associatedData.heatable = result.labEquipA.heatable;
              associatedData.heater = result.labEquipA.heater;
              associatedData.materials = result.labEquipA.materials;
            }
            if (result.imageS) {
              $('#' + selectedData.htmlid + '_img').attr("src", result.imageS);
              var width = $('#' + selectedData.htmlid + '_img').width() + 2
              var height = $('#' + selectedData.htmlid + '_img').height() + 2
              $('#' + selectedData.htmlid).css('width', width + 'px');
              $('#' + selectedData.htmlid).css('height', height + 'px');
            }
            if (result.imageA) {
              $('#' + associatedData.htmlid + '_img').attr("src", result.imageA);
              var width = $('#' + associatedData.htmlid + '_img').width() + 2
              var height = $('#' + associatedData.htmlid + '_img').height() + 2
              $('#' + associatedData.htmlid).css('width', width + 'px');
              $('#' + associatedData.htmlid).css('height', height + 'px');
            }
            $("#stepInfo").html("");
          },
          error: function (e) {
          }
        });

        // addStep();
      });
    }
    function addStep() {

    }
  }



  function cloneMaterials(materials) {
    var result;
    if (materials) {
      result = [];
      for (var i = 0; i < materials.length; i++) {
        var m = {};
        m.material = materials[i].material;
        m.quantity = materials[i].quantity;
        m.unit = materials[i].unit;
        result.push(m);
      }
    }
    return result;
  }

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

  function showProperties(id, selectedOrAssociated) {
    var nickname;
    var materials;
    for (var i = 0; i < equipsData.length; i++) {
      if (id == equipsData[i].htmlid) {
        nickname = equipsData[i].nickname;
        materials = equipsData[i].materials;
        //selectedEquipment
        $("#" + selectedOrAssociated).text(nickname);
        break;
      }
    }
    if (materials) {
      var materialsDiv = "";
      for (var i = 0; i < materials.length; i++) {
        var materialName = materials[i].material;
        var quantity = materials[i].quantity;
        var unit = materials[i].unit;
        // materialsDiv = materialsDiv + '<div>' + materialName + ': ' + quantity + unit + '</div></br>';
        materialsDiv = materialsDiv + materialName + ': ' + quantity + unit + '\n';
        // $("#selectedEquipmentMaterials").innerHtml = materialsDiv;
        $("#" + selectedOrAssociated + "Materials").text(materialsDiv);

      }
    }
  }
  //
  function unSelected() {
    $("#" + selected).css("border", "");
    $("#selectedEquipment").text("");
    $("#selectedEquipmentMaterials").text("");
    selected = null;
  }

  function doSelected(id) {
    $("#" + id).css("border", "2px");
    $("#" + id).css("border-style", "dashed");
    selected = id;
  }
  //
  function doAssociated(id) {
    associated = id;
    $("#" + id).css("border", "2px");
    $("#" + id).css("border-style", "dashed");
    $("#" + id).css("border-color", "#378ca3");
    var selectedOrAssociated = "associatedEquipment";
    showProperties(id, selectedOrAssociated);
  }

  function unAssociated() {
    $("#" + associated).css("border", "");
    $("#associatedEquipment").text("");
    $("#associatedEquipmentMaterials").text("");
    $("#addToAssociated").html("");
    $("#getFromAssociated").html("");
    $("#actionPopUp").css("visibility", "hidden");
    $("#interactionButton").css("visibility", "hidden");
    associated = null;
  }

  function cleanSelectedAndAssociated() {
    unSelected();
    unAssociated();
  }
  //(╯°Д°)╯︵ ┻━┻
});