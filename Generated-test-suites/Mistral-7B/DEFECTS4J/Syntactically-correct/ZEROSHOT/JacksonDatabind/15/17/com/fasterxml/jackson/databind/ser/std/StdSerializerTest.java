package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * Test class for {@link StdSerializer}
 */
public class StdSerializerTest {

    @Test
    public void testStdSerializer() {
        // Test default constructor
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, stringSerializer.handledType());

        // Test constructor with JavaType
        StdSerializer<Integer> intSerializer = new StdSerializer<Integer>(JsonTypeFactory.instance.constructType(int.class));
        assertEquals(int.class, intSerializer.handledType());

        // Test constructor with Class and boolean
        StdSerializer<Double> doubleSerializer = new StdSerializer<Double>(Double.class, false);
        assertEquals(Double.class, doubleSerializer.handledType());
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        // Create a mock JsonSerializer for testing
        class MockSerializer extends StdSerializer<String> {
            public MockSerializer() { super(String.class); }

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        }

        MockSerializer<String> mockSerializer = new MockSerializer();

        // Test serialization
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
        JsonGenerator gen = JsonFactory.instance.createGenerator(new ByteArrayOutputStream());
        mockSerializer.serialize(null, gen, null);
        gen.flush();
        String serializedData = new String(gen.getOutputBuffer().toByteArray());
        assertEquals("{}", serializedData);

        rootNode.put("value", JsonNodeFactory.instance.textNode("test"));
        gen.setRootNode(rootNode);
        mockSerializer.serialize(null, gen, null);
        serializedData = new String(gen.getOutputBuffer().toByteArray());
        assertEquals("{\"value\":\"test\"}", serializedData);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);

        ObjectNode schema = (ObjectNode) stringSerializer.getSchema(null, null);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaWithIsOptional() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);

        ObjectNode schema = (ObjectNode) stringSerializer.getSchema(null, null, true);
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.getBoolean("required"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);

        JsonSerializerProvider provider = new JsonSerializerProvider();
        JsonFormatVisitorWrapper visitor = provider.getFormatVisitorFactory().createJsonFormatVisitor(null, null, null, null);
        stringSerializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrow() throws IOException {
        SerializerProvider provider = new SerializerProvider();
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class);

        // Test with non-JsonMappingException
        wrapAndThrow(provider, new RuntimeException("Test RuntimeException"), null, "test");

        // Test with JsonMappingException
        throwJsonMappingException(provider, "Test JsonMappingException", null, "test");
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("list", JsonNodeFactory.instance.arrayNode(new Integer[]{1, 2, 3}));

        StdSerializer<ObjectNode> nodeSerializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        JsonSerializer<Integer[]> arraySerializer = new StdListSerializer<>(Integer[].class, null, null, null, null, null, null, null);

        JsonSerializer<ObjectNode> serializer = nodeSerializer.findConvertingContentSerializer(provider, null, node);
        assertEquals(arraySerializer.getClass(), serializer.getClass());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();

        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class);

        Object filterId = new Object();
        PropertyFilter filter = objectSerializer.findPropertyFilter(provider, filterId, null);
        assertNull(filter);
    }

    private void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, String fieldName)
            throws IOException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class);
        objectSerializer.wrapAndThrow(provider, t, bean, fieldName);
        fail("Test should have thrown JsonMappingException");
    }

    private void throwJsonMappingException(SerializerProvider provider, String message, Object bean, int index)
            throws JsonMappingException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class);
        objectSerializer.wrapAndThrow(provider, new JsonMappingException(message), bean, index);
    }
}