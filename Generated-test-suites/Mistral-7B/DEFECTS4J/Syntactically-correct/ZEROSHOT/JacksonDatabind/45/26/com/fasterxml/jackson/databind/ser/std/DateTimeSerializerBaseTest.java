package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;
    private SerializerProvider serializerProvider;
    private JsonParser jsonParser;
    private Annotated annotated;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        serializerProvider = mock(SerializerProvider.class);
        jsonParser = mock(JsonParser.class);
        annotated = mock(Annotated.class);
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, null, null);
        assertEquals(Long.TYPE, serializer.getType());
        assertFalse(serializer.isUseTimestamp());
        assertNull(serializer.getCustomFormat());
    }

    @Test
    public void testBooleanUseTimestampConstructor() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        assertTrue(serializer.isUseTimestamp());
        assertNull(serializer.getCustomFormat());
    }

    @Test
    public void testDateFormatConstructor() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, customFormat);
        assertEquals(Long.TYPE, serializer.getType());
        assertFalse(serializer.isUseTimestamp());
        assertEquals(customFormat, serializer.getCustomFormat());
    }

    @Test
    public void testwithFormatMethod() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        DateTimeSerializerBase<Long> withTimestamp = serializer.withFormat(true, null);
        assertTrue(withTimestamp.isUseTimestamp());
        assertNull(withTimestamp.getCustomFormat());

        DateTimeSerializerBase<Long> withFormat = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        assertFalse(withFormat.isUseTimestamp());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"), withFormat.getCustomFormat());
    }

    @Test
    public void testCreateContextualMethod() throws IOException {
        when(annotated.getMember()).thenReturn(null);
        when(annotated.hasAnnotation(JsonFormat.class)).thenReturn(true);
        JsonFormat jsonFormat = mock(JsonFormat.class);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(jsonFormat.getPattern()).thenReturn("yyyy-MM-dd HH:mm:ss.SSS");
        when(jsonFormat.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(jsonFormat.getLocale()).thenReturn(Locale.getDefault());
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(jsonFormat);

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        JsonSerializer<Long> jsonSerializer = serializer.createContextual(serializerProvider, null);

        SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        expectedDateFormat.setTimeZone(TimeZone.getDefault());
        expectedDateFormat.setLocale(Locale.getDefault());
        JsonSerializer<Long> expectedJsonSerializer = serializer.withFormat(false, expectedDateFormat);

        assertEquals(expectedJsonSerializer.getClass(), jsonSerializer.getClass());
        assertEquals(expectedJsonSerializer, jsonSerializer);
    }

    @Test
    public void testCreateContextualMethodWhenNoJsonFormatAnnotation() throws IOException {
        when(annotated.getMember()).thenReturn(null);
        when(annotated.hasAnnotation(JsonFormat.class)).thenReturn(false);

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        JsonSerializer<Long> jsonSerializer = serializer.createContextual(serializerProvider, null);

        assertEquals(serializer.getClass(), jsonSerializer.getClass());
        assertEquals(serializer, jsonSerializer);
    }

    @Test
    public void testSerializeMethodWhenUseTimestampIsTrue() throws IOException, JsonProcessingException {
        Long value = System.currentTimeMillis();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeNumber(eq(value))).thenAnswer(invocation -> jsonGenerator.writeNumber(value.longValue()));

        serializer.serialize(value, jsonGenerator, serializerProvider);

        JsonParser jsonParser = objectMapper.getFactory().createParser(serializer.getClass().getName() + ": " + value);
        Long readValue = objectMapper.readValue(jsonParser, Long.class);

        assertEquals(value, readValue);
    }

    @Test
    public void testSerializeMethodWhenUseTimestampIsFalseAndCustomFormatIsProvided() throws IOException {
        Long value = System.currentTimeMillis();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeString(eq("2023-03-10 15:21:32.456"))).thenAnswer(invocation -> {});

        serializer.serialize(value, jsonGenerator, serializerProvider);

        JsonParser jsonParser = objectMapper.getFactory().createParser(serializer.getClass().getName() + ": " + value);
        JsonNode readValue = objectMapper.readTree(jsonParser);
        assertEquals("\"2023-03-10 15:21:32.456\"", readValue.toString());
    }

    @Test
    public void testSerializeMethodWhenUseTimestampIsFalseAndCustomFormatIsNotProvided() throws IOException {
        Long value = System.currentTimeMillis();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeNumber(eq(value))).thenAnswer(invocation -> jsonGenerator.writeNumber(value.longValue()));

        serializer.serialize(value, jsonGenerator, serializerProvider);

        JsonParser jsonParser = objectMapper.getFactory().createParser(serializer.getClass().getName() + ": " + value);
        Long readValue = objectMapper.readValue(jsonParser, Long.class);

        assertEquals(value, readValue);
    }

    @Test
    public void testAcceptJsonFormatVisitorMethod() throws IOException {
        JavaType javaType = TypeFactory.getDefault().constructType(Long.class);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        serializerProvider.setSerializationConfig(objectMapper.getSerializationConfig());

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        serializer._acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType, _asTimestamp(serializerProvider));

        verify(jsonFormatVisitorWrapper, times(1)).visitStringFormat(eq(serializer), eq(javaType), any(JsonValueFormat.class));
    }

    @Test
    public void testAcceptJsonFormatVisitorMethodWhenUseTimestampIsTrue() throws IOException {
        JavaType javaType = TypeFactory.getDefault().constructType(Long.class);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        serializerProvider.setSerializationConfig(objectMapper.getSerializationConfig());

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        serializer._acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType, _asTimestamp(serializerProvider));

        verify(jsonFormatVisitorWrapper, times(1)).visitIntFormat(eq(serializer), eq(javaType), any(JsonParser.NumberType.class), any(JsonValueFormat.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptJsonFormatVisitorMethodWithNullSerializerProvider() throws IOException {
        JavaType javaType = TypeFactory.getDefault().constructType(Long.class);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        serializer._acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType, null);
    }

    @Test
    public void testGetSchemaMethod() throws IOException {
        TypeFactory typeFactory = TypeFactory.getDefault();
        JavaType javaType = typeFactory.constructType(Long.class);

        JsonSerializer<Long> jsonSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };

        JsonNode schemaNode = jsonSerializer.getSchema(serializerProvider, javaType);
        assertEquals("number", schemaNode.getTextValue());
    }

    @Test
    public void testGetSchemaMethodWhenUseTimestampIsTrue() throws IOException {
        TypeFactory typeFactory = TypeFactory.getDefault();
        JavaType javaType = typeFactory.constructType(Long.class);

        JsonSerializer<Long> jsonSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };

        JsonNode schemaNode = jsonSerializer.getSchema(serializerProvider, javaType);
        assertEquals("number", schemaNode.getTextValue());
    }

    @Test
    public void testIsEmptyMethodWhenValueIsNull() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmptyMethodWhenValueIsEmptyDate() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        Long emptyDate = 0L;
        assertTrue(serializer.isEmpty(emptyDate));
    }

    @Test
    public void testIsEmptyMethodWhenValueIsNotNull() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        Long value = System.currentTimeMillis();
        assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmptyMethodWhenValueIsNullAndUseTimestampIsTrue() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmptyMethodWhenValueIsEmptyDateAndUseTimestampIsTrue() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        Long emptyDate = 0L;
        assertTrue(serializer.isEmpty(emptyDate));
    }

    @Test
    public void testIsEmptyMethodWhenValueIsNotNullAndUseTimestampIsTrue() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        Long value = System.currentTimeMillis();
        assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmptyMethodWithNullSerializerProvider() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        assertNull(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmptyMethodWithSerializerProvider() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        Long value = System.currentTimeMillis();
        assertFalse(serializer.isEmpty(serializerProvider, value));
    }

    @Test
    public void test_asTimestampMethodWhenUseTimestampIsTrue() {
        assertTrue(_asTimestamp(serializerProvider));
    }

    @Test
    public void test_asTimestampMethodWhenUseTimestampIsNull() {
        assertFalse(_asTimestamp(serializerProvider));
    }

    @Test
    public void test_asTimestampMethodWhenCustomFormatIsProvided() throws IOException {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        assertFalse(_asTimestamp(serializerProvider));
    }

    private boolean _asTimestamp(SerializerProvider serializers) {
        return serializers != null && serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}