package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest {

    @SuppressWarnings("unchecked")
    private static <T> ObjectIdValueProperty<T> createObjectIdValueProperty(
            ObjectIdReader objectIdReader, PropertyMetadata metadata) {
        ObjectIdValueProperty<T> objectIdValueProperty = new ObjectIdValueProperty<>(
                objectIdReader, metadata);
        return objectIdValueProperty;
    }

    @Test
    public void testCreateObjectIdValueProperty() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        ObjectIdValueProperty<String> objectIdValueProperty = createObjectIdValueProperty(
                objectIdReader, metadata);
        assertEquals("propertyName", objectIdValueProperty.getName());
        assertEquals(String.class, objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testCreateObjectIdValuePropertyWithValueDeserializer() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty<String> objectIdValueProperty = createObjectIdValueProperty(
                objectIdReader, metadata, valueDeserializer);
        assertEquals("propertyName", objectIdValueProperty.getName());
        assertEquals(String.class, objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
        assertEquals(valueDeserializer, objectIdValueProperty._valueDeserializer);
    }

    @Test
    public void testCreateObjectIdValuePropertyWithName() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdValueProperty<String> objectIdValueProperty = createObjectIdValueProperty(
                objectIdReader, metadata, newName);
        assertEquals("newName", objectIdValueProperty.getName());
        assertEquals("propertyName", objectIdValueProperty.getOriginalName());
        assertEquals(String.class, objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty<String> objectIdValueProperty = createObjectIdValueProperty(
                Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));
        assertNull(objectIdValueProperty.getAnnotation(Mockito.mock(Annotation.class)));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty<String> objectIdValueProperty = createObjectIdValueProperty(
                Mockito.mock(ObjectIdReader.class), Mockito.mock(PropertyMetadata.class));
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        ObjectIdValueProperty<String> objectIdValueProperty = createObjectIdValueProperty(
                objectIdReader, metadata, valueDeserializer, nullValueProvider);
        ObjectParser objectParser = Mockito.mock(ObjectParser.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        Mockito.when(objectParser.getParser()).thenReturn(jsonParser);
        Mockito.when(jsonParser.getCurrentName()).thenReturn("propertyName");
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getValueAsString()).thenReturn("test-id");
        Mockito.when(deserializationContext.createObjectId(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(deserializationContext.findObjectId(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Mockito.mock(ReadableObjectId.class));

        objectIdValueProperty.deserializeAndSet(objectParser, deserializationContext, instance);

        // verify that ObjectId was correctly created and bound to instance
        Mockito.verify(deserializationContext).createObjectId(instance, objectIdReader.generator,
                objectIdReader.resolver);
        Mockito.verify(deserializationContext).findObjectId(ArgumentMatchers.eq("test-id"),
                ArgumentMatchers.eq(objectIdReader.generator), ArgumentMatchers.eq(objectIdReader.resolver));
        Mockito.verify(instance).setId(ArgumentMatchers.any());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        ObjectIdValueProperty<String> objectIdValueProperty = createObjectIdValueProperty(
                objectIdReader, metadata, valueDeserializer, nullValueProvider);
        ObjectParser objectParser = Mockito.mock(ObjectParser.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        Mockito.when(objectParser.getParser()).thenReturn(jsonParser);
        Mockito.when(jsonParser.getCurrentName()).thenReturn("propertyName");
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getValueAsString()).thenReturn("test-id");
        Mockito.when(deserializationContext.createObjectId(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(deserializationContext.findObjectId(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Mockito.mock(ReadableObjectId.class));
        Object id = objectIdValueProperty.deserializeSetAndReturn(objectParser, deserializationContext,
                instance);

        // verify that ObjectId was correctly created and bound to instance
        Mockito.verify(deserializationContext).createObjectId(instance, objectIdReader.generator,
                objectIdReader.resolver);
        Mockito.verify(deserializationContext).findObjectId(ArgumentMatchers.eq("test-id"),
                ArgumentMatchers.eq(objectIdReader.generator), ArgumentMatchers.eq(objectIdReader.resolver));
        Mockito.verify(instance).setId(ArgumentMatchers.any());
        assertEquals("test-id", id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        ObjectIdValueProperty<String> objectIdValueProperty = createObjectIdValueProperty(
                objectIdReader, metadata, valueDeserializer, nullValueProvider);
        ObjectParser objectParser = Mockito.mock(ObjectParser.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        Object id = Mockito.mock(Object.class);

        Mockito.when(objectParser.getParser()).thenReturn(jsonParser);
        Mockito.when(jsonParser.getCurrentName()).thenReturn("propertyName");
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getValueAsString()).thenReturn("test-id");
        Mockito.when(deserializationContext.createObjectId(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(deserializationContext.findObjectId(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(instance.setId(ArgumentMatchers.any()))
                .thenReturn(Mockito.mock(Object.class));

        Object result = objectIdValueProperty.setAndReturn(instance, id);

        // verify that ObjectId was correctly created and bound to instance
        Mockito.verify(deserializationContext).createObjectId(instance, objectIdReader.generator,
                objectIdReader.resolver);
        Mockito.verify(deserializationContext).findObjectId(ArgumentMatchers.eq("test-id"),
                ArgumentMatchers.eq(objectIdReader.generator), ArgumentMatchers.eq(objectIdReader.resolver));
        Mockito.verify(instance).setId(ArgumentMatchers.any());
        assertEquals("test-id", id);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = Mockito.mock(PropertyMetadata.class);
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        ObjectIdValueProperty<String> objectIdValueProperty = createObjectIdValueProperty(
                objectIdReader, metadata, valueDeserializer, nullValueProvider);
        ObjectParser objectParser = Mockito.mock(ObjectParser.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        Object id = Mockito.mock(Object.class);

        Mockito.when(objectParser.getParser()).thenReturn(jsonParser);
        Mockito.when(jsonParser.getCurrentName()).thenReturn("propertyName");
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getValueAsString()).thenReturn("test-id");
        Mockito.when(deserializationContext.createObjectId(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(deserializationContext.findObjectId(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(instance.setId(ArgumentMatchers.any()))
                .thenReturn(Mockito.mock(Object.class));

        objectIdValueProperty.set(instance, id);

        // verify that ObjectId was correctly created and bound to instance
        Mockito.verify(deserializationContext).createObjectId(instance, objectIdReader.generator,
                objectIdReader.resolver);
        Mockito.verify(deserializationContext).findObjectId(ArgumentMatchers.eq("test-id"),
                ArgumentMatchers.eq(objectIdReader.generator), ArgumentMatchers.eq(objectIdReader.resolver));
        Mockito.verify(instance).setId(ArgumentMatchers.any());
    }

    /**
     * Mock ObjectIdReader for testing.
     */
    static class TestObjectIdReader extends ObjectIdReader {

        public TestObjectIdReader() {
            super("testPropertyName", String.class, null, null);
        }

        @Override
        public ObjectIdReader.IdType getIdType() {
            return ObjectIdReader.IdType.INTERNAL;
        }

        @Override
        public JsonDeserializer<ObjectId> getIdResolver() {
            return null;
        }

        @Override
        public ObjectIdGenerator getIdGenerator() {
            return null;
        }

        @Override
        public PropertyName getIdProperty() {
            return null;
        }

        @Override
        public TypeDeserializer getIdTypeDeserializer() {
            return null;
        }
    }
}