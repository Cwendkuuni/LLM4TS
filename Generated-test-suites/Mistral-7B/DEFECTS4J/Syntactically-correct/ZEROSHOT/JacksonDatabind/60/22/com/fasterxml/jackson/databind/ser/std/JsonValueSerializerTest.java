package com.example.json; // replace with your test package name

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test with explicit value serializer
        JsonValueSerializer serializer1 = new JsonValueSerializer(
                AnnotatedMethod.construct(SomeClass.class, "getValueMethod1"), null);
        JsonSerializer<String> valueSerializer1 = new StringSerializer();
        JsonValueSerializer contextualSerializer1 = serializer1.withResolved(null, valueSerializer1, false);
        Assertions.assertEquals("expectedValue1", contextualSerializer1.serializeValue("input1", new JsonGenerator(new ByteArrayOutputStream()), new ObjectMapper().getSerializationConfig()));

        // Test with default value serializer
        JsonValueSerializer serializer2 = new JsonValueSerializer(
                AnnotatedMethod.construct(SomeClass.class, "getValueMethod2"), null);
        JsonValueSerializer contextualSerializer2 = serializer2;
        Assertions.assertEquals("expectedValue2", contextualSerializer2.serializeValue("input2", new JsonGenerator(new ByteArrayOutputStream()), new ObjectMapper().getSerializationConfig()));

        // Test with custom serializer for returned type
        JsonValueSerializer serializer3 = new JsonValueSerializer(
                AnnotatedMethod.construct(SomeClass.class, "getValueMethod3"), new MyCustomSerializer());
        JsonValueSerializer contextualSerializer3 = serializer3.withResolved(null, null, true);
        Assertions.assertEquals("expectedValue3", contextualSerializer3.serializeValue("input3", new JsonGenerator(new ByteArrayOutputStream()), new ObjectMapper().getSerializationConfig()));

        // Test with natural type
        JsonValueSerializer serializer4 = new JsonValueSerializer(
                AnnotatedMethod.construct(SomeClass.class, "getValueMethod4"), null);
        JsonValueSerializer contextualSerializer4 = serializer4;
        Assertions.assertEquals("3", contextualSerializer4.serializeValue(3, new JsonGenerator(new ByteArrayOutputStream()), new ObjectMapper().getSerializationConfig()));

        // Test with null value
        JsonValueSerializer serializer5 = new JsonValueSerializer(
                AnnotatedMethod.construct(SomeClass.class, "getValueMethod5"), null);
        JsonValueSerializer contextualSerializer5 = serializer5;
        Assertions.assertNull(contextualSerializer5.serializeValue(null, new JsonGenerator(new ByteArrayOutputStream()), new ObjectMapper().getSerializationConfig()));

        // Test jsonFormatVisitor method
        JsonFormatVisitorWrapper factory = mapper.schemaFactory().getJsonFormatVisitor(Serializers.Findable.class, ObjectMapper.DefaultTyping.JAVA);
        JsonValueSerializer serializer6 = new JsonValueSerializer(
                AnnotatedMethod.construct(SomeClass.class, "getValueMethod6"), null);
        serializer6.acceptJsonFormatVisitor(factory, Object.class.getType());

        // Test getSchema method
        ObjectMapper objectMapper = new ObjectMapper();
        JsonValueSerializer serializer7 = new JsonValueSerializer(
                AnnotatedMethod.construct(SomeClass.class, "getValueMethod7"), null);
        JsonNode schemaNode = serializer7.getSchema(objectMapper.getSerializationConfig(), Object.class);
        Assertions.assertNotNull(schemaNode);
    }

    static class SomeClass {
        @JsonValue
        public String getValueMethod1() {
            return "expectedValue1";
        }

        @JsonValue
        public int getValueMethod2() {
            return 2;
        }

        @JsonValue
        public String getValueMethod3() {
            return "expectedValue3";
        }

        public int getValueMethod4() {
            return 3;
        }

        @JsonValue
        public String getValueMethod5() {
            return null;
        }

        public String getValueMethod6() {
            return "expectedValue6";
        }

        public Integer getValueMethod7() {
            return 7;
        }
    }

    static class MyCustomSerializer extends JsonSerializer<Integer> {
        @Override
        public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStringValue("customValue");
        }
    }

    static class StringSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStringValue(value);
        }
    }
}