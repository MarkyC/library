package name.marcocirillo.library.seed.search.elasticsearch;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Just a simply BulkProcessor.Listener that only provides logging.
 * Taken from the Elasticsearch examples:
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-document-bulk.html#java-rest-high-document-bulk-processor
 */
public class LoggingBulkListener implements BulkProcessor.Listener {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingBulkListener.class.getName());
    @Override
    public void beforeBulk(long executionId, BulkRequest request) {
        int numberOfActions = request.numberOfActions();
        LOG.debug("Executing bulk [{}] with {} requests", executionId, numberOfActions);
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request,
                          BulkResponse response) {
        if (response.hasFailures()) {
            LOG.warn("Bulk [{}] executed with failures", executionId);
        } else {
            LOG.debug("Bulk [{}] completed in {} milliseconds",
                    executionId, response.getTook().getMillis());
        }
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
        LOG.error("Failed to execute bulk", failure);
    }
}
