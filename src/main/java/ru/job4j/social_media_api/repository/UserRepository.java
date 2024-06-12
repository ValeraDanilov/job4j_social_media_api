package ru.job4j.social_media_api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.social_media_api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findAll();

    @Query(
            """
                    select user from User user
                    where user.username = :username and user.password = :password
                    """
    )
    Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);


    @Query(
            """
                    select user from User user
                    join FriendRequest fr on user.id = fr.sender.id
                    where fr.receiver.id = :userId and fr.status = false
                    """
    )
    List<User> findAllSubscribers(@Param("userId") int userId);

    @Query(
            """
                    select user from User user
                    join FriendRequest fr on user = fr.sender or user = fr.receiver
                    where (fr.receiver.id = :userId or fr.sender.id = :userId)
                    and fr.status = true
                    and user.id != :userId
                    """
    )
    List<User> findAllFriends(@Param("userId") int userId);

    boolean deleteById(int userId);
}
