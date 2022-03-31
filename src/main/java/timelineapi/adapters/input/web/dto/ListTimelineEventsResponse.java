package timelineapi.adapters.input.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ListTimelineEventsResponse {

    private List<TimelineEventDto> items;

}
