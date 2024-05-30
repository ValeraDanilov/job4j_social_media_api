package ru.job4j.social_media_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.job4j.social_media_api.dto.ImageDTO;
import ru.job4j.social_media_api.dto.PostAndImagesDTO;
import ru.job4j.social_media_api.model.Image;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface ImageMapper {

    @Mapping(target = "post", ignore = true)
    Image getEntityFromDto(ImageDTO imageDto);

    ImageDTO getDtoFromEntity(Image image);

    default List<Image> getEntityFromDtoList(PostAndImagesDTO imageDto) {
        return imageDto.getImages().stream().map(this::getEntityFromDto).toList();
    }

    default List<ImageDTO> getDtoFromEntityList(List<Image> images) {
        return images.stream().map(this::getDtoFromEntity).toList();
    }
}
