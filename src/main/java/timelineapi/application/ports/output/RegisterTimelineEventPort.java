package timelineapi.application.ports.output;

import reactor.core.publisher.Mono;
import timelineapi.application.domain.TimelineEvent;

public interface RegisterTimelineEventPort {

    Mono<Boolean> register(TimelineEvent event);

}
