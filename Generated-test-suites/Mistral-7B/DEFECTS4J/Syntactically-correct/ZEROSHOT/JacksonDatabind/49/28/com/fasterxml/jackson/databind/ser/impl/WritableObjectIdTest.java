package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;

/**
 * Test class for {@link WritableObjectId} class.
 */
public class WritableObjectIdTest {

    /**
     * Test constructor with valid ObjectIdGenerator.
     *
     * @throws IOException on error
     */
    @Test
    public void testConstructWithObjectIdGenerator() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        assertNotNull(writableObjectId);
        assertNotNull(writableObjectId.generator);
    }

    /**
     * Test writeAsId method with valid JsonGenerator, SerializerProvider and ObjectIdWriter.
     *
     * @throws IOException on error
     */
    @Test
    public void testWriteAsIdWithValidParameters() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);

        when(generatorMock.generateId((Object) null)).thenReturn("generatedId");

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        when(genMock.canWriteObjectId()).thenReturn(true);

        boolean result = writableObjectId.writeAsId(genMock, providerMock, idWriterMock);
        assert(result);
        verify(genMock).writeObjectRef("generatedId");
    }

    /**
     * Test writeAsId method with invalid JsonGenerator and SerializerProvider.
     *
     * @throws IOException on error
     */
    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWithInvalidParameters() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        when(genMock.canWriteObjectId()).thenReturn(false);

        boolean result = writableObjectId.writeAsId(genMock, providerMock, idWriterMock);
        assert(result == false);
    }

    /**
     * Test generateId method with valid Object.
     *
     * @throws IOException on error
     */
    @Test
    public void testGenerateIdWithValidObject() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        Object forPojo = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);

        String generatedId = "generatedId";
        when(generatorMock.generateId(forPojo)).thenReturn(generatedId);

        Object result = writableObjectId.generateId(forPojo);
        assert(result.equals(generatedId));
    }

    /**
     * Test writeAsField method with valid JsonGenerator, SerializerProvider, ObjectIdWriter, and String property name.
     *
     * @throws IOException on error
     */
    @Test
    public void testWriteAsFieldWithValidParametersAndStringPropertyName() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);
        SerializableString propertyName = new SerializableString("propertyName");

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);

        when(genMock.canWriteObjectId()).thenReturn(false);
        when(idWriterMock.propertyName).thenReturn(propertyName);

        writableObjectId.writeAsField(genMock, providerMock, idWriterMock);

        verify(genMock).writeFieldName("propertyName");
    }

    /**
     * Test writeAsField method with valid JsonGenerator, SerializerProvider, ObjectIdWriter, and null property name.
     *
     * @throws IOException on error
     */
    @Test
    public void testWriteAsFieldWithValidParametersAndNullPropertyName() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);

        when(genMock.canWriteObjectId()).thenReturn(false);
        when(idWriterMock.propertyName).thenReturn(null);

        writableObjectId.writeAsField(genMock, providerMock, idWriterMock);

        // Nothing to verify here since null property name doesn't cause any action
    }

    /**
     * Static nested class representing ObjectIdWriter mock.
     */
    static class ObjectIdWriter {
        SerializableString propertyName;
        StdSerializer<Object> serializer;
        boolean alwaysAsId;
    }
}