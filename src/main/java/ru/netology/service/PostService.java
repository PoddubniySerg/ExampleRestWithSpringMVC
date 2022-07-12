package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository repository;

    private PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        final var posts = repository.all();
        if (posts == null || posts.isEmpty() || posts.stream().allMatch(Post::isDeleted)) return new ArrayList<>(0);
        return posts.stream().filter(post -> !post.isDeleted()).collect(Collectors.toList());
    }

    public Post getById(long id) {
        final var post = repository.getById(id).orElseThrow(NotFoundException::new);
        if (post.isDeleted()) throw new NotFoundException();
        return post;
    }

    public Post save(Post post) throws NotFoundException {
        final var newPost = repository.save(post);
        if (newPost.isDeleted()) throw new NotFoundException();
        return newPost;
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}

