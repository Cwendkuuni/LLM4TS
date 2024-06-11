package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.TestBase;

public class DateTimeSerializerBaseTest extends MockObjectTestCase {

private SerializerProvider serializers;
private JsonGenerator gen;
private JsonParser parser;

@Before
public void setUp() throws Exception {
SerializerProviderFactory serializersFactory = new SimpleSerializerProviderFactory();
serializers = serializersFactory.getProvider();
gen = serializers.getFactory().createGenerator(new ByteArrayOutputStream(), serializers);
parser = serializers.getFactory().createParser("2019-07-15T14:15:16.789Z");
}

@Test
public void testCreateContextualSerializer_nullProperty() throws JsonMappingException {
DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null);
assertEquals(serializer, serializer.createContextual(serializers, null));
}

@Test
public void testCreateContextualSerializer_defaultFormat() throws JsonMappingException {
JsonFormat format = new JsonFormat();
format.setShape(JsonFormat.Shape.NUMBER);
Annotated annotated = mock(Annotated.class);
when(annotated.hasAnnotation(JsonFormat.class)).thenReturn(true);
when(annotated.annotations()).thenReturn(new JsonFormat[]{format});
DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, null, null);
assertEquals(serializer, serializer.createContextual(serializers, null));
verify(annotated).hasAnnotation(JsonFormat.class);
}

@Test
public void testCreateContextualSerializer_customFormat() throws JsonMappingException {
JsonFormat format = new JsonFormat();
format.setShape(JsonFormat.Shape.STRING);
format.setTimeZone(TimeZone.getTimeZone("UTC"));
format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
Annotated annotated = mock(Annotated.class);
when(annotated.hasAnnotation(JsonFormat.class)).thenReturn(true);
when(annotated.annotations()).thenReturn(new JsonFormat[]{format});
DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null);
assertEquals(serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)), serializer.createContextual(serializers, null));
verify(annotated).hasAnnotation(JsonFormat.class);
}

@Test
public void testCreateContextualSerializer_customFormatWithTimestamp() throws JsonMappingException {
JsonFormat format = new JsonFormat();
format.setShape(JsonFormat.Shape.NUMBER);
format.setTimeZone(TimeZone.getTimeZone("UTC"));
format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
Annotated annotated = mock(Annotated.class);
when(annotated.hasAnnotation(JsonFormat.class)).thenReturn(true);
when(annotated.annotations()).thenReturn(new JsonFormat[]{format});
DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null);
assertEquals(serializer.withFormat(Boolean.TRUE, null), serializer.createContextual(serializers, null));
verify(annotated).hasAnnotation(JsonFormat.class);
}

@Test
public void testSerialize_numericFormat() throws IOException {
DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, Boolean.TRUE, null);
Long value = 1569971200000L; // Mon Jan 14 00:00:00 UTC 2019
serializer.serialize(value, gen, serializers);
assertEquals("1569971200000", gen.getOutputBuffer().toString().trim());
}

@Test
public void testSerialize_stringFormat() throws IOException {
DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US));
String value = "2019-07-15T14:15:16.789Z";
serializer.serialize(value, gen, serializers);
assertEquals("\"2019-07-15T14:15:16.789Z\"", gen.getOutputBuffer().toString().trim());
}

@Test
public void testAcceptJsonFormatVisitor() throws JsonMappingException {
JsonFormatVisitorWrapper visitor = createMock(JsonFormatVisitorWrapper.class);
when(visitor.getProvider()).thenReturn(serializers);
DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US));
serializer.acceptJsonFormatVisitor(visitor, null);
verify(visitor).visitStringFormat(any(JavaType.class), eq(JsonValueFormat.DATE_TIME), any(Object.class));
}

@Test
public void testGetSchema() throws IOException {
DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, Boolean.TRUE, null);
assertEquals("number", serializer.getSchema(serializers, null.getClass()).get("type").asText());
}

@Test
public void testIsEmpty() throws IOException {
DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, Boolean.TRUE, null);
Long value = null;
assertTrue(serializer.isEmpty(value));
value = 0L;
assertTrue(serializer.isEmpty(serializers, value));
}

class SimpleSerializerProviderFactory extends DefaultSerializerProviderFactory {
@Override
public SerializerProvider getProvider() {
return new SimpleSerializerProvider(new ObjectMapper(new AnnotationIntrospector()));
}
}

class SimpleSerializerProvider extends SerializerProvider {
private ObjectMapper mapper;

public SimpleSerializerProvider(ObjectMapper mapper) {
this.mapper = mapper;
}

@Override
public JsonFactory getFactory() {
return mapper;
}

@Override
public AnnotationIntrospector getAnnotationIntrospector() {
return mapper.getAnnotationIntrospector();
}
}
}