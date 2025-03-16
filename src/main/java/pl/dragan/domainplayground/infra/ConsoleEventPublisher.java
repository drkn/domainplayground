package pl.dragan.domainplayground.infra;

import org.springframework.stereotype.Component;
import pl.dragan.domainplayground.app.EventPublisher;
import pl.dragan.domainplayground.domain.common.Event;

@Component
class ConsoleEventPublisher implements EventPublisher {
    @Override
    public void publish(Event event) {
        System.out.println("Publishing event: " + event);
    }
}
