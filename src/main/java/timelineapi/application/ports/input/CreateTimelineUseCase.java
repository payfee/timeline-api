package timelineapi.application.ports.input;

import reactor.core.publisher.Mono;
import timelineapi.application.domain.Timeline;

public interface CreateTimelineUseCase {

    Mono<Timeline> create(String userId);

}
