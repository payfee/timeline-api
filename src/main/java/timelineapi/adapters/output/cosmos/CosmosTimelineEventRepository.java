package timelineapi.adapters.output.cosmos;

import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.models.PartitionKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import timelineapi.application.domain.TimelineEvent;
import timelineapi.application.ports.output.ListTimelineEventsPort;
import timelineapi.application.ports.output.RegisterTimelineEventPort;

import java.util.Map;

@RequiredArgsConstructor
public class CosmosTimelineEventRepository implements RegisterTimelineEventPort, ListTimelineEventsPort {

    private final CosmosAsyncContainer container;
    private final ObjectMapper mapper;

    @Override
    public Flux<TimelineEvent> list(String timelineId) {
        return container
                .readAllItems(new PartitionKey(timelineId), Map.class)
                .map(entity -> mapper.convertValue(entity, TimelineEvent.class))
                .sort(CosmosTimelineEventRepository::sortByTimestampDesc);
    }

    @Override
    public Mono<Boolean> register(TimelineEvent event) {
        return Mono
                .just(new PartitionKey(event.getTimelineId()))
                .map(pk -> Pair.of(pk, mapper.convertValue(event, Map.class)))
                .flatMap(pair -> container.createItem(pair.getRight(), pair.getLeft(), null))
                .thenReturn(Boolean.TRUE);
    }

    private static int sortByTimestampDesc(TimelineEvent left, TimelineEvent right) {
        return right.getTimestamp().compareTo(left.getTimestamp());
    }

}
