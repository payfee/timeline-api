package timelineapi.configuration;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import timelineapi.adapters.output.cosmos.CosmosTimelineEventRepository;
import timelineapi.adapters.output.cosmos.CosmosTimelineRepository;

@Configuration
public class AppConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Bean
    public CosmosAsyncClient cosmosAsyncClient(final EnvironmentVars envVars) {
        return new CosmosClientBuilder()
                .endpoint(envVars.getCosmosDBEndpoint())
                .key(envVars.getCosmosDBKey())
                .preferredRegions(envVars.getCosmosDBLocations())
                .consistencyLevel(ConsistencyLevel.SESSION)
                .contentResponseOnWriteEnabled(true)
                .buildAsyncClient();
    }

    @Bean
    public CosmosAsyncDatabase cosmosAsyncDatabase(final EnvironmentVars envVars,
                                                   final CosmosAsyncClient cosmosClient) {
        return cosmosClient.getDatabase(envVars.getCosmosDBName());
    }

    @Bean
    public CosmosTimelineRepository cosmosTimelineRepository(final CosmosAsyncDatabase cosmosDb,
                                                             final ObjectMapper mapper) {
        return new CosmosTimelineRepository(cosmosDb.getContainer("timelines"), mapper);
    }

    @Bean
    public CosmosTimelineEventRepository cosmosTimelineEventRepository(final CosmosAsyncDatabase cosmosDb,
                                                                       final ObjectMapper mapper) {
        return new CosmosTimelineEventRepository(cosmosDb.getContainer("timeline-events"), mapper);
    }

}