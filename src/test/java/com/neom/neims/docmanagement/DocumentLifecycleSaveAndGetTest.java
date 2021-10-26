package com.neom.neims.docmanagement;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class DocumentLifecycleSaveAndGetTest {

    public static final String DOC_TITLE = "doc title " + new Random().nextInt();
    public static final String FILE_NAME = "filename.txt";
    public static final String FILE_CONTENT = "file content";
    public static final String DIVISION = "Environmental Quality";
    public static final String TEAM = "Compliance";
    public static final Integer LOCATION = 4;
    public static final DocumentCategory DOCUMENT_CATEGORY = DocumentCategory.PERMIT;
    public static final String DOCUMENT_SUB_CATEGORY = "doc sub category";
    public static final String DOCUMENT_SUB_SUB_CATEGORY = "doc sub sub category";
    public static final Lifecycle LIFECYCLE = Lifecycle.USER_DEFINED;
    public static final Integer LIFECYCLE_USER_DEFINED_MONTHS = 23;
    public static final Boolean INTERNAL = true;
    public static final Instant DOCUMENT_CONTENT_DATE_START = Instant.parse("2008-11-03T10:15:30.00Z");
    public static final Instant DOCUMENT_CONTENT_DATE_END = Instant.parse("2007-04-03T10:15:30.00Z");
    public static final Instant DOCUMENT_PUBLISH_DATE = Instant.parse("2007-12-03T12:15:30.00Z");
    public static final Instant DOCUMENT_SURVEY_DATE = Instant.parse("2007-12-03T13:15:30.00Z");
    public static final String AUTHOR = "author";
    public static final String COMMISSIONER = "commissioner";
    public static final String TAGS = "tags";
    public static final String STATUS = "Draft";
    public static final boolean DATA_REVIEWED = true;
    public static final Integer CONFIDENTIALITY = 3;
    public static final boolean CAUTION_ADVISED = true;
    public static final String DATA_THEME = "dataTheme";
    public static final String DATA_CATEGORY = "dataCategory";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void saveTest() throws Exception {
        var document = RestAssuredMockMvc

                .given()
                .multiPart("file", FILE_NAME, "this is content body", "application/pdf")
                .param("docTitle", DOC_TITLE)
                .param("internal", String.valueOf(INTERNAL))
                .param("author", AUTHOR)
                .param("commissioner", COMMISSIONER)
                .param("tags", TAGS)
                .param("locationId", LOCATION.toString())
                .param("dataCategory", DATA_CATEGORY)
                .param("team", TEAM)
                .param("dataTheme", DATA_THEME)
                .param("lifecycle", LIFECYCLE.getValue())
                .param("lifecycleUserDefinedMonths", LIFECYCLE_USER_DEFINED_MONTHS.toString())
                .param("division", DIVISION)
                .param("documentContentDateStart", DOCUMENT_CONTENT_DATE_START.toString())
                .param("documentContentDateEnd", DOCUMENT_CONTENT_DATE_END.toString())
                .param("documentPublishDate", DOCUMENT_PUBLISH_DATE.toString())
                .param("documentSurveyDate", DOCUMENT_SURVEY_DATE.toString())
                .param("documentCategory", DOCUMENT_CATEGORY.getValue())
                .param("documentSubCategory", DOCUMENT_SUB_CATEGORY)
                .param("documentSubSubCategory", DOCUMENT_SUB_SUB_CATEGORY)
                .param("status", STATUS)
                .param("cautionAdvised", String.valueOf(CAUTION_ADVISED))
                .param("dataReviewed", String.valueOf(DATA_REVIEWED))
                .param("confidentiality", CONFIDENTIALITY.toString())

                .when()
                .post("/api/documents/v1")
                .prettyPeek()

                .then()
                .log().all()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract()
                .as(Document.class);

        validateDocument(document);

        var getDocument =
                RestAssuredMockMvc

                        .get("/api/documents/v1/" + document.getId())
                        .prettyPeek()

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract()
                        .as(Document.class);

        validateDocument(getDocument);

    }

    @Test
    void saveTest_onlyRequiredValues() {
        RestAssuredMockMvc

                .given()
                .multiPart("file", FILE_NAME, "this is content body", "application/pdf")
                .param("docTitle", DOC_TITLE)
                .param("documentCategory", DOCUMENT_CATEGORY.getValue())
                .param("dataCategory", DATA_CATEGORY)
                .param("dataTheme", DATA_THEME)

                .when()
                .post("/api/documents/v1")

                .then()
                .log().all()
                .statusCode(201)
                .contentType(ContentType.JSON);
    }

    @Test
    void saveTest_failOnMissingField() {
        RestAssuredMockMvc

                .given()
                .multiPart("file", FILE_NAME, "this is content body", "application/pdf")

                .when()
                .post("/api/documents/v1")

                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    void saveTest_failOnMissingSurveyProperties() {
        RestAssuredMockMvc

                .given()
                .multiPart("file", FILE_NAME, "this is content body", "application/pdf")
                .param("docTitle", DOC_TITLE)
                .param("documentCategory", DocumentCategory.SURVEY.getValue())

                .when()
                .post("/api/documents/v1")

                .then()
                .log().all()
                .statusCode(422);
    }

    @Test
    void saveTest_saveNonSurveyDocument() {
        RestAssuredMockMvc

                .given()
                .multiPart("file", FILE_NAME, "this is content body", "application/pdf")
                .param("docTitle", DOC_TITLE)
                .param("documentCategory", DocumentCategory.NON_SURVEY.getValue())

                .when()
                .post("/api/documents/v1")

                .then()
                .log().all()
                .statusCode(201);
    }

    private void validateDocument(Document document) {
        assertNotNull(document.getCreatedBy());
        assertNotNull(document.getCreatedTime());
        assertNotNull(document.getModifiedBy());
        assertNotNull(document.getModifiedTime());

        assertEquals(FILE_NAME, document.getFilename());
        assertEquals("1.1", document.getVersion());
        assertEquals(CONFIDENTIALITY, document.getConfidentiality());
        assertEquals(DIVISION, document.getDivision());
        assertEquals(LOCATION, document.getLocationId());
        assertEquals(DOC_TITLE, document.getDocTitle());
        assertEquals(TEAM, document.getTeam());
        assertEquals(DOCUMENT_CATEGORY, document.getDocumentCategory());
        assertEquals(DOCUMENT_SUB_CATEGORY, document.getDocumentSubCategory());
        assertEquals(DOCUMENT_SUB_SUB_CATEGORY, document.getDocumentSubSubCategory());
        assertEquals(INTERNAL, document.isInternal());
        assertEquals(ApprovalStatus.PENDING_REVIEW, document.getApprovalStatus());
        assertEquals(STATUS, document.getStatus());
        assertEquals(DATA_THEME, document.getDataTheme());
        assertEquals(DATA_CATEGORY, document.getDataCategory());
        assertEquals(LIFECYCLE, document.getLifecycle());
        assertEquals(LIFECYCLE_USER_DEFINED_MONTHS, document.getLifecycleUserDefinedMonths());
        assertEquals(DOCUMENT_CONTENT_DATE_START, document.getDocumentContentDateStart());
        assertEquals(DOCUMENT_CONTENT_DATE_END, document.getDocumentContentDateEnd());
        assertEquals(DOCUMENT_PUBLISH_DATE, document.getDocumentPublishDate());
        assertEquals(DOCUMENT_SURVEY_DATE, document.getDocumentSurveyDate());
        assertEquals(AUTHOR, document.getAuthor());
        assertEquals(COMMISSIONER, document.getCommissioner());
        assertEquals(CAUTION_ADVISED, document.isCautionAdvised());
        assertEquals(DATA_REVIEWED, document.isDataReviewed());
        assertEquals(TAGS, document.getTags());
        assertEquals("/api/documents/v1/" + document.getId() + "/content", document.getDocUrl());

        var expiryDate = DOCUMENT_CONTENT_DATE_START.atZone(ZoneId.systemDefault())
                .plusMonths(LIFECYCLE_USER_DEFINED_MONTHS).toInstant();
        assertEquals(expiryDate, document.getExpiryDate());
    }

}
