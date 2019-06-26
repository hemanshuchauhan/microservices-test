package com.tsukhu.demo.steps;

import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.tsukhu.demo.SpringIntegrationTest;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@Ignore
public class GoogleBookSteps extends SpringIntegrationTest {

    @Value("${app.googlebooks.path}")
    private String basePath;

    @Value("${app.googlebooks.uri}")
    private String baseURI;

    @Before
    public void setUp() {

        // In dev mode add the pact generator listener
        if (activeProfile != null && activeProfile.equalsIgnoreCase("dev") ) {
            wireMockPact = WireMockPactGenerator
                    .builder("orderMs", "googleBooksMs")
                    .withRequestPathWhitelist(
                            basePath+".*"
                    )
                    .build();
            wiremock.addMockServiceRequestListener(
                    wireMockPact
            );
        }

    }

    @Given("^service request timeout is set$")
    public void configureServiceTimeout() {
        config = RestAssured
                .config().httpClient(HttpClientConfig.httpClientConfig().
                        setParam("http.connection.timeout", timeOut).
                        setParam("http.socket.timeout", timeOut).
                        setParam("http.connection-manager.timeout", timeOut));
    }

    @Given("a book exists with an isbn of (.*)")
    public void a_book_exists_with_isbn(String isbn) {
        request = given().baseUri(baseURI).config(config).param("q", "isbn:" + isbn);
    }

    /**
     * Add multiple http headers
     * @param parameters Map of headers to send with name and value
     */
    @Given("^the client sets headers to:$")
    public void client_sets_headers(Map<String, String> parameters) {
        request = request.given().headers(parameters);
    }

    @And("^the client has the following cookies set:$")
    public void client_has_cookies(Map<String, String> cookies) {
        request = request.given().cookies(cookies);
    }

    @When("the client retrieves the book by isbn")
    public void the_client_retrieves_the_book_by_isbn() {
        response = request.when().get(basePath); //"http://localhost:8091/books/v1/volumes/");
        System.out.println("response: " + response.prettyPrint());
    }

    @Then("the book service status code is (\\d+)")
    public void the_book_service_code_check(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("book response includes the following$")
    public void book_response_equals(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
            } else {
                json.body(field.getKey(), equalTo(field.getValue()));
            }
        }
    }

    @And("book response includes the following in any order")
    public void book_response_contains_in_any_order(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), containsInAnyOrder(Integer.parseInt(field.getValue())));
            } else {
                json.body(field.getKey(), containsInAnyOrder(field.getValue()));
            }
        }
    }
}
