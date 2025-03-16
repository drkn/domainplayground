package pl.dragan.domainplayground.domain;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import pl.dragan.domainplayground.domain.validation.Failure;

import java.util.Objects;

import static pl.dragan.domainplayground.domain.validation.Validator.lengthBetween;
import static pl.dragan.domainplayground.domain.validation.Validator.requireNotEmpty;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Name {
    private final String value;

    public static Either<Failure, Name> of(String value) {
        return requireNotEmpty("name", value, "Name cannot be empty")
                .map(String::trim)
                .flatMap(it -> lengthBetween("name", it, 2, 100, "Name must be between 2 and 100 characters"))
                .map(Name::new);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Name name)) return false;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
