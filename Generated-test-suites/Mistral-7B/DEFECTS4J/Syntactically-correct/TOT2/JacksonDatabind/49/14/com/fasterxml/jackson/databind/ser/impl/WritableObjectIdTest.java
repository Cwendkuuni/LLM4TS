package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class WritableObjectIdTest {
    @Test
    public void testWritableObjectIdConstruction() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object bean) {
                return "id_1";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId.generator);
    }

    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        boolean result = writableObjectId.writeAsId(genMock, providerMock, w);

        when(generatorMock.generateId(Mockito.any())).thenReturn("id_1");
        when(w.alwaysAsId).thenReturn(false);
        genMock.expects(1).method("writeObjectRef").with(eq(String.valueOf("id_1")));

        result = writableObjectId.writeAsId(genMock, providerMock, w);
        verify(genMock).writeObjectRef(eq(String.valueOf("id_1")));
        assertThat(result).isTrue();
    }

    @Test
    public void testWriteAsId_whenIdIsNull_thenReturnFalse() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        boolean result = writableObjectId.writeAsId(genMock, providerMock, w);

        when(generatorMock.generateId(Mockito.any())).thenReturn(null);

        result = writableObjectId.writeAsId(genMock, providerMock, w);
        assertThat(result).isFalse();
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        when(generatorMock.generateId(Mockito.any())).thenReturn("id_1");
        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);

        Object result = writableObjectId.generateId(new Object());
        assertNotNull(result);
        assertNotNull(writableObjectId.id);
        assertEquals("id_1", writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        writableObjectId.writeAsField(genMock, providerMock, w);

        verify(genMock, atLeast(1)).writeFieldName(anyString());
        verify(genMock, atLeast(1)).writeString(String.valueOf(writableObjectId.id));
    }

    @Test
    public void testWriteAsField_whenGenCannotWriteObjectId() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        JsonGenerator genMockWritableObjectId = mock(JsonGenerator.class);
        when(genMock.canWriteObjectId()).thenReturn(false);

        writableObjectId.writeAsField(genMockWritableObjectId, providerMock, w);
        verify(genMockWritableObjectId, atLeast(1)).writeString(String.valueOf(writableObjectId.id));
        verify(genMockWritableObjectId, atLeast(1)).writeFieldName(anyString());
    }

    @Test
    public void testWriteAsField_whenPropertyNameIsNull() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        JsonGenerator genMockWithoutPropertyName = mock(JsonGenerator.class);

        writableObjectId.writeAsField(genMockWithoutPropertyName, providerMock, w);
        verify(genMockWithoutPropertyName, atLeast(1)).writeString(String.valueOf(writableObjectId.id));
    }

    @Test
    public void testWriteAsField_whenIdIsNull() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        WritableObjectId writableObjectIdWithNullId = new WritableObjectId(generatorMock);
        writableObjectIdWithNullId.id = null;

        writableObjectId.writeAsField(genMock, providerMock, w);
        verify(genMock).writeFieldName(anyString());

        writableObjectId = writableObjectIdWithNullId;
        writableObjectId.writeAsField(genMock, providerMock, w);
        verify(genMock).writeNull();
    }
}