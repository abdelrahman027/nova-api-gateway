package com.example.novaApiGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NovaApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(NovaApiGatewayApplication.class, args);
	}

}
