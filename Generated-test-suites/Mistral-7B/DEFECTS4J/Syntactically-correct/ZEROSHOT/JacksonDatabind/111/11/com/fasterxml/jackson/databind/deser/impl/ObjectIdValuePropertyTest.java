package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.NullValueProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    @Mock
    private JsonDeserializer<?> valueDeserializer;

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private PropertyName propertyName;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object instance;

    @Mock
    private Object id;

    @Mock
    private ReadableObjectId readableObjectId;

    @Mock
    private JsonDeserializer<?> idDeserializer;

    @Mock
    private SettableBeanProperty idProperty;

    @Mock
    private NullValueProvider nullProvider;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private ObjectIdReader.IdType idType;

    @Mock
    private NameTransformer nameTransformer;

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(objectIdReader.idProperty).thenReturn(idProperty);
        when(objectIdReader.generator).thenReturn(nameTransformer);
        when(objectIdReader.resolver).thenReturn(idType);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(propertyName, null, null, null, null, null));
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Mockito.any(Annotation.class)));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);
        assertEquals(newName, newObjectIdValueProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider newNullProvider = mock(NullValueProvider.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(newNullProvider);
        assertEquals(newNullProvider, newObjectIdValueProperty.getNullProvider());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(jsonParser.getCurrentName()).thenReturn("id");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("123");
        when(objectIdReader.getIdType()).thenReturn(idType);

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(idProperty, times(1)).setAndReturn(instance, id);
        verifyNoMoreInteractions(jsonParser, deserializationContext, instance, id, readableObjectId);
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        verifyNoInteractions(idProperty, jsonParser, deserializationContext, instance, id, readableObjectId);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NUMBER_INT)).thenReturn(true);
        when(jsonParser.nextIntValue()).thenReturn(123);

        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
        when(idDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);

        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(readableObjectId.bindItem(instance)).thenReturn(instance);

        when(objectIdReader.getIdType()).thenReturn(idType);
        when(idType.idFromString("123")).thenReturn(id);
        when(idType.idFromValue(id)).thenReturn(id);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(idProperty, times(1)).setAndReturn(instance, id);
        verify(readableObjectId, times(1)).bindItem(instance);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        objectIdValueProperty.set(instance, id);

        verify(idProperty, times(1)).setAndReturn(instance, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        Object result = objectIdValueProperty.setAndReturn(instance, id);

        assertEquals(instance, result);
    }
}