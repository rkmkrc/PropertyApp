package org.erkam.propertylistingreviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PropertyListingReviewServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyListingReviewServiceApplication.class, args);
    }

}
