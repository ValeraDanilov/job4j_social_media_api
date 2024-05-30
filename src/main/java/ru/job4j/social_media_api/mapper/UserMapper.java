package ru.job4j.social_media_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.springframework.stereotype.Component;
import ru.job4j.social_media_api.dto.UserDTO;
import ru.job4j.social_media_api.model.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User getEntityFromDto(UserDTO userDTO);

    UserDTO getDtoFromEntity(User user);
}
