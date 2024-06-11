package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Test class for {@link WritableObjectId}
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        idGenerator = Mockito.mock(IdIntGenerator.class);
        jsonGenerator = Mockito.mock(JsonGenerator.class);
        serializerProvider = Mockito.mock(SerializerProvider.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testWriteAsIdWithExistingIdAndIdWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = "existingId";

        ObjectIdWriter objectIdWriter = new ObjectIdWriter() {
            @Override
            public boolean alwaysAsId() {
                return false;
            }

            @Override
            public SerializableString propertyName() {
                return null;
            }

            @Override
            public void serialize(Object id, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // Do nothing, as this test only checks if the id was written
            }
        };

        StdSerializer<Object> objectSerializer = Mockito.mock(StdSerializer.class);
        Mockito.when(serializerProvider.findValueSerializer(Object.class)).thenReturn(objectSerializer);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeObjectRef(Mockito.eq("existingId"));
    }

    @Test
    public void testWriteAsIdWithExistingIdAndAlwaysAsIdFlagSet() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = "existingId";

        ObjectIdWriter objectIdWriter = new ObjectIdWriter() {
            @Override
            public boolean alwaysAsId() {
                return true;
            }

            @Override
            public SerializableString propertyName() {
                return null;
            }

            @Override
            public void serialize(Object id, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // Do nothing, as this test only checks if the id was written as a field
            }
        };

        StdSerializer<Object> objectSerializer = Mockito.mock(StdSerializer.class);
        Mockito.when(serializerProvider.findValueSerializer(Object.class)).thenReturn(objectSerializer);

        StdSerializer<String> stringSerializer = Mockito.mock(StdSerializer.class);
        Mockito.when(objectMapper.findValueSerializer(String.class)).thenReturn(stringSerializer);

        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        Mockito.when(serializerProvider.findTypeSerializer(String.class, null)).thenReturn(typeSerializer);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeFieldName(Mockito.anyString());
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeString(Mockito.eq("existingId"));
    }

    @Test
    public void testWriteAsIdWithNullId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        ObjectIdWriter objectIdWriter = new ObjectIdWriter() {
            @Override
            public boolean alwaysAsId() {
                return false;
            }

            @Override
            public SerializableString propertyName() {
                return null;
            }
        };

        StdSerializer<Object> objectSerializer = Mockito.mock(StdSerializer.class);
        Mockito.when(serializerProvider.findValueSerializer(Object.class)).thenReturn(objectSerializer);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        Mockito.verify(jsonGenerator, Mockito.never()).writeObjectRef(Mockito.anyString());
    }

    @Test
    public void testGenerateId() throws IOException {
        Object forPojo = new Object();

        Object id = writableObjectId.generateId(forPojo);
        assertEquals(id, idGenerator.generateId(forPojo));
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = "existingId";

        ObjectIdWriter objectIdWriter = new ObjectIdWriter() {
            @Override
            public boolean alwaysAsId() {
                return false;
            }

            @Override
            public SerializableString propertyName() {
                return new SerializableString("testPropertyName");
            }
        };

        StdSerializer<Object> objectSerializer = Mockito.mock(StdSerializer.class);
        Mockito.when(serializerProvider.findValueSerializer(Object.class)).thenReturn(objectSerializer);

        StdSerializer<String> stringSerializer = Mockito.mock(StdSerializer.class);
        Mockito.when(objectMapper.findValueSerializer(String.class)).thenReturn(stringSerializer);

        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        Mockito.when(serializerProvider.findTypeSerializer(String.class, null)).thenReturn(typeSerializer);

        WritableObjectId expectedWritableObjectId = new WritableObjectId(idGenerator) {
            @Override
            public void writeAsField(JsonGenerator gen, SerializerProvider provider, ObjectIdWriter w) throws IOException {
                super.writeAsField(gen, provider, w);
                w.serializer.serialize(id, gen, provider);
            }
        };

        WritableObjectId actualWritableObjectId = new WritableObjectId(idGenerator);
        actualWritableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeFieldName(Mockito.eq("testPropertyName"));
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeString(Mockito.eq("existingId"));

        assertEquals(expectedWritableObjectId, actualWritableObjectId);
    }

}

class ObjectIdWriter {
    private boolean alwaysAsId;
    private SerializableString propertyName;

    public boolean alwaysAsId() {
        return alwaysAsId;
    }

    public void setAlwaysAsId(boolean alwaysAsId) {
        this.alwaysAsId = alwaysAsId;
    }

    public SerializableString propertyName() {
        return propertyName;
    }

    public void setPropertyName(SerializableString propertyName) {
        this.propertyName = propertyName;
    }

    public StdSerializer<Object> serializer;
}