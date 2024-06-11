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
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.fasterxml.jackson.databind.util.StdName;

/**
 * JUnit test file for {@link WritableObjectId} class
 */
public class WritableObjectIdTest {

    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdGenerator<Object> idGenerator;
    private ObjectIdWriter idWriter;
    private WritableObjectId writableObjectId;

    @Before
    public void setup() throws IOException {
        idGenerator = mock(IdIntGenerator.class);
        when(idGenerator.generateId(null)).thenReturn(1L);

        idWriter = new ObjectIdWriter(null, new StdSerializer<ObjectNode>(ObjectNode.class), null);
        provider = mock(SerializerProvider.class);
        when(provider.isEnabled(JsonSerializer.Feature.WRITE_OBJECT_ID)).thenReturn(true);
        gen = mock(JsonGenerator.class);

        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testGenerateId() throws IOException {
        Object id = writableObjectId.generateId(null);
        assertNotNull(id);
        assertEquals(1L, id.longValue());
    }

    @Test
    public void testWriteAsId() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(true);
        writableObjectId.writeAsId(gen, provider, idWriter);
        verify(gen).writeObjectRef(eq(String.valueOf(1L)));

        when(gen.canWriteObjectId()).thenReturn(false);
        idWriter.alwaysAsId = true;
        writableObjectId.writeAsId(gen, provider, idWriter);
        verify(idWriter.serializer).serialize(eq(1L), eq(gen), eq(provider));
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWithNullGenerator() throws IOException {
        writableObjectId.writeAsId(null, provider, idWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWithNullProvider() throws IOException {
        writableObjectId.writeAsId(gen, null, idWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWithNullWriter() throws IOException {
        writableObjectId.writeAsId(gen, provider, null);
    }

    @Test
    public void testWriteAsField() throws IOException {
        when(gen.canWriteObjectId()).thenReturn(true);
        writableObjectId.writeAsField(gen, provider, idWriter);
        verify(gen).writeObjectId(eq(String.valueOf(1L)));

        when(gen.canWriteObjectId()).thenReturn(false);
        when(gen.writeFieldName(anyString())).thenReturn(gen);
        when(idWriter.propertyName.asText()).thenReturn("name");
        writableObjectId.writeAsField(gen, provider, idWriter);
        verify(gen).writeFieldName(eq("name"));
        verify(idWriter.serializer).serialize(eq(1L), eq(gen), eq(provider));
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsFieldWithNullGenerator() throws IOException {
        writableObjectId.writeAsField(null, provider, idWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsFieldWithNullProvider() throws IOException {
        writableObjectId.writeAsField(gen, null, idWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsFieldWithNullWriter() throws IOException {
        writableObjectId.writeAsField(gen, provider, null);
    }
}

class ObjectIdWriter extends StdSerializer<ObjectNode> {

    private final SerializerProvider serializerProvider;
    private final StdSerializer<ObjectNode> serializer;
    private final StdName propertyName;
    private boolean alwaysAsId = false;

    public ObjectIdWriter(SerializerProvider serializerProvider, StdSerializer<ObjectNode> serializer, StdName propertyName) {
        this.serializerProvider = serializerProvider;
        this.serializer = serializer;
        this.propertyName = propertyName;
    }

}