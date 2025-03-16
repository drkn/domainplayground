package pl.dragan.domainplayground.domain.validation;

public interface Failure {
    String message();

    sealed interface ValidationFailure extends Failure {
        String field();

        record ValueRequired(String field, String message) implements ValidationFailure {
        }

        record InvalidFormat(String field, String message) implements ValidationFailure {
        }

        record TooShort(String field, String message, int length) implements ValidationFailure {
        }

        record TooLong(String field, String message, int length) implements ValidationFailure {
        }
    }

    interface DomainFailure extends Failure {
    }
}
