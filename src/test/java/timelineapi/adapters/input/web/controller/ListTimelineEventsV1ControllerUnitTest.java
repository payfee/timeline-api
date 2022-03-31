package timelineapi.adapters.input.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import timelineapi.adapters.input.web.dto.ListTimelineEventsResponse;
import timelineapi.adapters.input.web.dto.TimelineEventDto;
import timelineapi.application.domain.TimelineEvent;
import timelineapi.application.ports.input.ListEventsUseCase;
import timelineapi.common.Errors;
import timelineapi.common.exception.CustomErrorException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListTimelineEventsV1ControllerUnitTest {

    @Mock
    private ListEventsUseCase listEventsUseCase;

    @InjectMocks
    private ListTimelineEventsV1Controller sut;

    @Test
    public void shouldListTimelineEventsCorrectly() {
        final var userId = UUID.randomUUID().toString();
        final var timelineId = UUID.randomUUID().toString();
        final var events = List.of(
                TimelineEvent.builder().build(),
                TimelineEvent.builder().build()
        );

        when(listEventsUseCase.list(timelineId, userId)).thenReturn(Flux.fromIterable(events));

        StepVerifier
                .create(sut.index(userId, timelineId))
                .assertNext(response -> {
                    verify(listEventsUseCase).list(timelineId, userId);

                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertTrue(response.getBody() instanceof ListTimelineEventsResponse);

                    final var body = (ListTimelineEventsResponse) response.getBody();
                    assertEquals(2, body.getItems().size());
                    assertItemResponse(events.get(0), body.getItems().get(0));
                    assertItemResponse(events.get(1), body.getItems().get(1));
                })
                .verifyComplete();
    }

    private void assertItemResponse(TimelineEvent event, TimelineEventDto dto) {
        assertEquals(event.getId(), dto.getId());
        assertEquals(event.getType(), dto.getType());
        assertEquals(event.getType(), dto.getCategory());
        assertEquals(event.getCustomData(), dto.getCustomData());
        assertEquals(event.getTimestamp(), dto.getTimestamp());
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    public void shouldReturnErrorWhenUserIdIsBlank(String userId) {
        final var timelineId = UUID.randomUUID().toString();

        StepVerifier
                .create(sut.index(userId, timelineId))
                .consumeErrorWith(error -> {
                    verify(listEventsUseCase, never()).list(any(), any());

                    assertTrue(error instanceof CustomErrorException);

                    final var ex = (CustomErrorException) error;
                    assertEquals(Errors.ERR0001, ex.getError());
                    assertEquals("x-user-id is required", ex.getMessage());
                })
                .verify();
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    public void shouldReturnErrorWhenTimelineIdIsBlank(String timelineId) {
        final var userId = UUID.randomUUID().toString();

        StepVerifier
                .create(sut.index(userId, timelineId))
                .consumeErrorWith(error -> {
                    verify(listEventsUseCase, never()).list(any(), any());

                    assertTrue(error instanceof CustomErrorException);

                    final var ex = (CustomErrorException) error;
                    assertEquals(Errors.ERR0001, ex.getError());
                    assertEquals("Timeline Id is required", ex.getMessage());
                })
                .verify();
    }

    private static Stream<String> blankStrings() {
        return Stream.of("", " ", null);
    }

}