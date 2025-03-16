package pl.dragan.domainplayground.app;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dragan.domainplayground.domain.EmailUniquenessChecker;
import pl.dragan.domainplayground.domain.NamePolicy;
import pl.dragan.domainplayground.domain.User;
import pl.dragan.domainplayground.domain.UserCommand.CreateUser;
import pl.dragan.domainplayground.domain.UserRepository;
import pl.dragan.domainplayground.domain.validation.Failure;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Supplier<UUID> idSupplier;
    private final Supplier<Instant> timeSupplier;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final EmailUniquenessChecker emailUniquenessChecker;
    private final NamePolicy namePolicy;

    public Either<Failure, User> handle(CreateUser command) {
        return User.handle(command, idSupplier, timeSupplier, emailUniquenessChecker, namePolicy)
                .peek(userRepository::save)
                .peek(user -> user.dequeueEvents().forEach(eventPublisher::publish));
    }
}
