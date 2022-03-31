package timelineapi.application.ports.input.registerEvent;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import timelineapi.common.SelfValidating;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class RegisterEventCommand extends SelfValidating<RegisterEventCommand> {

    @NotNull
    @Length(min = 36, max = 36)
    private String id;

    @NotBlank
    private String type;

    @NotBlank
    private String category;

    private Map<String, Object> customData;

    @NotNull
    private LocalDateTime timestamp;

}
