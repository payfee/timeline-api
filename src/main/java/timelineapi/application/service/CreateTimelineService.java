package timelineapi.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import timelineapi.application.domain.Timeline;
import timelineapi.application.ports.input.CreateTimelineUseCase;
import timelineapi.application.ports.output.CreateTimelinePort;

@Service
@RequiredArgsConstructor
public class CreateTimelineService implements CreateTimelineUseCase {

    private final CreateTimelinePort createTimelinePort;

    @Override
    public Mono<Timeline> create(String userId) {
        // TODO: check if user exists before creating timeline

        return Mono
                .just(Timeline.create(userId))
                .flatMap(timeline ->
                        createTimelinePort
                                .create(timeline)
                                .thenReturn(timeline));
    }

}
