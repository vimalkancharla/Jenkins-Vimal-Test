package com.neom.neims.docmanagement;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

@AllArgsConstructor
public enum ApprovalStatus {

    PENDING_REVIEW("Pending Review"),
    REJECTED("Rejected"),
    PARTIALLY_APPROVED("Partially Approved"),
    APPROVED("Approved"),
    ARCHIVED("Archived");

    public static final String ALLOWEABLE_VALUES = "Permit,Survey,Non Survey,ESIA";

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }


    public boolean checkIsUpdatable() {
        switch (this) {
            case PENDING_REVIEW:
                return true;
            case APPROVED:
                throw new NotImplementedException("Updating approved doc is not implemented, yet.");
            default:
                throw new UnsupportedOperationException("Nope, no way for updating document that is already in a " + this.getValue() + "status");
        }
    }
}
