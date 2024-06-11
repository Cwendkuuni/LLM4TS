package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest extends BaseMapTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty property;
    private BeanPropertyDefinition beanPropertyDefinition;

    @BeforeMethod
    public void setup() throws Exception {
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(propertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        beanPropertyDefinition = new BeanPropertyDefinition(property.getName(), property.getType(), null);
    }

    @Test
    public void testDefaultConstruction() {
        assertNotNull(property);
        assertEquals(property.getName(), "id");
        assertNotNull(property.getIdType());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertNull(property.getMember());
        assertNull(property.getAnnotation(JsonValueAnnotation.class));
        assertNull(property.getIdReader());
    }

    @Test
    public void testWithNameConstruction() {
        PropertyName newName = propertyName("newName");
        ObjectIdValueProperty propertyWithName = new ObjectIdValueProperty(property, newName);

        assertEquals(propertyWithName.getName(), newName);
        assertEquals(propertyWithName.getIdType(), property.getIdType());
        assertEquals(propertyWithName.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(propertyWithName.getNullProvider(), property.getNullProvider());
        assertEquals(propertyWithName.getMember(), property.getMember());
        assertEquals(propertyWithName.getAnnotation(JsonValueAnnotation.class), property.getAnnotation(JsonValueAnnotation.class));
        assertEquals(propertyWithName.getIdReader(), property.getIdReader());
    }

    @Test
    public void testWithValueDeserializerConstruction() {
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty propertyWithDeserializer = new ObjectIdValueProperty(property, valueDeserializer, property.getNullProvider());

        assertEquals(propertyWithDeserializer.getName(), property.getName());
        assertEquals(propertyWithDeserializer.getIdType(), property.getIdType());
        assertEquals(propertyWithDeserializer.getValueDeserializer(), valueDeserializer);
        assertEquals(propertyWithDeserializer.getNullProvider(), property.getNullProvider());
        assertEquals(propertyWithDeserializer.getMember(), property.getMember());
        assertEquals(propertyWithDeserializer.getAnnotation(JsonValueAnnotation.class), property.getAnnotation(JsonValueAnnotation.class));
        assertEquals(propertyWithDeserializer.getIdReader(), property.getIdReader());
    }

    @Test
    public void testWithNullProviderConstruction() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        ObjectIdValueProperty propertyWithNullProvider = new ObjectIdValueProperty(property, property.getValueDeserializer(), nullValueProvider);

        assertEquals(propertyWithNullProvider.getName(), property.getName());
        assertEquals(propertyWithNullProvider.getIdType(), property.getIdType());
        assertEquals(propertyWithNullProvider.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(propertyWithNullProvider.getNullProvider(), nullValueProvider);
        assertEquals(propertyWithNullProvider.getMember(), property.getMember());
        assertEquals(propertyWithNullProvider.getAnnotation(JsonValueAnnotation.class), property.getAnnotation(JsonValueAnnotation.class));
        assertEquals(propertyWithNullProvider.getIdReader(), property.getIdReader());
    }

    @Test
    public void testGetAnnotationReturnsNull() throws Exception {
        assertNull(property.getAnnotation(JsonValueAnnotation.class));
    }

    @Test
    public void testGetMemberReturnsNull() throws Exception {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSetNullValue() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(jsonParser("null"), context, instance);

        verify(context, times(1)).findObjectId(eq(null), eq(objectIdReader.generator), eq(objectIdReader.resolver));
        verifyNoMoreInteractions(context);
        assertNull(property.getValue(instance));
    }

    @Test
    public void testDeserializeAndSetValue() throws IOException {
        String id = "123";
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser("123")).thenReturn(mockParser());
        when(property.getValueDeserializer().deserialize(jsonParser("123"), context)).thenReturn(id);

        property.deserializeAndSet(jsonParser("123"), context, instance);

        verify(context, times(1)).findObjectId(eq(id), eq(objectIdReader.generator), eq(objectIdReader.resolver));
        verify(objectIdReader.idProperty, times(1)).bindItem(instance);
        assertEquals(property.getValue(instance), id);
    }

    @Test
    public void testSetAndReturnNullValue() throws IOException {
        Object instance = new Object();

        when(property.setAndReturn(instance, null)).thenReturn(instance);

        Object result = property.setAndReturn(instance, null);

        assertEquals(result, instance);
        assertNull(property.getValue(instance));
    }

    @Test
    public void testSetAndReturnValue() throws IOException {
        Object instance = new Object();
        String id = "123";

        when(property.setAndReturn(instance, id)).thenReturn(instance);

        Object result = property.setAndReturn(instance, id);

        assertEquals(result, instance);
        assertEquals(property.getValue(instance), id);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSetThrowsUnsupportedOperationException() throws IOException {
        Object instance = new Object();

        property.set(instance, "not valid");
    }
}