package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {

    private static final long START_ID = 0L;

    private final ConcurrentMap<Long, Post> posts;
    private final AtomicLong counter;

    public PostRepositoryStubImpl() {
        this.posts = new ConcurrentHashMap<>();
        this.counter = new AtomicLong(START_ID + 1);
    }

    @Override
    public List<Post> all() {
        return List.copyOf(posts.values());
    }

    @Override
    public Optional<Post> getById(long id) {
        return posts.containsKey(id) ? Optional.of(posts.get(id)) : Optional.empty();
    }

    @Override
    public Post save(Post post) {
        final var id = post.getId();
        if (id != START_ID && !posts.containsKey(post.getId())) throw new NotFoundException();
        if (id == START_ID) {
            post.setId(counter.getAndIncrement());
        }
        posts.put(post.getId(), post);
        return posts.get(post.getId());
    }

    @Override
    public void removeById(long id) {
        posts.remove(id);
    }
}
