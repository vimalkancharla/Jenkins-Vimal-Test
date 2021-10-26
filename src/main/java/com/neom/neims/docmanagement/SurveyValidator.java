package com.neom.neims.docmanagement;

import java.util.ArrayList;
import java.util.List;


public class SurveyValidator {

    public static void validate(CreateDocumentRequest dm) {
        List<String> missingProperties = new ArrayList<>();

        if (dm.getDataTheme() == null) {
            missingProperties.add("dataTheme");
        }
        if (dm.getDataCategory() == null) {
            missingProperties.add("dataCategory");
        }

        if (missingProperties.size() > 0) {
            throw new MissingPropertyException(missingProperties);
        }

    }
}
