package name.marcocirillo.library.seed.search.elasticsearch.config;

import name.marcocirillo.library.seed.search.elasticsearch.LoggingBulkListener;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class BulkListenerConfig {
    @Bean(name="bulkListener")
    @Profile({"dev", "test"})
    public BulkProcessor.Listener devBulkListener() {
        return new LoggingBulkListener();
    }
}
