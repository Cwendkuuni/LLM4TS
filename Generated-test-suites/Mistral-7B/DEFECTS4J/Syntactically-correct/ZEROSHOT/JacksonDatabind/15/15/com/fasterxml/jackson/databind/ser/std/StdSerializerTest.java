package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link StdSerializer}.
 */
public class StdSerializerTest {

    private StdSerializer<Integer> integerSerializer;
    private StdSerializer<String> stringSerializer;

    @Before
    public void setUp() {
        integerSerializer = new StdSerializer<Integer>(Integer.class);
        stringSerializer = new StdSerializer<String>(String.class);
    }

    @After
    public void tearDown() {
        integerSerializer = null;
        stringSerializer = null;
    }

    @Test
    public void testHandledType() {
        assertEquals(Integer.class, integerSerializer.handledType());
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectWriter writer = new ObjectMapper().writerFor(ObjectNode.class);
        ObjectNode node;

        node = writer.writeValueAsTree(integerSerializer.serialize(1, new JsonGenerator(), new SerializerProvider()));
        assertEquals("1", node.getTextValue());

        node = writer.writeValueAsTree(stringSerializer.serialize("test", new JsonGenerator(), new SerializerProvider()));
        assertEquals("test", node.getTextValue());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        assertEquals(createSchemaNode("integer"), integerSerializer.getSchema(new SerializerProvider(), null));
        assertEquals(createSchemaNode("string"), stringSerializer.getSchema(new SerializerProvider(), null));
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        assertEquals(createSchemaNode("integer"), integerSerializer.getSchema(new SerializerProvider(), null, false));
        assertEquals(createSchemaNode("string"), stringSerializer.getSchema(new SerializerProvider(), null, false));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper wrapper = new JsonFormatVisitorWrapper(null, null);
        integerSerializer.acceptJsonFormatVisitor(wrapper, null);
        stringSerializer.acceptJsonFormatVisitor(wrapper, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrow() {
        SerializerProvider provider = new SerializerProvider();
        Object bean = new Object();
        try {
            integerSerializer.wrapAndThrow(provider, new RuntimeException("Test"), bean, "field1");
        } catch (IOException e) {
            fail("IOException expected");
        }
        integerSerializer.wrapAndThrow(provider, new JsonMappingException("Test"), bean, "field1");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithIndex() {
        SerializerProvider provider = new SerializerProvider();
        Object bean = new Object();
        try {
            integerSerializer.wrapAndThrow(provider, new RuntimeException("Test"), bean, 0);
        } catch (IOException e) {
            fail("IOException expected");
        }
        integerSerializer.wrapAndThrow(provider, new JsonMappingException("Test"), bean, 0);
    }

    @Test
    public void testIsDefaultSerializer() {
        assertTrue(integerSerializer.isDefaultSerializer(integerSerializer));
        assertFalse(integerSerializer.isDefaultSerializer(new ObjectSerializer()));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty("test", Integer[].class);
        JsonSerializer<?> existingSerializer = new StdArraySerializer(Integer[].class);

        JsonSerializer<?> serializer = integerSerializer.findConvertingContentSerializer(provider, property, existingSerializer);
        assertTrue(serializer instanceof StdArraySerializer);
        assertEquals(Integer[].class, serializer.handledType());
    }

    @Test
    public void testFindPropertyFilter() {
        SerializerProvider provider = new SerializerProvider();
        Object valueToFilter = new Object();
        PropertyFilter filter = stringSerializer.findPropertyFilter(provider, "testFilter", valueToFilter);
        assertNull(filter);
    }

    private ObjectNode createSchemaNode(String type) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", type);
        return schema;
    }

}