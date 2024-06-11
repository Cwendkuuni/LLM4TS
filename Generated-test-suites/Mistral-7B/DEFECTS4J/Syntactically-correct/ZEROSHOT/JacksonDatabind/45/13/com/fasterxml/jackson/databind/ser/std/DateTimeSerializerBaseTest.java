package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonFormat.Shape;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        // Test constructor
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, false, customFormat);

        // Test getters
        assertFalse(serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);

        // Test createContextual method
        BeanProperty property = Mockito.mock(BeanProperty.class);
        Annotated annotated = Mockito.mock(Annotated.class);
        AnnotationIntrospector introspector = Mockito.mock(AnnotationIntrospector.class);
        SerializerProvider serializers = new SerializerProviders.Base();

        JsonFormat.Value format = Mockito.mock(JsonFormat.Value.class);
        when(introspector.findFormat((Annotated) property)).thenReturn(format);
        when(format.getShape()).thenReturn(Shape.STRING);
        when(format.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd HH:mm:ss");
        when(format.hasLocale()).thenReturn(Locale.getDefault());

        JsonSerializer<DateTime> serialized = serializer.createContextual(serializers, property);
        assertTrue(serialized instanceof DateTimeSerializerBase);
        assertEquals(customFormat, ((DateTimeSerializerBase<DateTime>) serialized)._customFormat);

        // Test isEmpty method
        DateTime date = new DateTime();
        assertTrue(serializer.isEmpty(date));
        assertTrue(serializer.isEmpty(serializers, date));

        // Test getSchema method
        JsonNode schema = serializer.getSchema(serializers, DateTime.class);
        assertEquals("string", schema.getType().getClassName());

        // Test acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        Typing typing = Mockito.mock(Typing.class);
        when(visitor.getType()).thenReturn(typing);
        when(typing.handledType()).thenReturn(DateTime.class);

        serializer.acceptJsonFormatVisitor(visitor, null);
        verify(visitor).visitStringFormat(eq(DateTime.class), any(JsonValueFormat.class));

        // Test serialize method
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        try {
            serializer.serialize(new DateTime(), gen, serializers);
            verify(gen).writeStringValue("2023-03-29 10:30:45");
        } catch (JsonProcessingException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (JsonGenerationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}