package pl.dragan.domainplayground.infra.http.common;

import io.vavr.collection.Seq;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import pl.dragan.domainplayground.domain.validation.Failure;
import pl.dragan.domainplayground.infra.http.common.FieldValidation.FieldValidationError;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.dragan.domainplayground.infra.http.common.FieldValidation.failureToCode;

@UtilityClass
public class HttpProblems {

    public static ResponseEntity fromFailures(List<Failure> failures) {
        return fromFailures(failures, Map.of());
    }

    public static ResponseEntity fromFailures(List<Failure> failures, Map<String, String> fieldMapping) {
        List<FieldValidationError> fieldValidationErrors =
                failures.stream()
                        .filter(Failure.ValidationFailure.class::isInstance)
                        .map(Failure.ValidationFailure.class::cast)
                        .map(failure -> FieldValidationError.from(failure, fieldMapping))
                        .toList();
        Optional<Failure> domainFailure = failures.stream().filter(it -> !(it instanceof Failure.ValidationFailure)).findFirst();
        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(domainFailure.map(_ -> 422).orElse(400)),
                domainFailure.map(Failure::message).orElse("Invalid parameters")
        );
        if (!fieldValidationErrors.isEmpty()) {
            problem.setProperty("errors", fieldValidationErrors);
        }
        return ResponseEntity.of(problem).build();
    }

    public static ResponseEntity fromValidationErrors(List<FieldValidationError> errors) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), "Invalid parameters");
        problem.setProperty("errors", errors);
        return ResponseEntity.of(problem).build();
    }

    public static ResponseEntity from(Seq<FieldValidationError> errors) {
        return fromValidationErrors(errors.toJavaList());
    }

    public static ResponseEntity from(Failure failure) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), failure.message());
        problem.setTitle(titleFromFailure(failure)); // may require more complex mapper
        return ResponseEntity.of(problem).build();
    }

    public static ResponseEntity from(String field, Failure failure) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), failure.message());
        problem.setTitle(titleFromFailure(failure)); // may require more complex mapper
        problem.setProperty("errors", List.of(
                new FieldValidationError(field, failureToCode(failure), failure.message())
        ));
        return ResponseEntity.of(problem).build();
    }

    private static String titleFromFailure(Failure failure) {
        String pascalCaseString = failure.getClass().getSimpleName();
        Pattern pattern = Pattern.compile("(?<=[a-z])(?=[A-Z])");
        Matcher matcher = pattern.matcher(pascalCaseString);
        return matcher.replaceAll(" ");
    }
}
