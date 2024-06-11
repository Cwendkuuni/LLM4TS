import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer integer = 123;
 String json = mapper.writeValueAsString(integer);
 JsonNode node = mapper.readTree(json);
 assertEquals(123, node.asInt());
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 Integer negativeNumber = -123;
 String json = mapper.writeValueAsString(negativeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(-123, node.asInt());
 }

 @Test
 public void testIntegerSerializerWithLargeNumber() throws JsonProcessingException {
 Long largeNumber = Long.MAX_VALUE;
 Integer integer = largeNumber.intValue();
 String json = mapper.writeValueAsString(integer);
 JsonNode node = mapper.readTree(json);
 assertEquals(largeNumber.longValue(), node.asLong());
 }

 @Test
 public void testIntegerSerializerWithMinusLargeNumber() throws JsonProcessingException {
 Long largeNumber = Long.MIN_VALUE;
 Integer integer = largeNumber.intValue();
 String json = mapper.writeValueAsString(integer);
 JsonNode node = mapper.readTree(json);
 assertEquals(largeNumber.longValue(), node.asLong());
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithString() throws JsonProcessingException {
 String json = "{\"number\": \"abc\"}";
 mapper.readValue(json, Integer.class);
 }

 @Test
 public void testIntegerSerializerWithNull() throws JsonProcessingException {
 Integer nullInteger = null;
 String json = mapper.writeValueAsString(nullInteger);
 JsonNode node = mapper.readTree(json);
 assertNull(node);
 }

 @Test
 public void testIntegerSerializerWithJsonFormatAnnotation() throws JsonProcessingException {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer stringInteger = "123";
 String json = mapper.writeValueAsString(stringInteger);
 JsonNode node = mapper.readTree(json);
 assertEquals("123", node.textValue());
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long longNumber = 123L;
 String json = mapper.writeValueAsString(longNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(123, node.asInt()); // testing int deserialization of long
 assertEquals(123L, node.asLong());
 }

 @Test
 public void testLongSerializerWithNegativeNumber() throws JsonProcessingException {
 Long negativeNumber = -123L;
 String json = mapper.writeValueAsString(negativeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(-123, node.asInt()); // testing int deserialization of long
 assertEquals(-123L, node.asLong());
 }

 @Test
 public void testLongSerializerWithLargeNumber() throws JsonProcessingException {
 Long largeNumber = Long.MAX_VALUE;
 String json = mapper.writeValueAsString(largeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(largeNumber.longValue(), node.asLong());
 }

 @Test
 public void testLongSerializerWithMinusLargeNumber() throws JsonProcessingException {
 Long largeNumber = Long.MIN_VALUE;
 String json = mapper.writeValueAsString(largeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(largeNumber.longValue(), node.asLong());
 }

 @Test(expected = JsonMappingException.class)
 public void testLongSerializerWithString() throws JsonProcessingException {
 String json = "{\"number\": \"abc\"}";
 mapper.readValue(json, Long.class);
 }

 @Test
 public void testLongSerializerWithNull() throws JsonProcessingException {
 Long nullLong = null;
 String json = mapper.writeValueAsString(nullLong);
 JsonNode node = mapper.readTree(json);
 assertNull(node);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float floatNumber = 123.45f;
 String json = mapper.writeValueAsString(floatNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(123.45f, node.asFloat(), 0.01f);
 }

 @Test
 public void testFloatSerializerWithNegativeNumber() throws JsonProcessingException {
 Float negativeNumber = -123.45f;
 String json = mapper.writeValueAsString(negativeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(-123.45f, node.asFloat(), 0.01f);
 }

 @Test
 public void testFloatSerializerWithLargeNumber() throws JsonProcessingException {
 Float largeNumber = Float.MAX_VALUE;
 String json = mapper.writeValueAsString(largeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(largeNumber.floatValue(), node.asFloat(), 0.01f);
 }

 @Test
 public void testFloatSerializerWithMinusLargeNumber() throws JsonProcessingException {
 Float largeNumber = Float.MIN_VALUE;
 String json = mapper.writeValueAsString(largeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(largeNumber.floatValue(), node.asFloat(), 0.01f);
 }

 @Test(expected = JsonMappingException.class)
 public void testFloatSerializerWithString() throws JsonProcessingException {
 String json = "{\"number\": \"abc\"}";
 mapper.readValue(json, Float.class);
 }

 @Test
 public void testFloatSerializerWithNull() throws JsonProcessingException {
 Float nullFloat = null;
 String json = mapper.writeValueAsString(nullFloat);
 JsonNode node = mapper.readTree(json);
 assertNull(node);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double doubleNumber = 123.45;
 String json = mapper.writeValueAsString(doubleNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(123.45, node.asDouble(), 0.01);
 }

 @Test
 public void testDoubleSerializerWithNegativeNumber() throws JsonProcessingException {
 Double negativeNumber = -123.45;
 String json = mapper.writeValueAsString(negativeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(-123.45, node.asDouble(), 0.01);
 }

 @Test
 public void testDoubleSerializerWithLargeNumber() throws JsonProcessingException {
 Double largeNumber = Double.MAX_VALUE;
 String json = mapper.writeValueAsString(largeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(largeNumber.doubleValue(), node.asDouble(), 0.01);
 }

 @Test
 public void testDoubleSerializerWithMinusLargeNumber() throws JsonProcessingException {
 Double largeNumber = Double.MIN_VALUE;
 String json = mapper.writeValueAsString(largeNumber);
 JsonNode node = mapper.readTree(json);
 assertEquals(largeNumber.doubleValue(), node.asDouble(), 0.01);
 }

 @Test(expected = JsonMappingException.class)
 public void testDoubleSerializerWithString() throws JsonProcessingException {
 String json = "{\"number\": \"abc\"}";
 mapper.readValue(json, Double.class);
 }

 @Test
 public void testDoubleSerializerWithNull() throws JsonProcessingException {
 Double nullDouble = null;
 String json = mapper.writeValueAsString(nullDouble);
 JsonNode node = mapper.readTree(json);
 assertNull(node);
 }
}