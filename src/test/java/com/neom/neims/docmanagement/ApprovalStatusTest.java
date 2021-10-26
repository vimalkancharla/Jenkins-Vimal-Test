package com.neom.neims.docmanagement;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ApprovalStatusTest {

    @Test
    public void whenSerializingToJson_thenCorrect() throws IOException {

        var enumAsString = new ObjectMapper().writeValueAsString(ApprovalStatus.PENDING_REVIEW);
        assertThat(enumAsString).contains(ApprovalStatus.PENDING_REVIEW.getValue());
    }

    @Test
    public void whenDeserializingFromJson_thenCorrect() throws IOException {

        var status = new ObjectMapper().readValue("\"" + ApprovalStatus.PENDING_REVIEW.getValue() + "\"", ApprovalStatus.class);
        assertThat(status).isEqualTo(ApprovalStatus.PENDING_REVIEW);
    }
}
