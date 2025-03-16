package pl.dragan.domainplayground.infra;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

@Component
class TimeSupplier implements Supplier<Instant> {

    private final Clock clock = Clock.systemUTC();

    @Override
    public Instant get() {
        return clock.instant().truncatedTo(ChronoUnit.MILLIS);
    }
}
