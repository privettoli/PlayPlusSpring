package org.spend.devblog.repository;

import org.spend.devblog.domain.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import play.libs.F.Promise;

import java.util.List;

import static org.spend.devblog.config.ExecutionContexts.databaseExecutionContext;
import static play.libs.F.Promise.promise;

@Repository
public class AsyncPostRepository {
    private PostRepository postRepository;

    @Autowired
    public AsyncPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Promise<Void> asyncSave(PostEntity post) {
        return promise(() -> {
            postRepository.save(post);
            return null;
        }, databaseExecutionContext);
    }

    public void save(PostEntity post) {
        databaseExecutionContext.execute(() -> {
            postRepository.save(post);
        });
    }

    public Promise<List<PostEntity>> ten(Integer page) {
        return promise(() -> postRepository.findAll(new PageRequest(page, 10)).getContent(),
                databaseExecutionContext);
    }
}
