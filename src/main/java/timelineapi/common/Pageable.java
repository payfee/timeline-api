package timelineapi.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pageable<T> {

    private Long page;
    private Long pageSize;
    private boolean hasMore;
    private Long firstPageUri;
    private Long previousPageUri;
    private Long nextPageUri;
    private List<T> data;

}
