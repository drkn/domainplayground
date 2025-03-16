package pl.dragan.domainplayground.domain;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.validator.routines.EmailValidator;
import pl.dragan.domainplayground.domain.validation.Failure;

import java.util.Objects;

import static pl.dragan.domainplayground.domain.validation.Validator.requireFormat;
import static pl.dragan.domainplayground.domain.validation.Validator.requireNotEmpty;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {
    private final String value;

    public static Either<Failure, Email> of(String value) {
        return requireNotEmpty("email", value, "Email cannot be empty")
                .map(String::trim)
                .map(String::toLowerCase)
                .flatMap(it -> requireFormat("email", it, EmailValidator.getInstance()::isValid, "Invalid email format"))
                .map(Email::new);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Email email)) return false;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

