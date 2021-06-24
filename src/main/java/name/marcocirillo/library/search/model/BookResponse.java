package name.marcocirillo.library.search.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableBookResponse.class)
@JsonDeserialize(as = ImmutableBookResponse.class)
public interface BookResponse {
    UUID getId();
    String getAuthorName();
    String getTitle();
    String getIsbn();
    int getInventoryLevel();
}
