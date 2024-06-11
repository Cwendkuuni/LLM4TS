package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultSerializerSerialization() throws JsonMappingException, IOException {
        StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        String value = "Test";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        stringSerializer.serialize(value, jsonGenerator, new SerializerProvider());
        ObjectNode JSONNode = objectMapper.readValue(System.out, ObjectNode.class);
        Assert.assertEquals("Test", JSONNode.getTextValue(""));
    }

    @Test
    public void testJsonFormatVisitableSerialization() throws JsonMappingException, IOException {
        StdSerializer<ObjectNode> objectNodeSerializer = new StdSerializer<ObjectNode>(ObjectNode.class) {
            @Override
            public ObjectNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                ObjectNode schema = createSchemaNode("null");
                schema.put("bsonType", "object");
                return schema;
            }
        };
        ObjectNode value = objectMapper.createObjectNode();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        objectNodeSerializer.serialize(value, jsonGenerator, new SerializerProvider());
        ObjectNode JSONNode = objectMapper.readValue(System.out, ObjectNode.class);
        Assert.assertEquals("null", JSONNode.get("type").asText());
        Assert.assertEquals("object", JSONNode.get("bsonType").asText());
    }

    @Test
    public void testSchemaAwareSerialization() throws JsonMappingException, IOException {
        StdSerializer<Number> numberSerializer = objectMapper.findValueSerializer(Number.class);
        Number value = 123;
        ObjectNode schemaNode = createSchemaNode("number");
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        numberSerializer.acceptJsonFormatVisitor(objectMapper.getSchemaFactory().getJsonFormatVisitor(schemaNode), Number.class);
        numberSerializer.serialize(value, jsonGenerator, new SerializerProvider());
        ObjectNode JSONNode = objectMapper.readValue(System.out, ObjectNode.class);
        Assert.assertEquals("number", JSONNode.get("type").asText());
    }

    @Test
    public void testExceptionHandling() throws JsonMappingException, IOException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                throw new IOException("Serialization error");
            }
        };
        Object value = new Object();
        try {
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
            objectSerializer.serialize(value, jsonGenerator, new SerializerProvider());
        } catch (IOException e) {
            // Expected
        }
    }

    private ObjectNode createSchemaNode(String type) {
        return objectMapper.getFactory().createObjectNode().put("type", type);
    }

    private ObjectNode createSchemaNode(String type, boolean isOptional) {
        ObjectNode schemaNode = createSchemaNode(type);
        schemaNode.put("required", !isOptional);
        return schemaNode;
    }
}