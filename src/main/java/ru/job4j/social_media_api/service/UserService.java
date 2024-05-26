package ru.job4j.social_media_api.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final String EXCEPTION = "User not found";

    /**
     * find the User.
     *
     * @param userId User`s id.
     * @return User or null.
     */
    public User getUserById(int userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

    /**
     * Finds a user by username and password.
     *
     * @param user The user object containing the username and password to search for.
     * @return An Optional containing the user if found, otherwise empty.
     * @throws IllegalArgumentException if the user is not found.
     */
    public Optional<User> findByUsernameAndPassword(User user) {
        Optional<User> findUser = this.userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (findUser.isPresent()) {
            return findUser;
        }
        throw new IllegalArgumentException(EXCEPTION);
    }

    /**
     * Finds all subscribers of a given user.
     *
     * @param receiver The user object for which to find subscribers.
     * @return A list of subscribers of the given user.
     * @throws IllegalArgumentException if the user is not found.
     */
    public List<User> findAllSubscribers(User receiver) {
        User findUser = getUserById(receiver.getId());
        if (findUser != null) {
            return this.userRepository.findAllSubscribers(findUser);
        }
        throw new IllegalArgumentException(EXCEPTION);
    }

    /**
     * Finds all friends of a given user.
     *
     * @param friend The user object for which to find friends.
     * @return A list of friends of the given user.
     * @throws IllegalArgumentException if the user is not found.
     */
    public List<User> findAllFriends(User friend) {
        User findUser = getUserById(friend.getId());
        if (findUser != null) {
            return this.userRepository.findAllFriends(findUser);
        }
        throw new IllegalArgumentException(EXCEPTION);
    }
}
