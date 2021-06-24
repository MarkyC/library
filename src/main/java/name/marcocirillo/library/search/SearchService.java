package name.marcocirillo.library.search;

import name.marcocirillo.library.search.dto.SearchRequestDto;
import name.marcocirillo.library.search.dto.SearchResponseDto;

public interface SearchService {
    SearchResponseDto search(SearchRequestDto request);
}
