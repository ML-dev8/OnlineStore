package com.company.tasks.online.store.ws.serializers;

import com.company.tasks.online.store.entities.user.User;
import com.company.tasks.online.store.helpers.TestDataProvider;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderUserSerializerTest {

    @Test
    void serialize_when_objHasOtherFields() throws IOException {
        //given
        User user = TestDataProvider.getAdminUser(1);

        //when
        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();

        //serialize
        new OrderUserSerializer().serialize(user, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        //then
        assertEquals("{\"id\":1,\"name\":\"userName_1\"}", jsonWriter.toString());
    }
}