package timelineapi.application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import timelineapi.application.domain.Timeline;
import timelineapi.application.domain.TimelineEvent;
import timelineapi.application.ports.output.GetTimelinePort;
import timelineapi.application.ports.output.ListTimelineEventsPort;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListEventServiceUnitTest {

    @Mock
    private GetTimelinePort getTimelinePort;

    @Mock
    private ListTimelineEventsPort listTimelineEventsPort;

    @InjectMocks
    private ListEventService sut;

    @Test
    public void shouldListTimelineEventsCorrectly() {
        final var timeline = Timeline.create(UUID.randomUUID().toString());
        final var events = List.of(
                TimelineEvent.builder().build(),
                TimelineEvent.builder().build(),
                TimelineEvent.builder().build()
        );

        when(getTimelinePort.get(timeline.getId())).thenReturn(Mono.just(timeline));
        when(listTimelineEventsPort.list(timeline.getId())).thenReturn(Flux.fromIterable(events));

        StepVerifier
                .create(sut.list(timeline.getId(), timeline.getOwnerId()).collectList())
                .assertNext(result -> {
                    Assertions.assertEquals(3, result.size());

                    Assertions.assertEquals(events.get(0), result.get(0));
                    Assertions.assertEquals(events.get(1), result.get(1));
                    Assertions.assertEquals(events.get(2), result.get(2));

                    verify(getTimelinePort).get(timeline.getId());
                    verify(listTimelineEventsPort).list(timeline.getId());
                })
                .verifyComplete();
    }

}