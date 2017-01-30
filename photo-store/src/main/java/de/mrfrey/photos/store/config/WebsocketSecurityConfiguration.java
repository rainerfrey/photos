package de.mrfrey.photos.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;


@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound( MessageSecurityMetadataSourceRegistry messages ) {
        messages
                .simpDestMatchers( "/app/comments" ).hasRole( "USER" )
                .simpSubscribeDestMatchers( "/topic/comments" ).hasRole( "USER" )
                .simpSubscribeDestMatchers( "/user/exchange/amq.direct/**" ).hasRole( "ADMIN" )
                ;
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
