import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIntegerSerialization() throws JsonProcessingException {
 Integer number = 123;
 String json = mapper.writeValueAsString(number);
 JsonNode jsonNode = mapper.readTree(json);
 assertEquals(number.intValue(), jsonNode.asInt());
 }

 @Test
 public void testIntegerDeserialization() throws JsonProcessingException {
 String jsonString = "123";
 JsonNode jsonNode = mapper.readTree(jsonString);
 Integer number = mapper.readValue(jsonNode.asText(), Integer.class);
 assertEquals(123, number.intValue());
 }

 @Test
 public void testLongSerialization() throws JsonProcessingException {
 Long number = 456L;
 String json = mapper.writeValueAsString(number);
 JsonNode jsonNode = mapper.readTree(json);
 assertEquals(number.longValue(), jsonNode.asLong());
 }

 @Test
 public void testLongDeserialization() throws JsonProcessingException {
 String jsonString = "456";
 JsonNode jsonNode = mapper.readTree(jsonString);
 Long number = mapper.readValue(jsonNode.asText(), Long.class);
 assertEquals(456L, number.longValue());
 }

 @Test
 public void testFloatSerialization() throws JsonProcessingException {
 Float number = 123.45f;
 String json = mapper.writeValueAsString(number);
 JsonNode jsonNode = mapper.readTree(json);
 assertEquals(number.floatValue(), jsonNode.asFloat(), 0.001f);
 }

 @Test
 public void testFloatDeserialization() throws JsonProcessingException {
 String jsonString = "123.45";
 JsonNode jsonNode = mapper.readTree(jsonString);
 Float number = mapper.readValue(jsonNode.asText(), Float.class);
 assertEquals(123.45f, number, 0.001f);
 }

 @Test
 public void testDoubleSerialization() throws JsonProcessingException {
 Double number = 123.456;
 String json = mapper.writeValueAsString(number);
 JsonNode jsonNode = mapper.readTree(json);
 assertEquals(number.doubleValue(), jsonNode.asDouble(), 0.001);
 }

 @Test
 public void testDoubleDeserialization() throws JsonProcessingException {
 String jsonString = "123.456";
 JsonNode jsonNode = mapper.readTree(jsonString);
 Double number = mapper.readValue(jsonNode.asText(), Double.class);
 assertEquals(123.456, number, 0.001);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJson() throws JsonProcessingException {
 String jsonString = "invalidJson";
 JsonNode jsonNode = mapper.readTree(jsonString);
 mapper.readValue(jsonNode.asText(), Integer.class);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidNumber() throws JsonProcessingException {
 String jsonString = "abcd";
 JsonNode jsonNode = mapper.readTree(jsonString);
 mapper.readValue(jsonNode.asText(), Integer.class);
 }
}