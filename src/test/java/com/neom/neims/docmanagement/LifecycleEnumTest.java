package com.neom.neims.docmanagement;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class LifecycleEnumTest {

    @Test
    public void whenSerializingToJson_thenCorrect() throws IOException {

        var enumAsString = new ObjectMapper().writeValueAsString(Lifecycle.USER_DEFINED);
        assertThat(enumAsString).contains(Lifecycle.USER_DEFINED.getValue());
    }

    @Test
    public void whenDeserializingFromJson_thenCorrect() throws IOException {

        var status = new ObjectMapper().readValue("\"" + Lifecycle.USER_DEFINED.getValue() + "\"", Lifecycle.class);
        assertThat(status).isEqualTo(Lifecycle.USER_DEFINED);
    }
}
