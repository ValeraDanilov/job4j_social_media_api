package ru.job4j.social_media_api.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.model.Post;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.repository.FriendRequestRepository;
import ru.job4j.social_media_api.repository.PostRepository;
import ru.job4j.social_media_api.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendRequestRepository friendRequestRepository;

    /**
     * Finds a user by user ID.
     *
     * @param userId The ID of the user to search for.
     * @return An Optional containing the User object if found, or an empty Optional if not found.
     */
    public Optional<User> getUserById(int userId) {
        return this.userRepository.findById(userId);
    }

    /**
     * Finds a user by username and password.
     *
     * @param user The User object containing the username and password to search for.
     * @return An Optional containing the User object if found, or an empty Optional if not found.
     */
    public Optional<User> findByUsernameAndPassword(User user) {
        return this.userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    /**
     * Finds all users in the system.
     *
     * @return A list of User objects representing all users in the system.
     */
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    /**
     * Finds all friends of a user with the given userId.
     *
     * @param userId The id of the user whose friends are to be found.
     * @return A list of User objects representing the friends of the user.
     */
    public List<User> findAllFriends(int userId) {
        Optional<User> findUser = getUserById(userId);
        return findUser.map(this.userRepository::findAllFriends).orElse(new ArrayList<>());
    }

    /**
     * Creates a new user in the database.
     *
     * @param user The user object to be created.
     * @return The user object that was created and saved in the database.
     */
    public User create(User user) {
        return this.userRepository.save(user);
    }

    /**
     * Updates the information of a user in the database.
     *
     * @param user The user object containing the updated information.
     * @return true if the user is found and successfully updated in the database, false otherwise.
     */
    public boolean updateUser(User user) {
        Optional<User> findUser = getUserById(user.getId());
        if (findUser.isPresent()) {
            this.userRepository.save(user);
        }
        return findUser.isPresent();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to be deleted.
     * @return true if the user was successfully deleted, false otherwise.
     */
    public boolean deleteUser(int userId) {
        Optional<User> findUser = getUserById(userId);
        if (findUser.isPresent()) {
            List<Post> findPosts = this.postRepository.findAllPostsByUserId(findUser.get().getId());
            findPosts.forEach(post -> this.postRepository.deletePostById(post.getId()));
            List<FriendRequest> findFriendRequest = this.friendRequestRepository.findReceiverById(userId);
            this.friendRequestRepository.deleteAll(findFriendRequest);
            this.userRepository.delete(findUser.get());
        }
        return findUser.isPresent();
    }

    /**
     * Finds all subscribers of a user by their ID.
     *
     * @param userId The ID of the user whose subscribers are to be found.
     * @return A list of users who are subscribers of the specified user.
     * Returns an empty list if the specified user is not found.
     */
    public List<User> findAllSubscribers(int userId) {
        Optional<User> findUser = getUserById(userId);
        return findUser.map(this.userRepository::findAllSubscribers).orElse(new ArrayList<>());
    }


}
