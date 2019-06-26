package com.tsukhu.demo;


import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration
// @AutoConfigureWireMock(port = 8091)
public abstract class SpringIntegrationTest {

    protected RequestSpecification request;
    protected Response response;
    protected ValidatableResponse json;
    protected RestAssuredConfig config;

    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(
            WireMockSpring
                    .options()
                    .port(8091));

    @Value("${app.endpoint.timeout}")
    protected Integer timeOut;


    @Value("${spring.profiles.active}")
    protected String activeProfile;

    protected WireMockPactGenerator wireMockPact;

}
