package timelineapi.application.service;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import timelineapi.application.domain.Timeline;
import timelineapi.application.ports.input.registerEvent.RegisterEventCommand;
import timelineapi.application.ports.output.GetTimelinePort;
import timelineapi.application.ports.output.RegisterTimelineEventPort;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class RegisterTimelineEventServiceUnitTest {

    @Mock
    private GetTimelinePort getTimelinePort;

    @Mock
    private RegisterTimelineEventPort registerTimelineEventPort;

    @InjectMocks
    private RegisterTimelineEventService sut;

    @Test
    public void shouldCreateTimelineEventCorrectly() {
        final var timeline = Timeline.create(UUID.randomUUID().toString());
        final var command = RegisterEventCommand
                .builder()
                .id(UUID.randomUUID().toString())
                .category(RandomString.make())
                .type(RandomString.make())
                .timestamp(LocalDateTime.now())
                .customData(Map.of("field", "value"))
                .build();

        Mockito.when(getTimelinePort.get(timeline.getId())).thenReturn(Mono.just(timeline));
        Mockito.when(registerTimelineEventPort.register(any())).thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier
                .create(sut.register(timeline.getId(), command))
                .assertNext(result -> {
                    Mockito.verify(getTimelinePort).get(timeline.getId());
                    Mockito.verify(registerTimelineEventPort).register(argThat(event -> {
                        Assertions.assertEquals(result, event);

                        Assertions.assertEquals(timeline.getId(), event.getTimelineId());
                        Assertions.assertEquals(command.getId(), event.getId());
                        Assertions.assertEquals(command.getType(), event.getType());
                        Assertions.assertEquals(command.getCategory(), event.getCategory());
                        Assertions.assertEquals(command.getCustomData(), event.getCustomData());
                        Assertions.assertEquals(command.getTimestamp(), event.getTimestamp());

                        return true;
                    }));
                })
                .verifyComplete();
    }

}