package name.marcocirillo.library.search.dto;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Value.Immutable
public interface SearchRequestDto {
    String getAuthor();
    String getTitle();
    String getIsbn();
    Optional<UUID> getAuthorId();
    Optional<UUID> getCategoryId();
    int getLimit();
    int getOffset();
}
