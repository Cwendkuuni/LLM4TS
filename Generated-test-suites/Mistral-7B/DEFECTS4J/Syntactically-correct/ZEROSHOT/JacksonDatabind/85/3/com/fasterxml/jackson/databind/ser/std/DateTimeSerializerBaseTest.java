import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseTest;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest extends BaseTest {

 private DateTimeSerializerBase<Long> serializerUnderTest;

 @Before
 public void setUp() {
 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 serializerUnderTest = new DateTimeSerializerBase<Long>(Long.TYPE, false, dateFormat);
 }

 @Test
 public void testCreateContextual() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializerUnderTest).getSerializerProvider().getJsonGenerator();

 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setLocale(Locale.US);
 format.setTimeZone(TimeZone.getDefault());
 format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 ContextualSerializer contextualSerializer = serializerUnderTest.createContextual(serializers, null);
 assertEquals(serializerUnderTest.getClass(), contextualSerializer.getClass());

 assertEquals(format.getShape(), contextualSerializer.getShape());
 assertEquals(format.getLocale(), contextualSerializer.getLocale());
 assertEquals(format.getTimeZone(), contextualSerializer.getTimeZone());
 assertEquals(format.getPattern(), contextualSerializer.getDateFormat().getPattern());
 }

 @Test
 public void testSerialize() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializerUnderTest).getSerializerProvider().getJsonGenerator();
 Long dateValue = 1597958400000L;

 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializerUnderTest).with(new WriteFeatures(Feature.WRITE_DATES_AS_TIMESTAMPS, false));
 String serializedDate = writer.writeValueAsString(dateValue);

 assertEquals("1970-01-01T03:11:40.000Z", serializedDate);

 gen.writeStartObject();
 SerializableSerializer serializer = serializerUnderTest.createContextual(serializers, null);
 serializer.serialize(dateValue, gen, serializers);
 gen.writeEndObject();

 String serializedDateFromSerializer = gen.getOutputContent().toString();
 assertEquals("1970-01-01T03:11:40.000Z", serializedDateFromSerializer);
 }

 @Test
 public void testSerializeWithCustomFormat() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializerUnderTest).getSerializerProvider().getJsonGenerator();
 Long dateValue = 1597958400000L;

 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 ContextualSerializer contextualSerializer = serializerUnderTest.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).createContextual(serializers, null);

 gen.writeStartObject();
 contextualSerializer.serialize(dateValue, gen, serializers);
 gen.writeEndObject();

 String serializedDateFromSerializer = gen.getOutputContent().toString();
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 Date date = dateFormat.parse("01-01-1970 03:11:40");
 assertEquals("{\"dd-MM-yyyy HH:mm:ss\":\"01-01-1970 03:11:40\"}", serializedDateFromSerializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithInvalidFormat() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonGenerator gen = mapper.getPolymorphicSerializer(serializerUnderTest).getSerializerProvider().getJsonGenerator();
 Long dateValue = 1597958400000L;

 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 ContextualSerializer contextualSerializer = serializerUnderTest.withFormat(false, null).createContextual(serializers, null);

 gen.writeStartObject();
 contextualSerializer.serialize(dateValue, gen, serializers);
 gen.writeEndObject();

 String serializedDateFromSerializer = gen.getOutputContent().toString();
 assertEquals("", serializedDateFromSerializer);
 }
}