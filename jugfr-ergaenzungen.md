Nachtrag zum Treffen der JUG Freiburg am 30.1.2017
==================================================
Hier noch einige Antworten und Berichtigungen zu meinem Vortrag.

Websocket-Subprotocol
---------------------
* Spring erwartet immer STOMP auf einem Websocket-Endpoint der für den Messaging-Support konfiguriert wurde
* darum funktioniert es auch ohne Subprotocol-Header
* im Beispiel war der Header nicht zu sehen, weil der JavaScript-Client von SockJS das nicht unterstützt
* wenn man ein Websocket direkt benutzt kann man den Header setzen

Im Frontend explizit Websocket verwenden
----------------------------------------
* client-seitig kann direkt ein Websocket geöffnet werden, auch wennn SockJS-Support auf dem Server konfiguriert ist
* `new Websocket(ws://host:port/configured-endpoint-path/websocket)` statt `new SockJS(http://host:port/configured-endpoint-path)`

Error-Handling für Application Destinations
-------------------------------------------
* per Default werden Exceptions nur geloggt
* mit `@MessageExceptionHandler` können Fehlermeldungen an beliebige Destinations geschickt werden
* Methoden ähnlich `@MessageMapping`
* globale Destinations sind möglich, üblich sind User Destinations

```java
    @MessageExceptionHandler
    @SendToUser("/exchange/amq.direct/errors")
    public Map<String, Object> handleException(StompHeaderAccessor headers, Exception x) {
        Map<String, Object> error = new HashMap<>();
        error.put("command", headers.getCommand().getMessageType());
        error.put("message-id", headers.getMessageId());
        error.put("destination", headers.getDestination());
        error.put("content-type", headers.getContentType());
        error.put("exception", x.getClass().getName());
        error.put("message", x.getMessage());
        return error;
    }
```

Websocket-Session und HTTP Session
----------------------------------
* HTTP Session-Attribute werden per default nicht ￿in die Websocket-Session übernommen - da lag ich leider falsch
* die Implementierung wird mitgeliefert, ist aber nicht automatisch aktiv
* wird konfiguriert als Handshake-Interceptor

```java
@EnableWebSocketMessageBroker
public class WebsocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .withSockJS();
    }
}
```

Testing von Application Destinations
------------------------------------
* mehrere Ansätze möglich, variieren im Aufrufverfahren 
* standalone, minimale Spring-Infrastruktur: Message Handler können im Test aufgesetzt und aufgerufen werden
* Spring context based: Spring-Konfiguration wird mit Spring Test Context Framework geladen
    * `clientInboundChannel` kann in Test injected werden
    * Controller werden durch Messages an diesen Channel aufgerufen
* embedded server: Embedded Tomcat wird gestartet, Tests können Java Client aus spring-messages verwenden
* Beispiele in [Stock Sample Application](https://github.com/rstoyanchev/spring-websocket-portfolio)