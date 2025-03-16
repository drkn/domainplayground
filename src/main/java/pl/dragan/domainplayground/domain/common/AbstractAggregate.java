package pl.dragan.domainplayground.domain.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class AbstractAggregate<IdType, EventType> implements Aggregate<IdType, EventType> {

    protected IdType id;
    protected long version;
    private final Queue<EventType> events = new LinkedList<>();

    @Override
    public IdType id() {
        return id;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public List<EventType> dequeueEvents() {
        List<EventType> dequeued = new ArrayList<>(events);
        events.clear();
        return dequeued;
    }

    protected AbstractAggregate<IdType, EventType> enqueue(EventType event) {
        events.add(event);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(this.getClass()) &&
                ((AbstractAggregate<?, ?>) obj).id.equals(this.id);
    }
}
