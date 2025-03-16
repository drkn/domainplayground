package pl.dragan.domainplayground.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findByEmail(Email email);

    Optional<User> findById(UserId id);

    List<User> findAll();
}
