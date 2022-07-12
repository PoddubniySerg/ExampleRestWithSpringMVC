package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class PostRepositoryStubImpl implements PostRepository {

    private static final long START_ID = 0L;

    private final ConcurrentMap<Long, Post> posts;
    private long counter;

    public PostRepositoryStubImpl() {
        this.posts = new ConcurrentHashMap<>();
        this.counter = START_ID + 1;
    }

    @Override
    public List<Post> all() {
        return List.copyOf(posts.values());
    }

    @Override
    public Optional<Post> getById(long id) {
        return posts.values().stream()
                .filter(post -> id == post.getId())
                .findFirst();
    }

    @Override
    public Post save(Post post) {
        final var id = post.getId();
        if ((id != START_ID && !posts.containsKey(post.getId())) || posts.get(id).isDeleted()) throw new NotFoundException();
        if (id == START_ID) {
            post.setId(counter);
            counter++;
        }
        posts.put(post.getId(), post);
        return posts.get(post.getId());
    }

    @Override
    public void removeById(long id) {
        if (posts.containsKey(id)) posts.get(id).setDeleted(true);
    }
}
