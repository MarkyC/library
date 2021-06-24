package name.marcocirillo.library.seed.search.elasticsearch;

import name.marcocirillo.library.seed.search.SearchIndexService;
import name.marcocirillo.library.seed.search.exception.SearchSeedException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Map;

import static name.marcocirillo.library.seed.search.SearchIndexService.Index.BOOK;
import static name.marcocirillo.library.seed.search.SearchIndexService.Index.BOOK_TEST;
import static name.marcocirillo.library.seed.search.exception.SearchSeedException.ErrorCodes.CREATE_SEARCH_INDEX_FAILED;

/**
 * Handles creating the schema for the Elasticsearch books index
 */
@Service
public class ElasticSearchIndexService implements SearchIndexService {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchIndexService.class.getName());

    private final RestHighLevelClient client;

    private final Map<Index, Resource> indexToResource;

    @Autowired
    public ElasticSearchIndexService(
            RestHighLevelClient client,
            @Value("classpath:search/book.schema.json") Resource bookSchema,
            @Qualifier("bookIndex") Index bookIndex
    ) {
        this.client = client;

        indexToResource = Map.of(
                BOOK, bookSchema,
                BOOK_TEST, bookSchema
        );
    }

    public void createIndexIfNotPresent(Index index) {
        try {
            createIndexIfNotPresent(index, Files.readString(indexToResource.get(index).getFile().toPath()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void createIndexIfNotPresent(Index index, String schema) {
        try {
            if (!indexExists(index)) {
                createIndex(index, schema);
            }
        } catch (IOException e) {
            throw new SearchSeedException(CREATE_SEARCH_INDEX_FAILED, String.format("Can not create index '%s'", index.getName()), e);
        }
    }

    private void createIndex(Index index, String schema) throws IOException {
        CreateIndexRequest indexRequest = new CreateIndexRequest(index.getName());
        indexRequest.mapping(schema, XContentType.JSON);
        CreateIndexResponse createIndexResponse = client.indices().create(indexRequest, RequestOptions.DEFAULT);
        if (!createIndexResponse.isAcknowledged()) {
            throw new SearchSeedException(CREATE_SEARCH_INDEX_FAILED, String.format("Can not create index '%s'", index.getName()));
        }
    }

    private boolean indexExists(Index index) throws IOException {
        return client.indices().exists(new GetIndexRequest(index.getName()), RequestOptions.DEFAULT);
    }

    @Override
    public void deleteIndex(Index index) {
        try {
            if (indexExists(index)) {
                client.indices().delete(new DeleteIndexRequest(index.getName()), RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void refreshIndex(Index index) {
        try {
            client.indices().refresh(new RefreshRequest(index.getName()), RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
