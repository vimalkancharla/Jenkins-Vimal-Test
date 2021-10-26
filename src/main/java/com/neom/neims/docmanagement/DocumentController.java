package com.neom.neims.docmanagement;

import com.neom.neims.docmanagement.alfresco.AflrescoAdapter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.UUID;

import static com.neom.neims.docmanagement.Document.FIELD_DESCRIPTION_DOC_TITLE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@RestController
@RequestMapping("/api/documents/v1")
@Slf4j
@Api(tags = "Neims Documents")
public class DocumentController {

    private final AflrescoAdapter alfrescoAdapter;
    private final DocumentService documentService;

    @Autowired
    public DocumentController(final AflrescoAdapter alfrescoAdapter, final DocumentService documentService) {
        this.documentService = documentService;
        this.alfrescoAdapter = alfrescoAdapter;
    }

    @PostMapping
    @ApiOperation("Creates the document")
    @ResponseStatus(CREATED)
    public Document createDocument(
            @ApiParam("Document content, the actual document file")
                    MultipartFile file,
            @Valid @ApiParam(value = FIELD_DESCRIPTION_DOC_TITLE, required = true)
            @NotNull @RequestParam
                    String docTitle,
            @Valid
                    CreateDocumentRequest createDocumentRequest
    ) throws IOException {
        if (!DocumentCategory.NON_SURVEY.equals(createDocumentRequest.getDocumentCategory())) {
            SurveyValidator.validate(createDocumentRequest);
        }
        return alfrescoAdapter.createDocument(docTitle, file, createDocumentRequest);
    }


    @PutMapping("{id}")
    @ApiOperation("Updates the document")
    public Document updateDocument(
            @ApiParam(value = FIELD_DESCRIPTION_DOC_TITLE)
            @PathVariable
                    UUID id,
            @ApiParam("Document content, the actual document file")
                    MultipartFile file,
            @ApiParam(value = FIELD_DESCRIPTION_DOC_TITLE, required = false)
            @RequestParam(required = false)
                    String docTitle,
            UpdateDocumentRequest updateDocumentRequest
    ) throws IOException {
        return documentService.updateDocument(id.toString(), file, docTitle, updateDocumentRequest);
    }


    @GetMapping("{id}/content")
    @ApiOperation("Returns document content")
    public ResponseEntity<byte[]> getDocumentContent(@PathVariable String id) {
        var headers = new HttpHeaders();
        var doc = alfrescoAdapter.getDocument(id);
        headers.setContentType(APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(doc.getFilename(), doc.getFilename());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(alfrescoAdapter.getDocumentContent(id), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @ApiOperation("Fetches document data")
    public Document getDocument(@PathVariable UUID id) {
        return alfrescoAdapter.getDocument(id.toString());
    }

}


