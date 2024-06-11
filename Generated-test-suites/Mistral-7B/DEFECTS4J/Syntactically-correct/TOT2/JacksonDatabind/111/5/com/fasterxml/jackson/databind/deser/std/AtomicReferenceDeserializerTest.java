package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TestDeserializationContext;

public class AtomicReferenceDeserializerTest extends BaseTest {

    @Test
    public void testGetNullValue() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new TestDeserializationContext();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
        assertNull(deserializer.getNullValue(ctxt));
    }

    @Test
    public void testGetEmptyValue() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new TestDeserializationContext();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        JsonNode emptyValue = deserializer.getEmptyValue(ctxt);
        assertNotNull(emptyValue);
    }

    @Test
    public void testReferenceValue() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new TestDeserializationContext();
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "test");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> atomicReference = deserializer.referenceValue(node.get("value"));
        assertEquals("test", atomicReference.get());
    }

    @Test
    public void testGetReferenced() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new TestDeserializationContext();
        AtomicReference<Object> atomicReference = new AtomicReference<Object>("test");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object referenceValue = deserializer.getReferenced(atomicReference);
        assertEquals("test", referenceValue);
    }

    @Test
    public void testUpdateReference() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new TestDeserializationContext();
        AtomicReference<Object> atomicReference = new AtomicReference<Object>("test");
        Object updatedValue = "newTest";
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> updatedAtomicReference = deserializer.updateReference(atomicReference, updatedValue);
        assertEquals(updatedValue, updatedAtomicReference.get());
    }

    @Test
    public void testExceptionHandling() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new TestDeserializationContext();
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "invalidType");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        assertThrows(JsonMappingException.class, () -> deserializer.referenceValue(node.get("value")));
    }
}