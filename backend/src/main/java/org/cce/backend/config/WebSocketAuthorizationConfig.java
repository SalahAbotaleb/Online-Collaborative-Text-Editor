package org.cce.backend.config;

import org.cce.backend.entity.UserDocSession;
import org.cce.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.util.UriTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthorizationConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
                    //User can subscribe
                    String connectionPath = accessor.getFirstNativeHeader("destination");
                    String docId = extractDocIdFromPath(connectionPath);
                }

                return message;
            }
        });
    }
    private String extractDocIdFromPath(String path) {
        UriTemplate uriTemplate = new UriTemplate("/docs/broadcast/changes/{id}");
        Map<String, String> matchResult = uriTemplate.match(path);
        return matchResult.getOrDefault("id","");
    }
}
