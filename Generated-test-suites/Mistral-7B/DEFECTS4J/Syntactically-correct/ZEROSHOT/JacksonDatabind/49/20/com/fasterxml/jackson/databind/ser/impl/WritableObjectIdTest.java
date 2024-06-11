package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/**
 * Test class for WritableObjectId class
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectMapper objectMapper;
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setup() throws IOException {
        idGenerator = mock(IdIntGenerator.class);
        when(idGenerator.generateId(Mockito.any())).thenReturn(1L);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        objectMapper = new ObjectMapper();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(new SimpleBeanPropertyFilter(SimpleFilterProvider.SimpleFilterProvider.EmptyFilterProvider),
                new SimpleFilterProvider(), null);
        this.objectIdWriter = objectIdWriter;
    }

    @Test
    public void testWritableObjectIdConstruction() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertSame(generator, writableObjectId.generator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsId() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectRef(Mockito.eq("1"))).thenReturn(jsonGenerator);
        when(objectIdWriter.alwaysAsId).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = 1L;

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        boolean actual = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(actual);
        verify(jsonGenerator).writeObjectRef(Mockito.eq("1"));

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        when(objectIdWriter.serializer.serialize(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(jsonGenerator);

        actual = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(actual);
        verify(jsonGenerator).writeFieldName(Mockito.eq("testPropertyName"));
        verify(jsonGenerator).writeString(Mockito.eq("1"));

        when(objectIdWriter.alwaysAsId).thenReturn(true);

        actual = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(actual);
        verify(objectIdWriter, never()).serializer;
    }

    @Test
    public void testGenerateId() throws IOException {
        when(idGenerator.generateId(Mockito.any())).thenReturn(1L);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object obj = new Object();
        Object actual = writableObjectId.generateId(obj);
        assertSame(1L, actual);
        verify(idGenerator).generateId(Mockito.eq(obj));
    }

    @Test
    public void testWriteAsField() throws IOException {
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        when(jsonGenerator.writeObjectId(Mockito.any())).thenReturn(jsonGenerator);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = 1L;

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator).writeObjectId(Mockito.eq("1"));

        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        when(objectIdWriter.propertyName).thenReturn(new SerializableString("testPropertyName"));
        when(objectIdWriter.serializer.serialize(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(jsonGenerator);

        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator).writeFieldName(Mockito.eq("testPropertyName"));
        verify(jsonGenerator).writeString(Mockito.eq("1"));
    }

    private void verify(Object... mocks) {
        for (Object mock : mocks) {
            verify(mock).times(1);
        }
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            fail("Expected true, but received false");
        }
    }
}