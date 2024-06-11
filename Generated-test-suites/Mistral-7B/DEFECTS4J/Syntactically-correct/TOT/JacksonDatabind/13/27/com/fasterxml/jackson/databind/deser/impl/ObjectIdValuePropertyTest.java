package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Testing for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

    ObjectMapper objectMapper;
    ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        ObjectNode configuration = objectMapper.getSerializationConfig();
        configuration.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectIdReader = new SimpleObjectIdReader(new PropertyName("id"));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("id", "1");

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        Object id = objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
        assertEquals("1", id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("id", "1");

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        Object id = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
        assertEquals("1", id);

        Object instance = objectIdValueProperty.getBean();
        Object actualId = objectIdValueProperty.getValue(instance);
        assertEquals("1", actualId);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = "1";
        Object instance = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        Object actualId = objectIdValueProperty.setAndReturn(instance, id);
        assertEquals("1", actualId);
        assertEquals("1", objectIdValueProperty.getValue(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        Object id = "1";
        Object instance = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        objectIdValueProperty.set(instance, null);
        objectIdValueProperty.set(instance, id);
    }

    @Test
    public void testDeserializeAndSetNullId() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        Object id = objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
        assertNull(id);
    }

    @Test
    public void testDeserializeSetAndReturnNullId() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        Object id = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
        assertNull(id);

        Object instance = objectIdValueProperty.getBean();
        assertNull(objectIdValueProperty.getValue(instance));
    }

    @Test
    public void testDeserializeAndSetEmptyJson() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        Object id = objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
        assertNull(id);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeSetAndReturnInvalidJson() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("invalid", "json");

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        Object id = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
    }

    @Test
    public void testDeserializeSetAndReturnBase64EncodedId() throws IOException {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("id", Base64Variants.STANDARD.encodeBase64String("1".getBytes()));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        Object id = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
        assertEquals("1", id);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeSetAndReturnNullObjectIdReader() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        Object id = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().createParser(jsonNode), objectMapper.getDeserializationContext(), new Object());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        assertNull(objectIdValueProperty.getAnnotation(JsonIdentityInfo.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STANDARD_PROPERTY_METADATA);
        assertNull(objectIdValueProperty.getMember());
    }
}