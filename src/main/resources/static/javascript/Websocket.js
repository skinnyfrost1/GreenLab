
class Websocket{

    constructor(){

        this.userId = null;      // need set from outside
        this.itemId = null; // need set from outside

        this.websocketId = null; // ok

        this.receiveMessage = null;  // need set from outside
        this.socket = null;
        this.stompClient = null;

        this.sendMessage = null;

        this.subscribeType = null

        this.connectBroken = null; // this is function handle connection broken
        //this.websocket = new Websocket();
        this.self = null;

        this.loadBoolean = false;
    }


    setWebsocketId( ){
        var websocket = this.self;
        var url = websocket.stompClient.ws._transport.url;
        var urlArr = url.split("/");
        var webSessionId =  urlArr[urlArr.length-2 ];
        websocket.websocketId = webSessionId;
    }


    __subscribeUnique(){

        // this is combination of userid and websocketid

        var websocket = this.self;
        websocket.stompClient.subscribe(
            "/topic/image/"
            +websocket.userId
            +"/"
            +websocket.websocketId , function (message) {
                websocket.receiveMessage(message.body);
            }
        );
    }

    __subscribeEquipmentFolder(){

        // this is userId only

        var websocket = this.self;
        websocket.stompClient.subscribe(
            "/topic/equipment/folder/"
            +websocket.userId
             , function (message) {
                websocket.receiveMessage(message.body);
            }
        );
    }

    __subscribeEquipmentPad(){  // the item here is equipment id

        // this is equipmentId only
            // once it broadcast
                // other user who work on this equipment can do operation on it
        var websocket = this.self;
        websocket.stompClient.subscribe(
            "/topic/equipment/pad/"
            +websocket.itemId
            , function (message) {
                websocket.receiveMessage(message.body);
            }
        );
    }

    __subscribeEquipmentBoard(){  // the item here is equipment id

        // this is equipmentId only
        // once it broadcast
        // other user who work on this equipment can do operation on it
        var websocket = this.self;
        websocket.stompClient.subscribe(
            "/topic/equipment/board/"
            +websocket.itemId
            , function (message) {
                websocket.receiveMessage(message.body);
            }
        );
    }

    __subscribeEquipFind(){  // the item here is lab id

        // this is equipmentId only
        // once it broadcast
        // other user who work on this equipment can do operation on it
        var websocket = this.self;
        websocket.stompClient.subscribe(
            "/topic/equipment/find"
            , function (message) {
                websocket.receiveMessage(message.body);
            }
        );
    }

    __subscribeLabFolder(){

        // this is userId only

        var websocket = this.self;
        websocket.stompClient.subscribe(
            "/topic/lab/folder/"
            +websocket.userId
            , function (message) {
                websocket.receiveMessage(message.body);
            }
        );
    }

    __subscribeLabPad(){  // the item here is lab id

        // this is equipmentId only
        // once it broadcast
        // other user who work on this equipment can do operation on it
        var websocket = this.self;
        websocket.stompClient.subscribe(
            "/topic/lab/pad/"
            +websocket.itemId
            , function (message) {
                websocket.receiveMessage(message.body);
            }
        );
    }

    __subscribeLabBoard(){  // the item here is lab id

        // this is equipmentId only
        // once it broadcast
        // other user who work on this equipment can do operation on it
        var websocket = this.self;
        websocket.stompClient.subscribe(
            "/topic/lab/board/"
            +websocket.itemId
            , function (message) {
                websocket.receiveMessage(message.body);
            }
        );
    }

    __subscribeLabFind(){  // the item here is lab id

        // this is equipmentId only
        // once it broadcast
        // other user who work on this equipment can do operation on it
        var websocket = this.self;
        websocket.stompClient.subscribe(
            "/topic/lab/find"
            , function (message) {
                websocket.receiveMessage(message.body);
            }
        );
    }

    subscribeCreateEquipment(){

        var websocket = this.self;
        websocket.__subscribeEquipmentFolder();
        websocket.__subscribeEquipmentPad();
        websocket.__subscribeUnique();

    }

    subscribeShareEquipment(){

        var websocket = this.self;
        websocket.__subscribeEquipmentFolder();
        websocket.__subscribeUnique();

    }

    subscribeFindEquipment(){

        var websocket = this.self;
        websocket.__subscribeEquipFind();
        websocket.__subscribeUnique();
        websocket.__subscribeEquipmentFolder();

    }

    subscribeBoardEquipment(){
        var websocket = this.self;
        websocket.__subscribeEquipmentBoard();
        websocket.__subscribeUnique();
    }



    __sendEquipmentFrontMessage( message ){
        var websocket = this.self;
        websocket.stompClient.send( "/app/equipment/front/"+websocket.userId+"/"+websocket.websocketId , {}, message);
    }

    __sendEquipmentBackMessage( message ){
        var websocket = this.self;
        websocket.stompClient.send( "/app/equipment/back/"+websocket.userId+"/"+websocket.websocketId , {}, message);
    }

    __sendLabFrontMessage( message ){
        var websocket = this.self;
        websocket.stompClient.send( "/app/lab/front/"+websocket.userId+"/"+websocket.websocketId , {}, message);
    }

    __sendLabBackMessage( message ){
        var websocket = this.self;
        websocket.stompClient.send( "/app/lab/back/"+websocket.userId+"/"+websocket.websocketId , {}, message);
    }

    // sendMessage( message ){
    //
    //     var websocket = this.self;
    //     websocket.sendMessage( message );
    //
    // }

    connect(  ){
        //var websocket = new Websocket();
        //websocket.
        var websocket = this.self;
        websocket.socket = new SockJS('/landon-stomp-chat');
        websocket.stompClient = Stomp.over(websocket.socket);
        websocket.stompClient.debug = null;
        websocket.stompClient.connect({}, function (frame) {
            websocket.setWebsocketId();
            websocket.subscribeType();
            var sendData = {"type":"connectSuccess"}
            websocket.sendMessage(JSON.stringify(sendData));
        }, function (message) {
            websocket.connectBroken();
        } );

    }


}

function quickPrepare( type, data ) {
    var sendData = {};
    sendData["type"] = type;
    if( itemId != null ){
        sendData["itemId"] = itemId;
    }
    if(data!=null){
        sendData["data"] = data;
    }
    return  JSON.stringify(sendData);
}

//var BooleanVar = false;
//_______________________ the code below teach you how to use the api ______________
//when you send information don't forget include itemId inside the message
    //

                // websocket = new Websocket();
                // websocket.userId = "666";
                // websocket.itemId=  "777";
                // websocket.receiveMessage = function (message){
                //     console.log(message);
                // } ;
                // websocket.connectBroken = function ( ){
                //
                //     console.log("connection broken");
                //
                // };
                // websocket.self = websocket;
                // websocket.subscribeType = websocket.subscribeCreateEquipment; // you need set the subscribe type
                // websocket.sendMessage = websocket.__sendEquipmentFrontMessage;
                // websocket.connect();

        // @MessageMapping("/equipment/front/{userId}/{sessionId}")
        // public void handleEquipmentFront( @DestinationVariable String userId ,  @DestinationVariable String sessionId , String message) throws JSONException {
        //
        //
        //     System.out.println( message+"__"+ userId +"__"+ sessionId);
        //
        //
        // }

        // @Configuration
        // @EnableWebSocketMessageBroker
        // public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
        //
        //     @Override
        //     public void registerStompEndpoints(StompEndpointRegistry registry) {
        //     registry.addEndpoint("/landon-stomp-chat").withSockJS();
        // }
        //
        // @Override
        // public void configureMessageBroker(MessageBrokerRegistry config) {
        //     config.enableSimpleBroker("/topic","/user", "/queue");
        //     //config.enableSimpleBroker("/user/queue/specific-user");
        //     config.setApplicationDestinationPrefixes("/app");
        //     config.setUserDestinationPrefix("/user/");
        //     //config.enableSimpleBroker("/queue");
        //     //config.enableSimpleBroker("/queue","/topic");
        // }
        //
        // }


class Ajax{

    constructor(){

        this.sendAddress = null;
        this.handle = null;
        this.self = null;
    }

    sendAjax( message ){
        var ajax = this.self;
        $.post(
            ajax.sendAddress,
            {"data": message },
            function(response){
                if(response.success){
                    ajax.handle( response.data );
                }
            },
            "json"
        );
    }
}
//ajax/requestId
//_______________________    ajax use tutorial      ____________________


                    // var ajax = new Ajax();
                    // ajax.sendAddress ="ajax/requestId";
                    // ajax.handle = function ( message ) {
                    //     console.log( message );
                    // }
                    // ajax.self = ajax;
                    // ajax.sendAjax("666");



        // @RequestMapping(value="/ajax/requestId" , method = RequestMethod.POST)
        // @ResponseBody
        // public Object SignupSumbit(HttpServletRequest request) throws JSONException {
        //
        //     String dataStr = request.getParameter("data");
        //
        //     System.out.println( dataStr );
        //
        //     Map<String,Object> sendData = new HashMap<>();
        //     sendData.put("success",true);
        //     sendData.put("data", "weixin.tang@stonybrook.edu" );
        //     return sendData;
        // }

