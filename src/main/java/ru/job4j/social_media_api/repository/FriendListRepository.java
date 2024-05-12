package ru.job4j.social_media_api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.social_media_api.model.FriendList;

public interface FriendListRepository extends CrudRepository<FriendList, Integer> {
}
