package pl.dragan.domainplayground.infra;

import org.springframework.stereotype.Component;
import pl.dragan.domainplayground.domain.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
class InMemoryUserRepository implements UserRepository, EmailUniquenessChecker {

    private final Set<User> users = new HashSet<>();

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return users.stream().filter(it -> it.email().equals(email)).findFirst();
    }

    @Override
    public Optional<User> findById(UserId id) {
        return users.stream().filter(it -> it.id().equals(id)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return users.stream().toList();
    }

    @Override
    public boolean isUnique(Email email) {
        return users.stream().noneMatch(it -> it.email().equals(email));
    }
}
