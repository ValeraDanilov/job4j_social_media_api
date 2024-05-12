package ru.job4j.social_media_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.social_media_api.model.Post;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findAllByUserId(int userId);
    List<Post> findAllByCreatedBetween(LocalDateTime startDate, LocalDateTime finishDate);
    Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);
}
