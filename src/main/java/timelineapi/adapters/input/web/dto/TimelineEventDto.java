package timelineapi.adapters.input.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import timelineapi.application.domain.TimelineEvent;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class TimelineEventDto {

    private String id;
    private String type;
    private String category;
    private Map<String, Object> customData;
    private LocalDateTime timestamp;

    public static TimelineEventDto from(TimelineEvent event) {
        return TimelineEventDto
                .builder()
                .id(event.getId())
                .type(event.getType())
                .category(event.getCategory())
                .customData(event.getCustomData())
                .timestamp(event.getTimestamp())
                .build();
    }

}
