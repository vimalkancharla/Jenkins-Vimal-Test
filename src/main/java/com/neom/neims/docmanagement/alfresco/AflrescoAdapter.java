package com.neom.neims.docmanagement.alfresco;

import com.neom.neims.docmanagement.Document;
import com.neom.neims.docmanagement.DocumentMapper;
import com.neom.neims.docmanagement.CreateDocumentRequest;
import com.neom.neims.docmanagement.UpdateDocumentRequest;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.alfresco.core.handler.NodesApiClient;
import org.alfresco.core.model.NodeBodyCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Slf4j
@Service
public class AflrescoAdapter {

    private final NodesApiClient nodesApiClient;
    private final DocumentMapper documentMapper;

    @Value("${alfrescoDocumentUploadDirectoryId}")
    private String alfrescoDocumentUploadDirectoryId;

    @Autowired
    public AflrescoAdapter(final NodesApiClient nodesApiClient,
                           final DocumentMapper documentMapper) {
        this.nodesApiClient = nodesApiClient;
        this.documentMapper = documentMapper;
    }

    public Document getDocument(String id) {
        var responseEntity = nodesApiClient.getNode(id, null, null, null);
        var node = responseEntity.getBody().getEntry();
        if (node == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource with id: " + id);
        }
        return documentMapper.buildDocument(node);
    }

    // TODO: optimise for file handling
    // https://neom.atlassian.net/browse/NRV-1298
    public byte[] getDocumentContent(String id) {
        try {
            return nodesApiClient.getNodeContent(id, false, null, null).getBody().getInputStream().readAllBytes();
        } catch (IOException e) {
            log.error("failed to fetch the document content", e);
            throw new RuntimeException("failed fetching the document content");
        }
    }

    public Document createDocument(String docTitle, MultipartFile file, CreateDocumentRequest createDocumentRequest) throws IOException {
        NodeBodyCreate body = documentMapper.buildNodeBodyCreate(docTitle, file, createDocumentRequest);

        try {
            var createNodeResponse = nodesApiClient.createNode(alfrescoDocumentUploadDirectoryId, body, true, null, true, null, null);
            var docId = createNodeResponse.getBody().getEntry().getId();

            // save doc - upload content
            var responseEntity = nodesApiClient.updateNodeContent(docId, file.getBytes(), false, "doc draft created", null, null, null);
            var node = responseEntity.getBody().getEntry();
            return documentMapper.buildDocument(node);
        } catch (FeignException e) {
            throw processFeignException(e);
        }
    }


    public Document updateDocument(String id, String docTitle, MultipartFile file, UpdateDocumentRequest updateDocumentRequest) throws IOException {
        try {
            var body = documentMapper.buildNodeBodyUpdate(docTitle, file, updateDocumentRequest);
            var responseEntity = nodesApiClient.updateNode(id, body, null, null);
            if(file != null){
                responseEntity = nodesApiClient.updateNodeContent(id, file.getBytes(), false, "doc draft updated", null, null, null);
            }
            var node = responseEntity.getBody().getEntry();
            return documentMapper.buildDocument(node);

        } catch (FeignException e) {
            throw processFeignException(e);
        }
    }

    private RuntimeException processFeignException (FeignException e) {
        var msg = e.responseBody()
                .map(bb -> new String(bb.array()))
                .orElse(e.getMessage());
        log.error(msg);
        if (e.status() == SC_UNPROCESSABLE_ENTITY) {
            return new UnprocessableEntityException(msg, e);
        }
        return new AlfrescoException(msg, e);
    }
}
