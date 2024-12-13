package org.example.orderprocessing.config;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.connection.SslSettings;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${order-processing.data.url}")
    private String url;

    @Value("${order-processing.data.name-db}")
    private String nameDB;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(url);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), nameDB);
    }

    @Bean
    public SslSettings sslSettings() {
        return SslSettings.builder()
                .enabled(true)
                .build();
    }

    @Override
    protected String getDatabaseName() {
        return nameDB;
    }
}