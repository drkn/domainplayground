package pl.dragan.domainplayground.domain;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import pl.dragan.domainplayground.domain.UserCommand.CreateUser;
import pl.dragan.domainplayground.domain.UserFailure.EmailNotUnique;
import pl.dragan.domainplayground.domain.UserFailure.NameNotValid;
import pl.dragan.domainplayground.domain.common.AbstractAggregate;
import pl.dragan.domainplayground.domain.validation.Failure;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class User extends AbstractAggregate<UserId, UserEvent> {
    private final UserId id;
    private final long version;
    private final Name name;
    private final Email email;
    private final Email secondaryEmail;

    private final Supplier<UUID> idSupplier;
    private final Supplier<Instant> timeSupplier;

    public static Either<Failure, User> handle(
            CreateUser command,
            Supplier<UUID> idSupplier,
            Supplier<Instant> timeSupplier,
            EmailUniquenessChecker emailUniquenessChecker,
            NamePolicy namePolicy
    ) {
        if (!emailUniquenessChecker.isUnique(command.email())) {
            return left(new EmailNotUnique(command.email()));
        }
        if (!namePolicy.isValid(command.name())) {
            return left(new NameNotValid(command.name()));
        }
        if (Objects.nonNull(command.secondaryEmail()) && command.secondaryEmail().equals(command.email())) {
            return left(new UserFailure.SecondaryEmailMustBeDifferent(command.secondaryEmail()));
        }

        UserId userId = UserId.of(idSupplier.get()).get();
        User user = new User(userId, 0, command.name(), command.email(), command.secondaryEmail(), idSupplier, timeSupplier);
        user.enqueue(new UserEvent.UserCreated(idSupplier.get(), timeSupplier.get(), userId.value().toString(), user.name.value(), user.email.value()));
        return right(user);
    }

    public Optional<Email> secondaryEmail() {
        return Optional.ofNullable(secondaryEmail);
    }
}
