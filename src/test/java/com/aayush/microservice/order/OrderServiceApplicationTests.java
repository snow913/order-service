package com.aayush.microservice.order;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.shaded.org.hamcrest.Matchers;
import org.testcontainers.utility.DockerImageName;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"));

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mysqlContainer.start();
    }

    @Test
    void shouldSubmitOrder() {
        // this test should submit order into database and we verify that
        String submitOrderJson = """
                {
                    "skuCode": "iphone_15",
                    "price": 1000,
                    "quantity": 1
                }
                """;

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();

        assertThat(responseBodyString, is("Order Placed Successfully"));

    }

}
