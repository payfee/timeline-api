package timelineapi.adapters.input.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import timelineapi.application.domain.Timeline;

@Getter
@AllArgsConstructor
public class CreateTimelineResponse {

    private String timelineId;

    public static CreateTimelineResponse from(Timeline timeline) {
        return new CreateTimelineResponse(timeline.getId());
    }

}
