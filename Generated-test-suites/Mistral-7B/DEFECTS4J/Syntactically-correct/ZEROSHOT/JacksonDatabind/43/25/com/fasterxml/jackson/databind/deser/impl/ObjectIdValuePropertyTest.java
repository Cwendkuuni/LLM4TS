package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
    private static final PropertyName propertyName = PropertyName.construct("objectId");
    private static final JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
    private static final TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
    private static final JsonDeserializer<Object> idPropertyDeserializer = mock(JsonDeserializer.class);

    @SuppressWarnings("unchecked")
    private static final ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(
            objectIdReader, propertyName, null, valueDeserializer, typeDeserializer, idPropertyDeserializer);

    @JsonDeserialize(using = ObjectIdValueProperty.class)
    static class TestObject {
        private Object id;

        public Object getId() {
            return id;
        }

        @JsonDeserialize(using = IdDeserializer.class)
        public void setId(Object id) {
            this.id = id;
        }
    }

    static class IdDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }
    }

    @org.junit.Test
    public void testGetAnnotation() {
        assertEquals(null, objectIdValueProperty.getAnnotation(JsonDeserialize.class));
    }

    @org.junit.Test
    public void testGetMember() {
        assertEquals(null, objectIdValueProperty.getMember());
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(1L);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));

        Object testInstance = new TestObject();

        objectIdValueProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), testInstance);

        verify(objectIdReader.generator).generateIdInternal(eq(1L), any(Object.class));
        verify(objectIdReader.resolver).resolveId(eq(1L), any(Object.class), eq(testInstance.getClass()));
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(1L);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));

        Object testInstance = new TestObject();

        Object result = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), testInstance);

        verify(objectIdReader.generator).generateIdInternal(eq(1L), eq(testInstance));
        verify(objectIdReader.resolver).resolveId(eq(1L), any(Object.class), eq(TestObject.class));
        assertEquals(testInstance, result);
    }

    @org.junit.Test
    public void testSet() throws IOException {
        TestObject testInstance = new TestObject();
        testInstance.setId(1L);

        objectIdValueProperty.set(testInstance, 2L);

        verify(objectIdReader.idProperty).set(testInstance, eq(2L));
    }

    @org.junit.Test
    public void testSetAndReturn() throws IOException {
        TestObject testInstance = new TestObject();
        testInstance.setId(1L);

        Object result = objectIdValueProperty.setAndReturn(testInstance, 2L);

        verify(objectIdReader.idProperty).set(testInstance, eq(2L));
        assertEquals(testInstance, result);
    }
}