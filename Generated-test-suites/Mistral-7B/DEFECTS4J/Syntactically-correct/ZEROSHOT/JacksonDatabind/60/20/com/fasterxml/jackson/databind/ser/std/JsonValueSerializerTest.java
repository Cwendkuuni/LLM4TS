package com.example.json; // replace with your test package name if necessary

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonValueSerializerTest")
public class JsonValueSerializerTest {

    private static final Object TEST_OBJECT = new TestClass("test");

    private static final Class<TestClass> TEST_CLASS = TestClass.class;
    private static final Method TEST_METHOD = TestClass.class.getDeclaredMethod("getValue");

    @BeforeAll
    static void setupClass() {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS, null);
        assertEquals(JsonValueSerializer.class, serializer.getClass());
    }

    @Test
    @DisplayName("Test serialization with annotated method")
    void testSerializationWithAnnotatedMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = new SimpleStringSerializer(); // replace with the actual value serializer

        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS, null);
        JsonGenerator generator = mapper.getPolymorphicSerializer(JsonValueSerializer.class).createContextual(null, null);

        ObjectWriter objectWriter = mapper.writer(new TypeReference<ObjectWriter>() {});
        String json = objectWriter.writeValueAsString(serializer.withResolved(null, valueSerializer, false).withContextual(null, null, null, null, null).getValue(TEST_OBJECT));

        JsonNode expectedJson = mapper.readTree(getClass().getResource("/expected_json.json")); // replace with the expected JSON file
        assertEquals(expectedJson, JsonParseUtils.parseJson(json));
    }

    @Test
    @DisplayName("Test serialization with default serializer")
    void testSerializationWithDefaultSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS, null);
        JsonSerializer<?> defaultSerializer = serializer.createContextual(null, null).getDelegate();
        JsonGenerator generator = mapper.getPolymorphicSerializer(JsonValueSerializer.class).createContextual(null, null);

        ObjectWriter objectWriter = mapper.writer(new TypeReference<ObjectWriter>() {});
        String json = objectWriter.writeValueAsString(serializer.withResolved(null, defaultSerializer, false).withContextual(null, null, null, null, null).getValue(TEST_OBJECT));

        JsonNode expectedJson = mapper.readTree(getClass().getResource("/expected_json_default_serializer.json")); // replace with the expected JSON file
        assertEquals(expectedJson, JsonParseUtils.parseJson(json));
    }

    @Test
    @DisplayName("Test schema generation")
    void testSchemaGeneration() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS, null);
        JsonSchema schema = serializer.getSchema(mapper.getSerializerProvider(), null);

        JsonNode expectedSchema = mapper.readTree(getClass().getResource("/expected_schema.json")); // replace with the expected schema file
        assertEquals(expectedSchema, schema);
    }

    @Test
    @DisplayName("Test serialization with null value")
    void testSerializationWithNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS, null);

        JsonGenerator generator = mapper.getPolymorphicSerializer(JsonValueSerializer.class).createContextual(null, null);
        ObjectWriter objectWriter = mapper.writer(JsonSerializer.NullSerializer.instance);
        String json = objectWriter.writeValueAsString(serializer.withResolved(null, null, false).withContextual(null, null, null, null, null).getValue(TEST_OBJECT));

        assertNull(json);
    }

    @Test
    @DisplayName("Test serialization with exception")
    void testSerializationWithException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS, null);
        JsonSerializer<Object> valueSerializer = new ThrowingSerializer(); // replace with the actual throwing serializer

        JsonGenerator generator = mapper.getPolymorphicSerializer(JsonValueSerializer.class).createContextual(null, null);

        assertThrows(JsonMappingException.class, () -> {
            ObjectWriter objectWriter = mapper.writer(new TypeReference<ObjectWriter>() {});
            String json = objectWriter.writeValueAsString(serializer.withResolved(null, valueSerializer, false).withContextual(null, null, null, null, null).getValue(TEST_OBJECT));
        });
    }

    @Test
    @DisplayName("Test serialization with missing value method")
    void testSerializationWithMissingValueMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS_NO_VALUE_METHOD, null);
        assertEquals(null, serializer);
    }

    @Test
    @DisplayName("Test serialization with value method returning void")
    void testSerializationWithValueMethodReturningVoid() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS_VALUE_METHOD_VOID, null);
        assertEquals(null, serializer);
    }

    @Test
    @DisplayName("Test serialization with unsupported return type")
    void testSerializationWithUnsupportedReturnType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS_UNSUPPORTED_RETURN_TYPE, null);
        assertEquals(null, serializer);
    }

    @Test
    @DisplayName("Test serialization with non-public value method")
    void testSerializationWithNonPublicValueMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TEST_CLASS_NON_PUBLIC_VALUE_METHOD, null);
        assertEquals(null, serializer);
    }

    private static class TestClass {
        private final String value;

        public TestClass(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return "test_value";
        }
    }

    private static class TestClassNoValueMethod {
        private final String value;
    }

    private static class TestClassValueMethodVoid {
        private final String value;

        @JsonValue
        public void getValue() {
            // empty
        }
    }

    private static class TestClassUnsupportedReturnType {
        private final String value;

        @JsonValue
        public int getValue() {
            return 0;
        }
    }

    private static class TestClassNonPublicValueMethod {
        private final String value;

        @JsonValue
        private String getValue() {
            return "test_value";
        }
    }

    private static class SimpleStringSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value);
        }
    }

    private static class ThrowingSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            throw new RuntimeException("Test exception");
        }
    }
}

class JsonParseUtils {
    public static JsonNode parseJson(String json) throws IOException {
        return new ObjectMapper().readTree(json);
    }
}