package com.neom.neims.docmanagement;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class DocumentGetTest {

    private static final String ID_OF_NON_EXISTENT_DOC = "cf81362a-7cdc-47ad-af8c-ef983729a1a7";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void saveTest_failOnResourceNotFound() throws Exception {

        RestAssuredMockMvc.given()

                .when()
                .get("/api/documents/v1/" + ID_OF_NON_EXISTENT_DOC)

                .then()
                .log().all()
                .statusCode(NOT_FOUND.value());
    }

}
