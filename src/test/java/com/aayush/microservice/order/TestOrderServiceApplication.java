package com.aayush.microservice.order;

import com.aayush.microservice.OrderServiceApplication;
import org.springframework.boot.SpringApplication;

public class TestOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(OrderServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
