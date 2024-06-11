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
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class StdSerializerTest {

    private StdSerializer<Object> serializer;
    private ObjectNode schemaNode;
    private JsonNodeFactory nodeFactory;
    private JsonGenerator generator;
    private SerializerProvider provider;
    private FilterProvider filterProvider;

    @Before
    public void setup() {
        nodeFactory = Mockito.mock(JsonNodeFactory.class);
        generator = Mockito.mock(JsonGenerator.class);
        provider = Mockito.mock(SerializerProvider.class);
        filterProvider = Mockito.mock(FilterProvider.class);
        schemaNode = Mockito.mock(ObjectNode.class);
        serializer = new StdSerializer<Object>(Object.class) {};
    }

    @Test
    public void testHandleType() {
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        // Stub the serialize method
        serializer.serialize(Mockito.any(), Mockito.eq(generator), Mockito.eq(provider));

        Object obj = new Object();
        serializer.serialize(obj, generator, provider);

        verify(generator).writeStartObject();
        verify(generator).writeEnd();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        when(serializer.getSchema(provider, Mockito.any()))
                .thenReturn(createSchemaNode("string"));

        assertEquals(createSchemaNode("string"), serializer.getSchema(provider, Object.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        when(serializer.acceptJsonFormatVisitor(Mockito.any(), Mockito.any()))
                .thenCallRealMethod();

        ObjectNode visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, Mockito.mock(JavaType.class));

        verify(visitor).expectAnyFormat(Mockito.any());
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        Throwable t = new RuntimeException("Test exception");
        when(provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(false);

        serializer.wrapAndThrow(provider, t, Mockito.any(), Mockito.anyString());

        assertEquals("Test exception", t.getMessage());
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        Converter<Object, Object> converter = Mockito.mock(Converter.class);
        JavaType delegateType = Mockito.mock(JavaType.class);
        StdSerializer<Object> existingSerializer = Mockito.mock(StdSerializer.class);
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, existingSerializer);

        when(provider.converterInstance(Mockito.any(), Mockito.any())).thenReturn(converter);
        when(provider.findValueSerializer(delegateType)).thenReturn(existingSerializer);

        JsonSerializer<?> result = serializer.findConvertingContentSerializer(provider, Mockito.mock(BeanProperty.class), null);

        assertEquals(delegatingSerializer, result);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);

        when(filters.findPropertyFilter(Mockito.anyString(), Mockito.any()))
                .thenReturn(filter);

        PropertyFilter result = serializer.findPropertyFilter(provider, "filterId", Mockito.any());

        assertEquals(filter, result);
    }

    private ObjectNode createSchemaNode(String type) {
        return (ObjectNode) schemaNode;
    }
}