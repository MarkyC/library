package name.marcocirillo.library.search.elasticsearch.mapper;

import name.marcocirillo.library.search.dto.ImmutableBookDto;
import name.marcocirillo.library.search.dto.ImmutableSearchResponseDto;
import name.marcocirillo.library.search.dto.SearchResponseDto;
import name.marcocirillo.library.util.Numbers;
import name.marcocirillo.library.util.Objects;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SearchResponseToSearchResponseDtoMapper implements Function<SearchResponse, SearchResponseDto> {
    @Override
    public SearchResponseDto apply(SearchResponse searchResponse) {
        return ImmutableSearchResponseDto.builder()
                .addAllResults(Arrays.stream(searchResponse.getHits().getHits())
                        .map(SearchHit::getSourceAsMap)
                        .map(hit -> ImmutableBookDto.builder()
                                .authorId(UUID.fromString((String) hit.get("authorId")))
                                .authorName((String) hit.get("authorName"))
                                .id(UUID.fromString((String) hit.get("bookId")))
                                .title((String) hit.get("title"))
                                .isbn((String) hit.get("isbn"))
                                .isAvailable(Boolean.TRUE.equals(hit.get("available")))
                                .inventoryLevel(Objects.firstNonNull((Integer) hit.get("inventoryLevel"), 0))
                                .build())
                .collect(Collectors.toList()))
                .count(Numbers.longToUnsignedIntClamped(searchResponse.getHits().getTotalHits().value))
                .build();
    }
}
