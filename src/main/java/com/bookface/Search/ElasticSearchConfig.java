package com.bookface.Search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.bookface.Search.Repos")
@ComponentScan(basePackages = "com.bookface.Search")
public class ElasticSearchConfig extends ElasticsearchConfiguration {


    @Value("${elastic.username}")
    String username;

    @Value("${elastic.password}")
    String password;

    @Value("${elastic.cert}")
    String cert;

    @Override
    public ClientConfiguration clientConfiguration() {

        SSLContext context = null;
        try {

            KeyStore ks = KeyStore.getInstance("pkcs12");
            ks.load(null, null);

            FileInputStream fis = new FileInputStream(cert);
            BufferedInputStream bis = new BufferedInputStream(fis);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(bis);

            ks.setCertificateEntry("ca", cert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                 KeyManagementException e) {
            throw new RuntimeException(e);
        }

        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .usingSsl(context)
                .withBasicAuth(username, password)
                .build();

    }

//    @Bean
//    public RestClient restClient(){
//        final ClientConfiguration clientConfiguration = clientConfiguration();
//
//    }

//    @Bean
//    public RestClient getRestClient() {
//        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();
//        return restClient;
//    }
//    @Bean
//    public ElasticsearchTransport getElasticsearchTransport() {
//        return new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
//    }
//
//    @Bean
//    public ElasticsearchClient getElasticsearchClient() {
//        ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport());
//        return client;
//    }


}