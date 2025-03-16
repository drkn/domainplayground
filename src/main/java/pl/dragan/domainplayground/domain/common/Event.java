package pl.dragan.domainplayground.domain.common;

import java.time.Instant;
import java.util.UUID;

public interface Event {
    UUID id();

    Instant occurredAt();
}
