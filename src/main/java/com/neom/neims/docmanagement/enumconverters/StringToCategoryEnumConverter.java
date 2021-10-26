package com.neom.neims.docmanagement.enumconverters;

import com.neom.neims.docmanagement.DocumentCategory;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;

public class StringToCategoryEnumConverter implements Converter<String, DocumentCategory> {

    private final Map<String, DocumentCategory> enumsMap = new HashMap<>();

    public StringToCategoryEnumConverter() {
        for (var enumm : DocumentCategory.values()) {
            enumsMap.put(enumm.getValue(), enumm);
        }
    }

    @Override
    public DocumentCategory convert(String source) {
        return enumsMap.get(source);
    }
}