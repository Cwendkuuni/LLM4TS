import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testSerializeDateWithDefaultFormat() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 Date date = new Date();
 String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
 Date deserializedDate = objectMapper.readValue(json, Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testSerializeDateWithCustomFormat() throws JsonGenerationException, JsonMappingException {
 DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy"));
 Date date = new Date();
 String json = new ObjectMapper().writeValueAsString(dateSerializer);
 String expectedJson = "\"29-12-2022\""; // assuming current date is 29-12-2022
 String deserializedJson = new ObjectMapper().writeValueAsString(new SimpleDateFormat("dd-MM-yyyy").parse(expectedJson));
 Date deserializedDate = new ObjectMapper().readValue(deserializedJson, Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testSerializeNullDate() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(null);
 assertNull(objectMapper.readValue(json, Date.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeInvalidCustomFormat() throws JsonGenerationException, JsonMappingException {
 DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("xx-xx-xxxx"));
 Date date = new Date();
 String json = new ObjectMapper().writeValueAsString(dateSerializer);
 }

 @Test
 public void testDeserializeWithDefaultFormat() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 String json = "\"2022-12-29T15:30:00.000Z\"";
 Date deserializedDate = objectMapper.readValue(json, Date.class);
 assertEquals(deserializedDate, new Date(1671504600000L)); // assuming current date is 29-12-2022
 }

 @Test
 public void testDeserializeWithCustomFormat() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy"));
 String json = "\"29-12-2022\"";
 Date deserializedDate = objectMapper.readValue(json, Date.class);
 assertEquals(deserializedDate, new Date()); // assuming current date is 29-12-2022
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidFormat() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 String json = "\"InvalidFormat\"";
 }

 @Test
 public void testSerializeUseTimestamp() throws JsonProcessingException {
 DateTimeSerializerBase<Long> timestampSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
 Long timestamp = 1671504600000L;
 String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(timestamp);
 Long deserializedTimestamp = objectMapper.readValue(json, Long.class);
 assertEquals(timestamp, deserializedTimestamp);
 }

 // Add more test cases if necessary
}