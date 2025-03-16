package pl.dragan.domainplayground.app;

import pl.dragan.domainplayground.domain.common.Event;

public interface EventPublisher {
    void publish(Event event);
}
