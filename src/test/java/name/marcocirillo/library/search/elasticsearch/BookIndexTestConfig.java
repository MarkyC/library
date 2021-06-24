package name.marcocirillo.library.search.elasticsearch;

import name.marcocirillo.library.seed.search.SearchIndexService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class BookIndexTestConfig {
    @Bean
    public SearchIndexService.Index bookIndex() {
        return SearchIndexService.Index.BOOK_TEST;
    }
}