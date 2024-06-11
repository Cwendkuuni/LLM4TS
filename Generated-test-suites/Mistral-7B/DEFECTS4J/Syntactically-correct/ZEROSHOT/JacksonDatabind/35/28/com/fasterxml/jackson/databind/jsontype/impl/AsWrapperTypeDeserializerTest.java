package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {AsWrapperTypeDeserializer} class
 */
public class AsWrapperTypeDeserializerTest {

    // Sample Pojo for testing
    static class TestPojo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class TestPojoDeserializer extends StdSerializer<TestPojo> {
        TestPojoDeserializer() {
            this(null);
        }

        TestPojoDeserializer(Class<TestPojo> t) {
            this(null, t);
        }

        TestPojoDeserializer(TestPojoDeserializer src, Class<TestPojo> t) {
            super(t);
        }

        @Override
        public TestPojo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TestPojo testPojo = new TestPojo();
            testPojo.setName(p.getValueAsString());
            return testPojo;
        }
    }

    static class TestPojoMapper extends JsonMapper {
        TestPojoMapper() {
            this(new HashMap<>());
        }

        TestPojoMapper(Map<Class<?>, Object> additionalProperties) {
            super(new AnnotationIntrospector(), new StandardSerializerFactory(), additionalProperties);
        }

        @Override
        public <T> T findValueToDeserialize(JsonParser p, DeserializationContext ctxt, TypeDeserializer deserializer, TypeDeserializerPropertyConfigurer configurer) throws IOException {
            if (deserializer instanceof AsWrapperTypeDeserializer) {
                return deserializeAsWrapperObject(p, ctxt, deserializer);
            }
            return super.findValueToDeserialize(p, ctxt, deserializer, configurer);
        }

        private <T> T deserializeAsWrapperObject(JsonParser p, DeserializationContext ctxt, TypeDeserializer deserializer) throws IOException {
            ObjectNode node = p.readValueAs(ObjectNode.class);
            String typeId = node.get("class").asText();
            Object value = deserializer.deserializeTypedFromAny(p, ctxt);
            return (T) deserialize(value.getClass(), p, ctxt);
        }
    }

    @ org.junit.BeforeClass
    public static void setUp() throws Exception {
        // Initialize test mapper
        TestPojoMapper mapper = new TestPojoMapper();
    }

    @ org.junit.Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Given
        String jsonString = "{\"class\":\"com.fasterxml.jackson.databind.jsontype.impl.TestPojo\",\"name\":\"Test Pojo\"}";
        ObjectNode jsonNode = mapper.readValue(jsonString, ObjectNode.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(TestPojo.class, mapper.getTypeIdResolver(), "_class", true, TestPojo.class);

        // When
        TestPojo testPojo = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());

        // Then
        TestPojo expectedTestPojo = new TestPojo();
        expectedTestPojo.setName("Test Pojo");
        assertEquals(expectedTestPojo, testPojo);
    }

    @ org.junit.Test
    public void testDeserializeTypedFromArray() throws IOException {
        // Given
        String jsonString = "[{\"class\":\"com.fasterxml.jackson.databind.jsontype.impl.TestPojo\",\"name\":\"Test Pojo 1\"},{\"class\":\"com.fasterxml.jackson.databind.jsontype.impl.TestPojo\",\"name\":\"Test Pojo 2\"}]";
        ObjectNode jsonNode = mapper.readValue(jsonString, ObjectNode.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(TestPojo.class, mapper.getTypeIdResolver(), "_class", true, TestPojo.class);

        // When
        Object result = deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());

        // Then
        TestPojo[] testPojos = (TestPojo[]) result;
        assertEquals(2, testPojos.length);
        TestPojo testPojo1 = testPojos[0];
        TestPojo testPojo2 = testPojos[1];
        assertEquals("Test Pojo 1", testPojo1.getName());
        assertEquals("Test Pojo 2", testPojo2.getName());
    }

    @ org.junit.Test
    public void testDeserializeTypedFromScalar() throws IOException {
        // Given
        String jsonString = "{\"class\":\"com.fasterxml.jackson.databind.jsontype.impl.TestPojo\",\"name\":\"Test Pojo\"}";
        ObjectNode jsonNode = mapper.readValue(jsonString, ObjectNode.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(TestPojo.class, mapper.getTypeIdResolver(), "_class", true, TestPojo.class);

        // When
        String name = deserializer.getTypeIdResolver().resolveValueAs(mapper.getFactory().createParser(jsonString), null, String.class);

        // Then
        TestPojo testPojo = mapper.readValue(name, TestPojo.class);
        assertEquals("Test Pojo", testPojo.getName());
    }

    @ org.junit.Test
    public void testDeserializeTypedFromAny() throws IOException {
        // Given
        String jsonString = "{\"class\":\"com.fasterxml.jackson.databind.jsontype.impl.TestPojo\",\"name\":\"Test Pojo\"}";
        ObjectNode jsonNode = mapper.readValue(jsonString, ObjectNode.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(TestPojo.class, mapper.getTypeIdResolver(), "_class", true, TestPojo.class);

        // When
        Object result = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());

        // Then
        TestPojo testPojo = (TestPojo) result;
        assertEquals("Test Pojo", testPojo.getName());
    }
}