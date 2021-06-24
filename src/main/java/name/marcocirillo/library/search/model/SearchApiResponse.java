package name.marcocirillo.library.search.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import name.marcocirillo.library.base.BaseApiResponse;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableSearchApiResponse.class)
@JsonDeserialize(as = ImmutableSearchApiResponse.class)
public abstract class SearchApiResponse extends BaseApiResponse {
    abstract List<BookResponse> getResults();
    abstract int getCount();
}
