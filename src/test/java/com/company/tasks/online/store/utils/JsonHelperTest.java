package com.company.tasks.online.store.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonHelperTest {

    private JsonHelper jsonHelper;

    @BeforeEach
    void setUp() {
        jsonHelper = new JsonHelper();
    }

    @Test
    void getJsonFromPojo_when_pojoProvided() throws IOException {
        //given
        TestPojo pojoToSerialize = new TestPojo("Test Test", 12);
        String expectedJson = "{\"fullName\":\"Test Test\",\"age\":12}";

        //when
        String actualJson = jsonHelper.getJsonFromPojo(pojoToSerialize);

        //then
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void getJsonFromPojo_when_pojoWithDefaultValuesProvided() throws IOException {
        //given
        TestPojo pojoToSerialize = new TestPojo();
        String expectedJson = "{\"fullName\":null,\"age\":0}";

        //when
        String actualJson = jsonHelper.getJsonFromPojo(pojoToSerialize);

        //then
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void getPojoFromJson_when_jsonProvided() throws IOException {
        //given
        String json = "{\"fullName\":\"Test Test\",\"age\":12}";
        TestPojo expectedPojo = new TestPojo("Test Test", 12);

        //when
        TestPojo actualPojo = jsonHelper.getPojoFromJson(json, TestPojo.class);

        //then
        assertEquals(expectedPojo, actualPojo);
    }

    @Test
    void getPojoFromJson_when_jsonWithDefaultValuesProvided() throws IOException {
        //given
        String json = "{\"fullName\":null,\"age\":0}";
        TestPojo expectedPojo = new TestPojo();

        //when
        TestPojo actualPojo = jsonHelper.getPojoFromJson(json, TestPojo.class);

        //then
        assertEquals(expectedPojo, actualPojo);
    }
}
