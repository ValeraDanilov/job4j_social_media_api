package ru.job4j.social_media_api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.social_media_api.model.FriendRequest;

import java.util.List;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {

    @Query(
            """
                    select rq from FriendRequest rq where rq.receiver.id = :userId or rq.sender.id = :userId
                    """
    )
    List<FriendRequest> findReceiverById(int userId);
}
