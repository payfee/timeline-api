package timelineapi.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import timelineapi.application.domain.TimelineEvent;
import timelineapi.application.ports.input.registerEvent.RegisterEventCommand;
import timelineapi.application.ports.input.registerEvent.RegisterEventUseCase;
import timelineapi.application.ports.output.GetTimelinePort;
import timelineapi.application.ports.output.RegisterTimelineEventPort;

@Service
@RequiredArgsConstructor
public class RegisterTimelineEventService implements RegisterEventUseCase {

    private final GetTimelinePort getTimelinePort;
    private final RegisterTimelineEventPort registerTimelineEventPort;

    @Override
    public Mono<TimelineEvent> register(String timelineId, RegisterEventCommand command) {
        return getTimelinePort
                .get(timelineId)
                .map(ignored -> createEvent(timelineId, command))
                .flatMap(timelineEvent -> registerTimelineEventPort
                        .register(timelineEvent)
                        .thenReturn(timelineEvent));
    }

    private TimelineEvent createEvent(String timelineId, RegisterEventCommand command) {
        return TimelineEvent
                .builder()
                .timelineId(timelineId)
                .id(command.getId())
                .type(command.getType())
                .category(command.getCategory())
                .customData(command.getCustomData())
                .timestamp(command.getTimestamp())
                .build();
    }

}
