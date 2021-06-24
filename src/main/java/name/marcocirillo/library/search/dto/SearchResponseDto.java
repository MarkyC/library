package name.marcocirillo.library.search.dto;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface SearchResponseDto {
    List<BookDto> getResults();
    int getCount();
}
