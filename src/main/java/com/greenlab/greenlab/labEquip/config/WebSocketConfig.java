package com.greenlab.greenlab.labEquip.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/landon-stomp-chat").withSockJS();
    }
	
	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/user", "/queue");
        //config.enableSimpleBroker("/user/queue/specific-user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user/");
        //config.enableSimpleBroker("/queue");
        //config.enableSimpleBroker("/queue","/topic");
    }

}