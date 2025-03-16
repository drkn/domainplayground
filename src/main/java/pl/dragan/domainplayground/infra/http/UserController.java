package pl.dragan.domainplayground.infra.http;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dragan.domainplayground.app.UserService;
import pl.dragan.domainplayground.domain.Email;
import pl.dragan.domainplayground.domain.Name;
import pl.dragan.domainplayground.domain.UserCommand;
import pl.dragan.domainplayground.domain.UserFailure;
import pl.dragan.domainplayground.infra.http.common.HttpProblems;

import java.util.Map;

import static io.vavr.control.Validation.combine;
import static pl.dragan.domainplayground.infra.http.common.FieldValidation.validateField;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @PostMapping("/commands/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUser.Request request) {
        return combine( // validate fields - only if they are present (does not check requirement)
                validateField("name", request.name(), Name::of),
                validateField("primaryEmail", request.primaryEmail(), Email::of),
                validateField("secondaryEmail", request.secondaryEmail(), Email::of))
                .ap(UserCommand.CreateUser::of) // create command which checks requirements
                .fold(HttpProblems::from, // Problem details if validateField detected problems
                        command -> command.fold(
                                invalidCommand -> HttpProblems.fromFailures(invalidCommand, // command didn't create and returned list of failures
                                        Map.of("email", "primaryEmail") // custom field mapping command.field -> request.field
                                ),
                                validCommand -> userService.handle(validCommand) // call app service with valid command
                                        .map(CreateUser.Response::from) // create response dto
                                        .map(ResponseEntity::ok) // wrap it with ResponseEntity
                                        .getOrElseGet(failure -> switch (failure) { // custom app service failure handler (could be HttpResponse::from)
                                            case UserFailure.EmailNotUnique _ ->
                                                    HttpProblems.from("primaryEmail", failure); // assign domain failure to field error
                                            default -> HttpProblems.from(failure); // return default error
                                        })
                        )
                );
    }

}
