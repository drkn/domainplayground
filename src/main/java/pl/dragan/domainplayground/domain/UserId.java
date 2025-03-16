package pl.dragan.domainplayground.domain;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import pl.dragan.domainplayground.domain.validation.Failure;

import java.util.Objects;
import java.util.UUID;

import static pl.dragan.domainplayground.domain.validation.Validator.*;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserId {
    private final UUID value;

    public static Either<Failure, UserId> of(UUID value) {
        return requireNonNull("userId", value, "UserId cannot be empty")
                .map(UserId::new);
    }

    public static Either<Failure, UserId> of(String value) {
        return requireNotEmpty("userId", value, "UserId cannot be empty")
                .flatMap(it -> tryFactory("userId", it, UUID::fromString, "Invalid UserId format"))
                .flatMap(UserId::of);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserId userId)) return false;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
