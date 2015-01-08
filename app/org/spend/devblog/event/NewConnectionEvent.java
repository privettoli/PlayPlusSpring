package org.spend.devblog.event;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import play.mvc.WebSocket;

@Getter
@AllArgsConstructor
public class NewConnectionEvent {
    private final String uuid;
    private final WebSocket.Out<JsonNode> out;
}
