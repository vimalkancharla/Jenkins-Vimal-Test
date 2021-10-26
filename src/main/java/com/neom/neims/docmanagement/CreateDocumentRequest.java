package com.neom.neims.docmanagement;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.Instant;

import static com.neom.neims.docmanagement.Document.*;


@Data
public class CreateDocumentRequest {

    @ApiParam(FIELD_DESCRIPTION_INTERNAL)
    private boolean internal = false;

    @NotNull
    @ApiParam(value = FIELD_DESCRIPTION_DOCUMENT_CATEGORY, allowableValues = DocumentCategory.ALLOWABLE_VALUES)
    private DocumentCategory documentCategory;

    @ApiParam(FIELD_DESCRIPTION_DOCUMENT_SUB_CATEGORY)
    private String documentSubCategory;

    @ApiParam(FIELD_DESCRIPTION_DOCUMENT_SUB_SUB_CATEGORY)
    private String documentSubSubCategory;

    @ApiParam(FIELD_DESCRIPTION_DATA_SOURCE)
    private boolean dataSource = false;

    @ApiParam(FIELD_DESCRIPTION_CAUTION_ADVISED)
    private boolean cautionAdvised = false;

    @ApiParam(FIELD_DESCRIPTION_DATA_REVIEWED)
    private boolean dataReviewed = false;

    @ApiParam(value = FIELD_DESCRIPTION_LIFECYCLE, allowableValues = Lifecycle.ALLOWABLE_VALUES)
    private Lifecycle lifecycle;

    @ApiParam(value = FIELD_DESCRIPTION_LIFECYCLE_USER_DEFINED_MONTHS, example = "1")
    private Integer lifecycleUserDefinedMonths;

    @ApiParam(FIELD_DESCRIPTION_EXPIRY_DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant expiryDate;

    @ApiParam(FIELD_DESCRIPTION_DIVISION)
    private String division;

    @ApiParam(FIELD_DESCRIPTION_TEAM)
    private String team;

    @ApiParam(value = FIELD_DESCRIPTION_LOCATION, example = "1")
    private Integer locationId;

    @ApiParam(FIELD_DESCRIPTION_DOCUMENT_PUBLISH_DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant documentPublishDate;

    @ApiParam(FIELD_DESCRIPTION_DOCUMENT_SURVEY_DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant documentSurveyDate;

    @ApiParam(FIELD_DESCRIPTION_DOCUMENT_CONTENT_DATE_START)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant documentContentDateStart;

    @ApiParam(FIELD_DESCRIPTION_DOCUMENT_CONTENT_DATE_END)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant documentContentDateEnd;

    @ApiParam(FIELD_DESCRIPTION_AUTHOR)
    private String author;

    @ApiParam(FIELD_DESCRIPTION_COMMISSIONER)
    private String commissioner;

    @ApiParam(FIELD_DESCRIPTION_STATUS)
    private String status;

    @ApiParam(value = FIELD_DESCRIPTION_DATA_THEME)
    private String dataTheme;

    @ApiParam(value = FIELD_DESCRIPTION_DATA_CATEGORY)
    private String dataCategory;

    @ApiParam(FIELD_DESCRIPTION_TAGS)
    private String tags;

    @ApiParam(value = FIELD_DESCRIPTION_CONFIDENTIALITY, example = "1")
    private Integer confidentiality;

}
