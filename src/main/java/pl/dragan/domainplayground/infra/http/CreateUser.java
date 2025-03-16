package pl.dragan.domainplayground.infra.http;

import pl.dragan.domainplayground.domain.Email;
import pl.dragan.domainplayground.domain.User;

sealed interface CreateUser {
    record Request(String name, String primaryEmail, String secondaryEmail) implements CreateUser {
    }

    record Response(String id, String name, String primaryEmail, String secondaryEmail) implements CreateUser {
        static Response from(User user) {
            return new Response(
                    user.id().value().toString(),
                    user.name().value(),
                    user.email().value(),
                    user.secondaryEmail().map(Email::value).orElse(null));
        }
    }
}
