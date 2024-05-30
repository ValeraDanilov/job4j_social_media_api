package ru.job4j.social_media_api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.social_media_api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

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
                    where fr.receiver = :receiver and fr.status = false
                    """
    )
    List<User> findAllSubscribers(@Param("receiver") User receiver);

    @Query(
            """
                    select user from User user
                    join FriendRequest fr on user = fr.sender or user = fr.receiver
                    where (fr.receiver = :friend or fr.sender = :friend)
                    and fr.status = true
                    and user != :friend
                    """
    )
    List<User> findAllFriends(@Param("friend") User friend);
}
