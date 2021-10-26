package com.neom.neims.docmanagement;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.alfresco.core.model.Node;
import org.alfresco.core.model.NodeBodyCreate;
import org.alfresco.core.model.NodeBodyUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DocumentMapper {

    private static final String PREFIX = "nms:";

    private final ObjectMapper serializingOM;
    private final ObjectMapper deserializingOM;

    @Autowired
    DocumentMapper(final Jackson2ObjectMapperBuilder objectMapperBuilder) {
        serializingOM = objectMapperBuilder.createXmlMapper(false).build();
        deserializingOM = objectMapperBuilder.createXmlMapper(false).build()
                .registerModule(
                        new SimpleModule()
                                .addDeserializer(Instant.class, new CustomAlfrescoDateDeserializer()));
    }

    public NodeBodyCreate buildNodeBodyCreate(String docTitle, MultipartFile file, CreateDocumentRequest createDocumentRequest) {
        var metadata = (Map<String, Object>) serializingOM.convertValue(createDocumentRequest, Map.class);
        var properties =
                metadata.entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .collect(Collectors.toMap(
                                entry -> PREFIX + entry.getKey(),
                                Map.Entry::getValue));

        properties.put(PREFIX + "docType", file.getContentType());
        properties.put(PREFIX + "filename", file.getOriginalFilename());
        properties.put(PREFIX + "approvalStatus", ApprovalStatus.PENDING_REVIEW);

        if (createDocumentRequest.getExpiryDate() == null) {
            getExpiryDate(createDocumentRequest.getLifecycle(),
                    createDocumentRequest.getDocumentContentDateStart(),
                    createDocumentRequest.getLifecycleUserDefinedMonths())
                    .ifPresent(expiryDate ->
                            properties.put(PREFIX + "expiryDate", expiryDate));
        } else {
            properties.put(PREFIX + "expiryDate", createDocumentRequest.getExpiryDate());
        }

        return new NodeBodyCreate()
                .nodeType("cm:" + "content")
                .name(docTitle)
                .properties(properties);
    }


    public NodeBodyUpdate buildNodeBodyUpdate(String docTitle, MultipartFile file, UpdateDocumentRequest dup) {
        var metadata = (Map<String, Object>) serializingOM.convertValue(dup, Map.class);
        var properties =
                metadata.entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .collect(Collectors.toMap(
                                entry -> PREFIX + entry.getKey(),
                                Map.Entry::getValue));

        if (file != null) {
            properties.put(PREFIX + "docType", file.getContentType());
            properties.put(PREFIX + "filename", file.getOriginalFilename());
        }

        if (dup.getExpiryDate() == null) {
            getExpiryDate(dup.getLifecycle(),
                    dup.getDocumentContentDateStart(),
                    dup.getLifecycleUserDefinedMonths())
                    .ifPresent(expiryDate ->
                            properties.put(PREFIX + "expiryDate", expiryDate));
        } else {
            properties.put(PREFIX + "expiryDate", dup.getExpiryDate());
        }

        var nodeBodyUpdate = new NodeBodyUpdate();
        if (docTitle != null) {
            nodeBodyUpdate.name(docTitle);
        }
        return nodeBodyUpdate
                .properties(properties);
    }


    // TODO: move to domain object later
    private Optional<Instant> getExpiryDate(Lifecycle lifecycle, Instant documentContentDateStart, Integer lifecycleUserDefinedMonths) {
        if (lifecycle == null || documentContentDateStart == null) {
            return Optional.empty();
        }
        switch (lifecycle) {
            case _12_MONTHS:
                return Optional.of(
                        documentContentDateStart
                                .atZone(ZoneId.systemDefault())
                                .plusYears(1)
                                .toInstant());
            case USER_DEFINED:
                if (lifecycleUserDefinedMonths == null) {
                    return Optional.empty();
                }
                return Optional.of(
                        documentContentDateStart
                                .atZone(ZoneId.systemDefault())
                                .plusMonths(lifecycleUserDefinedMonths)
                                .toInstant());
        }
        return Optional.empty();
    }

    public Document buildDocument(Node node) {
        var properties = (Map<String, Object>) node.getProperties();
        var metadata = properties.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(PREFIX))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().replace(PREFIX, ""),
                        Map.Entry::getValue));

        var doc = deserializingOM.convertValue(metadata, Document.class);
        doc.setId(UUID.fromString(node.getId()));
        doc.setDocTitle(node.getName());
        doc.setDocUrl("/api/documents/v1/" + node.getId() + "/content");

        doc.setModifiedBy(node.getModifiedByUser().getDisplayName());
        doc.setModifiedTime(node.getModifiedAt().toInstant());
        doc.setCreatedTime(node.getCreatedAt().toInstant());
        doc.setCreatedBy(node.getCreatedByUser().getDisplayName());

        doc.setVersion((String) properties.get("cm:versionLabel"));
        return doc;
    }


    static class CustomAlfrescoDateDeserializer extends StdDeserializer<Instant> {
        private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        CustomAlfrescoDateDeserializer() {
            this(null);
        }

        CustomAlfrescoDateDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            try {
                return DATE_FORMAT.parse(p.getText()).toInstant();
            } catch (ParseException e) {
                log.error("unable to parse date: " + p.getText(), e);
                throw new RuntimeException("unable to parse date :" + p.getText(), e);
            }
        }
    }
}
