package pl.dragan.domainplayground.infra.http.common;

import io.vavr.control.Either;
import io.vavr.control.Validation;
import lombok.experimental.UtilityClass;
import pl.dragan.domainplayground.domain.validation.Failure;
import pl.dragan.domainplayground.domain.validation.Failure.ValidationFailure;
import pl.dragan.domainplayground.domain.validation.Failure.ValidationFailure.InvalidFormat;
import pl.dragan.domainplayground.domain.validation.Failure.ValidationFailure.TooLong;
import pl.dragan.domainplayground.domain.validation.Failure.ValidationFailure.TooShort;
import pl.dragan.domainplayground.domain.validation.Failure.ValidationFailure.ValueRequired;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class FieldValidation {

    public record FieldValidationError(String field, String code, String message) {
        public static FieldValidationError from(ValidationFailure failure) {
            return from(failure, Map.of());

        }

        public static FieldValidationError from(ValidationFailure failure, Map<String, String> fieldMapping) {
            return new FieldValidationError(
                    fieldMapping.getOrDefault(failure.field(), failure.field()), failureToCode(failure), failure.message());
        }
    }

    public static <T> Validation<FieldValidationError, T> validateField(String field, String value, Function<String, Either<Failure, T>> domainFieldFactory) {
        return Objects.isNull(value)
                ? Validation.valid(null)
                : domainFieldFactory.apply(value)
                .mapLeft(it -> new FieldValidationError(field, failureToCode(it), it.message()))
                .toValidation();
    }

    public static String failureToCode(Failure failure) {
        return switch (failure) {
            case ValueRequired _ -> "required";
            case InvalidFormat _ -> "invalid_format";
            case TooLong _ -> "too_long";
            case TooShort _ -> "too_short";
            default -> defaultCodeFromFailure(failure);
        };
    }

    private static String defaultCodeFromFailure(Failure failure) {
        String pascalCaseString = failure.getClass().getSimpleName();
        Pattern pattern = Pattern.compile("(?<=[a-z])(?=[A-Z])");
        Matcher matcher = pattern.matcher(pascalCaseString);
        return matcher.replaceAll("_").toLowerCase();
    }
}
