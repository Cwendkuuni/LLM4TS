package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    @Test
    public void testStdSerializer() {
        // Test default constructor
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, stringSerializer.handledType());

        // Test constructor with JavaType
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        StdSerializer<String> stringSerializer2 = new StdSerializer<String>(javaType);
        assertEquals(String.class, stringSerializer2.handledType());

        // Test constructor with Class and boolean
        Class<String> stringClass = String.class;
        StdSerializer<String> stringSerializer3 = new StdSerializer<String>(stringClass, false);
        assertEquals(String.class, stringSerializer3.handledType());

        // Test serialization
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        String value = "test";
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
            stringSerializer.serialize(value, jsonGenerator, new SerializerProvider());
            String result = jsonGenerator.getOutputBuffer().toString();
            assertEquals("\"test\"", result);
        } catch (JsonProcessingException e) {
            fail("Serialization failed: " + e.getMessage());
        } finally {
            try {
                jsonGenerator.close();
            } catch (IOException e) {
                // Ignore
            }
        }

        // Test getSchema method
        try {
            ObjectNode schema = stringSerializer.getSchema(new SerializerProvider(), String.class);
            assertEquals("{\"type\":\"string\"}", schema.toString());
        } catch (JsonMappingException e) {
            fail("getSchema failed: " + e.getMessage());
        }

        // Test acceptJsonFormatVisitor method
        try {
            JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getFactory());
            stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
            assertTrue(visitor.getFormatter() instanceof JsonStringFormatter.Instance);
        } catch (JsonMappingException e) {
            fail("acceptJsonFormatVisitor failed: " + e.getMessage());
        }

        // Test isDefaultSerializer method
        JsonSerializer<Object> defaultSerializer = ObjectMapper.getDefaultInstance().findValueSerializer(Object.class);
        assertTrue(StdSerializer.class.isInstance(defaultSerializer));
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(StdSerializer.class.isInstance(stringSerializer2));
        assertTrue(StdSerializer.class.isInstance(stringSerializer3));
        assertFalse(ListSerializer.class.isInstance(stringSerializer));

        // Test findConvertingContentSerializer method
        ObjectMapper objectMapper2 = new ObjectMapper();
        BeanProperty property = new BeanProperty("testProperty", String[].class);
        JsonSerializer<String[]> existingSerializer = objectMapper2.findValueSerializer(String[].class);
        StdSerializer<String[]> stringArraySerializer = new StdSerializer<String[]>(String[].class);
        StdSerializer<String[]> stringArraySerializer2 = new StdSerializer<String[]>(JavaType.constructType(String[].class));
        StdSerializer<String[]> stringArraySerializer3 = new StdSerializer<String[]>(String[].class, false);
        assertEquals(String[].class, stringArraySerializer.handledType());
        assertEquals(String[].class, stringArraySerializer2.handledType());
        assertEquals(String[].class, stringArraySerializer3.handledType());
        JsonSerializer<Object> convertingSerializer = new StdArraySerializer<Object>(Object[].class);
        SerializerProvider provider = new SerializerProvider();
        JsonSerializer<?> resultSerializer = null;
        try {
            resultSerializer = stringArraySerializer.findConvertingContentSerializer(provider, property, existingSerializer);
            assertTrue(resultSerializer instanceof StdDelegatingSerializer);
            StdDelegatingSerializer delegatingSerializer = (StdDelegatingSerializer) resultSerializer;
            assertEquals(convertingSerializer, delegatingSerializer.getDelegatingSerializer());
            assertEquals(String[].class, delegatingSerializer.getDelegateType());
            assertEquals(existingSerializer, delegatingSerializer.getContentSerializer());
        } catch (JsonMappingException e) {
            fail("findConvertingContentSerializer failed: " + e.getMessage());
        }

        // Test findPropertyFilter method
        try {
            Object filterId = "testFilter";
            Object valueToFilter = new Object();
            PropertyFilter filter = new PropertyFilter() {
                @Override
                public void serializeAsField(ObjectFilterProvider provider, SerializerProvider p, Object bean, JsonGenerator g) throws IOException {
                    fail("Should not be called");
                }
                @Override
                public void serializeAsProperty(ObjectFilterProvider provider, SerializerProvider p, Object bean, ObjectNode node, JsonGenerator g) throws IOException {
                    node.put("test", "testValue");
                }
            };
            SerializerProvider provider2 = new SerializerProvider();
            provider2.setFilterProvider(new SimpleFilterProvider());
            provider2.getFilterProvider().setFilterID("testFilter", filter);
            PropertyFilter resultFilter = stringSerializer.findPropertyFilter(provider2, filterId, valueToFilter);
            assertEquals(filter, resultFilter);
        } catch (JsonMappingException e) {
            fail("findPropertyFilter failed: " + e.getMessage());
        }
    }
}