package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setUp() {
        objectMapper = new JsonMapper();
    }

    @Test
    public void testDefaultConstructor() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        Assert.assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testConstructorWithJavaType() {
        StdSerializer<String> serializer = new StdSerializer<String>(objectMapper.getTypeFactory().constructType(String.class));
        Assert.assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testConstructorWithClassAndBoolean() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class, false);
        Assert.assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        ObjectNode rootNode = objectMapper.createObjectNode();
        serializer.serialize("test", objectMapper.getFactory().createGenerator(rootNode), objectMapper.getSerializerProvider());
        ObjectNode stringNode = rootNode.get("test");
        Assert.assertEquals("test", stringNode.asText());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        ObjectNode schemaNode = serializer.getSchema(objectMapper.getSerializerProvider(), String.class);
        Assert.assertEquals("string", schemaNode.get("type").asText());
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        ObjectNode schemaNode = serializer.getSchema(objectMapper.getSerializerProvider(), String.class, false);
        Assert.assertEquals("string", schemaNode.get("type").asText());
        Assert.assertTrue(schemaNode.get("required").booleanValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        serializer.acceptJsonFormatVisitor(objectMapper.getSerializerProvider().getJsonFormatVisitorWrapper(null), String.class);
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        try {
            serializer.serialize(null, objectMapper.getFactory().createGenerator(new ObjectNode(JsonNodeFactory.instance)), provider);
            Assert.fail();
        } catch (JsonMappingException e) {
            serializer.wrapAndThrow(provider, e, null, null);
        }
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        Object valueToFilter = new Object();
        PropertyFilter propertyFilter = serializer.findPropertyFilter(provider, "filterId", valueToFilter);
        Assert.assertNull(propertyFilter);
    }
}