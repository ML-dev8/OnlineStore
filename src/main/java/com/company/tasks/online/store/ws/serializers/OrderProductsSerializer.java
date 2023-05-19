package com.company.tasks.online.store.ws.serializers;

import com.company.tasks.online.store.entities.Product;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Set;

public class OrderProductsSerializer extends JsonSerializer<Set<Product>> {

    @Override
    public void serialize(Set<Product> products, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (Product product : products) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("id", product.getId());
            jsonGenerator.writeStringField("name", product.getName());
            jsonGenerator.writeStringField("price", product.getPrice().toString());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
