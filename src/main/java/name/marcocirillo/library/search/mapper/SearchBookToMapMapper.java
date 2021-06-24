package name.marcocirillo.library.search.mapper;

import name.marcocirillo.library.seed.search.db.SearchBook;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SearchBookToMapMapper implements Function<SearchBook, Map<String, ?>> {
    @Override
    public Map<String, ?> apply(SearchBook searchBook) {
        return Map.ofEntries(
                Map.entry("authorId", searchBook.getAuthorId().toString()),
                Map.entry("authorName", searchBook.getAuthorName()),
                Map.entry("bookId", searchBook.getId().toString()),
                Map.entry("title", searchBook.getTitle()),
                Map.entry("isbn", searchBook.getIsbn()),
                Map.entry("categoryId", searchBook.getCategoryIds().stream().map(UUID::toString).collect(Collectors.toList())),
                Map.entry("available", searchBook.getAvailable()),
                Map.entry("inventoryLevel", searchBook.getInventoryLevel())
        );
    }
}
