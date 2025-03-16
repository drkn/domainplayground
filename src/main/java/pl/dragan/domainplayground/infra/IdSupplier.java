package pl.dragan.domainplayground.infra;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Supplier;

@Component
class IdSupplier implements Supplier<UUID> {
    @Override
    public UUID get() {
        return UUID.randomUUID();
    }
}
