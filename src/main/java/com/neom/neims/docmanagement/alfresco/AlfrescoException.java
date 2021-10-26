package com.neom.neims.docmanagement.alfresco;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class AlfrescoException extends RuntimeException {

    public AlfrescoException(String msg, Throwable t) {
        super(msg, t);
    }

}
