package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializeDateUsingDefaultFormat() throws JsonProcessingException, IOException {
        JsonSerializer<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
        SerializerProvider serializers = mapper.getSerializerProvider();

        JavaType javaUtilDateType = mapper.getTypeFactory().constructType(java.util.Date.class);

        dateSerializer.acceptJsonFormatVisitor(mapper.getTypeSerializer(javaUtilDateType), javaUtilDateType, true);

        java.util.Date date = new java.util.Date();
        String expectedOutput = "\"2023-03-15T12:34:56.789Z\""; // current date and time in ISO 8601 format

        String actualOutput = mapper.writeValueAsString(date);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeDateUsingCustomFormat() throws JsonProcessingException, IOException {
        JsonSerializer<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);

        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setCurrentLocale(Locale.US);
        serializers.setTimeZone(TimeZone.getTimeZone("UTC"));

        JavaType javaUtilDateType = mapper.getTypeFactory().constructType(java.util.Date.class);

        dateSerializer.acceptJsonFormatVisitor(mapper.getTypeSerializer(javaUtilDateType), javaUtilDateType, false);

        String customFormatPattern = "dd-MM-yyyy HH:mm:ss z";
        JsonFormat.Value customFormat = new JsonFormat.Value();
        customFormat.setShape(JsonFormat.Shape.STRING);
        customFormat.setPattern(customFormatPattern);
        serializers.setAnnotationIntrospector(new StandardAnnotationIntrospector());

        java.util.Date date = new java.util.Date();

        String expectedOutput = "\"15-03-2023 12:34:56 EDT\""; // current date and time in custom format

        String actualOutput = mapper.writeValueAsString(date, new SimpleObjectSerializerModifier() {
            @Override
            public void modifySerializer(SerializerSerializerProvider serializers, Annotated member, SerializerSerializerModifier.SerializerSerializerModifier modifier) throws JsonMappingException {
                JsonSerializer<?> serializer = serializers.findValueSerializer(member.getType());
                if (serializer instanceof DateTimeSerializerBase) {
                    JsonFormat.Value customJsonFormat = new JsonFormat.Value();
                    customJsonFormat.setShape(JsonFormat.Shape.STRING);
                    customJsonFormat.setPattern(customFormatPattern);
                    serializer.acceptAnnotation(customJsonFormat);
                }
            }
        });

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeNullDate() throws JsonProcessingException {
        JsonSerializer<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);

        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setCurrentLocale(Locale.US);
        serializers.setTimeZone(TimeZone.getTimeZone("UTC"));

        JavaType javaUtilDateType = mapper.getTypeFactory().constructType(java.util.Date.class);

        dateSerializer.acceptJsonFormatVisitor(mapper.getTypeSerializer(javaUtilDateType), javaUtilDateType, true);

        java.util.Date date = null;

        String expectedOutput = "null";

        String actualOutput = mapper.writeValueAsString(date);

        assertNull(actualOutput);
    }

    @Test
    public void testSerializeInstantUsingDefaultFormat() throws
            JsonProcessingException, IOException {
        JsonSerializer<Instant> instantSerializer = mapper.findValueSerializer(Instant.class);
        SerializerProvider serializers = mapper.getSerializerProvider();

        JavaType javaInstantType = mapper.getTypeFactory().constructType(Instant.class);

        instantSerializer.acceptJsonFormatVisitor(mapper.getTypeSerializer(javaInstantType), javaInstantType,
                _asTimestamp(serializers));

        Instant instant = Instant.now();
        String expectedOutput = "\"2023-03-15T12:34:56.789Z\""; // current date and time in ISO 8601 format

        String actualOutput = mapper.writeValueAsString(instant);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeInstantUsingCustomFormat() throws JsonProcessingException, IOException {
        JsonSerializer<Instant> instantSerializer = mapper.findValueSerializer(Instant.class);

        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setCurrentLocale(Locale.US);
        serializers.setTimeZone(TimeZone.getTimeZone("UTC"));

        JavaType javaInstantType = mapper.getTypeFactory().constructType(Instant.class);

        instantSerializer.acceptJsonFormatVisitor(mapper.getTypeSerializer(javaInstantType), javaInstantType, false);

        String customFormatPattern = "dd-MM-yyyy HH:mm:ss z";
        JsonFormat.Value customFormat = new JsonFormat.Value();
        customFormat.setShape(JsonFormat.Shape.STRING);
        customFormat.setPattern(customFormatPattern);
        serializers.setAnnotationIntrospector(new StandardAnnotationIntrospector());

        Instant instant = Instant.now();

        String expectedOutput = "\"15-03-2023 12:34:56 EDT\""; // current date and time in custom format

        String actualOutput = mapper.writeValueAsString(instant, new SimpleObjectSerializerModifier() {
            @Override
            public void modifySerializer(SerializerSerializerProvider serializers, Annotated member, SerializerSerializerModifier.SerializerSerializerModifier modifier) throws JsonMappingException {
                JsonSerializer<?> serializer = serializers.findValueSerializer(member.getType());
                if (serializer instanceof DateTimeSerializerBase) {
                    JsonFormat.Value customJsonFormat = new JsonFormat.Value();
                    customJsonFormat.setShape(JsonFormat.Shape.STRING);
                    customJsonFormat.setPattern(customFormatPattern);
                    serializer.acceptAnnotation(customJsonFormat);
                }
            }
        });

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeNullInstant() throws JsonProcessingException {
        JsonSerializer<Instant> instantSerializer = mapper.findValueSerializer(Instant.class);

        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setCurrentLocale(Locale.US);
        serializers.setTimeZone(TimeZone.getTimeZone("UTC"));

        JavaType javaInstantType = mapper.getTypeFactory().constructType(Instant.class);

        instantSerializer.acceptJsonFormatVisitor(mapper.getTypeSerializer(javaInstantType), javaInstantType, true);

        Instant instant = null;

        String expectedOutput = "null";

        String actualOutput = mapper.writeValueAsString(instant);

        assertNull(actualOutput);
    }
}