package ru.job4j.social_media_api.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.model.User;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {

    FriendRequest findByReceiver(User receiver);
}
