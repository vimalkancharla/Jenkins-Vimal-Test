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
public class DocumentUpdateTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }


    static class V1 {
        public static final String DOC_TITLE = "doc title " + new Random().nextInt();
        public static final String FILE_NAME = "filename.txt";
        public static final String FILE_CONTENT = "file content";
        public static final String DIVISION = "Environmental Quality";
        public static final String TEAM = "Compliance";
        public static final Integer LOCATION = 4;
        public static final DocumentCategory DOCUMENT_CATEGORY = DocumentCategory.PERMIT;
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
    }

    static class V2 {
        public static final String DOC_TITLE = V1.DOC_TITLE + " update";
        public static final String FILE_NAME = "filename-update.txt";
        public static final String FILE_CONTENT = "file content update";
        public static final String DIVISION = "Environmental Quality Update";
        public static final String TEAM = "Compliance Update";
        public static final Integer LOCATION = 5;
        public static final DocumentCategory DOCUMENT_CATEGORY = DocumentCategory.ESIA;
        public static final Lifecycle LIFECYCLE = Lifecycle.USER_DEFINED;
        public static final Integer LIFECYCLE_USER_DEFINED_MONTHS = 26;
        public static final Boolean INTERNAL = false;
        public static final Instant DOCUMENT_CONTENT_DATE_START = Instant.parse("2005-11-03T10:15:30.00Z");
        public static final Instant DOCUMENT_CONTENT_DATE_END = Instant.parse("2004-04-03T10:15:30.00Z");
        public static final Instant DOCUMENT_PUBLISH_DATE = Instant.parse("2003-12-03T12:15:30.00Z");
        public static final Instant DOCUMENT_SURVEY_DATE = Instant.parse("2002-12-03T13:15:30.00Z");
        public static final String AUTHOR = "author Update";
        public static final String COMMISSIONER = "commissioner Update";
        public static final String TAGS = "tags Update";
        public static final String STATUS = "Draft Update";
        public static final boolean DATA_REVIEWED = false;
        public static final Integer CONFIDENTIALITY = 4;
        public static final boolean CAUTION_ADVISED = false;
        public static final String DATA_THEME = "dataThemeUpdate";
        public static final String DATA_CATEGORY = "dataCategoryUpdate";
    }

    static class V3 {
        public static final String AUTHOR = "author Update v3";
    }

    @Test
    void updateTest() throws Exception {
        var document = RestAssuredMockMvc

                .given()
                .multiPart("file", V1.FILE_NAME, V1.FILE_CONTENT, "application/pdf")
                .param("docTitle", V1.DOC_TITLE)
                .param("internal", String.valueOf(V1.INTERNAL))
                .param("author", V1.AUTHOR)
                .param("commissioner", V1.COMMISSIONER)
                .param("tags", V1.TAGS)
                .param("locationId", V1.LOCATION.toString())
                .param("dataCategory", V1.DATA_CATEGORY)
                .param("team", V1.TEAM)
                .param("dataTheme", V1.DATA_THEME)
                .param("lifecycle", V1.LIFECYCLE.getValue())
                .param("lifecycleUserDefinedMonths", V1.LIFECYCLE_USER_DEFINED_MONTHS.toString())
                .param("division", V1.DIVISION)
                .param("documentContentDateStart", V1.DOCUMENT_CONTENT_DATE_START.toString())
                .param("documentContentDateEnd", V1.DOCUMENT_CONTENT_DATE_END.toString())
                .param("documentPublishDate", V1.DOCUMENT_PUBLISH_DATE.toString())
                .param("documentSurveyDate", V1.DOCUMENT_SURVEY_DATE.toString())
                .param("documentCategory", V1.DOCUMENT_CATEGORY.getValue())
                .param("status", V1.STATUS)
                .param("cautionAdvised", String.valueOf(V1.CAUTION_ADVISED))
                .param("dataReviewed", String.valueOf(V1.DATA_REVIEWED))
                .param("confidentiality", V1.CONFIDENTIALITY.toString())
                .when()
                .post("/api/documents/v1")
                .prettyPeek()

                .then()
                .log().all()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract()
                .as(Document.class);


        assertNotNull(document.getCreatedBy());
        assertNotNull(document.getCreatedTime());
        assertNotNull(document.getModifiedBy());
        assertNotNull(document.getModifiedTime());

        assertEquals(V1.FILE_NAME, document.getFilename());
        assertEquals("1.1", document.getVersion());
        assertEquals(V1.CONFIDENTIALITY, document.getConfidentiality());
        assertEquals(V1.DIVISION, document.getDivision());
        assertEquals(V1.LOCATION, document.getLocationId());
        assertEquals(V1.DOC_TITLE, document.getDocTitle());
        assertEquals(V1.TEAM, document.getTeam());
        assertEquals(V1.DOCUMENT_CATEGORY, document.getDocumentCategory());
        assertEquals(V1.INTERNAL, document.isInternal());
        assertEquals(ApprovalStatus.PENDING_REVIEW, document.getApprovalStatus());
        assertEquals(V1.STATUS, document.getStatus());
        assertEquals(V1.DATA_THEME, document.getDataTheme());
        assertEquals(V1.DATA_CATEGORY, document.getDataCategory());
        assertEquals(V1.LIFECYCLE, document.getLifecycle());
        assertEquals(V1.LIFECYCLE_USER_DEFINED_MONTHS, document.getLifecycleUserDefinedMonths());
        assertEquals(V1.DOCUMENT_CONTENT_DATE_START, document.getDocumentContentDateStart());
        assertEquals(V1.DOCUMENT_CONTENT_DATE_END, document.getDocumentContentDateEnd());
        assertEquals(V1.DOCUMENT_PUBLISH_DATE, document.getDocumentPublishDate());
        assertEquals(V1.DOCUMENT_SURVEY_DATE, document.getDocumentSurveyDate());
        assertEquals(V1.AUTHOR, document.getAuthor());
        assertEquals(V1.COMMISSIONER, document.getCommissioner());
        assertEquals(V1.CAUTION_ADVISED, document.isCautionAdvised());
        assertEquals(V1.DATA_REVIEWED, document.isDataReviewed());
        assertEquals(V1.TAGS, document.getTags());
        var expiryDateV1 = V1.DOCUMENT_CONTENT_DATE_START.atZone(ZoneId.systemDefault())
                .plusMonths(V1.LIFECYCLE_USER_DEFINED_MONTHS).toInstant();
        assertEquals(expiryDateV1, document.getExpiryDate());


        // when doc is fully updated 
        var updatedDocument = RestAssuredMockMvc

                .given()
                .multiPart("file", V2.FILE_NAME, V2.FILE_CONTENT, "application/pdf")
                .param("docTitle", V2.DOC_TITLE)
                .param("internal", String.valueOf(V2.INTERNAL))
                .param("author", V2.AUTHOR)
                .param("commissioner", V2.COMMISSIONER)
                .param("tags", V2.TAGS)
                .param("locationId", V2.LOCATION.toString())
                .param("dataCategory", V2.DATA_CATEGORY)
                .param("team", V2.TEAM)
                .param("dataTheme", V2.DATA_THEME)
                .param("lifecycle", V2.LIFECYCLE.getValue())
                .param("lifecycleUserDefinedMonths", V2.LIFECYCLE_USER_DEFINED_MONTHS.toString())
                .param("division", V2.DIVISION)
                .param("documentContentDateStart", V2.DOCUMENT_CONTENT_DATE_START.toString())
                .param("documentContentDateEnd", V2.DOCUMENT_CONTENT_DATE_END.toString())
                .param("documentPublishDate", V2.DOCUMENT_PUBLISH_DATE.toString())
                .param("documentSurveyDate", V2.DOCUMENT_SURVEY_DATE.toString())
                .param("documentCategory", V2.DOCUMENT_CATEGORY.getValue())
                .param("status", V2.STATUS)
                .param("cautionAdvised", String.valueOf(V2.CAUTION_ADVISED))
                .param("dataReviewed", String.valueOf(V2.DATA_REVIEWED))
                .param("confidentiality", V2.CONFIDENTIALITY.toString())
                //workaround: restassured does not support PUT for files
                .postProcessors(request -> {
                    request.setMethod("PUT");
                    return request;
                })

                .when()
                .post("/api/documents/v1/" + document.getId())
                .prettyPeek()

                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(Document.class);


        //then all updated fields get saved in alfresco
        assertNotNull(updatedDocument.getCreatedBy());
        assertNotNull(updatedDocument.getCreatedTime());
        assertNotNull(updatedDocument.getModifiedBy());
        assertNotNull(updatedDocument.getModifiedTime());

        assertEquals(document.getCreatedTime(), updatedDocument.getCreatedTime());

        assertEquals(V2.FILE_NAME, updatedDocument.getFilename());
        assertEquals("1.2", updatedDocument.getVersion());
        assertEquals(V2.CONFIDENTIALITY, updatedDocument.getConfidentiality());
        assertEquals(V2.DIVISION, updatedDocument.getDivision());
        assertEquals(V2.LOCATION, updatedDocument.getLocationId());
        assertEquals(V2.DOC_TITLE, updatedDocument.getDocTitle());
        assertEquals(V2.TEAM, updatedDocument.getTeam());
        assertEquals(V2.DOCUMENT_CATEGORY, updatedDocument.getDocumentCategory());
        assertEquals(V2.INTERNAL, updatedDocument.isInternal());
        assertEquals(ApprovalStatus.PENDING_REVIEW, updatedDocument.getApprovalStatus());
        assertEquals(V2.STATUS, updatedDocument.getStatus());
        assertEquals(V2.DATA_THEME, updatedDocument.getDataTheme());
        assertEquals(V2.DATA_CATEGORY, updatedDocument.getDataCategory());
        assertEquals(V2.LIFECYCLE, updatedDocument.getLifecycle());
        assertEquals(V2.LIFECYCLE_USER_DEFINED_MONTHS, updatedDocument.getLifecycleUserDefinedMonths());
        assertEquals(V2.DOCUMENT_CONTENT_DATE_START, updatedDocument.getDocumentContentDateStart());
        assertEquals(V2.DOCUMENT_CONTENT_DATE_END, updatedDocument.getDocumentContentDateEnd());
        assertEquals(V2.DOCUMENT_PUBLISH_DATE, updatedDocument.getDocumentPublishDate());
        assertEquals(V2.DOCUMENT_SURVEY_DATE, updatedDocument.getDocumentSurveyDate());
        assertEquals(V2.AUTHOR, updatedDocument.getAuthor());
        assertEquals(V2.COMMISSIONER, updatedDocument.getCommissioner());
        assertEquals(V2.CAUTION_ADVISED, updatedDocument.isCautionAdvised());
        assertEquals(V2.DATA_REVIEWED, updatedDocument.isDataReviewed());
        assertEquals(V2.TAGS, updatedDocument.getTags());
        var expiryDateV2 = V2.DOCUMENT_CONTENT_DATE_START.atZone(ZoneId.systemDefault())
                .plusMonths(V2.LIFECYCLE_USER_DEFINED_MONTHS).toInstant();
        assertEquals(expiryDateV2, updatedDocument.getExpiryDate());


        // doc is minimally updated
        var againUpdatedDocument = RestAssuredMockMvc

                .given()
                .multiPart("file", V2.FILE_NAME, V2.FILE_CONTENT, "application/pdf")
                .param("author", V3.AUTHOR)
                //workaround: restassured does not support PUT for files
                .postProcessors(request -> {
                    request.setMethod("PUT");
                    return request;
                })

                .when()
                .post("/api/documents/v1/" + document.getId())
                .prettyPeek()

                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(Document.class);


        //then only a minimal set of fields get updated in alfresco

        assertNotNull(againUpdatedDocument.getCreatedBy());
        assertNotNull(againUpdatedDocument.getCreatedTime());
        assertNotNull(againUpdatedDocument.getModifiedBy());
        assertNotNull(againUpdatedDocument.getModifiedTime());

        assertEquals(document.getCreatedTime(), againUpdatedDocument.getCreatedTime());

        assertEquals(V3.AUTHOR, againUpdatedDocument.getAuthor());
        assertEquals(V2.FILE_NAME, againUpdatedDocument.getFilename());
        assertEquals("1.3", againUpdatedDocument.getVersion());
        assertEquals(V2.CONFIDENTIALITY, againUpdatedDocument.getConfidentiality());
        assertEquals(V2.DIVISION, againUpdatedDocument.getDivision());
        assertEquals(V2.LOCATION, againUpdatedDocument.getLocationId());
        assertEquals(V2.DOC_TITLE, againUpdatedDocument.getDocTitle());
        assertEquals(V2.TEAM, againUpdatedDocument.getTeam());
        assertEquals(V2.DOCUMENT_CATEGORY, againUpdatedDocument.getDocumentCategory());
        assertEquals(V2.INTERNAL, againUpdatedDocument.isInternal());
        assertEquals(ApprovalStatus.PENDING_REVIEW, againUpdatedDocument.getApprovalStatus());
        assertEquals(V2.STATUS, againUpdatedDocument.getStatus());
        assertEquals(V2.DATA_THEME, againUpdatedDocument.getDataTheme());
        assertEquals(V2.DATA_CATEGORY, againUpdatedDocument.getDataCategory());
        assertEquals(V2.LIFECYCLE, againUpdatedDocument.getLifecycle());
        assertEquals(V2.LIFECYCLE_USER_DEFINED_MONTHS, againUpdatedDocument.getLifecycleUserDefinedMonths());
        assertEquals(V2.DOCUMENT_CONTENT_DATE_START, againUpdatedDocument.getDocumentContentDateStart());
        assertEquals(V2.DOCUMENT_CONTENT_DATE_END, againUpdatedDocument.getDocumentContentDateEnd());
        assertEquals(V2.DOCUMENT_PUBLISH_DATE, againUpdatedDocument.getDocumentPublishDate());
        assertEquals(V2.DOCUMENT_SURVEY_DATE, againUpdatedDocument.getDocumentSurveyDate());
        assertEquals(V2.COMMISSIONER, againUpdatedDocument.getCommissioner());
        assertEquals(V2.CAUTION_ADVISED, againUpdatedDocument.isCautionAdvised());
        assertEquals(V2.DATA_REVIEWED, againUpdatedDocument.isDataReviewed());
        assertEquals(V2.TAGS, againUpdatedDocument.getTags());
        assertEquals(expiryDateV2, againUpdatedDocument.getExpiryDate());
    }

}
