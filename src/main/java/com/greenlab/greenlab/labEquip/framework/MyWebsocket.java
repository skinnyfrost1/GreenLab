package com.greenlab.greenlab.labEquip.framework;//package green.lab.framework;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class MyWebsocket {
//
//
//    //messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, jsonObject.toString() );
//
//    //messagingTemplate.convertAndSend("/topic/equipment/pad/" + equipmentId, jsonObject2.toString() );
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    private static MyWebsocket myWebsocket;
//
//    public MyWebsocket(){
//
//    }
//
//    public static MyWebsocket getMyWebsocket() {
//
//        if( myWebsocket == null ){
//            myWebsocket = new MyWebsocket();
//        }
//        return myWebsocket;
//    }
//
//    public void sendUnique(String userId , String sessionId , String message ){
//        messagingTemplate.convertAndSend("/topic/image/" + userId + "/" + sessionId, message );
//    }
//
//    public void sendEquipFolder( String userId , String message ){
//        messagingTemplate.convertAndSend("/topic/equipment/folder/" + userId , message );
//    }
//
//    public void sendEquipPad( String itemId ,  String message ){
//        messagingTemplate.convertAndSend("/topic/equipment/pad/" + itemId , message );
//    }
//
//    public void sendEquipBoard( String itemId ,  String message ){
//        messagingTemplate.convertAndSend("/topic/equipment/board/" + itemId , message );
//    }
//
//    public void sendEquipFind( String message ){
//        messagingTemplate.convertAndSend("/topic/equipment/find"  , message );
//    }
//
//    public void sendLabFolder( String userId , String message ){
//
//        messagingTemplate.convertAndSend("/topic/lab/folder/" + userId , message );
//    }
//
//    public void sendLabPad( String itemId ,  String message ){
//        messagingTemplate.convertAndSend("/topic/lab/pad/" + itemId , message );
//    }
//
//    public void sendLabBoard( String itemId ,  String message ){
//        messagingTemplate.convertAndSend("/topic/lab/board/" + itemId , message );
//    }
//
//    public void sendLabFind( String message ){
//        messagingTemplate.convertAndSend("/topic/lab/find"  , message );
//    }
//
//}
