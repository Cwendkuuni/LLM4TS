package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdSerializerTest {

    private ObjectMapper mapper;
    private StdSerializer<Object> stdSerializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString("Value: " + value.toString());
            }
        };
    }

    @Test
    public void testHandleType() throws JsonMappingException {
        assertEquals(Object.class, stdSerializer.handledType());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null);
        stdSerializer.acceptJsonFormatVisitor(visitor, null);
        // Default implementation specifies no format
        assertTrue(visitor.wasVisited(StdSerializer.class));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = null;
        ObjectNode schema = stdSerializer.getSchema(provider, Object.class);
        ObjectNode expectedSchema = createSchemaNode("string");
        assertEquals("string", schema.get("type").asText());
        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        SerializerProvider provider = null;
        ObjectNode schema = stdSerializer.getSchema(provider, Object.class, true);
        ObjectNode expectedSchema = createSchemaNode("string");
        assertTrue(schema.get("required").asBoolean());
        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testSerialize() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectNode jsonNode = mapper.createObjectNode();
        JsonGenerator jgen = jsonNode.getFactory().createGenerator(System.out);
        stdSerializer.serialize(new Object(), jgen, mapper.getSerializerProvider());
        String expectedOutput = "\"Value: null\"";
        assertEquals(expectedOutput, jsonNode.toString());
    }

    @Test
    public void testWrapAndThrow() {
        SerializerProvider provider = mapper.getSerializerProvider();
        Object bean = new Object();
        try {
            stdSerializer.wrapAndThrow(provider, new IOException("Test Exception"), bean, "test");
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Test Exception", e.getCause().getMessage());
            assertEquals("test", e.getPath());
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        assertTrue(stdSerializer.isDefaultSerializer(stdSerializer));
        assertFalse(stdSerializer.isDefaultSerializer(new ObjectSerializer()));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        ObjectNode jsonNode = mapper.createObjectNode();

        JsonSerializer<Object> existingSerializer = null;
        JsonSerializer<Object> resultSerializer = stdSerializer.findConvertingContentSerializer(provider, Object.class, jsonNode);

        assertEquals(existingSerializer, resultSerializer);

        JsonSerializer<String> stringConverterSerializer = mapper.findValueSerializer(String.class);
        existingSerializer = stringConverterSerializer;

        resultSerializer = stdSerializer.findConvertingContentSerializer(provider, String.class, jsonNode);

        assertNotEquals(existingSerializer, resultSerializer);
        assertTrue(resultSerializer instanceof StdDelegatingSerializer);
        Converter<Object, String> converter = ((StdDelegatingSerializer) resultSerializer).getDelegate();
        assertEquals(String.class, converter.getOutputType(mapper.getTypeFactory()).getRawClass());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = mapper.getSerializerProvider();
        FilterProvider filters = new SimpleFilterProvider();
        PropertyFilter filter = new IdentityFilter();
        filters.addFilter(PropertyFilter.FilterId.STDOUT, filter);

        provider.setFilterProvider(filters);
        Object valueToFilter = new Object();
        PropertyFilter resultFilter = stdSerializer.findPropertyFilter(provider, PropertyFilter.FilterId.STDOUT, valueToFilter);

        assertEquals(filter, resultFilter);
    }

    private ObjectNode createSchemaNode(String type) {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("type", type);
        return schema;
    }
}

class IdentityFilter implements PropertyFilter {

    @Override
    public void serializeAsId(SerializerProvider p, Type serType, PropertyDescriptor pd, JsonGenerator jg)
            throws IOException, JsonMappingException {
        jg.writeString(pd.getName());
    }

    @Override
    public void serializeAsId(SerializerProvider p, Type serType, PropertyDescriptor pd, Object bean, JsonGenerator jg)
            throws IOException, JsonMappingException {
        jg.writeString(pd.getName());
    }

    @Override
    public boolean isEmpty(SerializerProvider p, Type serType, JsonGenerator jg)
            throws IOException, JsonMappingException {
        return false;
    }
}

class ObjectSerializer extends JsonSerializer<Object> {

}