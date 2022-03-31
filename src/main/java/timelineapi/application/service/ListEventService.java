package timelineapi.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import timelineapi.application.domain.Timeline;
import timelineapi.application.domain.TimelineEvent;
import timelineapi.application.ports.input.ListEventsUseCase;
import timelineapi.application.ports.output.GetTimelinePort;
import timelineapi.application.ports.output.ListTimelineEventsPort;
import timelineapi.common.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class ListEventService implements ListEventsUseCase {

    private final GetTimelinePort getTimelinePort;
    private final ListTimelineEventsPort listTimelineEventsPort;

    @Override
    public Flux<TimelineEvent> list(final String timelineId,
                                    final String userId) {
        return getTimelinePort
                .get(timelineId)
                .doOnNext(timeline -> this.checkOwnership(timeline, userId))
                .flux()
                .flatMap(timeline -> listTimelineEventsPort.list(timelineId));
    }

    private void checkOwnership(Timeline timeline, String userId) {
        if (!timeline.getOwnerId().equals(userId))
            throw new ResourceNotFoundException("Timeline");
    }

}
