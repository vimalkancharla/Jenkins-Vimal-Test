package com.neom.neims.docmanagement;

import com.neom.neims.docmanagement.alfresco.AflrescoAdapter;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentService {

    private final AflrescoAdapter alfrescoAdapter;

    @Autowired
    public DocumentService(final AflrescoAdapter alfrescoAdapter) {
        this.alfrescoAdapter = alfrescoAdapter;
    }


    public Document updateDocument(
            String id,
            MultipartFile file,
            String docTitle,
            UpdateDocumentRequest updateDocumentRequest
    ) throws IOException {
        Document document = alfrescoAdapter.getDocument(id);

        if (document.isDraft()) {
            return updateDraft(id, file, docTitle, updateDocumentRequest, document);
        } else {
            return updateSubsequentVersion();
        }

    }

    private Document updateDraft(
            String id,
            MultipartFile file,
            String docTitle,
            UpdateDocumentRequest updateDocumentRequest,
            Document document
    ) throws IOException {
        if (document.getApprovalStatus().checkIsUpdatable()) {
            return alfrescoAdapter.updateDocument(id, docTitle, file, updateDocumentRequest);
        }
        throw new RuntimeException("unable to update doc");
    }

    private Document updateSubsequentVersion() {
        throw new NotImplementedException("updating subsequent version is yet not supported");
    }

}


