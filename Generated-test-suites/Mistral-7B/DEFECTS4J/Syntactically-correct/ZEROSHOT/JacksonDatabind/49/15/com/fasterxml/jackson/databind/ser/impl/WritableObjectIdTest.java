package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.ser.std.ObjectIdWriter;

/**
 * Tests for {@link WritableObjectId} class.
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private SerializerProvider provider;
    private JsonGenerator gen;
    private ObjectIdWriter idWriter;

    @Before
    public void setup() {
        idGenerator = mock(ObjectIdGenerator.class);
        provider = mock(SerializerProvider.class);
        gen = mock(JsonGenerator.class);
        idWriter = new ObjectIdWriter(null, null);
    }

    @Test
    public void testConstructor() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId.generator);
    }

    @Test
    public void testWriteAsId() throws IOException {
        Object id = new Object();
        when(idGenerator.generateId(id)).thenReturn(id);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        when(gen.canWriteObjectId()).thenReturn(true);

        boolean result = writableObjectId.writeAsId(gen, provider, idWriter);
        assert(result);
    }

    @Test
    public void testWriteAsIdWhenObjectIdCannotBeWritten() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        when(gen.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(gen, provider, idWriter);
        assert(!result);
    }

    @Test
    public void testWriteAsIdWhenIdIsEmpty() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        boolean result = writableObjectId.writeAsId(gen, provider, idWriter);
        assert(!result);
    }

    @Test
    public void testGenerateId() throws JsonMappingException {
        Object id = new Object();
        when(idGenerator.generateId(null)).thenReturn(id);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object result = writableObjectId.generateId(null);
        assert(result.equals(id));
    }

    @Test
    public void testGenerateIdWhenIdIsProvided() throws JsonMappingException {
        Object id = new Object();

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;
        Object result = writableObjectId.generateId(null);
        assert(result.equals(id));
    }

    @Test
    public void testWriteAsField() throws IOException {
        Object id = new Object();
        when(idGenerator.generateId(null)).thenReturn(id);

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, idWriter);

        // Verify that idWritten flag is set to true
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWhenGenCannotWriteObjectId() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        when(gen.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsField(gen, provider, idWriter);

        // Verify that idWritten flag is not set to true
        assertFalse(writableObjectId.idWritten);
    }
}