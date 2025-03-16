package pl.dragan.domainplayground.domain;

import pl.dragan.domainplayground.domain.validation.Failure.DomainFailure;

public sealed interface UserFailure extends DomainFailure {
    record EmailNotUnique(Email email) implements UserFailure {
        @Override
        public String message() {
            return "Email " + email.value() + " is not unique";
        }
    }

    record NameNotValid(Name name) implements UserFailure {
        @Override
        public String message() {
            return "Name " + name.value() + " is not valid";
        }
    }

    record SecondaryEmailMustBeDifferent(Email email) implements UserFailure {
        @Override
        public String message() {
            return "Secondary email " + email.value() + " must be different from primary email";
        }
    }
}
