import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper = new JsonMapper();

 @Test
 public void testSerializationOfShort() throws JsonProcessingException {
 Short shortValue = Short.valueOf((short) 127);
 String json = objectMapper.writeValueAsString(shortValue);
 assertEquals("127", json);
 }

 @Test
 public void testSerializationOfInteger() throws JsonProcessingException {
 Integer integerValue = Integer.valueOf(12345);
 String json = objectMapper.writeValueAsString(integerValue);
 assertEquals("12345", json);
 }

 @Test
 public void testSerializationOfLong() throws JsonProcessingException {
 Long longValue = Long.valueOf(Long.MAX_VALUE);
 String json = objectMapper.writeValueAsString(longValue);
 assertEquals("9223372036854775807", json);
 }

 @Test
 public void testSerializationOfFloat() throws JsonProcessingException {
 Float floatValue = new Float(123.45f);
 String json = objectMapper.writeValueAsString(floatValue);
 assertEquals("123.45", json);
 }

 @Test
 public void testSerializationOfDouble() throws JsonProcessingException {
 Double doubleValue = new Double(123.456);
 String json = objectMapper.writeValueAsString(doubleValue);
 assertEquals("123.456", json);
 }

 @Test
 public void testSerializationWithCustomJsonFormat() throws JsonProcessingException {
 ObjectMapper mapper = objectMapper.copy();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // disable fail-on-empty-beans for tests
 Short shortValue = Short.valueOf((short) 127);
 AnnotatedMember member = ReflectionTestUtils.findAnnotation(shortValue, JsonFormat.class);
 if (member != null) {
 JsonFormat jsonFormat = member.getAnnotation(JsonFormat.class);
 mapper.addSerializer(JsonSerializer.NullSerializer.class, new CustomShortSerializer());
 JsonFormat annotatedFormat = new JsonFormat.Std(jsonFormat.shape(), jsonFormat.value());
 mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = mapper.writerWithView(CustomView.class).writeValueAsString(shortValue);
 assertEquals("{\"value\":127}", json);
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationOfString() throws JsonProcessingException {
 String stringValue = "test";
 String json = objectMapper.writeValueAsString(stringValue);
 fail("Expected JsonMappingException but received: " + json);
 }

 @Test
 public void testDeserializationOfShort() throws JsonProcessingException {
 String json = "127";
 Short shortValue = objectMapper.readValue(json, Short.class);
 assertEquals((short) 127, shortValue);
 }

 @Test
 public void testDeserializationOfInteger() throws JsonProcessingException {
 String json = "12345";
 Integer integerValue = objectMapper.readValue(json, Integer.class);
 assertEquals(12345, integerValue.intValue());
 }

 @Test
 public void testDeserializationOfLong() throws JsonProcessingException {
 String json = "9223372036854775807";
 Long longValue = objectMapper.readValue(json, Long.class);
 assertEquals(Long.MAX_VALUE, longValue);
 }

 @Test
 public void testDeserializationOfFloat() throws JsonProcessingException {
 String json = "123.45";
 Float floatValue = objectMapper.readValue(json, Float.class);
 assertEquals(123.45f, floatValue, 0.001f);
 }

 @Test
 public void testDeserializationOfDouble() throws JsonProcessingException {
 String json = "123.456";
 Double doubleValue = objectMapper.readValue(json, Double.class);
 assertEquals(123.456, doubleValue, 0.001);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationOfArray() throws JsonProcessingException {
 String json = "[1, 2, 3]";
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(json, Object[].class);
 fail("Expected JsonMappingException but received: " + json);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationOfMap() throws JsonProcessingException {
 String json = "[{\"k\":1,\"v\":2},{\"k\":3,\"v\":4}]";
 ObjectMapper mapper = new ObjectMapper();
 mapper.readValue(json, Map.class);
 fail("Expected JsonMappingException but received: " + json);
 }

 @Test
 public void testDeserializationWithCustomJsonFormat() throws JsonProcessingException {
 ObjectMapper mapper = objectMapper.copy();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{\"value\":127}";
 Short shortValue = mapper.readValue(json, Short.class);
 assertEquals((short) 127, shortValue);
 }
}

class CustomShortSerializer extends ShortSerializer {
 @Override
 public ShortSerializer withFormat(JsonFormat format) {
 return new CustomShortSerializer(); // return self to avoid recursion
 }
}

class CustomView {
 // empty for testing purposes
}