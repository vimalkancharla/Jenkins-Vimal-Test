package com.neom.neims.docmanagement;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DocumentCategory {
    PERMIT("Permit"),
    SURVEY("Survey"),
    NON_SURVEY("Non Survey"),
    ESIA("ESIA");

    public static final String ALLOWABLE_VALUES = "Permit,Survey,Non Survey,ESIA";

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

}
