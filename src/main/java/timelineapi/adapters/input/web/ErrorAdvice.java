package timelineapi.adapters.input.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import timelineapi.common.ErrorResponse;
import timelineapi.common.exception.CustomErrorException;

@ControllerAdvice
public class ErrorAdvice {

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ErrorResponse> handleCustomError(CustomErrorException ex) {
        return ResponseEntity
                .status(ex.getError().getStatus())
                .body(new ErrorResponse(ex.getError().toString(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(null);
    }

}
