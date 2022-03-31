package timelineapi.adapters.input.web.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import timelineapi.adapters.input.web.dto.CreateTimelineResponse;
import timelineapi.application.ports.input.CreateTimelineUseCase;
import timelineapi.common.Errors;
import timelineapi.common.exception.CustomErrorException;

@RestController
@RequiredArgsConstructor
public class CreateTimelineV1Controller {

    private static final String USER_ID = "x-user-id";
    private final CreateTimelineUseCase createTimelineUseCase;

    @PostMapping(value = "/v1/timeline", produces = "application/json")
    public Mono<ResponseEntity<?>> index(@RequestHeader(USER_ID) String userId) {
        return validateHeader(userId)
                .flatMap(createTimelineUseCase::create)
                .map(CreateTimelineResponse::from)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    private Mono<String> validateHeader(String userId) {
        if (StringUtils.isBlank(userId))
            return Mono.error(new CustomErrorException(Errors.ERR0001, USER_ID));

        return Mono.just(userId);
    }

}
