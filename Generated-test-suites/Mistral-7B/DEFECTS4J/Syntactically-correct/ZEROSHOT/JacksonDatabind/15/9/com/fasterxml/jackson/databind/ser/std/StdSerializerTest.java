package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdSerializerTest {

    private ObjectMapper objectMapper;
    private SerializerProvider serializerProvider;
    private StdSerializer<String> stringSerializer;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(Mockito.any(SerializationFeature.class))).thenAnswer(invocation -> true);
        when(serializerProvider.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));
        when(serializerProvider.getFilterProvider()).thenReturn(mock(FilterProvider.class));
        when(serializerProvider.findValueSerializer(Mockito.any(Class.class))).thenReturn(mock(JsonSerializer.class));
        when(serializerProvider.converterInstance(Mockito.any(AnnotatedMember.class), Mockito.any(Class.class))).thenReturn(mock(Converter.class));
        when(serializerProvider.getTypeFactory()).thenReturn(mock(TypeFactory.class));
        stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
                jgen.writeString(value);
            }
        };
    }

    @Test
    public void testHandledType() throws Exception {
        assertEquals("java.lang.String", stringSerializer.handledType().getName());
    }

    @Test
    public void testDefaultConstructor() throws Exception {
        StdSerializer<?> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

            }
        };
        assertEquals("java.lang.Object", serializer.handledType().getName());
    }

    @Test
    public void testJsonSerializerAbstractMethods() throws Exception {
        when(serializerProvider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenAnswer(invocation -> false);
        when(serializerProvider.getFilterProvider()).thenReturn(null);

        stringSerializer.serialize(null, mock(JsonGenerator.class), serializerProvider);
        stringSerializer.serialize("test", mock(JsonGenerator.class), serializerProvider);
        stringSerializer.serialize("test", null, serializerProvider);

        assertNull(stringSerializer.getSchema(serializerProvider, String.class));
        assertNull(stringSerializer.getSchema(serializerProvider, String.class, false));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    @Test
    public void testWrapAndThrow() throws Exception {
        IOException ioException = new IOException("Test IOException");
        JsonMappingException jsonMappingException = new JsonMappingException("Test JsonMappingException");
        when(serializerProvider.getFilterProvider()).thenReturn(null);

        stringSerializer.wrapAndThrow(serializerProvider, ioException, "test", 0);
        stringSerializer.wrapAndThrow(serializerProvider, jsonMappingException, "test", 0);

        stringSerializer.wrapAndThrow(serializerProvider, ioException, "test", -1);
        stringSerializer.wrapAndThrow(serializerProvider, jsonMappingException, "test", -1);
    }

    @Test
    public void testIsDefaultSerializer() throws Exception {
        JsonSerializer<String> serializer = mock(JsonSerializer.class);
        assertEquals(false, StdSerializer.class.isInstance(serializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        Converter<Object, Object> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> existingSerializer = mock(JsonSerializer.class);

        when(serializerProvider.converterInstance(mock(AnnotatedMember.class), Mockito.any(Class.class)))
                .thenReturn(converter);
        when(delegateType.getRawClass()).thenReturn(Object.class);

        PropertyFilter propertyFilter = mock(PropertyFilter.class);
        FilterProvider filterProvider = mock(FilterProvider.class);
        when(filterProvider.findPropertyFilter(Mockito.anyString(), Mockito.any(Object.class))).thenReturn(propertyFilter);

        JsonSerializer<Object> convertingContentSerializer = new StdDelegatingSerializer(converter, delegateType, existingSerializer);

        assertEquals(convertingContentSerializer, stringSerializer.findConvertingContentSerializer(serializerProvider, null, null));
        assertEquals(existingSerializer, stringSerializer.findConvertingContentSerializer(serializerProvider, null, null));
        assertEquals(existingSerializer, stringSerializer.findConvertingContentSerializer(serializerProvider, null, null));

        ObjectNode jsonNode = mock(ObjectNode.class);
        when(jsonNode.get("someKey")).thenReturn(jsonNode);

        JsonNode schemaNode = stringSerializer.getSchema(serializerProvider, String.class);
        assertEquals(schemaNode.getClass(), ObjectNode.class);

        JsonNode optionalSchemaNode = stringSerializer.getSchema(serializerProvider, String.class, true);
        assertEquals(optionalSchemaNode.getClass(), ObjectNode.class);

        JsonNode optionalSchemaNodeWithFilter = stringSerializer.getSchema(serializerProvider, String.class, true, "someKey", jsonNode);
        assertEquals(optionalSchemaNodeWithFilter.getClass(), ObjectNode.class);

        when(serializerProvider.getFilterProvider()).thenReturn(filterProvider);
        JsonSchema schema = mock(JsonSchema.class);
        when(schema.getSchema(Mockito.any(JavaType.class))).thenReturn(schemaNode);
        when(schema.getSchema(Mockito.any(JavaType.class), Mockito.anyBoolean())).thenReturn(optionalSchemaNode);
        when(schema.getSchema(Mockito.any(JavaType.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(JsonNode.class))).thenReturn(optionalSchemaNodeWithFilter);

        JsonSerializer<JsonNode> jsonNodeSerializer = mock(JsonSerializer.class);
        when(serializerProvider.findValueSerializer(Mockito.any(Class.class))).thenReturn(jsonNodeSerializer);

        JsonNode jsonNodeValue = mock(JsonNode.class);
        when(jsonNodeValue.get("someKey")).thenReturn(jsonNode);

        PropertyFilter propertyFilter2 = mock(PropertyFilter.class);
        when(propertyFilter.getFilterId()).thenReturn("someFilterId");
        when(propertyFilter.applyFilter(jsonNodeValue, serializerProvider)).thenReturn(jsonNodeValue);
        when(filterProvider.findPropertyFilter(Mockito.anyString(), jsonNodeValue)).thenReturn(propertyFilter2);

        JsonNode jsonNodeWithFilter = stringSerializer.getSchema(serializerProvider, String.class, true, "someKey", jsonNodeValue);
        assertEquals(jsonNodeWithFilter.getClass(), ObjectNode.class);

        ObjectNode jsonNodeSchema = createObjectNode();
        jsonNodeSchema.put("type", "string");
        when(schema.getSchemaProvider()).thenReturn(objectMapper.getFactory());
        when(schema.getSchema(Mockito.any(JavaType.class))).thenReturn(jsonNodeSchema);

        JsonNode jsonNodeSchemaOptional = createObjectNode();
        jsonNodeSchemaOptional.put("type", "string");
        jsonNodeSchemaOptional.put("required", false);
        when(schema.getSchema(Mockito.any(JavaType.class), Mockito.anyBoolean())).thenReturn(jsonNodeSchemaOptional);

        JsonNode jsonNodeSchemaOptionalWithFilter = createObjectNode();
        jsonNodeSchemaOptionalWithFilter.put("type", "string");
        jsonNodeSchemaOptionalWithFilter.put("required", false);
        jsonNodeSchemaOptionalWithFilter.put("properties", jsonNodeSchema);
        when(schema.getSchema(Mockito.any(JavaType.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any(JsonNode.class))).thenReturn(jsonNodeSchemaOptionalWithFilter);

        assertEquals(jsonNodeSchema, stringSerializer.getSchema(serializerProvider, String.class));
        assertEquals(jsonNodeSchemaOptional, stringSerializer.getSchema(serializerProvider, String.class, true));
        assertEquals(jsonNodeSchemaOptionalWithFilter, stringSerializer.getSchema(serializerProvider, String.class, true, "someKey", jsonNodeValue));
    }

    private ObjectNode createObjectNode() {
        return objectMapper.getNodeFactory().objectNode();
    }
}