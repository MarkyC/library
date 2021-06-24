package name.marcocirillo.library.search.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
//@EnableElasticsearchRepositories(basePackages
//        = "io.pratik.elasticsearch.repositories")
//@ComponentScan(basePackages = { "io.pratik.elasticsearch" })
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
    private final String elasticsearchUrl;

    @Autowired
    public ElasticsearchConfig(
            @Value("${app.elasticsearch.url}") String elasticsearchUrl
    ) {
        this.elasticsearchUrl = elasticsearchUrl;
    }

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        return RestClients.create(ClientConfiguration.builder()
                .connectedTo(elasticsearchUrl)
                .build())
                .rest();
    }
}


