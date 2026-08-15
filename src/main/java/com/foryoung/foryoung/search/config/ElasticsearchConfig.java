package com.foryoung.foryoung.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest5_client.Rest5ClientTransport;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {


    private static final String SCHEME = "http";
    private static final String HOST = "localhost";
    private static final int PORT = 9200;


    @Bean
    public ElasticsearchClient elasticsearchClient() {

        Rest5Client restClient = Rest5Client.builder(new HttpHost(SCHEME, HOST, PORT)).build();

        ElasticsearchTransport transport = new Rest5ClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);

    }


}