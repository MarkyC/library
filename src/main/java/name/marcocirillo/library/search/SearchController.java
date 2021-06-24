package name.marcocirillo.library.search;

import name.marcocirillo.library.search.dto.SearchRequestDto;
import name.marcocirillo.library.search.dto.SearchResponseDto;
import name.marcocirillo.library.search.mapper.SearchRequestMapper;
import name.marcocirillo.library.search.mapper.SearchResponseMapper;
import name.marcocirillo.library.search.model.SearchApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class SearchController {
    private final SearchService searchService;
    private final SearchRequestMapper searchRequestMapper;
    private final SearchResponseMapper searchResponseMapper;

    public SearchController(
            SearchService searchService,
            SearchRequestMapper searchRequestMapper,
            SearchResponseMapper searchResponseMapper
    ) {
        this.searchService = searchService;
        this.searchRequestMapper = searchRequestMapper;
        this.searchResponseMapper = searchResponseMapper;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<SearchApiResponse> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) UUID authorId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset
    ) {
        SearchRequestDto searchRequestDto = searchRequestMapper.apply(title, authorName, isbn, authorId, categoryId, limit, offset);
        SearchResponseDto searchResponseDto = searchService.search(searchRequestDto);
        SearchApiResponse response = searchResponseMapper.apply(searchResponseDto);
        return ResponseEntity.ok(response);
    }
}
