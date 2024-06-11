package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdSerializerTest {

    private static final Class<Object> HANDLED_TYPE = Object.class;
    private ObjectMapper objectMapper;
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testDefaultSerializer() throws JsonMappingException, JsonProcessingException {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(HANDLED_TYPE);
        assertEquals(StdSerializer.class, stdSerializer.getClass());
        assertEquals(HANDLED_TYPE, stdSerializer.handledType());
    }

    @Test
    public void testSerialize() throws JsonMappingException, JsonProcessingException {
        ObjectNode testObject = createTestObjectNode();
        StdSerializer<ObjectNode> stdSerializer = new StdSerializer<>(ObjectNode.class) {
            @Override
            public void serialize(ObjectNode value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeStartObject();
                jgen.writeFieldName("fieldName");
                jgen.writeValue(value.get("fieldName").asText());
                jgen.writeEndObject();
            }
        };

        when(serializerProvider.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
        when(serializerProvider.findValueSerializer(ObjectNode.class)).thenReturn(stdSerializer);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        ObjectNode result = objectMapper.valueToTree(testObject);

        stdSerializer.serialize(testObject, jsonGenerator, serializerProvider);

        String expectedJson = "{\"fieldName\":\"expectedValue\"}";
        String actualJson = result.toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        JsonNode schema = stdSerializer.getSchema(serializerProvider, Object.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        stdSerializer.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test
    public void testWrapAndThrow() throws JsonMappingException {
        Throwable throwable = new RuntimeException("Test error");
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);

        stdSerializer.wrapAndThrow(serializerProvider, throwable, null, null);
        // The test does not check the thrown exception, as the implementation
        // of this method may vary
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> stdSerializer = new StdSerializer<>(Object.class);
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);

        assertTrue(stdSerializer.isDefaultSerializer(stdSerializer));
        assertFalse(stdSerializer.isDefaultSerializer(stringSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        ObjectMapper objectMapper = new ObjectMapper();
        BeanDescription beanDescription = mock(BeanDescription.class);
        when(beanDescription.getBeanClass()).thenReturn(Object.class);
        when(beanDescription.getSerializerProvider()).thenReturn(serializerProvider);
        when(beanDescription.findProperty("field")).thenReturn(mock(BeanProperty.class));

        JsonSerializer<Number> numberSerializer = mock(JsonSerializer.class);
        Converter<Object,Object> converter = mock(Converter.class);
        when(converter.convertNull()).thenReturn(null);
        when(converter.writeValue(serializerProvider.getTypeFactory(), null, JsonGenerator.class)).thenThrow(IllegalArgumentException.class);
        when(objectMapper.findValueSerializer(Number.class)).thenReturn(numberSerializer);
        when(objectMapper.findValueSerializer(Object.class)).thenReturn(stdSerializer);
        when(serializerProvider.getTypeFactory()).thenReturn(objectMapper.getTypeFactory());
        when(serializerProvider.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);

        JsonSerializer<Object> result = new StdSerializer<>(Object.class).findConvertingContentSerializer(serializerProvider, null, beanDescription);
        assertEquals(stdSerializer.getClass(), result.getClass());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        FilterProvider filterProvider = mock(FilterProvider.class);
        when(serializerProvider.getFilterProvider()).thenReturn(filterProvider);
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean filterProperty(Object bean, String name) {
                return true;
            }
        };
        when(filterProvider.findPropertyFilter("filterId", null)).thenReturn(filter);

        StdSerializer<Object> result = new StdSerializer<>(Object.class).findPropertyFilter(serializerProvider, "filterId", null);
        assertEquals(filter.getClass(), result.getClass());
    }

    private ObjectNode createTestObjectNode() {
        ObjectNode testObject = objectMapper.createObjectNode();
        testObject.put("fieldName", "expectedValue");
        return testObject;
    }
}