package timelineapi.application.ports.input;

import reactor.core.publisher.Flux;
import timelineapi.application.domain.TimelineEvent;

public interface ListEventsUseCase {

    Flux<TimelineEvent> list(String timelineId, String userId);

}
