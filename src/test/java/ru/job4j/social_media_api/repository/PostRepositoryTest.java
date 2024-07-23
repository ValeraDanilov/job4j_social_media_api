package ru.job4j.social_media_api.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.social_media_api.SocialMediaApiApplication;
import ru.job4j.social_media_api.model.FriendRequest;
import ru.job4j.social_media_api.model.Post;
import ru.job4j.social_media_api.model.User;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SocialMediaApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @AfterEach
    void setUp() {
        this.friendRequestRepository.deleteAll();
        this.postRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(postRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(friendRequestRepository).isNotNull();
    }

    @Test
    void whenCreateUserAndPostThanFindAllPostsByUserId() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userRepository.save(user);
        Post post = new Post();
        post.setUser(user);
        post.setTitle("Title12345");
        post.setDescription("Description");
        this.postRepository.save(post);
        List<Post> posts = this.postRepository.findAllPostsByUserId(user.getId());
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void wenCreatePostAndUserThanFindAllByCreatedBetween() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userRepository.save(user);
        Post post = new Post();
        post.setUser(user);
        post.setTitle("Title12345");
        post.setDescription("Description");
        post.setCreated(LocalDateTime.of(2022, 10, 15, 12, 30));
        this.postRepository.save(post);
        Post post2 = new Post();
        post2.setUser(user);
        post2.setTitle("Title12345");
        post2.setDescription("Description");
        post2.setCreated(LocalDateTime.of(2023, 10, 15, 12, 30));
        this.postRepository.save(post2);
        Post post3 = new Post();
        post3.setUser(user);
        post3.setTitle("Title12345");
        post3.setDescription("Description");
        post3.setCreated(LocalDateTime.of(2024, 10, 15, 12, 30));
        this.postRepository.save(post3);
        List<Post> posts = this.postRepository.findAllByCreatedBetween(post.getCreated(), post2.getCreated());
        assertThat(posts.size()).isEqualTo(2);
    }

    @Test
    void findAllByOrderByCreatedDesc() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        this.userRepository.save(user);
        Post post = new Post();
        post.setUser(user);
        post.setTitle("Title12345");
        post.setDescription("Description");
        this.postRepository.save(post);
        Post post2 = new Post();
        post2.setUser(user);
        post2.setTitle("Atitle12345");
        post2.setDescription("Description");
        this.postRepository.save(post2);
        Page<Post> posts = this.postRepository.findAllByOrderByCreatedDesc(null);
        assertThat(posts.stream().toList().get(0).getTitle()).isEqualTo(post2.getTitle());
    }

    @Transactional
    @Test
    void whenCreateUserAndPostThanUpdatePostTitleAndDescription() {
        User user = new User();
        user.setUsername("username1");
        user.setPassword("password1");
        user.setEmail("email@email.com1");
        this.userRepository.save(user);
        Post post = new Post();
        post.setUser(user);
        post.setTitle("Title12345");
        post.setDescription("Description");
        this.postRepository.save(post);
        post.setTitle("UpdateTitle");
        post.setDescription("UpdateDescription");
        this.postRepository.updatePostTitleAndDescription(post.getTitle(), post.getDescription(), post.getId());
        assertThat(post.getTitle()).isEqualTo("UpdateTitle");
        assertThat(post.getDescription()).isEqualTo("UpdateDescription");
    }

    @Test
    void whenCrateUsersAndPostThanFindAllPostFromUserSubscriptions() {
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
        var post = new Post();
        post.setTitle("UpdateTitle");
        post.setDescription("UpdateDescription");
        post.setUser(secondUser);
        this.postRepository.save(post);
        var foundSub = this.postRepository.findAllPostFromUserSubscriptions(firstUser, null);
        assertThat(foundSub.stream().toList().get(0).getTitle()).isEqualTo("UpdateTitle");
    }

    @Test
    void whenCreateUserAndPostThanDeletePostById() {
        User user = new User();
        user.setUsername("username1");
        user.setPassword("password1");
        user.setEmail("email@email.com1");
        this.userRepository.save(user);
        Post post = new Post();
        post.setUser(user);
        post.setTitle("Title12345");
        post.setDescription("Description");
        this.postRepository.save(post);
        int isDeleted = this.postRepository.deleteById(post.getId());
        assertThat(isDeleted > 0L).isTrue();
    }
}
