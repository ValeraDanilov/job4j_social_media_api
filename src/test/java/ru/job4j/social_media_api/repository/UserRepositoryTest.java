package ru.job4j.social_media_api.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.social_media_api.SocialMediaApiApplication;
import ru.job4j.social_media_api.dto.request.SignupRequestDTO;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.model.User;
import ru.job4j.social_media_api.service.UserService;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SocialMediaApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @AfterEach
    public void setUp() {
        this.friendRequestRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(userRepository).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(friendRequestRepository).isNotNull();
    }

    @Test
    void whenCreateUserAndFindByUsername() {
        var user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userRepository.save(user);
        var foundUser = this.userRepository.findByUsername(user.getUsername());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("username");
    }

    @Test
    void whenCreateUserAndCheckExistsByUsernameIsTrue() {
        var user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userRepository.save(user);
        var foundUser = this.userRepository.existsByUsername(user.getUsername());
        assertThat(foundUser).isTrue();
    }

    @Test
    void whenCreateUserAndCheckExistsByUsernameIsFalse() {
        var user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userRepository.save(user);
        var foundUser = this.userRepository.existsByUsername("usernamE");
        assertThat(foundUser).isFalse();
    }

    @Test
    void whenCreateUserAndCheckExistsByEmailIsTrue() {
        var user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userRepository.save(user);
        var foundUser = this.userRepository.existsByEmail(user.getEmail());
        assertThat(foundUser).isTrue();
    }

    @Test
    void whenCreateUserAndCheckExistsByEmailIsFalse() {
        var user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userRepository.save(user);
        var foundUser = this.userRepository.existsByEmail("Email");
        assertThat(foundUser).isFalse();
    }

    @Test
    void whenCreateUsersAndFindAll() {
        var user = new User();
        user.setUsername("John Doe");
        user.setPassword("password");
        user.setEmail("john.doe@example.com");
        var user2 = new User();
        user2.setUsername("Jane Doe");
        user2.setPassword("password");
        user2.setEmail("jane.doe@example.com");
        this.userRepository.save(user);
        this.userRepository.save(user2);
        var users = this.userRepository.findAll();
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getUsername).contains("John Doe", "Jane Doe");
    }

        @Test
    void whenCreateThanFindByUsernameAndPassword() {
            var user = new User();
            user.setUsername("username");
            user.setPassword("password");
            user.setEmail("email@email.com");
            this.userRepository.save(user);
            var foundUser = this.userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
            assertThat(foundUser.get()).isNotNull();
    }

    @Test
    void whenCreateAndNotFindByUsernameAndPassword() {
        var user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userRepository.save(user);
        var foundUser = this.userRepository.findByUsernameAndPassword(user.getUsername(), "1");
        assertThat(foundUser).isEmpty();
    }

    @Test
    void whenCreateUsersThanSendRequestAndUserNotAcceptThanFindAllSubscribers() {
        var firstUser = new User();
        firstUser.setUsername("Ira");
        firstUser.setPassword("password");
        firstUser.setEmail("email@email.com11");
        this.userRepository.save(firstUser);
        var secondUser = new User();
        secondUser.setUsername("Valera");
        secondUser.setPassword("password");
        secondUser.setEmail("email@email.com12");
        this.userRepository.save(secondUser);
        var sendRequest = new FriendRequest();
        sendRequest.setSender(firstUser);
        sendRequest.setReceiver(secondUser);
        sendRequest.setStatus(false);
        this.friendRequestRepository.save(sendRequest);
        var foundSub = this.userRepository.findAllSubscribers(secondUser.getId());
        assertThat(foundSub).isNotNull();
        assertThat(foundSub.get(0).getUsername()).isEqualTo("Ira");
    }

    @Test
    void whenCreateUsersThanSendRequestAndUserAcceptThanFindAllFriends() {
        var firstUser = new User();
        firstUser.setUsername("Ira");
        firstUser.setPassword("password");
        firstUser.setEmail("email@email.com11");
        this.userRepository.save(firstUser);
        var secondUser = new User();
        secondUser.setUsername("Valera");
        secondUser.setPassword("password");
        secondUser.setEmail("email@email.com12");
        this.userRepository.save(secondUser);
        var sendRequest = new FriendRequest();
        sendRequest.setSender(firstUser);
        sendRequest.setReceiver(secondUser);
        sendRequest.setStatus(true);
        this.friendRequestRepository.save(sendRequest);
        var foundSub = this.userRepository.findAllFriends(firstUser.getId());
        assertThat(foundSub).isNotNull();
        assertThat(foundSub.get(0).getUsername()).isEqualTo("Valera");
    }

    @Test
    void whenCreateUserAndDeleteIsSuccessful() {
        var user = new SignupRequestDTO();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userService.signUp(user);
        Optional<User> foundUser = this.userRepository.findByUsername(user.getUsername());
        int isDeleted = this.userRepository.deleteById(foundUser.get().getId());
        assertThat(isDeleted > 0L).isTrue();
    }

    @Test
    void whenCreateUserAndDeleteByIdIsFalse() {
        var user = new SignupRequestDTO();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userService.signUp(user);
        var foundUser = this.userRepository.deleteById(-1);
        assertThat(foundUser > 0L).isFalse();
    }
}
