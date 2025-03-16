package pl.dragan.domainplayground.domain.common;

import java.util.List;

public interface Aggregate<I, E> {
    I id();

    long version();

    List<E> dequeueEvents();
}
