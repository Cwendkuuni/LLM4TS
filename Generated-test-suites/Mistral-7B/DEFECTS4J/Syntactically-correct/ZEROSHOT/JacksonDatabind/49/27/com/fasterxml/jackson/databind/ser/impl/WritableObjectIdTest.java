package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test class for WritableObjectId class
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstructor() {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object obj) {
                return "GeneratedId_" + obj.hashCode();
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        assertEquals(objIdGenerator, writableObjectId.generator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdWithNativeObjectId() throws IOException {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object obj) {
                return "NativeId_" + obj.hashCode();
            }
        };
        JsonGenerator jsonGenerator = createMock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        SerializerProvider serializerProvider = createMock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        ToStringSerializer toStringSerializer = objectMapper.findValueSerializer(ToStringSerializer.class);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, stringSerializer, typeSerializer, toStringSerializer);
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        Object id = new Object() {
            @Override
            public String toString() {
                return "TestId";
            }
        };
        when(serializerProvider.findValueSerializer(WritableObjectId.class)).thenReturn(new StdSerializer<WritableObjectId>(WritableObjectId.class) {
            @Override
            public void serialize(WritableObjectId writableObjectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeObjectId(String.valueOf(writableObjectId.id));
            }
        });
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(result);
        verify(jsonGenerator).writeObjectRef(eq("NativeId_" + id.hashCode()));
    }

    @Test
    public void testWriteAsIdWithNonNativeObjectId() throws IOException {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object obj) {
                return "NonNativeId_" + obj.hashCode();
            }
        };
        JsonGenerator jsonGenerator = createMock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(false);
        SerializerProvider serializerProvider = createMock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        ToStringSerializer toStringSerializer = objectMapper.findValueSerializer(ToStringSerializer.class);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, stringSerializer, typeSerializer, toStringSerializer);
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        Object id = new Object() {
            @Override
            public String toString() {
                return "TestId";
            }
        };
        when(serializerProvider.findValueSerializer(WritableObjectId.class)).thenReturn(new StdSerializer<WritableObjectId>(WritableObjectId.class) {
            @Override
            public void serialize(WritableObjectId writableObjectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(String.valueOf(writableObjectId.id));
            }
        });
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertFalse(result);
        verify(jsonGenerator).writeString(eq("TestId"));
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteAsIdWithNullId() throws IOException {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object obj) {
                return null;
            }
        };
        JsonGenerator jsonGenerator = createMock(JsonGenerator.class);
        SerializerProvider serializerProvider = createMock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        ToStringSerializer toStringSerializer = objectMapper.findValueSerializer(ToStringSerializer.class);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, stringSerializer, typeSerializer, toStringSerializer);
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        fail("Expected JsonMappingException but got " + result);
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object obj) {
                return "GeneratedId_" + obj.hashCode();
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        Object forPojo = new Object() {
            @Override
            public String toString() {
                return "TestPojo";
            }
        };
        Object id = writableObjectId.generateId(forPojo);
        assertEquals("GeneratedId_" + forPojo.hashCode(), id.toString());
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object obj) {
                return "GeneratedId_" + obj.hashCode();
            }
        };
        JsonGenerator jsonGenerator = createMock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        SerializerProvider serializerProvider = createMock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        ToStringSerializer toStringSerializer = objectMapper.findValueSerializer(ToStringSerializer.class);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, stringSerializer, typeSerializer, toStringSerializer);
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        Object id = new Object() {
            @Override
            public String toString() {
                return "TestId";
            }
        };
        when(serializerProvider.findValueSerializer(WritableObjectId.class)).thenReturn(new StdSerializer<WritableObjectId>(WritableObjectId.class) {
            @Override
            public void serialize(WritableObjectId writableObjectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeObjectId(String.valueOf(writableObjectId.id));
            }
        });
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator).writeObjectId(eq("GeneratedId_" + id.hashCode()));
    }

    @Test
    public void testWriteAsFieldWithNullPropertyName() throws IOException {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object obj) {
                return "GeneratedId_" + obj.hashCode();
            }
        };
        JsonGenerator jsonGenerator = createMock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        SerializerProvider serializerProvider = createMock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        ToStringSerializer toStringSerializer = objectMapper.findValueSerializer(ToStringSerializer.class);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, stringSerializer, typeSerializer, toStringSerializer);
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, null);
        verifyNoMoreInteractions(jsonGenerator);
    }

    @Test
    public void testWriteAsFieldWithStringPropertyName() throws IOException {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object obj) {
                return "GeneratedId_" + obj.hashCode();
            }
        };
        JsonGenerator jsonGenerator = createMock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        SerializerProvider serializerProvider = createMock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        ToStringSerializer toStringSerializer = objectMapper.findValueSerializer(ToStringSerializer.class);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, stringSerializer, typeSerializer, toStringSerializer);
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        String propertyName = "testPropertyName";
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        verify(jsonGenerator).writeFieldName(eq(propertyName));
    }

    @Test
    public void testWriteAsFieldWithNullId() {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object obj) {
                return null;
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithIdWritten() {
        ObjectIdGenerator<Object> objIdGenerator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object obj) {
                return "GeneratedId_" + obj.hashCode();
            }
        };
        JsonGenerator jsonGenerator = createMock(JsonGenerator.class);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        SerializerProvider serializerProvider = createMock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        ToStringSerializer toStringSerializer = objectMapper.findValueSerializer(ToStringSerializer.class);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider, stringSerializer, typeSerializer, toStringSerializer);
        WritableObjectId writableObjectId = new WritableObjectId(objIdGenerator);
        Object id = new Object() {
            @Override
            public String toString() {
                return "TestId";
            }
        };
        when(serializerProvider.findValueSerializer(WritableObjectId.class)).thenReturn(new StdSerializer<WritableObjectId>(WritableObjectId.class) {
            @Override
            public void serialize(WritableObjectId writableObjectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeObjectId(String.valueOf(writableObjectId.id));
                writableObjectId.idWritten = true;
            }
        });
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(writableObjectId.idWritten);
    }
}