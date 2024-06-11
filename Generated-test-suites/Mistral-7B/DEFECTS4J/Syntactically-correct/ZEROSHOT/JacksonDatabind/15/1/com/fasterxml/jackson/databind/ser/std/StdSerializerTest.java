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
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

/**
 * Test class for StdSerializer
 */
public class StdSerializerTest {

    @Test
    public void testConstructor() {
        Class<String> handledType = String.class;
        StdSerializer<String> serializer = new StdSerializer<>(handledType);
        assertEquals(handledType, serializer.handledType());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        assertTrue(visitor.expectAnyFormat(Object.class));
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode node = serializer.createSchemaNode("string");
        assertEquals("string", node.get("type").asText());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode schema = serializer.getSchema(null, String.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        JsonSerializer<Object> otherSerializer = new JsonStringSerializer();
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertFalse(StdSerializer.class.isInstance(otherSerializer));
        assertTrue(StdSerializer.isDefaultSerializer(serializer));
        assertFalse(StdSerializer.isDefaultSerializer(otherSerializer));
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        SerializerProvider provider = mapper.getSerializerProvider();
        try {
            serializer.serialize(null, null, provider);
            fail("Expected IOException");
        } catch (IOException e) {
            serializer.wrapAndThrow(provider, e, null, null);
            assertSame(e, serializer.wrapAndThrow(provider, e, null, null).getCause());
        }
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty();
        Converter<Object, Object> converter = (Converter<Object, Object>) () -> new ObjectNode(JsonNodeFactory.instance);
        StdSerializer<ObjectNode> delegateSerializer = new StdTextSerializer();
        JsonSerializer<ObjectNode> existingSerializer = new StdTextSerializer();
        ObjectNode node = converter.convertValue(null, provider.getTypeFactory().constructType(ObjectNode.class));
        StdSerializer<ObjectNode> convertingSerializer = serializer.findConvertingContentSerializer(provider, property, existingSerializer);
        assertSame(delegateSerializer.getClass(), convertingSerializer.getClass());
        assertSame(delegateSerializer.handledType(), convertingSerializer.handledType());
        assertEquals(ObjectNode.class, convertingSerializer.handledType());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        SerializerProvider provider = mapper.getSerializerProvider();
        Object filterId = "testFilter";
        FilterProvider filters = provider.getFilterProvider();
        PropertyFilter filter = filters.findPropertyFilter(filterId, null);
        assertEquals(filter, serializer.findPropertyFilter(provider, filterId, null));
    }
}