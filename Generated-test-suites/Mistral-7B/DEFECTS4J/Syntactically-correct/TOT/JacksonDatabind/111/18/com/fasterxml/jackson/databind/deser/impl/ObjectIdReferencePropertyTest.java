package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdReferencePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectReader objectReader;
    private DeserializationContext deserializationContext;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectReader = objectMapper.readerForClass(TestPojo.class);
        deserializationContext = Mockito.mock(DeserializationContext.class);
    }

    @Test
    public void testSimpleDeserialization() throws JsonProcessingException {
        TestPojo testPojo = new TestPojo();
        ObjectNode jsonNode = Mockito.mock(ObjectNode.class);

        when(jsonNode.has("id")).thenReturn(true);
        when(jsonNode.get("id")).thenReturn(Mockito.anyString());

        JsonDeserializer<TestPojo> jsonDeserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, jsonDeserializer, null);

        TestPojo deserializedObject = objectIdReferenceProperty.deserializeSetAndReturn(Mockito.mock(JsonParser.class), deserializationContext, testPojo);
        assertEquals(deserializedObject, testPojo);
    }

    @Test
    public void testUnresolvedForwardReference() throws JsonProcessingException {
        TestPojo testPojo = new TestPojo();
        ObjectNode jsonNode = Mockito.mock(ObjectNode.class);

        when(jsonNode.has("forwardReference")).thenReturn(true);
        when(jsonNode.get("forwardReference")).thenReturn(Mockito.anyString());

        JsonDeserializer<TestPojo> jsonDeserializer = mock(JsonDeserializer.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, jsonDeserializer, null);

        UnresolvedForwardReference unresolvedForwardReference = new UnresolvedForwardReference("forwardReference", null, null, null);
        objectIdReferenceProperty.deserializeSetAndReturn(Mockito.mock(JsonParser.class), deserializationContext, testPojo);

        assertEquals(null, testPojo.getForwardReference());
        assertEquals(unresolvedForwardReference, objectIdReferenceProperty.getUnresolvedForwardReference());
    }

    @Test
    public void testSettingValue() throws JsonProcessingException {
        TestPojo testPojo1 = new TestPojo();
        TestPojo testPojo2 = new TestPojo();
        TestPojo deserializedObject = objectIdReferenceProperty.deserializeSetAndReturn(Mockito.mock(JsonParser.class), deserializationContext, testPojo1);

        when(deserializationContext.findBeanProperty("forwardReference", TestPojo.class)).thenReturn(Mockito.mock(SettableBeanProperty.class));

        objectIdReferenceProperty.set(testPojo2, deserializedObject);
        assertEquals(deserializedObject, testPojo2.getForwardReference());
    }

    @Test
    public void testHandlingResolvedForwardReference() {
        TestPojo testPojo1 = new TestPojo();
        TestPojo testPojo2 = new TestPojo();
        TestPojo deserializedObject = new TestPojo();
        deserializedObject.setId(1L);

        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, null);
        PropertyReferring referring = new PropertyReferring(objectIdReferenceProperty, null, TestPojo.class, testPojo1);

        objectIdReferenceProperty.handleResolvedForwardReference(1L, deserializedObject);
        assertEquals(deserializedObject, testPojo1.getForwardReference());
    }

    @JsonDeserialize(using = StdSerializer.class)
    static class TestPojo {
        private Long id;
        private TestPojo forwardReference;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public TestPojo getForwardReference() {
            return forwardReference;
        }

        public void setForwardReference(TestPojo forwardReference) {
            this.forwardReference = forwardReference;
        }
    }
}