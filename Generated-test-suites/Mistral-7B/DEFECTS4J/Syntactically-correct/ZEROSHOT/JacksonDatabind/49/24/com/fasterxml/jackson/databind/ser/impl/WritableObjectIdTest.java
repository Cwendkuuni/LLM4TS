package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGeneratorTestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import com.fasterxml.jackson.databind.ser.std.SerializableObjectIdGenerator;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest extends ObjectIdGeneratorTestBase {

    @Test
    public void testWritableObjectId() throws IOException {
        ObjectIdGenerator<Object> idGenerator = new SerializableObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        assertNotNull(writableObjectId.generator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);

        Object testObject = new Object();
        writableObjectId.generateId(testObject);
        Object testId = writableObjectId.id;
        assertNotNull(testId);
    }

    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator<Object> idGenerator = new SerializableObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter() {
            @Override
            protected JsonSerializer<Object> getSerializer(SerializerProvider provider) {
                return mock(JsonSerializer.class);
            }

            @Override
            public boolean alwaysAsId() {
                return false;
            }
        };

        when(gen.canWriteObjectId()).thenReturn(true);

        Object testObject = new Object();
        Object testId = writableObjectId.generateId(testObject);
        writableObjectId.id = testId;

        when(gen.writeObjectRef(String.valueOf(testId))).thenReturn(null);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        verify(gen).writeObjectRef(String.valueOf(testId));
    }

    @Test
    public void testWriteAsIdWithUnsupportedJsonGenerator() throws IOException {
        ObjectIdGenerator<Object> idGenerator = new SerializableObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter() {
            @Override
            protected JsonSerializer<Object> getSerializer(SerializerProvider provider) {
                return mock(JsonSerializer.class);
            }

            @Override
            public boolean alwaysAsId() {
                return false;
            }
        };

        when(gen.canWriteObjectId()).thenReturn(false);

        Object testObject = new Object();
        Object testId = writableObjectId.generateId(testObject);
        writableObjectId.id = testId;

        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(provider.findValueSerializer(Object.class, typeSerializer)).thenReturn(serializer);

        SerializableString name = new SerializableString("testField");

        when(w.propertyName).thenReturn(name);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        verify(gen).writeFieldName(name);
        verify(serializer).serialize(testId, gen, provider);
    }

    @Test
    public void testWriteAsIdWithAlwaysAsId() throws IOException {
        ObjectIdGenerator<Object> idGenerator = new SerializableObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter() {
            @Override
            protected JsonSerializer<Object> getSerializer(SerializerProvider provider) {
                return mock(JsonSerializer.class);
            }

            @Override
            public boolean alwaysAsId() {
                return true;
            }
        };

        Object testObject = new Object();
        Object testId = writableObjectId.generateId(testObject);
        writableObjectId.id = testId;

        when(gen.canWriteObjectId()).thenReturn(false);

        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(provider.findValueSerializer(Object.class, typeSerializer)).thenReturn(serializer);

        SerializableString name = new SerializableString("testField");

        when(w.propertyName).thenReturn(name);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        verify(gen).writeObjectId(String.valueOf(testId));
    }

    @Test
    public void testGenerateId() throws IOException {
        ObjectIdGenerator<Object> idGenerator = new SerializableObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object testObject = new Object();

        Object testId = writableObjectId.generateId(testObject);
        assertNotNull(testId);
        assertFalse(writableObjectId.idWritten);
        assertEquals(testId, writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<Object> idGenerator = new SerializableObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter() {
            @Override
            protected JsonSerializer<Object> getSerializer(SerializerProvider provider) {
                return mock(JsonSerializer.class);
            }

            @Override
            public boolean alwaysAsId() {
                return false;
            }
        };

        Object testObject = new Object();
        Object testId = writableObjectId.generateId(testObject);
        writableObjectId.id = testId;

        when(gen.canWriteObjectId()).thenReturn(true);

        SerializableString name = new SerializableString("testField");
        when(w.propertyName).thenReturn(name);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen).writeObjectId(String.valueOf(testId));
    }

    @Test
    public void testWriteAsFieldWithUnsupportedJsonGenerator() throws IOException {
        ObjectIdGenerator<Object> idGenerator = new SerializableObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter() {
            @Override
            protected JsonSerializer<Object> getSerializer(SerializerProvider provider) {
                return mock(JsonSerializer.class);
            }

            @Override
            public boolean alwaysAsId() {
                return false;
            }
        };

        Object testObject = new Object();
        Object testId = writableObjectId.generateId(testObject);
        writableObjectId.id = testId;

        when(gen.canWriteObjectId()).thenReturn(false);

        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(provider.findValueSerializer(Object.class, typeSerializer)).thenReturn(serializer);

        SerializableString name = new SerializableString("testField");

        when(w.propertyName).thenReturn(name);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen).writeFieldName(name);
        verify(serializer).serialize(testId, gen, provider);
    }

    @Test
    public void testWriteAsFieldWithAlwaysAsId() throws IOException {
        ObjectIdGenerator<Object> idGenerator = new SerializableObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = new ObjectIdWriter() {
            @Override
            protected JsonSerializer<Object> getSerializer(SerializerProvider provider) {
                return mock(JsonSerializer.class);
            }

            @Override
            public boolean alwaysAsId() {
                return true;
            }
        };

        Object testObject = new Object();
        Object testId = writableObjectId.generateId(testObject);
        writableObjectId.id = testId;

        when(gen.canWriteObjectId()).thenReturn(false);

        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(provider.findValueSerializer(Object.class, typeSerializer)).thenReturn(serializer);

        SerializableString name = new SerializableString("testField");

        when(w.propertyName).thenReturn(name);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen).writeObjectId(String.valueOf(testId));
    }

    @Test
    public void testSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object testObject = new Object();
        ObjectIdGenerator<Object> idGenerator = new SerializableObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.generateId(testObject);

        String json = mapper.writeValueAsString(writableObjectId);

        Object result = mapper.readValue(json, WritableObjectId.class);

        assertEquals(writableObjectId.generator, result.generator);
        assertNotNull(result.id);
        assertEquals(writableObjectId.id, result.id);
        assertTrue(writableObjectId.idWritten == result.idWritten);
    }
}