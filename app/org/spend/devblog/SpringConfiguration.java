package org.spend.devblog;

import akka.actor.ActorRef;
import org.spend.devblog.actor.PostActor;
import org.spend.devblog.repository.AsyncPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static akka.actor.Props.create;
import static play.libs.Akka.system;

@Configuration
@ComponentScan
@EnableJpaAuditing
@EnableJpaRepositories
@EnableAutoConfiguration
public class SpringConfiguration {
    @Autowired
    private AsyncPostRepository asyncPostRepository;

    @Bean
    @Qualifier("postActor")
    public ActorRef postActor() {
        return system().actorOf(create(PostActor.class, asyncPostRepository));
    }
}
