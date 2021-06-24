package name.marcocirillo.library.search.mapper;

import name.marcocirillo.library.search.dto.SearchResponseDto;
import name.marcocirillo.library.search.model.ImmutableBookResponse;
import name.marcocirillo.library.search.model.ImmutableSearchApiResponse;
import name.marcocirillo.library.search.model.SearchApiResponse;
import name.marcocirillo.library.util.Objects;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SearchResponseMapper implements Function<SearchResponseDto, SearchApiResponse> {
    @Override
    public SearchApiResponse apply(SearchResponseDto searchResponseDto) {
        return ImmutableSearchApiResponse.builder()
                .results(searchResponseDto.getResults().stream()
                        .map(book -> ImmutableBookResponse.builder()
                                .id(book.getId())
                                .authorName(book.getAuthorName())
                                .title(book.getTitle())
                                .isbn(book.getIsbn())
                                .inventoryLevel(Objects.firstNonNull(book.getInventoryLevel(), 0))
                                .build())
                        .collect(Collectors.toList()))
                .count(searchResponseDto.getCount())
                .build();
    }
}
