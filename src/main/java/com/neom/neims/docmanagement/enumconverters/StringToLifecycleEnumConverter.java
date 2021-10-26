package com.neom.neims.docmanagement.enumconverters;

import com.neom.neims.docmanagement.Lifecycle;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;

public class StringToLifecycleEnumConverter implements Converter<String, Lifecycle> {

    private final Map<String, Lifecycle> enumsMap = new HashMap<>();

    public StringToLifecycleEnumConverter() {
        for (var lifecycle : Lifecycle.values()) {
            enumsMap.put(lifecycle.getValue(), lifecycle);
        }
    }

    @Override
    public Lifecycle convert(String source) {
        return enumsMap.get(source);
    }
}