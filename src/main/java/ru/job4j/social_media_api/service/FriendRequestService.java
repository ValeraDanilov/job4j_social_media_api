package ru.job4j.social_media_api.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.repository.FriendRequestRepository;

@Service
@AllArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    /**
     * find the recipient.
     *
     * @param receiver User.
     * @return FriendRequest.
     */
    public FriendRequest findByReceiver(User receiver) {
        return this.friendRequestRepository.findByReceiver(receiver);
    }

    /**
     * update friend status.
     * if status = true (friend).
     * if status = false (follower).
     *
     * @param userReceiver FriendRequest.
     */
    @Transactional
    public void deleteFriendAndKeepFollower(FriendRequest userReceiver) {
        userReceiver.setStatus(false);
        this.friendRequestRepository.save(userReceiver);
    }

    /**
     * The method adds a friend request between two users.
     *
     * @param sender user who sends the request.
     * @param receiver user to whom the request is sent.
     * @param result the result of whether you accepted the friend request or rejected it.
     */
    @Transactional
    public void sendOrReceiverFriendRequest(User sender, User receiver, boolean result) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(result);
        this.friendRequestRepository.save(friendRequest);
    }
}
