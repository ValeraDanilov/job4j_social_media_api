package ru.job4j.social_media_api.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.social_media_api.model.Image;
import java.util.Optional;

public interface ImageRepository extends CrudRepository<Image, Integer> {

    @Modifying(clearAutomatically = true)
    @Query(
            """
                    delete Image image where image.id = :imageId and image.post.id = :postId
                    """
    )
    void deleteImageFromPost(@Param("postId") int postId, @Param("imageId") int imageId);

    @Modifying(clearAutomatically = true)
    @Query(
            """
                    delete Image image where image.post.id = :postId
                    """
    )
    void deleteImagesByPostId(@Param("postId") int postId);


    @Modifying(clearAutomatically = true)
    @Query(
            """
                    update Image image set image.name = :name, image.imageData = :imageData where image.id = :id
                    """
    )
    void updateImage(@Param("name") String name, @Param("imageData") byte[] imageData, @Param("id") int id);

    @Query(
            """
                    select image from Image image where image.post.id = :postId
                    """
    )
    Optional<Image> findImageByPostId(@Param("postId") int postId);
}
