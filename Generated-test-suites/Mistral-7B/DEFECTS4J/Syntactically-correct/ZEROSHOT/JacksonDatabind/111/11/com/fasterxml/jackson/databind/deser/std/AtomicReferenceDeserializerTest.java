package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapperFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AtomicReferenceDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper(new JsonMapperFactoryBuilder()
                .build());
    }

    @Test
    public void testEmptyConstruction() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class),
                null, new EmptyTypeDeserializer(), null);
        assertNotNull(deserializer);
    }

    @Test
    public void testWithDeserializers() {
        JavaType fullType = TypeFactory.defaultInstance.constructType(AtomicReference.class, String.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, null, null,
                new JsonDeserializer<String>() {});
        assertNotNull(deserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class),
                null, null, null);
        AtomicReference<Object> nullValue = deserializer.getNullValue(null);
        assertTrue(nullValue instanceof AtomicReference);
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class),
                null, null, null);
        AtomicReference<Object> emptyValue = deserializer.getEmptyValue(null);
        assertTrue(emptyValue instanceof AtomicReference);
        assertNull(emptyValue.get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = "test";
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class),
                null, null, null);
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);
        assertTrue(referenceValue instanceof AtomicReference);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() throws JsonMappingException {
        AtomicReference<Object> reference = new AtomicReference<Object>("initial value");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class),
                null, null, null);
        Object referenced = deserializer.getReferenced(reference);
        assertTrue(referenced instanceof String);
        assertEquals("initial value", referenced);
    }

    @Test
    public void testUpdateReference() throws JsonMappingException {
        AtomicReference<Object> reference = new AtomicReference<Object>("initial value");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class),
                null, null, null);
        deserializer.updateReference(reference, "updated value");
        assertEquals("updated value", reference.get());
    }

    @Test
    public void testSupportsUpdate() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class),
                null, null, null);
        assertTrue(deserializer.supportsUpdate(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFullType() {
        new AtomicReferenceDeserializer(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValueInstantiator() {
        new AtomicReferenceDeserializer(TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeDeserializer() {
        new AtomicReferenceDeserializer(TypeFactory.defaultInstance.constructType(AtomicReference.class),
                null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDeserializer() {
        new AtomicReferenceDeserializer(TypeFactory.defaultInstance.constructType(AtomicReference.class), null, null, null);
    }

    @Test
    public void testDeserialize() throws IOException, JsonMappingException {
        String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\",\"value\":1}";
        AtomicReference<Number> reference = objectMapper.readValue(json, AtomicReference.class);
        assertEquals(1, reference.get());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJson() throws IOException {
        String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\",\"invalid\":\"json\"}";
        objectMapper.readValue(json, AtomicReference.class);
    }
}