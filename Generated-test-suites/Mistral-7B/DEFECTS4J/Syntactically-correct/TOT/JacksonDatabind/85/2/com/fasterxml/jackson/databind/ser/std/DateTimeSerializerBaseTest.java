import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateTimeSerializerBaseTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializationWithDefaultSettings() throws IOException {
 DateTimeSerializerBase<Long> serializer = mapper.findValueSerializer(Long.class);
 Long date = 1623356800000L; // 2021-03-15 00:00:00.000
 String json = "\"\\/Number\\\":\\" + date + "\"";
 assertEquals(json, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date));
 }

 @Test
 public void testSerializationWithCustomFormat() throws IOException {
 DateTimeSerializerBase<String> serializer = mapper.findValueSerializer(String.class);
 serializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
 String date = "25-12-2021 13:30:00";
 String json = "\"25-12-2021 13:30:00\"";
 assertEquals(json, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date));
 }

 @Test
 public void testSerializationWithUseTimestampFlag() throws IOException {
 DateTimeSerializerBase<Long> serializer = mapper.findValueSerializer(Long.class);
 serializer = serializer.withFormat(Boolean.TRUE, null);
 Long date = 1623356800000L; // 2021-03-15 00:00:00.000
 String json = "1623356800000";
 assertEquals(json, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithNullValue() throws IOException {
 DateTimeSerializerBase<String> serializer = mapper.findValueSerializer(String.class);
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(null);
 assertEquals("", json); // Should not reach this point
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidFormat() throws IOException, ParseException {
 DateTimeSerializerBase<String> serializer = mapper.findValueSerializer(String.class);
 serializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss-z"));
 String date = "25-12-2021 13:30:00-05:00";
 mapper.readerForUpdating(Object.class).readValue(mapper.readerForUpdating(String.class).readValue(date), DateTimeSerializerBase.class);
 }

 @Test
 public void testDeserializationWithDefaultSettings() throws IOException {
 DateTimeSerializerBase<Long> deserializer = mapper.findValueDeserializer(Long.class);
 String json = "\"1623356800000\"";
 Long date = deserializer.deserialize(mapper.readTree(json), null);
 assertEquals(1623356800000L, date.longValue()); // 2021-03-15 00:00:00.000
 }

 @Test
 public void testDeserializationWithCustomFormat() throws IOException, ParseException {
 DateTimeSerializerBase<String> deserializer = mapper.findValueDeserializer(String.class);
 deserializer = deserializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
 String json = "\"25-12-2021 13:30:00\"";
 String date = deserializer.deserialize(mapper.readTree(json), null).getValueAsString();
 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
 Date parsedDate = formatter.parse(date);
 assertEquals("25-12-2021 13:30:00", formatter.format(parsedDate));
 }

 @Test
 public void testDeserializationWithUseTimestampFlag() throws IOException {
 DateTimeSerializerBase<Long> deserializer = mapper.findValueDeserializer(Long.class);
 deserializer = deserializer.withFormat(Boolean.TRUE, null);
 String json = "1623356800000";
 Long date = deserializer.deserialize(mapper.readTree(json), null);
 assertEquals(1623356800000L, date.longValue()); // 2021-03-15 00:00:00.000
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithNullValue() throws IOException {
 DateTimeSerializerBase<String> deserializer = mapper.findValueDeserializer(String.class);
 String json = "null";
 deserializer.deserialize(mapper.readTree(json), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidFormat() throws IOException, ParseException {
 DateTimeSerializerBase<String> deserializer = mapper.findValueDeserializer(String.class);
 deserializer = deserializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss-z"));
 String json = "\"25-12-2021 13:30:00-05:00\"";
 DateTimeSerializerBase deserialized = deserializer.deserialize(mapper.readTree(json), null);
 assertEquals(null, deserialized);
 }
}