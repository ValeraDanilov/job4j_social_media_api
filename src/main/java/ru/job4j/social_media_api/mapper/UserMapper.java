package ru.job4j.social_media_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.job4j.social_media_api.dto.UserDTO;
import ru.job4j.social_media_api.dto.RoleDTO;
import ru.job4j.social_media_api.model.Role;
import ru.job4j.social_media_api.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", source = "roleDTOs", qualifiedByName = "mapRolesToEntity")
    User getEntityFromDto(UserDTO userDTO);

    @Mapping(target = "roleDTOs", source = "roles", qualifiedByName = "mapRolesToDTO")
    UserDTO getDtoFromEntity(User user);

    @Named("mapRolesToEntity")
    default Set<Role> mapRolesToEntity(Set<RoleDTO> roleDTOs) {
        if (roleDTOs == null) {
            return null;
        }
        return roleDTOs.stream()
                .map(roleDTO -> new Role(roleDTO.getId(), roleDTO.getERole()))
                .collect(Collectors.toSet());
    }

    @Named("mapRolesToDTO")
    default Set<RoleDTO> mapRolesToDTO(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .collect(Collectors.toSet());
    }
}