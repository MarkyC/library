package name.marcocirillo.library.seed.search.elasticsearch;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BulkProcessorFactory {
    private final BulkProcessor.Listener bulkListener;
    private final RestHighLevelClient client;

    @Autowired
    public BulkProcessorFactory(
            @Qualifier("bulkListener") BulkProcessor.Listener bulkListener,
            RestHighLevelClient client
    ) {
        this.bulkListener = bulkListener;
        this.client = client;
    }

    public BulkProcessor create() {
        return BulkProcessor.builder(
                (bulkRequest, bulkListener) -> client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, bulkListener),
                bulkListener
        ).build();
    }
}
