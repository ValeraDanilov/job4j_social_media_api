package ru.job4j.social_media_api.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.repository.FriendRequestRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    /**
     * Finds a friend request by request ID.
     *
     * @param requestId The ID of the friend request to search for.
     * @return An Optional containing the FriendRequest object if found, or an empty Optional if not found.
     */
    public Optional<FriendRequest> findById(int requestId) {
        return this.friendRequestRepository.findById(requestId);
    }

    /**
     * This method deletes a friend request and keeps the follower relationship between two users.
     *
     * @param requestId The ID of the friend request to be deleted.
     * @param userId    The ID of the user performing the action.
     * @return true if the friend request was successfully deleted and follower relationship kept, false otherwise.
     */
    @Transactional
    public boolean deleteFriendAndKeepFollower(int requestId, int userId) {
        Optional<FriendRequest> findFriendRequest = findById(requestId);
        if (findFriendRequest.get().isStatus()
                && (findFriendRequest.get().getSender().getId() == userId
                || findFriendRequest.get().getReceiver().getId() == userId)) {
            FriendRequest newFriendRequest = findFriendRequest.get();
            if (findFriendRequest.get().getSender().getId() == userId) {
                User sender = newFriendRequest.getSender();
                newFriendRequest.setSender(findFriendRequest.get().getReceiver());
                newFriendRequest.setReceiver(sender);
            }
            newFriendRequest.setStatus(false);
            this.friendRequestRepository.save(newFriendRequest);
            return true;
        }
        return false;
    }

    /**
     * Deletes a friend request if the specified user is the sender and the request has not been accepted.
     *
     * @param requestId the unique identifier of the friend request to be deleted
     * @param userId    the unique identifier of the user attempting to delete the friend request
     * @return true if the friend request was successfully deleted, false otherwise
     */
    @Transactional
    public boolean deleteRequest(int requestId, int userId) {
        Optional<FriendRequest> findFriendRequest = findById(requestId);
        if (findFriendRequest.get().getSender().getId() == userId
                && !findFriendRequest.get().isStatus()) {
            this.friendRequestRepository.delete(findFriendRequest.get());
            return true;
        }
        return false;
    }

    /**
     * Sends a friend request by saving it in the database with a status of false.
     *
     * @param sendRequest the friend request to send
     */
    @Transactional
    public void sendRequest(FriendRequest sendRequest) {
        sendRequest.setStatus(false);
        this.friendRequestRepository.save(sendRequest);
    }

    /**
     * Accepts a friend request if the specified user is the receiver.
     *
     * @param requestId the unique identifier of the friend request to be accepted
     * @param userId    the unique identifier of the user accepting the friend request
     * @return true if the friend request was successfully accepted, false otherwise
     */
    @Transactional
    public boolean acceptRequest(int requestId, int userId) {
        Optional<FriendRequest> findFriendRequest = findById(requestId);
        if (findFriendRequest.get().getReceiver().getId() == userId) {
            findFriendRequest.get().setStatus(true);
            this.friendRequestRepository.save(findFriendRequest.get());
            return true;
        }
        return false;
    }
}
