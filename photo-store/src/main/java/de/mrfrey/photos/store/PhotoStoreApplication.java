package de.mrfrey.photos.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import java.util.Map;

import static org.springframework.integration.dsl.support.Transformers.fromJson;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableDiscoveryClient
public class PhotoStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhotoStoreApplication.class, args);
    }

    @Bean
    public Sampler defaultTraceSampler() {
        return new AlwaysSampler();
    }

//    @Bean
//    public IntegrationFlow addMetadata(Processor processor, PhotoStorageService photoStorageService) {
//        return IntegrationFlows
//                .from(processor.input())
////                .transform(fromJson(Metadata.class))
//                .handle(Map.class, (payload, headers) -> {
//                    photoStorageService.addMetadata((String) headers.get("photo-id"), payload);
//                    return null;
//                }).get();
//    }
}
