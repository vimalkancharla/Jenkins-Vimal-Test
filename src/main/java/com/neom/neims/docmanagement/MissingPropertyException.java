package com.neom.neims.docmanagement;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ResponseStatus(UNPROCESSABLE_ENTITY)
public class MissingPropertyException extends RuntimeException {

    public MissingPropertyException(List<String> properties) {
        super("Following properties are missing: " + properties.toString());
    }

}
