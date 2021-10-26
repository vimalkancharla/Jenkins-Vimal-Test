package com.neom.neims.docmanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;


@Data
@Api
@Builder
@ApiModel("Document Model")
@FieldNameConstants(innerTypeName = "MetaModel")
public class Document {

    public static final String FIELD_DESCRIPTION_ID = "Unique id of the document";
    @ApiModelProperty(value = FIELD_DESCRIPTION_ID)
    private UUID id;

    public static final String FIELD_DESCRIPTION_DOC_TITLE = "The Title of the Document/Survey (not the file name)";
    @NotBlank(message = "title is mandatory")
    @ApiModelProperty(value = FIELD_DESCRIPTION_DOC_TITLE)
    private String docTitle;

    public static final String FIELD_DESCRIPTION_FILENAME = "The name of the file";
    @ApiModelProperty(value = FIELD_DESCRIPTION_FILENAME)
    private String filename;

    public static final String FIELD_DESCRIPTION_DOC_TYPE = "The file type";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DOC_TYPE)
    private String docType;

    public static final String FIELD_DESCRIPTION_DOCUMENT_CATEGORY = "The business category for a document. (Admin-ProjectMan category has simplified tagging requirements)";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DOCUMENT_CATEGORY, allowableValues = DocumentCategory.ALLOWABLE_VALUES)
    private DocumentCategory documentCategory;

    public static final String FIELD_DESCRIPTION_DOCUMENT_SUB_CATEGORY = "The sub category of the document";
    @ApiModelProperty(FIELD_DESCRIPTION_DOCUMENT_SUB_CATEGORY)
    private String documentSubCategory;

    public static final String FIELD_DESCRIPTION_DOCUMENT_SUB_SUB_CATEGORY = "The third level of a document category";
    @ApiModelProperty(FIELD_DESCRIPTION_DOCUMENT_SUB_SUB_CATEGORY)
    private String documentSubSubCategory;

    public static final String FIELD_DESCRIPTION_INTERNAL = "Default to false as more docs will be external. Set true for internally created docs in NEV/NEIMS";
    @ApiModelProperty(value = FIELD_DESCRIPTION_INTERNAL)
    private boolean internal;

    public static final String FIELD_DESCRIPTION_VERSION = "The numbered version of the document";
    @ApiModelProperty(value = FIELD_DESCRIPTION_VERSION)
    private String version;

    public static final String FIELD_DESCRIPTION_STATUS = "The 'real world' status of the document";
    @ApiModelProperty(value = FIELD_DESCRIPTION_STATUS,
            notes = " Currently just Draft, Published or blank (default), but in the future there may be other status values that could be added. " +
                    "(This has got nothing to do with the NEIMS publish/approval status). " +
                    "When a dcument has a documentPublishDate set on the document metadata, then this property value should be 'Published'. " +
                    "However, it is possible to have a document set to published without a documentPublishDate (date not known etc)"
    )
    private String status;

    public static final String FIELD_DESCRIPTION_DOCUMENT_DATE = "This is the publish date for the document (NOT the NEIMS publish date)";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DOCUMENT_DATE,
            notes = "E.g a survey could be undertaken on a particular date but not actually published for months. " +
                    "NOTE: This is different to the publishDateNEIMS attribute below which is when the document" +
                    " has gone through the NEIMS approval process and is then subsequently published")
    private Instant documentDate;

    public static final String FIELD_DESCRIPTION_DOCUMENT_CONTENT_DATE_START = "This is the that relates to the survey,study, " +
            "or any document Category where a date defines when the content/data within the document is for";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DOCUMENT_CONTENT_DATE_START,
            notes = "E.g. if a survey is undertaken on a particular date, then this date should be entered here. " +
                    "For many NEIMS documents, this is the date the survey was undertaken. In NEIMS this field should have the label 'Survey Date'." +
                    "It can also be thought of as the start date for the data for that survey. " +
                    "This defines the start date that the 'lifecycle' field in the document meta-data model" +
                    " is applied to to to derive the expiry date which is also on the document meta data.")
    private Instant documentContentDateStart;

    public static final String FIELD_DESCRIPTION_DOCUMENT_CONTENT_DATE_END = "If there is a time range for the content/data then this is captured with the start and end date";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DOCUMENT_CONTENT_DATE_END)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant documentContentDateEnd;

    public static final String FIELD_DESCRIPTION_DOCUMENT_SURVEY_DATE = "The Survey date is more aligned to the date of acquisition of the survey data (or the start date that the data applies to.) The Data lifecycle ‘tag’ below is then based on this date as it’s start date. OPTIONAL";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DOCUMENT_SURVEY_DATE)
    private Instant documentSurveyDate;

    public static final String FIELD_DESCRIPTION_DOCUMENT_PUBLISH_DATE = "This is the publish date for the document (NOT the NEIMS publish date). E.g a survey could be undertaken on a particular date but not actually published for months. NOTE: This is different to the publishDateNEIMS attribute below which is when the document has gone through the NEIMS approval process and is then subsequently published";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DOCUMENT_PUBLISH_DATE)
    private Instant documentPublishDate;

    public static final String FIELD_DESCRIPTION_UPLOAD_DATE_NEIMS = "the datetime when the document was published on NEIMS ";
    @ApiModelProperty(value = FIELD_DESCRIPTION_UPLOAD_DATE_NEIMS)
    private Instant uploadDateNEIMS;

    public static final String FIELD_DESCRIPTION_PUBLISH_DATE_NEIMS = "the datetime when the document was published on NEIMS after going through an approval process";
    @ApiModelProperty(value = FIELD_DESCRIPTION_PUBLISH_DATE_NEIMS)
    private Instant publishDateNEIMS;


    public static final String FIELD_DESCRIPTION_AUTHOR = "The name of the company that produced the report/document.";
    @ApiModelProperty(value = FIELD_DESCRIPTION_AUTHOR,
            notes = "E.g. The Consultancy which carried out a Survey and then produced a report for it.")
    private String author;

    public static final String FIELD_DESCRIPTION_MODIFIED_BY = "The name/id of the NEIMS user who modified the document";
    @ApiModelProperty(value = FIELD_DESCRIPTION_MODIFIED_BY)
    private String modifiedBy;

    public static final String FIELD_DESCRIPTION_DIVISION = "Divisions within the NEOM Environment Department";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DIVISION)
    private String division;


    public static final String FIELD_DESCRIPTION_TEAM = "Teams within the NEOM Environment Department";
    @ApiModelProperty(value = FIELD_DESCRIPTION_TEAM)
    private String team;

    public static final String FIELD_DESCRIPTION_LOCATION = "This is a named location id in NEOM (place,area,town etc)";
    @ApiModelProperty(value = FIELD_DESCRIPTION_LOCATION)
    private Integer locationId;

    public static final String FIELD_DESCRIPTION_TAGS = "Contains ingested keywords from document content, originated from Alfresco";
    @ApiModelProperty(value = FIELD_DESCRIPTION_TAGS)
    private String tags;

    public static final String FIELD_DESCRIPTION_DOC_URL = "Url directing to document's content";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DOC_URL)
    private String docUrl;

    public static final String FIELD_DESCRIPTION_ARCHIVE_DATE_NEIMS = "The date the document was archived in NEIMS and therefore not generally available in NEIMS";
    @ApiModelProperty(value = FIELD_DESCRIPTION_ARCHIVE_DATE_NEIMS)
    private Instant archiveDateNEIMS;

    public static final String FIELD_DESCRIPTION_DATA_SOURCE = "to tag a document as a data source doc that typically acts as a data source for a survey";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DATA_SOURCE)
    private boolean dataSource;

    public static final String FIELD_DESCRIPTION_LIFECYCLE = "Lifecycle determines the expiry date for the document. If 'Perpetuity' is selected then the expiryDate field should be blank. If 12 months is selected then add 1 year to the documentContentDateStart. If x months is entered by a user then add x months to the documentContentDateStart.";
    @ApiModelProperty(value = FIELD_DESCRIPTION_LIFECYCLE)
    private Lifecycle lifecycle;

    public static final String FIELD_DESCRIPTION_LIFECYCLE_USER_DEFINED_MONTHS = "User defined lifecycle duration in months";
    @ApiModelProperty(value = FIELD_DESCRIPTION_LIFECYCLE_USER_DEFINED_MONTHS)
    private Integer lifecycleUserDefinedMonths;

    public static final String FIELD_DESCRIPTION_CREATED_BY = "The name/id of the NEIMS user who uploaded the document";
    @ApiModelProperty(value = FIELD_DESCRIPTION_CREATED_BY)
    private String createdBy;

    public static final String FIELD_DESCRIPTION_CREATED_TIME = "The time when the document was created";
    @ApiModelProperty(value = FIELD_DESCRIPTION_CREATED_TIME)
    private Instant createdTime;

    public static final String FIELD_DESCRIPTION_MODIFIED_DATE = "The time when the document was last modified";
    @ApiModelProperty(value = FIELD_DESCRIPTION_MODIFIED_DATE)
    private Instant modifiedTime;

    public static final String FIELD_DESCRIPTION_CONFIDENTIALITY = "Confidential access level for the document. (See NEIMS Wav1 requirements for this in Confluence)";
    @ApiModelProperty(value = FIELD_DESCRIPTION_CONFIDENTIALITY)
    private Integer confidentiality;

    public static final String FIELD_DESCRIPTION_APPROVAL_STATUS = "The NIEMS based approval status of the document based on NEIMS approval processes";
    @ApiModelProperty(value = FIELD_DESCRIPTION_APPROVAL_STATUS, allowableValues = ApprovalStatus.ALLOWEABLE_VALUES)
    private ApprovalStatus approvalStatus;

    public static final String FIELD_DESCRIPTION_CAUTION_ADVISED = "Users will apply this tag for a survey with questionable data";
    @ApiModelProperty(value = FIELD_DESCRIPTION_CAUTION_ADVISED)
    private boolean cautionAdvised;

    public static final String FIELD_DESCRIPTION_DATA_REVIEWED = "Set to true when a Data Officer has reviewd the data aossicated with a document";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DATA_REVIEWED)
    private boolean dataReviewed;

    public static final String FIELD_DESCRIPTION_PENDING_VERSION = "when a new document version is uploaded it needs to be held on a pending version record which is subsequently approved by an Admmin";
    @ApiModelProperty(value = FIELD_DESCRIPTION_PENDING_VERSION)
    private boolean pendingVersion;

    public static final String FIELD_DESCRIPTION_COMMISSIONER = "TODO missing description";
    @ApiModelProperty(value = FIELD_DESCRIPTION_COMMISSIONER)
    private String commissioner;

    public static final String FIELD_DESCRIPTION_DATA_CATEGORY = "tag values to describe the types of data/content contained within the document";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DATA_CATEGORY)
    private String dataCategory;

    public static final String FIELD_DESCRIPTION_DATA_THEME = "tag values to describe thef data Theme associated with the document";
    @ApiModelProperty(value = FIELD_DESCRIPTION_DATA_THEME)
    private String dataTheme;

    public static final String FIELD_DESCRIPTION_EXPIRY_DATE = "The date the document content/data expires.";
    @ApiModelProperty(value = FIELD_DESCRIPTION_EXPIRY_DATE,
            notes = "(See 'documentContentDateStart' property and 'lifecycle' property)")
    private Instant expiryDate;

    @JsonIgnore
    public boolean isDraft() {
        return Double.parseDouble(version) < 2;
    }
}
