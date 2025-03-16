package pl.dragan.domainplayground.domain.validation;

import io.vavr.control.Either;
import io.vavr.control.Try;
import pl.dragan.domainplayground.domain.validation.Failure.ValidationFailure.InvalidFormat;
import pl.dragan.domainplayground.domain.validation.Failure.ValidationFailure.TooLong;
import pl.dragan.domainplayground.domain.validation.Failure.ValidationFailure.TooShort;
import pl.dragan.domainplayground.domain.validation.Failure.ValidationFailure.ValueRequired;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static java.util.Objects.isNull;

public final class Validator {

    public static <T> Either<Failure, T> requireNonNull(String field, T value, String message) {
        return isNull(value) ? left(new ValueRequired(field, message)) : right(value);
    }

    public static Either<Failure, String> requireNotEmpty(String field, String value, String message) {
        return requireNonNull(field, value, message)
                .flatMap(v -> v.trim().isEmpty() ? left(new ValueRequired(field, message)) : right(v));
    }

    public static Either<Failure, String> requireFormat(String field, String value, Function<String, Boolean> formatChecker, String message) {
        return requireNonNull(field, value, message)
                .flatMap(v -> formatChecker.apply(v) ? right(v) : left(new InvalidFormat(field, message)));
    }

    public static Either<Failure, String> minLength(String field, String value, int minLength, String message) {
        return requireNonNull(field, value, message)
                .flatMap(v -> v.length() >= minLength ? right(v) : left(new TooShort(field, message, minLength)));
    }

    public static Either<Failure, String> maxLength(String field, String value, int maxLength, String message) {
        return requireNonNull(field, value, message)
                .flatMap(v -> v.length() <= maxLength ? right(v) : left(new TooLong(field, message, maxLength)));
    }

    public static Either<Failure, String> lengthBetween(String field, String value, int minLength, int maxLength, String message) {
        return minLength(field, value, minLength, message)
                .flatMap(v -> maxLength(field, v, maxLength, message));
    }

    public static <T> Either<Failure, T> tryFactory(String field, String value, Function<String, T> factory, String message) {
        return Try.of(() -> factory.apply(value))
                .map(Either::<Failure, T>right)
                .getOrElse(() -> left(new InvalidFormat(field, message)));
    }

    public static Either<List<Failure>, Void> validateRequired(Map<String, Object> fields) {
        List<Failure> failures = fields.entrySet().stream()
                .map(entry -> requireNonNull(entry.getKey(), entry.getValue(), entry.getKey() + " is required"))
                .filter(Either::isLeft)
                .map(Either::getLeft)
                .toList();
        return failures.isEmpty()
                ? Either.right(null)
                : Either.left(failures);
    }
}
