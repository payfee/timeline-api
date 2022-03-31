package timelineapi.adapters.output.cosmos;

import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.implementation.NotFoundException;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.PartitionKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Mono;
import timelineapi.application.domain.Timeline;
import timelineapi.application.ports.output.CreateTimelinePort;
import timelineapi.application.ports.output.GetTimelinePort;
import timelineapi.common.exception.ResourceNotFoundException;

import java.util.Map;

@AllArgsConstructor
public class CosmosTimelineRepository implements GetTimelinePort, CreateTimelinePort {

    private final CosmosAsyncContainer container;
    private final ObjectMapper mapper;

    @Override
    public Mono<Timeline> get(String timelineId) {
        return Mono
                .just(new PartitionKey(timelineId))
                .flatMap(pk -> container.readItem(timelineId, pk, Map.class))
                .map(CosmosItemResponse::getItem)
                .map(entity -> mapper.convertValue(entity, Timeline.class))
                .onErrorMap(NotFoundException.class, error -> new ResourceNotFoundException("Timeline"));
    }

    @Override
    public Mono<Boolean> create(Timeline timeline) {
        return Mono
                .just(new PartitionKey(timeline.getId()))
                .map(pk -> Pair.of(pk, mapper.convertValue(timeline, Map.class)))
                .flatMap(pair -> container.createItem(pair.getRight(), pair.getLeft(), null))
                .thenReturn(Boolean.TRUE);
    }

}
