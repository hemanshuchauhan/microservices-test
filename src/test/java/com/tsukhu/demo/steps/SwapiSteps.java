package com.tsukhu.demo.steps;

import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.tsukhu.demo.SpringIntegrationTest;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@Ignore
public class SwapiSteps extends SpringIntegrationTest {

    @Value("${app.swapi.path}")
    private String basePath;

    @Value("${app.swapi.uri}")
    private String baseURI;


    @Before
    public void setUp() {

        if (activeProfile != null && activeProfile.equalsIgnoreCase("dev") ) {
            wireMockPact =
                    WireMockPactGenerator
                            .builder("orderMs", "swapiMs")
                            .withRequestPathWhitelist(
                                    basePath+".*"
                            )
                            .build();
            wiremock.addMockServiceRequestListener(
                    wireMockPact
            );
            wiremock.stubFor(get(urlMatching(basePath+".*"))
                    .willReturn(
                            aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", "application/json")
                                    .withBodyFile("mocks/people.json")));
        }
        config = RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().
                setParam("http.connection.timeout",timeOut).
                setParam("http.socket.timeout",timeOut).
                setParam("http.connection-manager.timeout",timeOut));
    }

    @When("this client retrieves people by id (\\d+)")
    public void the_client_retrieves_people_by_id(int id){
        request = given().baseUri(baseURI).config(config);
        response = request.when().get(basePath+id+"/");
        System.out.println("response: " + response.prettyPrint());
    }

    @Then("the people service status code is (\\d+)")
    public void the_people_service_status_code_check(int statusCode){
        json = response.then().statusCode(statusCode);
    }


    @And("the people response body has a valid (.*) schema")
    public void response_equals(String schema){
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/"+schema+".json"));
    }

    @And("people response contains the following$")
    public void people_response_contains(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
            } else {
                json.body(field.getKey(), equalTo(field.getValue()));
            }
        }
    }

}
