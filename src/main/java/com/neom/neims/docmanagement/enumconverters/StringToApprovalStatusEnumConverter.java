package com.neom.neims.docmanagement.enumconverters;

import com.neom.neims.docmanagement.ApprovalStatus;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;

public class StringToApprovalStatusEnumConverter implements Converter<String, ApprovalStatus> {

    private final Map<String, ApprovalStatus> enumsMap = new HashMap<>();

    public StringToApprovalStatusEnumConverter() {
        for (var enumm : ApprovalStatus.values()) {
            enumsMap.put(enumm.getValue(), enumm);
        }
    }

    @Override
    public ApprovalStatus convert(String source) {
        return enumsMap.get(source);
    }
}