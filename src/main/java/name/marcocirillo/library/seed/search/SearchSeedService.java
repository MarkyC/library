package name.marcocirillo.library.seed.search;

import name.marcocirillo.library.seed.search.db.SearchBook;

import java.util.stream.Stream;

public interface SearchSeedService {
    void seedBookSearch(SearchIndexService.Index index);
    void seedBookSearch(SearchIndexService.Index index, Stream<SearchBook> books);
}
