package de.mrfrey.photos.extractor;

import de.mrfrey.photos.extractor.metadata.MetadataExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.web.client.RestTemplate;

import static org.springframework.integration.dsl.Transformers.toJson;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableDiscoveryClient
public class MetadataExtractorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetadataExtractorApplication.class, args);
    }

    @Bean
    @Scope("prototype")
    public Logger logger(InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
    }

    @Bean
    @Primary
    public IntegrationFlow newPhoto(Processor processor, MetadataExtractor extractor, Logger logger, Jackson2ObjectMapperBuilder objectMapperBuilder) {
        Jackson2JsonObjectMapper jsonObjectMapper = new Jackson2JsonObjectMapper(objectMapperBuilder.build());
        return IntegrationFlows
                .from(processor.input())
                .enrichHeaders(e -> e.headerExpression("photo-id", "payload"))
                .wireTap(sf -> sf.handle(m -> logger.info(m.toString())))
                .handle(String.class, (payload, headers) ->
                        extractor.extractMetadata(payload)
                )
                .transform(toJson(jsonObjectMapper))
                .wireTap(sf ->
                        sf.handle(m -> logger.info(m.toString()))
                )
                .channel(processor.output())
                .get();
    }
}
