package name.marcocirillo.library.search.elasticsearch.mapper;

import name.marcocirillo.library.search.dto.SearchRequestDto;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class SearchRequestDtoToQueryBuilderMapper implements Function<SearchRequestDto, QueryBuilder> {
    @Override
    public QueryBuilder apply(SearchRequestDto request) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("available", true))
                .must(QueryBuilders.rangeQuery("inventoryLevel").gt(0));

        if (StringUtils.isNotBlank(request.getAuthor())) {
            queryBuilder.must(partialOrFuzzy("authorName", request.getAuthor(), 3));
        }

        if (StringUtils.isNotBlank(request.getTitle())) {
            queryBuilder.must(partialOrFuzzy("title", request.getTitle(), 3));
        }

        if (StringUtils.isNotBlank(request.getIsbn())) {
            queryBuilder.must(QueryBuilders.matchQuery("isbn", request.getIsbn()));
        }

        request.getAuthorId().ifPresent(id ->
                queryBuilder.must(QueryBuilders.matchQuery("authorId", id.toString())));

        request.getCategoryId().ifPresent(id ->
                queryBuilder.filter(QueryBuilders.termsQuery("categoryId", List.of(id.toString()))));

        return queryBuilder;
    }

    /**
     * Attempts to match both
     *
     * 1. incomplete (partial) searches: West -> Westworld
     * 2. typos (using fuzziness): Wextwxrld -> Westworld
     *
     * @param name  the field name to search
     * @param text  the query
     * @param fuzziness Levenshtein distance between text and a potential result
     */
    private QueryBuilder partialOrFuzzy(String name, Object text, int fuzziness) {
        return QueryBuilders.boolQuery()
                .should(QueryBuilders.queryStringQuery("*" + text + "*").field(name))
                .should(QueryBuilders.matchQuery(name, text).fuzziness(fuzziness));
    }
}
