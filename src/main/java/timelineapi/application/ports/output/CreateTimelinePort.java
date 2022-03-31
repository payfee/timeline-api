package timelineapi.application.ports.output;

import reactor.core.publisher.Mono;
import timelineapi.application.domain.Timeline;

public interface CreateTimelinePort {

    Mono<Boolean> create(Timeline timeline);

}
