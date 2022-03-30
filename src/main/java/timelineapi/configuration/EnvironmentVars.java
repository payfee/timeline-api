package timelineapi.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class EnvironmentVars {

    @Value("${cosmos.endpoint}")
    private String cosmosDBEndpoint;

    @Value("${cosmos.key}")
    private String cosmosDBKey;

    @Value("${cosmos.locations}")
    private String cosmosDBLocations;

    @Value("${cosmos.db_name}")
    private String cosmosDBName;

    public List<String> getCosmosDBLocations() {
        return Arrays.stream(cosmosDBLocations.split(",")).toList();
    }

}
