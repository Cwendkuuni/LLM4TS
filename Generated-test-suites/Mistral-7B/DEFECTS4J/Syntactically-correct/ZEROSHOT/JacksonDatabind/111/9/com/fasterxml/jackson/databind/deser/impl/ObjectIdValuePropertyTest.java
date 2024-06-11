package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Unit tests for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testDefaultConstruction() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null, null);
        assertNull(property.getName());
        assertNull(property.getIdType());
        assertNull(property.getMetadata());
        assertNull(property.getValueDeserializer());
        assertNull(property._objectIdReader);
    }

    @Test
    public void testConstructionWithObjectIdReader() {
        PropertyName propertyName = Mockito.mock(PropertyName.class);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        assertEquals(propertyName, property.getName());
        assertEquals(null, property.getIdType());
        assertNull(property.getMetadata());
        assertNull(property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testCopyConstructor() {
        PropertyName propertyName = Mockito.mock(PropertyName.class);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty source = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(source, null);

        assertEquals(source.getName(), property.getName());
        assertEquals(source.getIdType(), property.getIdType());
        assertEquals(source.getMetadata(), property.getMetadata());
        assertEquals(source.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testRenamingConstructor() {
        PropertyName propertyName = Mockito.mock(PropertyName.class);
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty source = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(source, newName);

        assertEquals(newName, property.getName());
        assertEquals(source.getIdType(), property.getIdType());
        assertEquals(source.getMetadata(), property.getMetadata());
        assertEquals(source.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithName() {
        PropertyName propertyName = Mockito.mock(PropertyName.class);
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertEquals(newName, newProperty.getName());
        assertEquals(property.getIdType(), newProperty.getIdType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        PropertyName propertyName = Mockito.mock(PropertyName.class);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);

        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getIdType(), newProperty.getIdType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(deserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        PropertyName propertyName = Mockito.mock(PropertyName.class);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        NullValueProvider nullProvider = Mockito.mock(NullValueProvider.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newProperty = property.withNullProvider(nullProvider);

        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getIdType(), newProperty.getIdType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(nullProvider, newProperty._nullProvider);
        assertEquals(objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(parser.currentName()).thenReturn("id");
        Mockito.when(parser.getCurrentName()).thenReturn("id");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getValueAsString()).thenReturn("123");

        property.deserializeAndSet(parser, context, instance);

        Object id = context.findValueToReplace("123", JsonToken.VALUE_STRING);
        assertEquals(id, instance);
        // ObjectIdValueProperty does not modify instance directly

        // Test null id
        Mockito.when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        property.deserializeAndSet(parser, context, instance);
        assertNull(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(parser.currentName()).thenReturn("id");
        Mockito.when(parser.getCurrentName()).thenReturn("id");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getValueAsString()).thenReturn("123");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        ObjectIdReader idReader = Mockito.mock(ObjectIdReader.class);
        Object id = new Object();
        Mockito.when(idReader.propertyName).thenReturn(propertyName);
        Mockito.when(context.findObjectId(id, Mockito.any(ObjectIdGenerator.class), Mockito.any(ObjectIdResolver.class)))
                .thenReturn(Mockito.mock(ReadableObjectId.class));
        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        Mockito.when(property.getValueDeserializer()).thenReturn(valueDeserializer);
        Mockito.when(valueDeserializer.deserialize(parser, context)).thenReturn(id);

        Object result = property.deserializeSetAndReturn(parser, context, instance);

        assertEquals(instance, result);
        Mockito.verify(idProperty).setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object value = new Object();

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.when(idProperty.setAndReturn(instance, value)).thenReturn(instance);
        Mockito.when(property._objectIdReader.idProperty).thenReturn(idProperty);

        Object result = property.setAndReturn(instance, value);

        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullOnIdProperty() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object value = null;

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.when(idProperty.setAndReturn(instance, value)).thenReturn(instance);
        Mockito.when(property._objectIdReader.idProperty).thenReturn(idProperty);

        property.set(instance, value);
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Mockito.when(parser.currentName()).thenReturn("id");
        Mockito.when(parser.getCurrentName()).thenReturn("id");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getValueAsString()).thenReturn("123");

        Object result = property.deserialize(parser, context, instance);

        Object id = context.findValueToReplace("123", JsonToken.VALUE_STRING);
        assertEquals(instance, result);
        assertEquals(id, instance);
    }

    @Test
    public void testSetValue() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object value = new Object();

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.when(idProperty.setAndReturn(instance, value)).thenReturn(instance);
        Mockito.when(property._objectIdReader.idProperty).thenReturn(idProperty);

        property.setValue(instance, value);

        assertEquals(instance, instance);
        Mockito.verify(idProperty).setAndReturn(instance, value);
    }

    @Test
    public void testSetValueNull() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.when(idProperty.setAndReturn(instance, null)).thenReturn(instance);
        Mockito.when(property._objectIdReader.idProperty).thenReturn(idProperty);

        property.setValue(instance, null);

        assertEquals(instance, instance);
        Mockito.verify(idProperty).setAndReturn(instance, null);
    }

    @Test
    public void testGetValue() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object value = new Object();

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.when(idProperty.getValue(instance)).thenReturn(value);
        Mockito.when(property._objectIdReader.idProperty).thenReturn(idProperty);

        Object result = property.getValue(instance);

        assertEquals(value, result);
    }

    @Test
    public void testGetValueNull() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();

        ObjectIdValueProperty idProperty = Mockito.mock(ObjectIdValueProperty.class);
        Mockito.when(idProperty.getValue(instance)).thenReturn(null);
        Mockito.when(property._objectIdReader.idProperty).thenReturn(idProperty);

        Object result = property.getValue(instance);

        assertNull(result);
    }

    static class ObjectIdReader {
        public PropertyName propertyName;
        public ObjectIdGenerator generator;
        public ObjectIdResolver resolver;
        public JsonDeserializer<?> idPropertyDeserializer;

        public ObjectIdReader(PropertyName propertyName) {
            this.propertyName = propertyName;
        }

        public ObjectIdReader(PropertyName propertyName, ObjectIdGenerator generator, ObjectIdResolver resolver,
                JsonDeserializer<?> idPropertyDeserializer) {
            this.propertyName = propertyName;
            this.generator = generator;
            this.resolver = resolver;
            this.idPropertyDeserializer = idPropertyDeserializer;
        }

        public ObjectIdValueProperty createValueProperty(PropertyMetadata metadata) {
            JsonDeserializer<?> deserializer = new StdSerializer<Object>(Object.class) {};
            return new ObjectIdValueProperty(this, metadata, deserializer);
        }
    }

    static class ObjectIdGenerator {
    }

    static class ObjectIdResolver {
    }

    static class Object {
    }
}