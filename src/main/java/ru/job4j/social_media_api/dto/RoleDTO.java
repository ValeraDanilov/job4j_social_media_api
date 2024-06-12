package ru.job4j.social_media_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.social_media_api.model.ERole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private int id;
    private ERole eRole;
}
