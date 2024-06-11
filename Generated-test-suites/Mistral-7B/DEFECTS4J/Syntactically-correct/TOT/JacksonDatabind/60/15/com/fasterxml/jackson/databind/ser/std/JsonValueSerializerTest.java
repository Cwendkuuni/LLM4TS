package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.core.JsonTest.*;

/**
 * Test cases for {@link JsonValueSerializer} class.
 */
public class JsonValueSerializerTest {

    @Test
    public void testSimpleValueSerialization() throws Exception {
        JsonMapper mapper = new JsonMapper();

        // Given
        PropertyName propertyName = new PropertyName("testProp");
        String expectedJson = "{\"testProp\":\"TestValue\"}";
        JsonValueSerializer serializer = new JsonValueSerializer(
                JsonTest.testAnnotatedMethod(JsonValueSerializerTest.class, "testgetValue"),
                propertyName, mapper.getSerializerProvider().findValueSerializer(String.class), false);

        // When
        String resultJson = mapper.writer().writeValueAsString(new ObjectTestData(42));

        // Then
        Assert.assertEquals(expectedJson, resultJson);

        // And when we test serialization with type information
        JsonNode resultNode = mapper.writerWithTypeInfo(null).writeValueAsTree(new ObjectTestData(42));
        JsonNode expectedNode = mapper.readTree(expectedJson);
        Assert.assertEquals(expectedNode, resultNode);
    }

    @Test
    public void testNullValueSerialization() throws Exception {
        JsonMapper mapper = new JsonMapper();

        // Given
        PropertyName propertyName = new PropertyName("testProp");
        String expectedJson = "null";
        JsonValueSerializer serializer = new JsonValueSerializer(
                JsonTest.testAnnotatedMethod(JsonValueSerializerTest.class, "testgetValueNull"),
                propertyName, mapper.getSerializerProvider().findValueSerializer(String.class), false);

        // When
        String resultJson = mapper.writer().writeValueAsString(new ObjectTestDataWithNullValue());

        // Then
        Assert.assertEquals(expectedJson, resultJson);

        // And when we test serialization with type information
        JsonNode resultNode = mapper.writerWithTypeInfo(null).writeValueAsTree(new ObjectTestDataWithNullValue());
        JsonNode expectedNode = mapper.readTree("null");
        Assert.assertEquals(expectedNode, resultNode);
    }

    @Test
    public void testExceptionDuringSerialization() throws Exception {
        JsonMapper mapper = new JsonMapper();

        // Given
        PropertyName propertyName = new PropertyName("testProp");
        String expectedJson = "{\"testProp\":\"TestValue\"}";
        JsonValueSerializer serializer = new JsonValueSerializer(
                JsonTest.testAnnotatedMethod(JsonValueSerializerTest.class, "testgetValueThrowingException"),
                propertyName, mapper.getSerializerProvider().findValueSerializer(String.class), false);

        // When
        try {
            String resultJson = mapper.writer().writeValueAsString(new ObjectTestDataThrowingException());
            Assert.fail("Expected serialization exception");
        } catch (JsonProcessingException e) {
            // Then
            String actualJson = e.getOriginalMessage();
            Assert.assertEquals(expectedJson, actualJson);
        }

        // And when we test serialization with type information
        try {
            JsonNode resultNode = mapper.writerWithTypeInfo(null).writeValueAsTree(new ObjectTestDataThrowingException());
            Assert.fail("Expected serialization exception");
        } catch (JsonMappingException e) {
            JsonNode actualNode = e.asNode();
            Assert.assertEquals(expectedJson, actualNode.get("valueAsString"));
        }
    }

    @JsonValue
    private static String testgetValue(ObjectTestData data) {
        return data.getValue();
    }

    @JsonValue
    private static String testgetValueNull() {
        return null;
    }

    @JsonValue
    private static String testgetValueThrowingException() throws IOException {
        throw new IOException("Test Exception");
    }

    private static class ObjectTestData {
        private final String value;

        public ObjectTestData(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static class ObjectTestDataWithNullValue {
    }

    private static class ObjectTestDataThrowingException extends ObjectTestData {
        public ObjectTestDataThrowingException() throws IOException {
            super(null);
        }
    }
}