package com.example.json; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    @JsonSerialize(using = JsonValueSerializer.class)
    static class TestClass {
        private int intValue;
        private boolean booleanValue;
        private String stringValue;

        @JsonValue
        public int getIntValue() {
            return intValue;
        }

        @JsonValue
        public boolean getBooleanValue() {
            return booleanValue;
        }

        @JsonValue
        public String getStringValue() {
            return stringValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public void setBooleanValue(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
    }

    @org.junit.Test
    public void testJsonValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization of primitive types
        TestClass testObj = new TestClass();
        testObj.setIntValue(1);
        testObj.setBooleanValue(true);
        testObj.setStringValue("test");

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        mapper.writeValue(gen, testObj);

        gen.flush();
        gen.close();

        // Test deserialization of primitive types
        TestClass deserializedObj = mapper.readValue(System.in, TestClass.class);
        assertEquals(1, deserializedObj.getIntValue());
        assertTrue(deserializedObj.getBooleanValue());
        assertEquals("test", deserializedObj.getStringValue());

        // Test JSON schema validation
        ObjectNode schemaNode = mapper.readValue(getClass().getResource("/test-schema.json"), ObjectNode.class);
        JsonSchema schema = new JsonSchema(schemaNode);
        ObjectNode actualNode = mapper.readValue(new ByteArrayInputStream(mapper.writeValueAsBytes(testObj)), ObjectNode.class);
        assertTrue(schema.isValid(actualNode));

        // Test serialization of String type
        TestClass stringTestObj = new TestClass();
        stringTestObj.setStringValue("test");
        mapper.writeValue(System.out, stringTestObj);
        String stringSerializedValue = mapper.readValue(System.in, String.class);
        assertEquals("test", stringSerializedValue);

        // Test JSON schema validation for String type
        ObjectNode stringSchemaNode = mapper.readValue(getClass().getResource("/test-string-schema.json"), ObjectNode.class);
        JsonSchema stringSchema = new JsonSchema(stringSchemaNode);
        ObjectNode stringActualNode = mapper.readValue(new ByteArrayInputStream(mapper.writeValueAsBytes(stringTestObj)), ObjectNode.class);
        assertTrue(stringSchema.isValid(stringActualNode));

        // Test serialization of custom class with @JsonValue annotation
        TestCustomClass customTestObj = new TestCustomClass();
        mapper.writeValue(System.out, customTestObj);
        String customSerializedValue = mapper.readValue(System.in, String.class);
        assertEquals("CUSTOM_VALUE", customSerializedValue);

        // Test JSON schema validation for custom class
        ObjectNode customSchemaNode = mapper.readValue(getClass().getResource("/test-custom-schema.json"), ObjectNode.class);
        JsonSchema customSchema = new JsonSchema(customSchemaNode);
        ObjectNode customActualNode = mapper.readValue(new ByteArrayInputStream(mapper.writeValueAsBytes(customTestObj)), ObjectNode.class);
        assertTrue(customSchema.isValid(customActualNode));
    }

    static class TestCustomClass {
        @JsonValue
        public String getCustomValue() {
            return "CUSTOM_VALUE";
        }
    }
}