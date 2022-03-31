package timelineapi.application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import timelineapi.application.domain.Timeline;
import timelineapi.application.ports.output.CreateTimelinePort;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateTimelineServiceUnitTest {

    @Mock
    private CreateTimelinePort createTimelinePort;

    @InjectMocks
    private CreateTimelineService sut;

    @Test
    public void shouldCreateTimelineCorrectly() {
        final var userId = UUID.randomUUID().toString();
        AtomicReference<Timeline> timeline = new AtomicReference<>();

        when(createTimelinePort.create(argThat(t -> {
            timeline.set(t);
            return true;
        }))).thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier
                .create(sut.create(userId))
                .assertNext(response -> {
                    Assertions.assertEquals(timeline.get(), response);
                    Assertions.assertEquals(userId, response.getOwnerId());

                    verify(createTimelinePort).create(eq(timeline.get()));
                })
                .verifyComplete();
    }

}