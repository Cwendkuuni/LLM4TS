package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private JsonMapper mapper;
    private StdSerializer<String> stringSerializer;

    @Before
    public void setup() {
        mapper = new JsonMapper();
        stringSerializer = new StdSerializer<String>(String.class);
    }

    @Test
    public void testHandleType() {
        Assert.assertEquals("class java.lang.String", stringSerializer.handledType().getName());
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        ObjectNode rootNode = mapper.createObjectNode();
        stringSerializer.serialize(null, mapper.getRootValueInstancer().createValueInstance(String.class), mapper, rootNode);
        Assert.assertNull(rootNode.get("value"));
    }

    @Test
    public void testGetSchema() {
        ObjectNode schemaNode = stringSerializer.getSchema(mapper.getSerializerProvider(), String.class);
        Assert.assertEquals("string", schemaNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFactory().getJsonFormatVisitor(mapper.getSerializerProvider(), String.class);
        stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
        Assert.assertEquals("STRING", visitor.getCurrentName());
    }

    @Test
    public void testWrapAndThrow() {
        try {
            mapper.serializeValue("", mapper.getSerializerProvider(), Object.class);
        } catch (IOException e) {
            stringSerializer.wrapAndThrow(mapper.getSerializerProvider(), e, null, "");
            Assert.fail("Expected JsonMappingException");
        }

        try {
            throw new RuntimeException();
        } catch (IOException e) {
            Assert.fail("Unexpected JsonMappingException");
        } finally {
            stringSerializer.wrapAndThrow(mapper.getSerializerProvider(), new RuntimeException(), null, "");
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);
        Assert.assertTrue(StdSerializer.class.isInstance(serializer));
        Assert.assertTrue(stringSerializer.isDefaultSerializer(serializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        JsonSerializer<List<Integer>> listSerializer = mapper.findValueSerializer(List.class);
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode node = mapper.createObjectNode();
        ObjectNode propertyNode = mapper.createObjectNode();
        propertyNode.put("name", "list");
        propertyNode.set("contentSerializer", mapper.findValueSerializer(Integer[].class));
        node.set("properties", propertyNode);
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        BeanProperty property = BeanProperty.construct(null, "list", List.class, provider.getTypeFactory());
        Assert.assertEquals(listSerializer, stringSerializer.findConvertingContentSerializer(provider, property, null));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        PropertyFilter filter = stringSerializer.findPropertyFilter(mapper.getSerializerProvider(), "filterId", null);
        Assert.assertNull(filter);
    }
}