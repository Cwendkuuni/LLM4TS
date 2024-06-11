package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testDefaultInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        ValueInstantiator instantiator = mapper.findValueInstantiator(factory, String.class);

        // Test empty JSON
        String emptyJson = "{}";
        Object result = instantiator.createUsingDefault(mapper.getDeserializationContext().builder()
                .build(), factory.createParser(emptyJson));
        assertNull(result);

        // Test JSON with a valid String value
        String validJson = "{\"value\": \"test\"}";
        result = instantiator.createUsingDefault(mapper.getDeserializationContext().builder()
                .build(), factory.createParser(validJson));
        assertEquals("test", result);

        // Test JSON with an invalid value
        String invalidJson = "{\"value\": 42}";
        try {
            result = instantiator.createUsingDefault(mapper.getDeserializationContext().builder()
                    .build(), factory.createParser(invalidJson));
            fail("Expected JacksonException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        class MyClass {
            private int value;

            public MyClass(int value) {
                this.value = value;
            }

            @JsonCreator
            public MyClass(@JsonProperty("value") int value) {
                this.value = value;
            }
        }

        ValueInstantiator instantiator = new StdValueInstantiator.Builder(MyClass.class)
                .createUsingObjectWith(MyClass.class, new AnnotatedWithParams(MyClass.class, new AnnotatedParameter[]{}))
                .build();

        String jsonWithValidArgs = "{\"value\": 123}";
        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext().builder()
                .build(), new Object[] {123});
        MyClass expected = new MyClass(123);
        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected.value, ((MyClass) result).value);

        String jsonWithMissingArgs = "{\"value\": 123, \"invalid\": 42}";
        try {
            result = instantiator.createFromObjectWith(mapper.getDeserializationContext().builder()
                    .build(), new Object[] {123, 42});
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        String jsonWithInvalidType = "{\"invalid\": 123}";
        try {
            result = instantiator.createFromObjectWith(mapper.getDeserializationContext().builder()
                    .build(), new Object[] {123});
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        ValueInstantiator instantiator = mapper.findValueInstantiator(factory, String.class);

        String jsonWithValidString = "{\"value\": \"test\"}";
        Object result = instantiator.createFromString(mapper.getDeserializationContext().builder()
                .build(), jsonWithValidString);
        assertEquals("test", result);

        String jsonWithInvalidString = "{\"value\": 123}";
        try {
            result = instantiator.createFromString(mapper.getDeserializationContext().builder()
                    .build(), jsonWithInvalidString);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }

        String jsonWithEmptyString = "{\"value\": \"\"}";
        Object expected = null;
        Object resultEmpty = instantiator.createFromString(mapper.getDeserializationContext().builder()
                .build(), jsonWithEmptyString);
        assertEquals(expected.getClass(), resultEmpty.getClass());
        assertEquals(expected, resultEmpty);
    }

    @Test
    public void testCreateFromInt() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        ValueInstantiator instantiator = mapper.findValueInstantiator(factory, Integer.class);

        String jsonWithValidInt = "{\"value\": 123}";
        Object result = instantiator.createFromInt(mapper.getDeserializationContext().builder()
                .build(), 123);
        assertEquals(123, result.hashCode());
        assertEquals(Integer.class, result.getClass());

        String jsonWithInvalidInt = "{\"value\": \"test\"}";
        try {
            result = instantiator.createFromInt(mapper.getDeserializationContext().builder()
                    .build(), 123);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromLong() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        ValueInstantiator instantiator = mapper.findValueInstantiator(factory, Long.class);

        String jsonWithValidLong = "{\"value\": 123}";
        Object result = instantiator.createFromLong(mapper.getDeserializationContext().builder()
                .build(), 123L);
        assertEquals(123L, result.hashCode());
        assertEquals(Long.class, result.getClass());

        String jsonWithInvalidLong = "{\"value\": \"test\"}";
        try {
            result = instantiator.createFromLong(mapper.getDeserializationContext().builder()
                    .build(), 123L);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        ValueInstantiator instantiator = mapper.findValueInstantiator(factory, Boolean.class);

        String jsonWithValidBoolean = "{\"value\": true}";
        Object result = instantiator.createFromBoolean(mapper.getDeserializationContext().builder()
                .build(), true);
        assertEquals(true, result);
        assertEquals(Boolean.class, result.getClass());

        String jsonWithInvalidBoolean = "{\"value\": \"test\"}";
        try {
            result = instantiator.createFromBoolean(mapper.getDeserializationContext().builder()
                    .build(), true);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }
}