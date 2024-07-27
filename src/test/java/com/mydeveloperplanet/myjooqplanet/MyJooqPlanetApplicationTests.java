package com.mydeveloperplanet.myjooqplanet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyJooqPlanetApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer =  new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"));

	@Test
	void whenCreateCustomer_thenReturnSuccess() {
		String body = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "country": "Belgium"
                }
                """;

        webTestClient
				.post()
				.uri("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

	}

	@Test
	void givenCustomer_whenRetrieveAllCustomers_thenReturnSuccess() {
		String body = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "country": "Belgium"
                }
                """;

        webTestClient
				.post()
				.uri("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

        webTestClient
				.get()
				.uri("/customers")
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$[0].customerId").isEqualTo(1)
				.jsonPath("$[0].firstName").isEqualTo("John")
				.jsonPath("$[0].lastName").isEqualTo("Doe")
				.jsonPath("$[0].country").isEqualTo("Belgium");

	}

}
