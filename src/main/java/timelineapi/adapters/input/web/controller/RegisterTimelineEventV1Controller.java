package timelineapi.adapters.input.web.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import timelineapi.adapters.input.web.dto.RegisterTimelineEventResponse;
import timelineapi.application.ports.input.registerEvent.RegisterEventCommand;
import timelineapi.application.ports.input.registerEvent.RegisterEventUseCase;
import timelineapi.common.Errors;
import timelineapi.common.exception.CustomErrorException;

@RestController
@RequiredArgsConstructor
public class RegisterTimelineEventV1Controller {

    private static final String USER_ID = "x-user-id";
    private final RegisterEventUseCase registerEventUseCase;

    @PostMapping(value = "/v1/timeline/{timeline_id}/events", produces = "application/json")
    public Mono<ResponseEntity<?>> index(
            @RequestHeader(USER_ID) String userId,
            @PathVariable("timeline_id") String timelineId,
            @RequestBody RegisterEventCommand command) {
        return validateHeader(userId, timelineId)
                .doOnNext(__ -> command.validate())
                .flatMap(__ -> registerEventUseCase.register(timelineId, command))
                .map(RegisterTimelineEventResponse::from)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    private Mono<String> validateHeader(String userId, String timelineId) {
        if (StringUtils.isBlank(userId))
            return Mono.error(new CustomErrorException(Errors.ERR0001, USER_ID));

        if (StringUtils.isBlank(timelineId))
            return Mono.error(new CustomErrorException(Errors.ERR0001, "Timeline Id"));

        return Mono.just(userId);
    }

}
