import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private static final DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

 @Test
 public void testSerializationWithDefaultFormat() throws JsonProcessingException, JsonGenerationException {
 Date date = new Date();
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(date);
 assertTrue(json.contains("\"$date\":\"" + date.toISO8601String() + "\""));
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonProcessingException, JsonGenerationException {
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(date);
 assertTrue(json.contains(customFormat.format(date)));
 }

 @Test
 public void testSerializationAsTimestamp() throws JsonProcessingException, JsonGenerationException {
 Date date = new Date();
 DateTimeSerializerBase<Date> timestampSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(date);
 assertTrue(json.matches("-?[0-9]+(\\.[0-9]+)"));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithNullValue() throws JsonProcessingException, JsonGenerationException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 writer.writeValueAsString(null);
 fail("Serialization should have failed with JsonMappingException for null value");
 }

 @Test
 public void testDeserialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String dateString = "2022-03-01T14:30:00Z";
 Date date = mapper.readValue(dateString, Date.class);
 assertEquals(dateString, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String invalidDateString = "01-01-2022-03-01";
 try {
 mapper.readValue(invalidDateString, Date.class);
 fail("Deserialization should have failed with JsonMappingException for invalid format");
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithNullInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 try {
 mapper.readValue(null, Date.class);
 fail("Deserialization should have failed with JsonMappingException for null input");
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test
 public void testDeserializationWithCustomFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
 customFormat.setLenient(true);
 JsonDeserializer<Date> customDeserializer = new JsonDeserializer<Date>(Date.class) {
 @Override
 public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 return customFormat.parse(p.getText());
 }
 };
 mapper.registerModule(new SimpleModule() {{
 addDeserializer(Date.class, customDeserializer);
 }});
 String dateString = "01-03-2022 14:30:00 EST";
 Date date = mapper.readValue(dateString, Date.class);
 assertEquals(dateString, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date));
 }
}