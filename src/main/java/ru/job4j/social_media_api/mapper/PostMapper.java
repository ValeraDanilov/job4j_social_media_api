package ru.job4j.social_media_api.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.job4j.social_media_api.dto.PostAndImagesDTO;
import ru.job4j.social_media_api.dto.PostDTO;
import ru.job4j.social_media_api.dto.UserDTO;
import ru.job4j.social_media_api.model.Post;
import ru.job4j.social_media_api.model.User;

@Mapper(componentModel = "spring")
@Component
public interface PostMapper {
    @Mapping(target = "user", expression = "java(getEntityFromDto(postDto.getPost().getUser()))")
    @Mapping(target = "title", source = "post.title")
    @Mapping(target = "description", source = "post.description")
    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "created", source = "post.created")
    Post getEntityFromDto(PostAndImagesDTO postDto);

    PostDTO getDtoFromEntity(Post post);

    default User getEntityFromDto(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        return user;
    }

}
