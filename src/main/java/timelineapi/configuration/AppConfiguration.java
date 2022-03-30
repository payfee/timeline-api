package timelineapi.configuration;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public CosmosAsyncClient cosmosAsyncClient(EnvironmentVars envVars) {
        return new CosmosClientBuilder()
                .endpoint(envVars.getCosmosDBEndpoint())
                .key(envVars.getCosmosDBKey())
                .preferredRegions(envVars.getCosmosDBLocations())
                .consistencyLevel(ConsistencyLevel.SESSION)
                .contentResponseOnWriteEnabled(true)
                .buildAsyncClient();
    }

    @Bean
    public CosmosAsyncContainer cosmosAsyncContainer(EnvironmentVars envVars,
                                                     CosmosAsyncClient cosmosClient) {
        return cosmosClient.getDatabase(envVars.getCosmosDBName()).getContainer("transactions");
    }
}