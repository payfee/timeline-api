package timelineapi.adapters.input.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import timelineapi.application.domain.TimelineEvent;

@Getter
@AllArgsConstructor
public class RegisterTimelineEventResponse {

    private String event_id;

    public static RegisterTimelineEventResponse from(TimelineEvent event) {
        return new RegisterTimelineEventResponse(event.getId());
    }

}
