package timelineapi.adapters.input.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import timelineapi.adapters.input.web.dto.CreateTimelineResponse;
import timelineapi.application.domain.Timeline;
import timelineapi.application.ports.input.CreateTimelineUseCase;
import timelineapi.common.Errors;
import timelineapi.common.exception.CustomErrorException;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateTimelineV1ControllerUnitTest {

    @Mock
    private CreateTimelineUseCase createTimelineUseCase;

    @InjectMocks
    private CreateTimelineV1Controller sut;

    @ParameterizedTest
    @MethodSource("blankStrings")
    public void shouldThrowExceptionWhenValidationFails(String userId) {
        StepVerifier
                .create(sut.index(userId))
                .consumeErrorWith(error -> {
                    assertTrue(error instanceof CustomErrorException);

                    final var ex = (CustomErrorException) error;
                    assertEquals(Errors.ERR0001, ex.getError());
                    assertEquals("x-user-id is required", ex.getMessage());

                    verify(createTimelineUseCase, Mockito.never()).create(any());
                })
                .verify();
    }

    @Test
    public void shouldCreateTimelineCorrectly() {
        final var userId = UUID.randomUUID().toString();
        final var timeline = Timeline.create(userId);

        when(createTimelineUseCase.create(userId)).thenReturn(Mono.just(timeline));

        StepVerifier
                .create(sut.index(userId))
                .assertNext(response -> {
                    verify(createTimelineUseCase).create(userId);

                    assertEquals(HttpStatus.CREATED, response.getStatusCode());
                    assertNotNull(response.getBody());

                    assertTrue(response.getBody() instanceof CreateTimelineResponse);

                    final var body = (CreateTimelineResponse) response.getBody();
                    assertEquals(timeline.getId(), body.getTimelineId());
                })
                .verifyComplete();
    }

    private static Stream<String> blankStrings() {
        return Stream.of("", " ", null);
    }

}