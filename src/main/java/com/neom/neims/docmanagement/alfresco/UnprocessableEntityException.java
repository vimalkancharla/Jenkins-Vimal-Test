package com.neom.neims.docmanagement.alfresco;

import feign.FeignException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ResponseStatus(UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends AlfrescoException {

    public UnprocessableEntityException(String msg, FeignException t) {
        super(msg, t);
    }

}
