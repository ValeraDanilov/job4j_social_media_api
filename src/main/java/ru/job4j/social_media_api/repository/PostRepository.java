package ru.job4j.social_media_api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.social_media_api.model.Post;

public interface PostRepository extends CrudRepository<Post, Integer> {
}
