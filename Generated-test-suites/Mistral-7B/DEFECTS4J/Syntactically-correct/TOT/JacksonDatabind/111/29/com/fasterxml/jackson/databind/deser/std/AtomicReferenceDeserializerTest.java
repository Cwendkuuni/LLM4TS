package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testAtomicReferenceDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AtomicReference<String> expected = new AtomicReference<>("initialValue");
        TypeDeserializer typeDeser = mapper.getTypeDeserializers().findValueDeserializer(AtomicReference.class, String.class);
        JsonDeserializer<AtomicReference<String>> deserializer = mapper.findValueDeserializer(AtomicReference.class, String.class);

        AtomicReference<String> actual = mapper.readerForUpdating(new AtomicReference<String>()).readValue(mapper.createParser("{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\"}"));

        assertEquals(expected, actual);
        assertEquals("initialValue", expected.get());

        // testing update
        expected.set("updatedValue");
        actual = mapper.readerForUpdating(actual).readValue(mapper.createParser("{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\", \"value\":\"updatedValue\"}"));
        assertEquals(expected, actual);
        assertEquals("updatedValue", actual.get());

        // testing null value
        expected = new AtomicReference<String>(null);
        actual = mapper.readerForUpdating(expected).readValue(mapper.createParser("{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\", \"value\":null}"));
        assertNull(actual.get());

        // testing empty value
        expected = new AtomicReference<String>();
        actual = mapper.readerForUpdating(expected).readValue(mapper.createParser("{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\"}"));
        assertNull(actual.get());
    }

    @Test
    public void testAtomicReferenceDeserializationWithCustomTypeDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // create custom TypeDeserializer
        TypeDeserializerFactory typeDeserializerFactory = mapper.getTypeDeserializers().findValueDeserializerFactory(AtomicReference.class);
        TypeDeserializer customTypeDeser = typeDeserializerFactory.createValueDeserializer(null, new TypeSerializer.Empty(), new None());

        AtomicReference<Integer> expected = new AtomicReference<>(1);
        AtomicReference<Integer> actual = customTypeDeser.deserialize(mapper.getConfiguration(), null, mapper.createParser("{\"@type\":\"com.fasterxml.jackson.databind.deser.std.AtomicReference\", \"value\":1}"));

        assertEquals(expected, actual);
        assertEquals(1, expected.get());
    }

    @Test(expected = JsonMappingException.class)
    public void testAtomicReferenceDeserializationWithInvalidType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        AtomicReference<Integer> actual = mapper.readerForUpdating(new AtomicReference<Integer>()).readValue(mapper.createParser("{\"@type\":\"com.fasterxml.jackson.databind.deser.std.ListTypeDeserializer\"}"));
        fail("Expected JsonMappingException but got: " + actual);
    }
}