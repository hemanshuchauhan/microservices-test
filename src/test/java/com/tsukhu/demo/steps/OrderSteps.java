package com.tsukhu.demo.steps;

import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.tsukhu.demo.SpringIntegrationTest;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@Ignore
public class OrderSteps extends SpringIntegrationTest {

    @Value("${app.order.path}")
    private String basePath;

    @Value("${app.order.uri}")
    private String baseURI;

    @Value("${app.user.path}")
    private String userBasePath;

    @Value("${app.user.uri}")
    private String userBaseURI;

    @Before
    public void setUp() {
        wireMockPact =
                WireMockPactGenerator
                        .builder("orderMs", "jsonPlaceHolderMs")
                        .withRequestPathWhitelist(
                                userBasePath+".*"
                        )
                        .build();
        wiremock.addMockServiceRequestListener(
                wireMockPact
        );
        if (activeProfile != null && activeProfile.equalsIgnoreCase("dev") ) {
            wiremock.stubFor(get(urlMatching(userBasePath+".*"))
                    .willReturn(
                            aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", "application/json")
                                    .withBodyFile("mocks/user.json")));
        }
    }

    @Given("^order service request is configured$")
    public void configureService() {
        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
        config = RestAssured
                .config().httpClient(HttpClientConfig.httpClientConfig().
                        setParam("http.connection.timeout", timeOut).
                        setParam("http.socket.timeout", timeOut).
                        setParam("http.connection-manager.timeout", timeOut));
    }



    @When("this client retrieves order by sku (.*)")
    public void the_client_retrieves_order_by_sku(String skuCode){
        response = given()
                .baseUri(baseURI)
                .pathParam("skuCode",skuCode)
                .spec(request)
                .when()
                .get(basePath+"{skuCode}/");
    }

    @Then("the order service status code is (.*)")
    public void the_order_service_status_code_check(int statusCode){
        json = response.then().statusCode(statusCode);
    }

    @And("response body has a valid (.*) schema")
    public void order_response_equals(String schema){
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/"+schema+".json"));
    }

    @And("order response includes the following$")
    public void order_response_includes(Map<String,String> responseFields){
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if(StringUtils.isNumeric(field.getValue())){
                json.body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
            }
            else{
                json.body(field.getKey(), equalTo(field.getValue()));
            }
        }
    }
}
