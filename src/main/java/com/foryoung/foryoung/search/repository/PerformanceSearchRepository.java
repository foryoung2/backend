package com.foryoung.foryoung.search.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.foryoung.foryoung.global.exception.ElasticsearchOperationException;
import com.foryoung.foryoung.search.document.PerformanceDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PerformanceSearchRepository {


    private static final String INDEX_NAME = "performances";

    private final ElasticsearchClient elasticsearchClient;


    public void save(PerformanceDocument document) {

        try {

            elasticsearchClient.index(
                    index -> index
                            .index(INDEX_NAME)
                            .id(String.valueOf(document.getId()))
                            .document(document)
            );

        } catch (IOException e) {
            throw new ElasticsearchOperationException("Failed to save performance document", e);
        }

    }


    public List<PerformanceDocument> search(String keyword) {

        try {

            SearchResponse<PerformanceDocument> response =
                    elasticsearchClient.search(
                            search -> search
                                    .index(INDEX_NAME)
                                    .query(query -> query
                                            .multiMatch(multiMatch -> multiMatch
                                                    .query(keyword)
                                                    .fields("title", "artist")
                                            )
                                    ),
                            PerformanceDocument.class
                    );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (IOException e) {
            throw new ElasticsearchOperationException("Failed to search performances", e);
        }

    }


}