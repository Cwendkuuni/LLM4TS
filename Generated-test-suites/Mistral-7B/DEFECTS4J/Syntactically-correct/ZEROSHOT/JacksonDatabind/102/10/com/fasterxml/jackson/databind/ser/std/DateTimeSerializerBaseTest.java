import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializerBase;
 private SerializerProvider serializers;
 private JsonGenerator generator;
 private JsonParser parser;

 @Before
 public void setup() {
 serializers = mock(SerializerProvider.class);
 generator = mock(JsonGenerator.class);
 parser = mock(JsonParser.class);
 dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 }

 @Test
 public void testCreateContextualWithDefault() throws IOException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 ObjectCodec codec = mock(ObjectCodec.class);
 when(serializers.getCodec()).thenReturn(codec);
 when(codec.findValueSerializer(Date.class)).thenReturn(dateTimeSerializerBase);

 dateTimeSerializerBase.createContextual(serializers, null);
 assertEquals(dateTimeSerializerBase, visitor.getSerializationConfig().findValueSerializer(Date.class));
 }

 @Test
 public void testCreateContextualWithFormat() throws IOException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 ObjectCodec codec = mock(ObjectCodec.class);
 when(serializers.getCodec()).thenReturn(codec);
 when(codec.findValueSerializer(Date.class)).thenReturn(dateTimeSerializerBase);

 JsonFormat format = mock(JsonFormat.class);
 JsonFormat.Shape shape = JsonFormat.Shape.STRING;
 when(format.getShape()).thenReturn(shape);
 when(format.getPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 when(format.getLocale()).thenReturn(Locale.ENGLISH);
 when(format.getTimeZone()).thenReturn(TimeZone.getDefault());

 when(serializers.findValueSerializer(JsonFormat.class, Date.class)).thenReturn(format);

 JsonFormatFormatVisitor formatVisitor = mock(JsonFormatFormatVisitor.class);
 when(visitor.getFormatVisitor()).thenReturn(formatVisitor);

 dateTimeSerializerBase.createContextual(serializers, null);
 assertEquals(DateFormat.class, formatVisitor.getClass());

 DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 df.setLocale(Locale.ENGLISH);
 df.setTimeZone(TimeZone.getDefault());
 assertEquals(df, formatVisitor.getDateFormat());
 }

 @Test
 public void testSerializeDateWithDefault() throws IOException {
 Date date = new Date();
 when(generator.writeNumber(date.getTime())).thenAnswer(invocation -> {
 String expected = Long.toString(date.getTime());
 generator.writeString(expected);
 return null;
 });

 dateTimeSerializerBase.serialize(date, generator, serializers);
 }

 @Test
 public void testSerializeDateWithFormat() throws IOException {
 Date date = new Date();
 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 format.setLocale(Locale.ENGLISH);
 format.setTimeZone(TimeZone.getDefault());

 when(generator.writeString(format.format(date))).thenAnswer(invocation -> {
 String expected = format.format(date);
 generator.writeString(expected);
 return null;
 });

 dateTimeSerializerBase = dateTimeSerializerBase.withFormat(Boolean.FALSE, format);

 dateTimeSerializerBase.serialize(date, generator, serializers);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeDateWithNullProvider() throws IOException {
 dateTimeSerializerBase.serialize(new Date(), generator, null);
 }

 @Test
 public void testIsEmpty() {
 assertFalse(dateTimeSerializerBase.isEmpty(serializers, null));
 }

 @Test
 public void testGetSchema() {
 assertEquals("string", dateTimeSerializerBase.getSchema(serializers, Date.class).getClassID());
 }
}