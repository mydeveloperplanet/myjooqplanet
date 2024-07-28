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
                  "street": "John Doe Street",
                  "city": "Brussels",
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
                  "street": "John Doe Street",
                  "city": "Brussels",
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
				.jsonPath("$[0].street").isEqualTo("John Doe Street")
				.jsonPath("$[0].city").isEqualTo("Brussels")
				.jsonPath("$[0].country").isEqualTo("Belgium");

	}

	@Test
	void whenCreateArticle_thenReturnSuccess() {
		String body = """
                {
                  "name": "article1"
                }
                """;

		webTestClient
				.post()
				.uri("/articles")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

	}

	@Test
	void whenCreateOrder_thenReturnSuccess() {

		givenCustomer();
		givenArticles();

		String body = """
                {
                  "customerId": 1,
                  "articles": [
                  	{
                  		"articleId": 1,
                  		"number": 10
                  	},
                  	{
                  		"articleId": 2,
                  		"number": 20
                  	}
                  ]
                }
                """;

		webTestClient
				.post()
				.uri("/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

	}

	@Test
	void givenOrder_whenRetrieveOrder_thenReturnSuccess() {

		givenCustomer();
		givenArticles();

		String body = """
                {
                  "customerId": 1,
                  "articles": [
                  	{
                  		"articleId": 1,
                  		"number": 10
                  	},
                  	{
                  		"articleId": 2,
                  		"number": 20
                  	}
                  ]
                }
                """;

		webTestClient
				.post()
				.uri("/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

		webTestClient
				.get()
				.uri("/orders/1")
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("customerId").isEqualTo(1)
				.jsonPath("orderId").isEqualTo(1)
				.jsonPath("articles[0].articleId").isEqualTo(1)
				.jsonPath("articles[0].number").isEqualTo(10)
				.jsonPath("articles[1].articleId").isEqualTo(2)
				.jsonPath("articles[1].number").isEqualTo(20);

	}

	@Test
	void whenRetrieveOrdersOfCustomer_thenReturnSuccess() {

		givenCustomer();
		givenArticles();

		String body = """
                {
                  "customerId": 1,
                  "articles": [
                  	{
                  		"articleId": 1,
                  		"number": 10
                  	},
                  	{
                  		"articleId": 2,
                  		"number": 20
                  	}
                  ]
                }
                """;

		webTestClient
				.post()
				.uri("/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

		webTestClient
				.post()
				.uri("/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

		webTestClient
				.get()
				.uri("/orders/customers/1")
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$[0]").isEqualTo(1)
				.jsonPath("$[1]").isEqualTo(2);

	}

	private void givenCustomer() {
		String body = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "street": "John Doe Street",
                  "city": "Brussels",
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

	private void givenArticles() {
		String body = """
                {
                  "name": "article1"
                }
                """;

		webTestClient
				.post()
				.uri("/articles")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

		body = """
                {
                  "name": "article2"
                }
                """;

		webTestClient
				.post()
				.uri("/articles")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();
	}

}
