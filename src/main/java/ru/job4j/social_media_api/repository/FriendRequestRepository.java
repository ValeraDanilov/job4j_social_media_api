package ru.job4j.social_media_api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.social_media_api.model.FriendRequest;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {
}
