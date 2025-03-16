package pl.dragan.domainplayground.domain;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import pl.dragan.domainplayground.domain.validation.Failure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static pl.dragan.domainplayground.domain.validation.Validator.validateRequired;

// User commands - validated, always in consistent state
public sealed interface UserCommand {

    @Getter
    @Accessors(fluent = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    final class CreateUser implements UserCommand {
        private final Name name;
        private final Email email;
        private final Email secondaryEmail;

        public static Either<List<Failure>, CreateUser> of(Name name, Email email, Email secondaryEmail) {
            Map<String, Object> fields = new LinkedHashMap<>();
            fields.put("name", name);
            fields.put("email", email);
            return validateRequired(fields).map(_ -> new CreateUser(name, email, secondaryEmail));
        }
    }
}
