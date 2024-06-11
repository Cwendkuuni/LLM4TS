package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdSerializerTest {

    private ObjectMapper mapper;
    private SerializerProvider provider;
    private FilterProvider filters;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(Json.Include.NON_NULL);
        provider = mapper.getSerializerProvider();
        filters = mapper.getFilterProvider();
    }

    @Test
    public void testDefaultSerializer() throws JsonMappingException, JsonProcessingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertEquals("", serializer.createSchemaNode("string").toString());
        String value = "";
        ObjectNode result = mapper.writerWithRootValue(value).writeValueAsObject(provider);
        assertEquals("\"\"", result.toString());
        serializer.serialize(value, mapper.getFactory().createGenerator(provider), provider);
        assertNull(provider.getActiveContext().getCurrentValue());
    }

    @Test
    public void testCustomSchema() throws JsonMappingException, JsonProcessingException {
        StdSerializer<Integer> serializer = new MyIntSerializer();
        ObjectNode schema = serializer.getSchema(provider, Integer.TYPE);
        assertEquals("number", schema.get("type").asText());
        assertNull(schema.get("maximum"));
        assertNull(schema.get("minimum"));

        ObjectNode schemaWithRange = serializer.getSchema(provider, Integer.TYPE, true);
        assertEquals("number", schemaWithRange.get("type").asText());
        ObjectNode rangeNode = schemaWithRange.get("properties").get("value");
        assertEquals(1, rangeNode.size());
        ObjectNode rangeSchema = rangeNode.get(0);
        assertEquals("integer", rangeSchema.get("type").asText());
        assertEquals(0, rangeSchema.get("minimum").asInt());
        assertEquals(10, rangeSchema.get("maximum").asInt());
    }

    @Test
    public void testFiltering() throws JsonMappingException, JsonProcessingException {
        ObjectNode valueNode = createValueNode(1, "value");
        PropertyFilter filter = filters.findPropertyFilter("MyFilter", valueNode);
        ObjectNode filteredValueNode = filter.serializeAsSchema(provider, valueNode);
        assertEquals(0, filteredValueNode.size());

        StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.writeNull();
            }
        };
        ObjectNode serializedValue = serializer.serializeWithType(valueNode, provider);
        assertEquals("null", serializedValue.toString());
    }

    private ObjectNode createValueNode(int value, String name) {
        ObjectNode valueNode = mapper.createObjectNode();
        valueNode.put(name, value);
        return valueNode;
    }

    public static class MyIntSerializer extends StdSerializer<Integer> {
        public MyIntSerializer() {
            this(Integer.class);
        }

        public MyIntSerializer(JavaType type) {
            super(type);
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                throws JsonMappingException {
            return createSchemaNode("number", true, 0, 10);
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                throws JsonMappingException {
            visitor.writeNumberFormat(JsonFormat.Value.NUMBER_INT);
        }
    }
}