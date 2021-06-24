package name.marcocirillo.library.search.mapper;

import name.marcocirillo.library.search.dto.ImmutableSearchRequestDto;
import name.marcocirillo.library.search.dto.SearchRequestDto;
import name.marcocirillo.library.util.Objects;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SearchRequestMapper {
    public SearchRequestDto apply(
            String title,
            String authorName,
            String isbn,
            UUID authorId,
            UUID categoryId,
            Integer limit,
            Integer offset
    ) {
        return ImmutableSearchRequestDto.builder()
                .author(Objects.firstNonNull(authorName, ""))
                .title(Objects.firstNonNull(title, ""))
                .isbn(Objects.firstNonNull(isbn, ""))
                .authorId(Optional.ofNullable(authorId))
                .categoryId(Optional.ofNullable(categoryId))
                .limit(Objects.firstNonNull(limit, 10))
                .offset(Objects.firstNonNull(offset, 0))
                .build();
    }
}
