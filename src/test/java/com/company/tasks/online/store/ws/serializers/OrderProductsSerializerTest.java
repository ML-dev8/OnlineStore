package com.company.tasks.online.store.ws.serializers;

import com.company.tasks.online.store.entities.Product;
import com.company.tasks.online.store.helpers.TestDataProvider;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProductsSerializerTest {

    @Test
    void serialize_when_objHasOtherFields() throws IOException {
        //given
        //set of one product for simplicity
        Set<Product> products = Set.of(TestDataProvider.getProduct(1));

        //when
        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();

        //serialize
        new OrderProductsSerializer().serialize(products, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        //then
        assertEquals("[{\"id\":1,\"name\":\"productName_1\",\"price\":\"10\"}]", jsonWriter.toString());
    }
}