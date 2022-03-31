package timelineapi.adapters.input.web.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import timelineapi.adapters.input.web.dto.ListTimelineEventsResponse;
import timelineapi.adapters.input.web.dto.TimelineEventDto;
import timelineapi.application.ports.input.ListEventsUseCase;
import timelineapi.common.Errors;
import timelineapi.common.exception.CustomErrorException;

@RestController
@RequiredArgsConstructor
public class ListTimelineEventsV1Controller {

    private static final String USER_ID = "x-user-id";
    private final ListEventsUseCase listEventsUseCase;

    @GetMapping(value = "/v1/timeline/{timeline_id}/events", produces = "application/json")
    public Mono<ResponseEntity<?>> index(
            @RequestHeader(USER_ID) String userId,
            @PathVariable("timeline_id") String timelineId) {
        return validateHeader(userId, timelineId)
                .flux()
                .flatMap(__ -> listEventsUseCase.list(timelineId, userId))
                .map(TimelineEventDto::from)
                .collectList()
                .map(ListTimelineEventsResponse::new)
                .map(ResponseEntity::ok);
    }

    private Mono<String> validateHeader(String userId, String timelineId) {
        if (StringUtils.isBlank(userId))
            return Mono.error(new CustomErrorException(Errors.ERR0001, USER_ID));

        if (StringUtils.isBlank(timelineId))
            return Mono.error(new CustomErrorException(Errors.ERR0001, "Timeline Id"));

        return Mono.just(userId);
    }

}
