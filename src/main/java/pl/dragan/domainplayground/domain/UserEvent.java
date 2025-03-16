package pl.dragan.domainplayground.domain;

import pl.dragan.domainplayground.domain.common.Event;

import java.time.Instant;
import java.util.UUID;

public sealed interface UserEvent extends Event {
    record UserCreated(UUID id, Instant occurredAt, String userId, String name, String email) implements UserEvent {
    }
}
