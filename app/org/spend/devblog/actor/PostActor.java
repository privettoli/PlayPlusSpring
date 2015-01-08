package org.spend.devblog.actor;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import org.spend.devblog.domain.PostEntity;
import org.spend.devblog.event.CloseConnectionEvent;
import org.spend.devblog.event.NewConnectionEvent;
import org.spend.devblog.event.NewPostEvent;
import org.spend.devblog.service.PostService;
import play.Logger;
import play.mvc.WebSocket.Out;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.String.format;
import static java.util.Optional.of;
import static play.libs.Json.toJson;

public class PostActor extends UntypedActor {
    private static final ConcurrentMap<String, Out<JsonNode>> connections = new ConcurrentHashMap<>(10);

    private PostService postService;

    public PostActor(PostService postService) {
        this.postService = postService;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof NewConnectionEvent) {
            final NewConnectionEvent newConnection = (NewConnectionEvent) message;
            connections.put(newConnection.getUuid(), newConnection.getOut());
            Logger.info("New browser connection: " + newConnection.getUuid());
        } else if (message instanceof CloseConnectionEvent) {
            final CloseConnectionEvent closeConnection = (CloseConnectionEvent) message;
            connections.remove(closeConnection.getUuid());
            Logger.info(format("Connection %s closed", closeConnection.getUuid()));
        } else if (message instanceof NewPostEvent) {
            NewPostEvent newPostEvent = (NewPostEvent) message;
            final PostEntity post = newPostEvent.getPost();

            postService.save(post, of(() -> {
                connections.forEach((uuid, webSocketOut) -> {
                    webSocketOut.write(toJson(post));
                });
            }));
        } else {
            unhandled(message);
        }
    }
}
