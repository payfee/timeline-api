package timelineapi.application.ports.output;

import reactor.core.publisher.Flux;
import timelineapi.application.domain.TimelineEvent;

public interface ListTimelineEventsPort {

    Flux<TimelineEvent> list(String timelineId);

}
