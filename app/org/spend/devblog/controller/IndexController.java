package org.spend.devblog.controller;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import org.spend.devblog.domain.PostEntity;
import org.spend.devblog.event.CloseConnectionEvent;
import org.spend.devblog.event.NewConnectionEvent;
import org.spend.devblog.event.NewPostEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import org.spend.devblog.service.PostService;
import views.html.index;

import java.util.Collections;

import static akka.actor.ActorRef.noSender;
import static java.util.Collections.reverse;
import static java.util.UUID.randomUUID;
import static play.data.Form.form;
import static play.libs.Json.toJson;

@org.springframework.stereotype.Controller
public class IndexController extends Controller {
    @Autowired
    private PostService postService;
    @Autowired
    @Qualifier("postActor")
    private ActorRef postActorRef;

    public Result page() {
        return ok(index.render());
    }

    public Result newPost() {
        final PostEntity newPost = form(PostEntity.class).bindFromRequest(request()).get();
        postActorRef.tell(new NewPostEvent(newPost), noSender());
        return ok();
    }

    public WebSocket<JsonNode> newPostsWebSocket() {
        return new WebSocket<JsonNode>() {
            @Override
            public void onReady(In<JsonNode> in, Out<JsonNode> out) {
                final String uuid = randomUUID().toString();
                postActorRef.tell(new NewConnectionEvent(uuid, out), noSender());
                in.onClose(() -> {
                    postActorRef.tell(new CloseConnectionEvent(uuid), noSender());
                });
            }
        };
    }

    public Promise<Result> getTen(Integer page) {
        return postService.ten(page).map(list -> ok(toJson(list)));
    }
}
