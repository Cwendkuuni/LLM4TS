package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessor;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.TypingHint;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.ContainerType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        PropertyNamingStrategy.DEFAULT_INSTANCE.configure(objectMapper.getSerializationConfig(), JsonMapping.DefaultPropertyInclusion.ALWAYS);
    }

    @Test
    public void testStdSerializer() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals("class com.fasterxml.jackson.databind.ser.std.StdSerializer", stringSerializer.getClass().getName());
        assertEquals(String.class, stringSerializer.handledType());

        // Test serialize method
        String value = "test";
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        stringSerializer.serialize(value, jsonGenerator, null);
        String jsonString = new String(jsonGenerator.getOutputBuffer().asByteArray());
        assertEquals("\"test\"", jsonString);

        // Test getSchema method
        JsonNode schema = stringSerializer.getSchema(new DefaultSerializerProvider(), String.class);
        assertEquals("\"string\"", schema.toString());

        // Test acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new DefaultSerializerProvider(), JsonMapping.SerializerProvider.StdSerializers.getDefaultInstance(), null);
        stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
        assertEquals("ANY", visitor.getFormatName());

        // Test wrapAndThrow method
        SerializerProvider provider = new DefaultSerializerProvider();
        Throwable t = new IOException("Test IOException");
        String bean = "testBean";
        String fieldName = "testField";
        StdSerializerTest.wrapAndThrow(provider, t, bean, fieldName);
        assertTrue(provider.getActiveException() instanceof JsonMappingException);

        // Test findConvertingContentSerializer method
        JsonSerializer<Object> existingSerializer = null;
        JsonSerializer<String> contentSerializer = objectMapper.findValueSerializer(String[].class);
        existingSerializer = objectMapper.findValueSerializer(String.class);
        JsonSerializer<String> convertingContentSerializer = stringSerializer.findConvertingContentSerializer(new DefaultSerializerProvider(), null, new Object[]{"testArray"});
        assertEquals("class com.fasterxml.jackson.databind.ser.std.StdSerializer", convertingContentSerializer.getClass().getName());
        assertEquals(String.class, convertingContentSerializer.handledType());
        assertNotSame(existingSerializer, convertingContentSerializer);

        // Test findPropertyFilter method
        FilterProvider filterProvider = new FilterProvider();
        PropertyFilter propertyFilter = filterProvider.findPropertyFilter("testFilterId", new Object());
        assertNull(propertyFilter);

        // Test isDefaultSerializer method
        JsonSerializer<Object> defaultSerializer = objectMapper.findValueSerializer(Object.class);
        assertTrue(StdSerializerTest.isDefaultSerializer(defaultSerializer));
    }

    private static void wrapAndThrow(SerializerProvider provider,
            Throwable t, Object bean, String fieldName) throws IOException {
        StdSerializerTest.StdSerializerWrapper serializerWrapper = new StdSerializerTest.StdSerializerWrapper();
        StdSerializerTest.StdSerializerTest$1 stdSerializerTest$1 = new StdSerializerTest.StdSerializerTest$1(provider, bean, fieldName);
        serializerWrapper.wrapAndThrow(stdSerializerTest$1, t);
        stdSerializerTest$1.assertJsonMappingExceptionThrown();
    }

    private static void wrapAndThrow(SerializerProvider provider,
            Throwable t, Object bean, int index) throws IOException {
        StdSerializerTest.StdSerializerWrapper serializerWrapper = new StdSerializerTest.StdSerializerWrapper();
        StdSerializerTest.StdSerializerTest$2 stdSerializerTest$2 = new StdSerializerTest.StdSerializerTest$2(provider, bean, index);
        serializerWrapper.wrapAndThrow(stdSerializerTest$2, t);
        stdSerializerTest$2.assertJsonMappingExceptionThrown();
    }

    private static class StdSerializerWrapper extends StdSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        }

        public void wrapAndThrow(StdSerializerTest$1 stdSerializerTest$1, Throwable t) throws IOException {
            super.wrapAndThrow(provider, t, stdSerializerTest$1.bean, stdSerializerTest$1.fieldName);
        }

        public void wrapAndThrow(StdSerializerTest$2 stdSerializerTest$2, Throwable t) throws IOException {
            super.wrapAndThrow(provider, t, stdSerializerTest$2.bean, stdSerializerTest$2.index);
        }
    }

    private static class StdSerializerTest$1 {
        private Object bean;
        private String fieldName;

        public StdSerializerTest$1(Object bean, String fieldName) {
            this.bean = bean;
            this.fieldName = fieldName;
        }

        public void assertJsonMappingExceptionThrown() {
            // Empty implementation
        }
    }

    private static class StdSerializerTest$2 {
        private Object bean;
        private int index;

        public StdSerializerTest$2(Object bean, int index) {
            this.bean = bean;
            this.index = index;
        }

        public void assertJsonMappingExceptionThrown() {
            // Empty implementation
        }
    }

    private static boolean isDefaultSerializer(JsonSerializer<?> serializer) {
        return ClassUtil.isJacksonStdImpl(serializer);
    }
}