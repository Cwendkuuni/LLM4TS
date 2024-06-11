package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderRef;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaGenerator;
import com.fasterxml.jackson.databind.jsonschema.SchemaProvider;

import com.fasterxml.jackson.test.BaseTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdGeneratorShapeAware;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest extends BaseTest {

    private ObjectIdGenerator<?> generator;
    private SerializerProvider provider;
    private ObjectIdWriter writer;
    private WriterObjectIdIdTestPojo pojo;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        provider = mapper.getSerializerProvider();
        generator = new ObjectIdGeneratorShapeAware();
        writer = new ObjectIdWriter();
        pojo = new WriterObjectIdIdTestPojo();
    }

    /**
     * Test method for {@link WritableObjectId#WritableObjectId(ObjectIdGenerator)}.
     */
    @Test
    public void testWritableObjectIdConstruction() {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId);
    }

    /**
     * Test method for {@link WritableObjectId#writeAsId(JsonGenerator, SerializerProvider, ObjectIdWriter)}.
     */
    @Test
    public void testWriteAsId() throws IOException, JsonProcessingException, JsonGenerationException {
        ObjectWriter writer = mapper.writerWithView(View.ANY);
        JsonGenerator gen = writer.getSerializerProvider().getJsonFactory().createGenerator(getTestOutputStream());

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = writableObjectId.generateId(pojo);
        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertTrue(result);
        assertNotNull(id);

        // Verify if the ObjectId has been written to the JSON stream
        String jsonString = getTestOutputStream().toString();
        JsonSchema schema = new JsonSchemaGenerator().generateSchema(getClass());
        assertTrue(schema.validate(new SchemaProvider() {
            @Override
            public JsonSchema getSchema(String id) {
                return null;
            }

            @Override
            public JsonSchema getSchema(Class<?> clazz) {
                return schema.getSchemaForClass(clazz);
            }
        }).getSchema(WriterObjectIdIdTestPojo.class));

        JsonParser parser = mapper.getFactory().createParser(new StringReader(jsonString));
        parser.nextToken();
        JsonToken token = parser.getCurrentToken();
        assertTrue(token.isStartObject());
        parser.nextToken();
        JsonToken expectedToken = JsonToken.FIELD_NAME;
        JsonToken actualToken = parser.getCurrentToken();
        assertEquals(expectedToken, actualToken);
        assertEquals("id", parser.getCurrentName());
        parser.nextToken();
        assertNotNull(parser.getValueAsId());
    }

    /**
     * Test method for {@link WritableObjectId#generateId(Object)}.
     */
    @Test
    public void testGenerateId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = writableObjectId.generateId(pojo);
        assertNotNull(id);
    }

    /**
     * Test method for {@link WritableObjectId#writeAsField(JsonGenerator, SerializerProvider, ObjectIdWriter)}.
     */
    @Test
    public void testWriteAsField() throws IOException, JsonProcessingException, JsonGenerationException {
        JsonGenerator gen = provider.getJsonFactory().createGenerator(getTestOutputStream());

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = writableObjectId.generateId(pojo);
        writableObjectId.writeAsField(gen, provider, writer);

        // Verify if the ObjectId has been written to the JSON stream
        String jsonString = getTestOutputStream().toString();
        JsonParser parser = mapper.getFactory().createParser(new StringReader(jsonString));
        parser.nextToken();
        JsonToken token = parser.getCurrentToken();
        assertTrue(token.isStartObject());
        parser.nextToken();
        JsonToken expectedToken = JsonToken.FIELD_NAME;
        JsonToken actualToken = parser.getCurrentToken();
        assertEquals(expectedToken, actualToken);
        assertEquals("id", parser.getCurrentName());
        parser.nextToken();
        assertNotNull(parser.getValueAsId());
    }

    /**
     * Test class for the Pojo used to generate ObjectId.
     */
    public static class WriterObjectIdIdTestPojo {
        @JsonSerialize(using = StdSerializer.class)
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public static class View extends BaseView {}
    }

    /**
     * Mock ObjectIdWriter.
     */
    static class ObjectIdWriter {
        boolean alwaysAsId = false;
        SerializerProvider serializer;
        SerializableString propertyName;

        public ObjectIdWriter() {
            // Constructor
        }

        public ObjectIdWriter(SerializerProvider serializer, SerializableString propertyName) {
            this();
            this.serializer = serializer;
            this.propertyName = propertyName;
        }

        public boolean isAlwaysAsId() {
            return alwaysAsId;
        }

        public void setAlwaysAsId(boolean alwaysAsId) {
            this.alwaysAsId = alwaysAsId;
        }

        public SerializerProvider getSerializerProvider() {
            return provider;
        }
    }
}