package ru.job4j.social_media_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.social_media_api.model.Post;
import ru.job4j.social_media_api.model.User;


import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findAllPostsByUserId(int userId);

    List<Post> findAllByCreatedBetween(LocalDateTime startDate, LocalDateTime finishDate);

    Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);


    @Modifying(clearAutomatically = true)
    @Query(
            """
                    update Post post set post.title = :title, post.description = :description
                    where post.id = :id
                    """
    )
    void updatePostTitleAndDescription(@Param("title") String title, @Param("description") String description, @Param("id") int id);

    @Query(
            """
                    select post from Post post
                    join FriendRequest  fr on fr.sender = :sender
                    where fr.receiver.id = post.user.id and fr.status = false order by post.created desc
                    """)
    Page<Post> findAllPostFromUserSubscriptions(@Param("sender") User sender, Pageable pageable);
}
