package fr.example.config;

import org.springframework.web.reactive.socket.WebSocketHandler;

public interface ReactiveWebSocketHandler extends WebSocketHandler {
	
    String getPath();
       
}