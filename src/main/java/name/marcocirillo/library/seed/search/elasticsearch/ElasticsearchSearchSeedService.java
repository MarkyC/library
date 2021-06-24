package name.marcocirillo.library.seed.search.elasticsearch;

import name.marcocirillo.library.search.mapper.SearchBookToMapMapper;
import name.marcocirillo.library.seed.search.SearchIndexService;
import name.marcocirillo.library.seed.search.SearchSeedService;
import name.marcocirillo.library.seed.search.db.SearchBook;
import name.marcocirillo.library.seed.search.db.SearchBookRespository;
import name.marcocirillo.library.seed.search.exception.SearchSeedException;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static name.marcocirillo.library.seed.search.exception.SearchSeedException.ErrorCodes.SEARCH_SEED_INTERRUPTED;
import static name.marcocirillo.library.seed.search.exception.SearchSeedException.ErrorCodes.SEARCH_SEED_TIMEOUT;

@Service
public class ElasticsearchSearchSeedService implements SearchSeedService {
    private final BulkProcessorFactory bulkProcessorFactory;
    private final ElasticSearchIndexService indexService;
    private final SearchBookToMapMapper searchBookToMapMapper;
    private final SearchBookRespository searchBookRespository;

    @Autowired
    public ElasticsearchSearchSeedService(
            BulkProcessorFactory bulkProcessorFactory,
            ElasticSearchIndexService indexService,
            SearchBookToMapMapper searchBookToMapMapper,
            SearchBookRespository searchBookRespository
    ) {
        this.bulkProcessorFactory = bulkProcessorFactory;
        this.indexService = indexService;
        this.searchBookToMapMapper = searchBookToMapMapper;
        this.searchBookRespository = searchBookRespository;
    }

    @Override
    @Transactional(readOnly = true)
    public void seedBookSearch(SearchIndexService.Index index) {
       seedBookSearch(index, searchBookRespository.getAll());
    }

    @Override
    public void seedBookSearch(SearchIndexService.Index index, Stream<SearchBook> books) {
        BulkProcessor bulkProcessor = bulkProcessorFactory.create();

        indexService.createIndexIfNotPresent(index);

        books
                .forEach(searchBook -> bulkProcessor.add(new IndexRequest(index.getName())
                        .id(searchBook.getId().toString())
                        .source(searchBookToMapMapper.apply(searchBook))));

        try {
            boolean success = bulkProcessor.awaitClose(1, TimeUnit.HOURS);
            if (!success) {
                throw new SearchSeedException(SEARCH_SEED_TIMEOUT);
            }
        } catch (InterruptedException e) {
            throw new SearchSeedException(SEARCH_SEED_INTERRUPTED, e);
        }
    }
}
