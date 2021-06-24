package name.marcocirillo.library.seed;

import name.marcocirillo.library.seed.db.DatabaseSeedService;
import name.marcocirillo.library.seed.search.SearchIndexService;
import name.marcocirillo.library.seed.search.SearchSeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.sql.SQLException;


@Service
public class SeedService {
    private final DatabaseSeedService dbSeedService;
    private final SearchIndexService.Index bookIndex;
    private final SearchSeedService searchSeedService;

    @Autowired
    public SeedService(
            DatabaseSeedService dbSeedService,
            @Qualifier("bookIndex") SearchIndexService.Index bookIndex,
            SearchSeedService searchSeedService
    ) {
        this.dbSeedService = dbSeedService;
        this.bookIndex = bookIndex;
        this.searchSeedService = searchSeedService;
    }

    public void seed(Path sqlFile) throws SQLException {
        dbSeedService.seed(sqlFile);
    }

    public void seedSearch() {
        searchSeedService.seedBookSearch(bookIndex);
    }
}
