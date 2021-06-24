package name.marcocirillo.library.search.elasticsearch.mapper;

import name.marcocirillo.library.search.dto.SearchRequestDto;
import name.marcocirillo.library.seed.search.SearchIndexService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
public class SearchRequestDtoToSearchRequestMapper implements Function<SearchRequestDto, SearchRequest> {
    private final SearchIndexService.Index bookIndex;
    private final SearchRequestDtoToQueryBuilderMapper queryBuilderMapper;

    @Autowired
    public SearchRequestDtoToSearchRequestMapper(
            @Qualifier("bookIndex") SearchIndexService.Index bookIndex,
            SearchRequestDtoToQueryBuilderMapper queryBuilderMapper
    ) {
        this.bookIndex = bookIndex;
        this.queryBuilderMapper = queryBuilderMapper;
    }

    @Override
    public SearchRequest apply(SearchRequestDto request) {
        return new SearchRequest(new String[]{bookIndex.getName()}, new SearchSourceBuilder()
                .from(request.getOffset())
                .size(request.getLimit())
                .query(queryBuilderMapper.apply(request)));
    }
}
