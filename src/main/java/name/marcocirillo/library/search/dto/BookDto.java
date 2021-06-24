package name.marcocirillo.library.search.dto;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface BookDto {
    UUID getId();
    UUID getAuthorId();
    String getAuthorName();
    String getTitle();
    String getIsbn();
    Boolean isAvailable();
    Integer getInventoryLevel();
}
