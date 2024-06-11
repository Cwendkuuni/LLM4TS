package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for StdSerializer class")
public class StdSerializerTest {

    @Test
    @DisplayName("Test default constructor")
    public void testDefaultConstructor() {
        StdSerializer serializer = new StdSerializer<>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    @DisplayName("Test constructor with JavaType")
    public void testConstructorWithJavaType() {
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        StdSerializer serializer = new StdSerializer<>(javaType);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    @DisplayName("Test constructor with Class and dummy boolean")
    public void testConstructorWithClassAndDummyBoolean() {
        StdSerializer serializer = new StdSerializer<>(String.class, false);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    @DisplayName("Test serialize method")
    public void testSerializeMethod() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        String value = "Hello World";
        StringWriter writer = new StringWriter();
        JsonGenerator generator = mapper.getFactory().createGenerator(writer);
        serializer.serialize(value, generator, mapper.getSerializationConfig());
        assertEquals("\"Hello World\"", writer.toString());
    }

    @Test
    @DisplayName("Test getSchema method for string type")
    public void testGetSchemaForStringType() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        JsonNode schema = serializer.getSchema(mapper.getSerializationConfig(), String.class);
        assertEquals("{\"type\":\"string\"}", schema.toString());
    }

    @Test
    @DisplayName("Test acceptJsonFormatVisitor method")
    public void testAcceptJsonFormatVisitorMethod() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getRootValueSchema(String.class);
        serializer.acceptJsonFormatVisitor(visitor, ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class));
        assertTrue(visitor.isAccepted(JsonFormatVisitorWrapper.ValueType.ANY));
    }

    @Test
    @DisplayName("Test wrapAndThrow method with JsonMappingException as exception")
    public void testWrapAndThrowWithJsonMappingException() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<?> serializer = mapper.findValueSerializer(String.class);
        Throwable t = new JsonMappingException("Test JsonMappingException");
        serializer.wrapAndThrow(mapper, t, null, null);
        assertTrue(t instanceof JsonMappingException);
    }

    @Test
    @DisplayName("Test wrapAndThrow method with IOException as exception")
    public void testWrapAndThrowWithIOException() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<?> serializer = mapper.findValueSerializer(String.class);
        Throwable t = new IOException("Test IOException");
        serializer.wrapAndThrow(mapper, t, null, null);
        assertTrue(t instanceof IOException);
    }

    @Test
    @DisplayName("Test isDefaultSerializer method")
    public void testIsDefaultSerializerMethod() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        assertTrue(StdSerializerTest.class.getPackage().getName() + ".StdSerializer".equals(serializer.getClass().getName()));
        JsonSerializer<Object> serializerForObject = ObjectMapper.getDefaultInstance().findValueSerializer(Object.class);
        assertFalse(StdSerializerTest.class.getPackage().getName() + ".StdSerializer".equals(serializerForObject.getClass().getName()));
    }

    @Test
    @DisplayName("Test findConvertingContentSerializer method")
    public void testFindConvertingContentSerializerMethod() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<List<String>> serializer = mapper.findValueSerializer(List.class);
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        JsonSerializer<Object> defaultSerializer = mapper.findValueSerializer(Object.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("values", java.util.Arrays.asList("Test1", "Test2"));
        StdSerializer<List<String>> newSerializer = serializer.findConvertingContentSerializer(mapper, null, node);
        assertEquals(StringSerializer.class, newSerializer.getClass());
        assertEquals(stringSerializer, newSerializer.getDelegate());
        assertEquals(defaultSerializer, newSerializer.getDelegate().getDelegate());
    }

    @Test
    @DisplayName("Test findPropertyFilter method")
    public void testFindPropertyFilterMethod() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<String> serializer = mapper.findValueSerializer(String.class);
        PropertyFilter propertyFilter = serializer.findPropertyFilter(mapper.getFilterProvider(), "filterId", null);
        assertNull(propertyFilter);
    }
}