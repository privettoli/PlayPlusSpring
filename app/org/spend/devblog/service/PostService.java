package org.spend.devblog.service;

import org.spend.devblog.domain.PostEntity;
import org.spend.devblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import play.libs.F.Promise;
import scala.concurrent.ExecutionContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static play.libs.F.Promise.promise;

@Service
public class PostService {
    private PostRepository postRepository;
    private ExecutionContext defaultExecutionContext;
    private ExecutionContext databaseExecutionContext;

    @Autowired
    public PostService(PostRepository postRepository,
                       @Qualifier("databaseExecutionContext") ExecutionContext databaseExecutionContext,
                       @Qualifier("defaultExecutionContext") ExecutionContext defaultExecutionContext) {
        this.databaseExecutionContext = databaseExecutionContext;
        this.postRepository = postRepository;
        this.defaultExecutionContext = defaultExecutionContext;
    }

    public Promise<Void> asyncSave(PostEntity post) {
        return promise(() -> {
            postRepository.save(post);
            return null;
        }, databaseExecutionContext);
    }

    public void save(PostEntity post, Optional<Runnable> runnable) {
        databaseExecutionContext.execute(() -> {
            postRepository.save(post);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runnable.ifPresent(defaultExecutionContext::execute);
        });
    }

    public void save(PostEntity post) {
        save(post, Optional.<Runnable>empty());
    }

    public Promise<List<PostEntity>> ten(Integer page) {
        return promise(() -> postRepository.findAll(
                new PageRequest(page, 10, DESC, "modifiedDate")
        ).getContent(), databaseExecutionContext);
    }


}
