package timelineapi.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Errors {
    ERR0001("%s is required", HttpStatus.BAD_REQUEST),
    ERR0002("%s is invalid", HttpStatus.BAD_REQUEST),
    ERR0003("%s not found", HttpStatus.NOT_FOUND),
    ERR0004("%s already exists", HttpStatus.CONFLICT),
    ERR0005("%s already being used", HttpStatus.CONFLICT),
    ERR0006("%s is empty", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    Errors(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
