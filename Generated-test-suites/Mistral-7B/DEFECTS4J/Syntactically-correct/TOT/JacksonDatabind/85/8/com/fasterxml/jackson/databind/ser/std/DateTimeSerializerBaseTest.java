import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @BeforeClass
 public static void setUp() {
 mapper.setTimeZone(TimeZone.getDefault());
 }

 @Test
 public void testDefaultSerialization() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
 Date date = new Date();
 String json = mapper.writeValueAsString(date);
 JsonParser parser = mapper.getFactory().createParser(json);
 Date deserializedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testCustomFormatSerialization() throws JsonProcessingException {
 DateTimeSerializerBase<String> stringSerializer = mapper.findValueSerializer(String.class)
 .withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
 String dateString = "25-12-2021";
 String json = mapper.writeValueAsString(dateString);
 JsonParser parser = mapper.getFactory().createParser(json);
 String deserializedDateString = mapper.readValue(parser, String.class);
 SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
 Date date = format.parse(dateString);
 assertEquals(dateString, deserializedDateString);
 assertEquals(date, mapper.readValue(parser, Date.class));
 }

 @Test
 public void testUseTimestampSerialization() throws JsonProcessingException {
 DateTimeSerializerBase<Long> timestampSerializer = mapper.findValueSerializer(Long.class).withFormat(Boolean.TRUE, null);
 Date date = new Date();
 long timestamp = date.getTime();
 String json = mapper.writeValueAsString(timestamp);
 JsonParser parser = mapper.getFactory().createParser(json);
 long deserializedTimestamp = mapper.readValue(parser, Long.class);
 Date deserializedDate = new Date(deserializedTimestamp);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonProcessingException {
 DateTimeSerializerBase<Object> nullSerializer = mapper.findValueSerializer(Object.class);
 String json = mapper.writeValueAsString(null);
 JsonParser parser = mapper.getFactory().createParser(json);
 Object deserializedValue = mapper.readValue(parser, Object.class);
 assertNotNull(deserializedValue);
 }

 @Test
 public void testSerializationWithInvalidValue() throws JsonProcessingException {
 DateTimeSerializerBase<String> stringSerializer = mapper.findValueSerializer(String.class)
 .withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
 String invalidDateString = "32-12-2021";
 String json = mapper.writeValueAsString(invalidDateString);
 try {
 JsonParser parser = mapper.getFactory().createParser(json);
 mapper.readValue(parser, Date.class);
 fail("Expected JsonProcessingException");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 @After
 public void tearDown() throws Exception {
 mapper.close();
 }

}