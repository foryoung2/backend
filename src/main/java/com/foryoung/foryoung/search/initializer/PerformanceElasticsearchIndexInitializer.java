package com.foryoung.foryoung.search.initializer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import com.foryoung.foryoung.global.exception.ElasticsearchInitializationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchIndexInitializer {


    private static final String INDEX_NAME = "performances";

    private final ElasticsearchClient elasticsearchClient;


    @PostConstruct
    public void initialize() {

        try {

            boolean exists =
                    elasticsearchClient.indices()
                            .exists(
                                    ExistsRequest.of(request ->
                                            request.index(INDEX_NAME)
                                    )
                            )
                            .value();

            if (exists) {

                log.info("Elasticsearch index '{}' already exists.", INDEX_NAME);

                return;
            }

            log.info("Elasticsearch index '{}' does not exist. Creating...", INDEX_NAME);

            createIndex();

            log.info("Elasticsearch index '{}' created successfully.", INDEX_NAME);

        } catch (Exception e) {

            log.error(
                    "Elasticsearch index initialization failed. index={}",
                    INDEX_NAME,
                    e
            );

            throw new ElasticsearchInitializationException(
                    "Failed to initialize Elasticsearch index: " + INDEX_NAME,
                    e
            );
        }
    }


    private void createIndex() {

        try {

            elasticsearchClient.indices()
                    .create(create -> create
                            .index(INDEX_NAME)
                            .settings(this::configureSettings)
                            .mappings(this::configureMappings)
                    );

        } catch (Exception e) {

            log.error(
                    "Failed to create Elasticsearch index. index={}",
                    INDEX_NAME,
                    e
            );

            throw new ElasticsearchInitializationException("Failed to create Elasticsearch index", e);
        }

    }


    private IndexSettings.Builder configureSettings(IndexSettings.Builder settings) {

        return settings

                .index(index -> index
                        .maxNgramDiff(18)
                )

                .analysis(analysis -> analysis

                        .charFilter(
                                "remove_spaces",
                                charFilter -> charFilter
                                        .definition(
                                                definition -> definition
                                                        .patternReplace(
                                                                patternReplace -> patternReplace
                                                                        .pattern("\\s+")
                                                                        .replacement("")
                                                        )
                                        )
                        )

                        .filter(
                                "performance_ngram",
                                filter -> filter
                                        .definition(
                                                definition -> definition
                                                        .ngram(
                                                                ngram -> ngram
                                                                        .minGram(2)
                                                                        .maxGram(20)
                                                        )
                                        )
                        )

                        .analyzer(
                                "performance_index_analyzer",
                                analyzer -> analyzer
                                        .custom(custom -> custom
                                                .charFilter("remove_spaces")
                                                .tokenizer("standard")
                                                .filter("lowercase", "performance_ngram")
                                        )
                        )

                        .analyzer(
                                "performance_search_analyzer",
                                analyzer -> analyzer
                                        .custom(custom -> custom
                                                .charFilter("remove_spaces")
                                                .tokenizer("standard")
                                                .filter("lowercase")
                                        )
                        )
                );

    }


    private TypeMapping.Builder configureMappings(TypeMapping.Builder mappings) {

        return mappings

                .properties(
                        "id",
                        property -> property
                                .long_(longType -> longType)
                )

                .properties(
                        "title",
                        property -> property
                                .text(text -> text
                                        .analyzer("performance_index_analyzer")
                                        .searchAnalyzer("performance_search_analyzer")
                                )
                )

                .properties(
                        "artist",
                        property -> property
                                .text(text -> text
                                        .analyzer("performance_index_analyzer")
                                        .searchAnalyzer("performance_search_analyzer")
                                )
                )

                .properties(
                        "posterImageUrl",
                        property -> property
                                .keyword(keyword -> keyword)
                );

    }


}