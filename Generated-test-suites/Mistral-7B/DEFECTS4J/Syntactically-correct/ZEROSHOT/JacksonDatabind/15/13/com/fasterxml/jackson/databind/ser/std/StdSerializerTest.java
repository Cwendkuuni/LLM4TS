package com.fasterxml.jackson.databind.ser.std;

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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    private StdSerializer<Object> serializer;
    private ObjectNode schemaNode;

    @Before
    public void setUp() {
        serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                // Empty implementation
            }
        };
        schemaNode = JsonNodeFactory.instance.objectNode();
    }

    @Test
    public void testDefaultSerialization() throws JsonMappingException {
        ObjectNode result = mapper.writeValueAsObject(new Object());
        assertEquals(result.getType(), JsonNodeType.OBJECT);
        assertNull(serializer.getSchema(null, Object.class));
        serializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        schemaNode = serializer.getSchema(null, Object.class);
        assertEquals(schemaNode.get("type"), "string");
        assertNotNull(serializer.getSchema(null, Object.class, true));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetSchemaNullProvider() throws JsonMappingException {
        schemaNode = serializer.getSchema(null, Object.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithTypeHint() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, JsonType.construct(String.class));
    }

    @Test
    public void testIsDefaultSerializer() throws JsonMappingException {
        assertTrue(StdSerializerTest.class.newInstance()
                .isDefaultSerializer(serializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findProperty("test", String[].class);
        JsonSerializer<Object> existingSerializer = mapper.findValueSerializer(JsonType.construct(String[].class));
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.getOutputType(Mockito.any(JsonTypeFactory.class))).thenReturn(JsonType.construct(String.class));
        SerializerProvider provider = mapper.getSerializerProvider();
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(introspector.findSerializationContentConverter(member)).thenReturn(new JacksonAnnotation(converter));
        JsonSerializer<?> result = serializer.findConvertingContentSerializer(provider, property, existingSerializer);
        assertTrue(result instanceof StdDelegatingSerializer);
        StdDelegatingSerializer delegatingSerializer = (StdDelegatingSerializer) result;
        assertEquals(converter, delegatingSerializer.getConvertingContentConverter());
        assertEquals(JsonType.construct(String[].class), delegatingSerializer.delegateTo());
        assertEquals(existingSerializer, delegatingSerializer.delegateFrom());
    }

    @Test
    public void testFindConvertingContentSerializerNullProvider() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findProperty("test", String[].class);
        JsonSerializer<Object> existingSerializer = mapper.findValueSerializer(JsonType.construct(String[].class));
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.getOutputType(Mockito.any(JsonTypeFactory.class))).thenReturn(JsonType.construct(String.class));
        SerializerProvider provider = null;
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(introspector.findSerializationContentConverter(member)).thenReturn(new JacksonAnnotation(converter));
        JsonSerializer<?> result = serializer.findConvertingContentSerializer(provider, property, existingSerializer);
        assertNull(result);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        Object filterId = "testFilter";
        PropertyFilter result = serializer.findPropertyFilter(provider, filterId, new Object());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterNullProvider() throws JsonMappingException {
        SerializerProvider provider = null;
        Object filterId = "testFilter";
        PropertyFilter result = serializer.findPropertyFilter(provider, filterId, new Object());
    }

    @Test
    public void testFindPropertyFilterWithFilter() throws JsonMappingException {
        FilterProvider filters = new FilterProviderDefaultImpl();
        Object filterId = "testFilter";
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);
        when(filters.findPropertyFilter(filterId, Mockito.any(Object.class))).thenReturn(filter);
        SerializerProvider provider = new SerializerProvider() {
            @Override
            public FilterProvider getFilterProvider() {
                return filters;
            }
        };
        PropertyFilter result = serializer.findPropertyFilter(provider, filterId, new Object());
        assertEquals(filter, result);
    }
}

class FilterProviderDefaultImpl implements FilterProvider {
    @Override
    public PropertyFilter findPropertyFilter(String filterId, Object valueToFilter) {
        return null;
    }
}