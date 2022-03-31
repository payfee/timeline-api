package timelineapi.application.ports.input.registerEvent;

import reactor.core.publisher.Mono;
import timelineapi.application.domain.TimelineEvent;

public interface RegisterEventUseCase {

    Mono<TimelineEvent> register(String timelineId, RegisterEventCommand command);

}
