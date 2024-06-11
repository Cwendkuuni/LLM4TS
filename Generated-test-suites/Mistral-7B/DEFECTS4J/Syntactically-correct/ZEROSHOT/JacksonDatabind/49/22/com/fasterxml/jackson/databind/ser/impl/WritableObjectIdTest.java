package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Test;

/**
 * JUnit 4 test file for WritableObjectId class.
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstruction() {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);
        assertSame(mockGenerator, writableObjectId.generator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdTrue() throws IOException {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockObjectIdWriter = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);

        when(mockGenerator.generateId(mock(Object.class))).thenReturn("idValue");
        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);

        when(mockObjectIdWriter.alwaysAsId).thenReturn(false);

        assertTrue(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
        assertSame("idValue", writableObjectId.id);
    }

    @Test
    public void testWriteAsIdFalse() throws IOException {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockObjectIdWriter = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);

        assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter));
        assertNull(writableObjectId.id);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdNullId() throws IOException {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockObjectIdWriter = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(true);
        when(mockObjectIdWriter.alwaysAsId).thenReturn(false);

        writableObjectId.id = null;
        writableObjectId.writeAsId(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
    }

    @Test
    public void testGenerateId() throws IOException {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);
        Object forPojo = new Object();
        String expectedId = "expectedId";

        when(mockGenerator.generateId(forPojo)).thenReturn(expectedId);

        writableObjectId.generateId(forPojo);
        assertSame(expectedId, writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockObjectIdWriter = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);

        SerializableString propertyName = new SerializableString("propertyName");

        when(mockJsonGenerator.canWriteObjectId()).thenReturn(false);

        when(mockObjectIdWriter.propertyName).thenReturn(propertyName);

        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
        assertTrue(writableObjectId.idWritten);
        assertSame("propertyName", mockJsonGenerator.getCurrentName());
    }

    @Test
    public void testWriteAsFieldWithNullPropertyName() throws IOException {
        ObjectIdGenerator<?> mockGenerator = mock(ObjectIdGenerator.class);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        ObjectIdWriter mockObjectIdWriter = mock(ObjectIdWriter.class);
        WritableObjectId writableObjectId = new WritableObjectId(mockGenerator);

        writableObjectId.writeAsField(mockJsonGenerator, mockSerializerProvider, mockObjectIdWriter);
        assertFalse(writableObjectId.idWritten);
    }
}