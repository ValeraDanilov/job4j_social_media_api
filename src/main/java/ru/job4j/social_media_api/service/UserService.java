package ru.job4j.social_media_api.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.model.Post;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.repository.FriendRequestRepository;
import ru.job4j.social_media_api.repository.PostRepository;
import ru.job4j.social_media_api.repository.UserRepository;
import ru.job4j.social_media_api.dto.request.SignupRequestDTO;
import ru.job4j.social_media_api.dto.response.RegisterDTO;
import ru.job4j.social_media_api.model.ERole;
import ru.job4j.social_media_api.model.Role;
import ru.job4j.social_media_api.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

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
     * Retrieves a list of all friends of a specified user.
     * <p>
     * This method retrieves a list of all friends of the user with the specified ID. The list includes
     * all users who are connected to the specified user as friends.
     *
     * @param userId the ID of the user for whom to retrieve the list of friends
     * @return a list of User objects representing the friends of the specified user
     */
    public List<User> findAllFriends(int userId) {
        return this.userRepository.findAllFriends(userId);
    }

    /**
     * Updates the information of a user in the database.
     *
     * @param user The user object containing the updated information.
     * @return true if the user is found and successfully updated in the database, false otherwise.
     */
    @Transactional
    public boolean updateUser(User user) {
        Optional<User> findUser = getUserById(user.getId());
        if (findUser.isPresent()) {
            this.userRepository.save(user);
        }
        return findUser.isPresent();
    }

    /**
     * Deletes a user and all associated data from the system.
     * <p>
     * This method deletes the specified user along with all their posts and friend requests.
     *
     * @param userId the ID of the user to be deleted
     * @return true if the user was successfully deleted, false otherwise
     */
    @Transactional
    public boolean deleteUser(int userId) {
        List<Post> findPosts = this.postRepository.findAllPostsByUserId(userId);
        findPosts.forEach(post -> this.postRepository.deleteById(post.getId()));
        List<FriendRequest> findFriendRequest = this.friendRequestRepository.findReceiverById(userId);
        this.friendRequestRepository.deleteAll(findFriendRequest);
        return this.userRepository.deleteById(userId) > 0L;
    }

    /**
     * Retrieves a list of all subscribers for the specified user.
     *
     * @param userId the ID of the user for whom to retrieve subscribers
     * @return a list of User objects representing the subscribers
     */
    public List<User> findAllSubscribers(int userId) {
        return this.userRepository.findAllSubscribers(userId);
    }


    public RegisterDTO signUp(SignupRequestDTO signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))
                || Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            return new RegisterDTO(HttpStatus.BAD_REQUEST, "Error: Username or Email is already taken!");
        }

        User person = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        Supplier<RuntimeException> supplier = () -> new RuntimeException("Error: Role is not found.");

        if (strRoles == null) {
            roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(supplier));
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(supplier));
                    case "mod" -> roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(supplier));
                    default -> roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(supplier));
                }
            });
        }
        person.setRoles(roles);
        userRepository.save(person);
        return new RegisterDTO(HttpStatus.OK, "Person registered successfully!");
    }
}
