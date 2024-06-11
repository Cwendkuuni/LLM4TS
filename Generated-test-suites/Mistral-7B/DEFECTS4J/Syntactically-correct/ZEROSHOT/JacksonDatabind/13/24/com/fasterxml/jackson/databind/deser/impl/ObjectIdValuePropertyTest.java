package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerProvider;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectIdGenerator;
import com.fasterxml.jackson.databind.node.ObjectIdResolver;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstruction() {
        String propertyName = "objectId";
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata, valueDeserializer);
        assertEquals(propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
        assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() {
        String propertyName = "objectId";
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty srcObjectIdValueProperty = new ObjectIdValueProperty(objectIdReader, "propertyName", valueDeserializer);
        ObjectIdValueProperty destObjectIdValueProperty = new ObjectIdValueProperty(srcObjectIdValueProperty, valueDeserializer);
        assertEquals(propertyName, srcObjectIdValueProperty.getName());
        assertEquals(propertyName, destObjectIdValueProperty.getName());
        assertEquals(objectIdReader, srcObjectIdValueProperty._objectIdReader);
        assertEquals(objectIdReader, destObjectIdValueProperty._objectIdReader);
        assertEquals(valueDeserializer, srcObjectIdValueProperty.getValueDeserializer());
        assertEquals(valueDeserializer, destObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyNameChange() {
        String srcPropertyName = "objectId";
        String destPropertyName = "newObjectId";
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty srcObjectIdValueProperty = new ObjectIdValueProperty(objectIdReader, srcPropertyName, valueDeserializer);
        ObjectIdValueProperty destObjectIdValueProperty = new ObjectIdValueProperty(srcObjectIdValueProperty, "newPropertyName");
        assertEquals(srcPropertyName, srcObjectIdValueProperty.getName());
        assertEquals(destPropertyName, destObjectIdValueProperty.getName());
    }

    @Test
    public void testObjectIdValuePropertyValueDeserializerChange() {
        String propertyName = "objectId";
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<?> srcValueDeserializer = Mockito.mock(JsonDeserializer.class);
        JsonDeserializer<?> destValueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty srcObjectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyName, srcValueDeserializer);
        ObjectIdValueProperty destObjectIdValueProperty = new ObjectIdValueProperty(srcObjectIdValueProperty, destValueDeserializer);
        assertEquals(srcValueDeserializer, srcObjectIdValueProperty.getValueDeserializer());
        assertEquals(destValueDeserializer, destObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, "objectId", Mockito.mock(JsonDeserializer.class));
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, "objectId", Mockito.mock(JsonDeserializer.class));
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String propertyName = "objectId";
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyName, valueDeserializer);

        Mockito.doAnswer(invocation -> {
            Object deserializedObject = invocation.getArgument(0).deserialize(jsonParser, ctxt);
            ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
            ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
            ReadableObjectId readableObjectId = Mockito.mock(ReadableObjectId.class);
            Mockito.doReturn(readableObjectId).when(ctxt).findObjectId(eq(deserializedObject), eq(objectIdGenerator), eq(objectIdResolver));
            return readableObjectId;
        }).when(objectIdReader).generateId(jsonParser, ctxt);

        Object id = new Object();
        Mockito.doReturn(id).when(valueDeserializer).deserialize(jsonParser, ctxt);

        Object result = objectIdValueProperty.deserializeAndSet(jsonParser, ctxt, instance);
        assertEquals(instance, result);

        ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
        Mockito.doReturn(objectIdResolver).when(ctxt).findObjectIdResolver();
        Mockito.doReturn(objectIdGenerator).when(ctx -> ctx.findObjectIdGenerator(eq(ObjectIdGenerator.class), eq(JsonParser.class), any()));

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.doReturn(idProperty).when(objectIdReader).getIdProperty();
        Mockito.doNothing().when(idProperty).setAndReturn(instance, id);

        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String propertyName = "objectId";
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyName, valueDeserializer);

        Mockito.doAnswer(invocation -> {
            Object deserializedObject = invocation.getArgument(0).deserialize(jsonParser, ctxt);
            ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
            ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
            ReadableObjectId readableObjectId = Mockito.mock(ReadableObjectId.class);
            Mockito.doReturn(readableObjectId).when(ctxt).findObjectId(eq(deserializedObject), eq(objectIdGenerator), eq(objectIdResolver));
            return readableObjectId;
        }).when(objectIdReader).generateId(jsonParser, ctxt);

        Object id = new Object();
        Mockito.doReturn(id).when(valueDeserializer).deserialize(jsonParser, ctxt);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, ctxt, instance);
        assertEquals(instance, result);

        ObjectIdGenerator objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
        Mockito.doReturn(objectIdResolver).when(ctxt).findObjectIdResolver();
        Mockito.doReturn(objectIdGenerator).when(ctx -> ctx.findObjectIdGenerator(eq(ObjectIdGenerator.class), eq(JsonParser.class), any()));

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.doReturn(idProperty).when(objectIdReader).getIdProperty();
        Object returnValue = new Object();
        Mockito.doReturn(returnValue).when(idProperty).setAndReturn(instance, id);

        assertEquals(returnValue, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        String propertyName = "objectId";
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyName, valueDeserializer);
        Object instance = new Object();
        Object id = new Object();

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.doReturn(idProperty).when(objectIdReader).getIdProperty();
        Mockito.doNothing().when(idProperty).setAndReturn(instance, id);

        Object result = objectIdValueProperty.setAndReturn(instance, id);
        assertEquals(instance, result);
        assertEquals(id, result);
    }

    @Test
    public void testSet() throws IOException {
        String propertyName = "objectId";
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyName, valueDeserializer);
        Object instance = new Object();
        Object id = new Object();

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.doReturn(idProperty).when(objectIdReader).getIdProperty();
        Mockito.doNothing().when(idProperty).setAndReturn(instance, id);

        objectIdValueProperty.set(instance, id);
        assertEquals(instance, instance);
    }
}