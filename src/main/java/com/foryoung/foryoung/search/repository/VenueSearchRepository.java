package com.foryoung.foryoung.search.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.foryoung.foryoung.global.exception.ElasticsearchOperationException;
import com.foryoung.foryoung.search.document.VenueDocument;
import com.foryoung.foryoung.search.dto.VenueSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class VenueSearchRepository {


    private static final String INDEX_NAME = "venues";

    private final ElasticsearchClient elasticsearchClient;


    public void save(VenueDocument document) {

        try {

            elasticsearchClient.index(
                    index -> index
                            .index(INDEX_NAME)
                            .id(String.valueOf(document.getId()))
                            .document(document)
            );

        } catch (IOException e) {
            throw new ElasticsearchOperationException("Failed to save venue document", e);
        }

    }


    public VenueSearchResult search(String keyword,
                                    int page,
                                    int size) {

        try {

            SearchResponse<VenueDocument> response =
                    elasticsearchClient.search(

                            search -> search
                                    .index(INDEX_NAME)
                                    .from(page * size)
                                    .size(size)
                                    .query(query -> query
                                            .match(match -> match
                                                    .field("name")
                                                    .query(keyword)
                                            )
                                    ),

                            VenueDocument.class
                    );

            List<VenueDocument> documents =
                    response.hits()
                            .hits()
                            .stream()
                            .map(Hit::source)
                            .filter(Objects::nonNull)
                            .toList();

            long totalElements =
                    response.hits()
                            .total()
                            .value();

            return new VenueSearchResult(
                    documents,
                    totalElements
            );

        } catch (IOException e) {
            throw new ElasticsearchOperationException("Failed to search venues", e);
        }

    }


}