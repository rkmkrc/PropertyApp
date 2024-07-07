package org.erkam.propertyregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class PropertyRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyRegistryApplication.class, args);
    }

}
