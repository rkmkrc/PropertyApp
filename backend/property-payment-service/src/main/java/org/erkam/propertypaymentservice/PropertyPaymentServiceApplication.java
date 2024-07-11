package org.erkam.propertypaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PropertyPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyPaymentServiceApplication.class, args);
    }

}
