package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectIdReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    @Mock
    private JsonDeserializer<?> _valueDeserializer;

    @Mock
    private ObjectIdReader _objectIdReader;

    @Mock
    private JsonDeserializationContext _deserializationContext;

    @Mock
    private Object _instance;

    @Mock
    private PropertyName _propertyName;

    @Mock
    private NullValueProvider _nullProvider;

    @Mock
    private JsonParser _jsonParser;

    @Mock
    private TypeDeserializer _typeDeserializer;

    @InjectMocks
    private ObjectIdValueProperty _objectIdValueProperty;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(_objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        doNothing().when(_objectIdValueProperty)._setField("_objectIdReader", _objectIdReader);
    }

    @After
    public void tearDown() throws Exception {
        reset(_objectIdValueProperty);
        reset(_jsonParser);
        reset(_deserializationContext);
        reset(_instance);
        reset(_propertyName);
        reset(_valueDeserializer);
        reset(_nullProvider);
        reset(_typeDeserializer);
    }

    @Test
    public void testConstructor() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(_objectIdReader, new PropertyMetadata());
        assertNotNull(objectIdValueProperty);
        assertEquals(_objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty result = _objectIdValueProperty.withName(newName);
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        SettableBeanProperty result = _objectIdValueProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty result = _objectIdValueProperty.withNullProvider(nva);
        assertEquals(nva, result.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(_objectIdValueProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(_objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = new Object();
        when(_valueDeserializer.deserialize(eq(_jsonParser), eq(_deserializationContext))).thenReturn(id);
        when(_objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(_objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));

        _objectIdValueProperty.deserializeAndSet(_jsonParser, _deserializationContext, _instance);

        verify(_objectIdReader, times(1)).getIdType();
        verify(_objectIdReader, times(1)).idProperty;
        verify(_deserializationContext, times(1)).findObjectId(eq(id), eq(_objectIdReader.generator), eq(_objectIdReader.resolver));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = new Object();
        when(_valueDeserializer.deserialize(eq(_jsonParser), eq(_deserializationContext))).thenReturn(id);
        ReadableObjectId readableObjectId = mock(ReadableObjectId.class);
        when(_deserializationContext.findObjectId(eq(id), eq(_objectIdReader.generator), eq(_objectIdReader.resolver))).thenReturn(readableObjectId);
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(readableObjectId.bindItem(_instance)).thenReturn(idProp);

        Object result = _objectIdValueProperty.deserializeSetAndReturn(_jsonParser, _deserializationContext, _instance);
        assertEquals(idProp, result);
    }

    @Test
    public void testSet() throws IOException {
        Object id = new Object();
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(idProp.setAndReturn(_instance, id)).thenReturn(_instance);
        when(_objectIdReader.idProperty).thenReturn(idProp);

        _objectIdValueProperty.set(_instance, id);

        verify(_instance, times(1)).set(eq(id));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = new Object();
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(idProp.setAndReturn(_instance, id)).thenReturn(_instance);
        when(_objectIdReader.idProperty).thenReturn(idProp);

        Object result = _objectIdValueProperty.setAndReturn(_instance, id);
        assertEquals(_instance, result);
    }
}