package name.marcocirillo.library.search.elasticsearch;

import name.marcocirillo.library.search.SearchService;
import name.marcocirillo.library.search.dto.SearchRequestDto;
import name.marcocirillo.library.search.dto.SearchResponseDto;
import name.marcocirillo.library.search.elasticsearch.mapper.SearchRequestDtoToSearchRequestMapper;
import name.marcocirillo.library.search.elasticsearch.mapper.SearchResponseToSearchResponseDtoMapper;
import name.marcocirillo.library.search.exception.SearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static name.marcocirillo.library.search.exception.SearchException.ErrorCodes.UNKNOWN;

@Service
public class ElasticsearchSearchService implements SearchService {
    private final RestHighLevelClient client;
    private final SearchRequestDtoToSearchRequestMapper searchRequestMapper;
    private final SearchResponseToSearchResponseDtoMapper searchResponseMapper;

    @Autowired
    public ElasticsearchSearchService(
            RestHighLevelClient client,
            SearchRequestDtoToSearchRequestMapper searchRequestMapper,
            SearchResponseToSearchResponseDtoMapper searchResponseMapper
    ) {
        this.client = client;
        this.searchRequestMapper = searchRequestMapper;
        this.searchResponseMapper = searchResponseMapper;
    }

    @Override
    public SearchResponseDto search(SearchRequestDto request) {
        SearchResponse searchResponse = doSearch(searchRequestMapper.apply(request));
        return searchResponseMapper.apply(searchResponse);
    }

    private SearchResponse doSearch(SearchRequest searchRequest) {
        try {
            return client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new SearchException(UNKNOWN, String.format("Failed to search for '%s'", searchRequest.toString()), e);
        }
    }
}
