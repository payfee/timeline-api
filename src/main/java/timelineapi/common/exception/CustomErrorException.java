package timelineapi.common.exception;

import lombok.Getter;
import timelineapi.common.Errors;

@Getter
public class CustomErrorException extends RuntimeException {

    private final Errors error;

    public CustomErrorException(Errors error, String resource) {
        super(String.format(error.getMessage(), resource));
        this.error = error;
    }
}
