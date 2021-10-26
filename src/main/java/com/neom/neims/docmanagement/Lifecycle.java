package com.neom.neims.docmanagement;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Lifecycle {
    _PERPETUITY("Perpetuity"),
    _12_MONTHS("12 months"),
    USER_DEFINED("User Defined");

    public static final String ALLOWABLE_VALUES = "Perpetuity,12 months,User Defined";

    @JsonValue
    private final String value;

    public String getValue() {
        return value;
    }

}
