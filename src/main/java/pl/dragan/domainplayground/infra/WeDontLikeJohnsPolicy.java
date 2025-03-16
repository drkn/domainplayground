package pl.dragan.domainplayground.infra;

import org.springframework.stereotype.Component;
import pl.dragan.domainplayground.domain.Name;
import pl.dragan.domainplayground.domain.NamePolicy;

@Component
class WeDontLikeJohnsPolicy implements NamePolicy {
    @Override
    public boolean isValid(Name name) {
        return !name.value().equalsIgnoreCase("john");
    }
}
