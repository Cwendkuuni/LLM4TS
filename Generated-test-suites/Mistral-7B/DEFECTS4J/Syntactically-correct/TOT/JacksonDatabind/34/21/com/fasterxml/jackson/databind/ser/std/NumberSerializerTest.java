import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 BigDecimal bigDecimal = new BigDecimal("123.456");
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 JsonNode actual = writer.writeValueAsTree(bigDecimal);
 JsonNode expected = JsonNodeFactory.instance.numberNode("123.456");
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, JsonGenerationException {
 BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 JsonNode actual = writer.writeValueAsTree(bigInteger);
 JsonNode expected = JsonNodeFactory.instance.numberNode("123456789012345678901234567890");
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 Integer integer = 123;
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 JsonNode actual = writer.writeValueAsTree(integer);
 JsonNode expected = JsonNodeFactory.instance.numberNode("123");
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 Long longValue = 1234567890123456789L;
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 JsonNode actual = writer.writeValueAsTree(longValue);
 JsonNode expected = JsonNodeFactory.instance.numberNode("1234567890123456789");
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 Double doubleValue = 123.456;
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 JsonNode actual = writer.writeValueAsTree(doubleValue);
 JsonNode expected = JsonNodeFactory.instance.numberNode("123.456");
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 Float floatValue = 123.45f;
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 JsonNode actual = writer.writeValueAsTree(floatValue);
 JsonNode expected = JsonNodeFactory.instance.numberNode("123.45");
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException {
 Byte byteValue = (byte) 123;
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 JsonNode actual = writer.writeValueAsTree(byteValue);
 JsonNode expected = JsonNodeFactory.instance.numberNode("123");
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException {
 Short shortValue = (short) 123;
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 JsonNode actual = writer.writeValueAsTree(shortValue);
 JsonNode expected = JsonNodeFactory.instance.numberNode("123");
 assertEquals(expected, actual);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 Number nullNumber = null;
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 writer.writeValueAsTree(nullNumber);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeString() throws JsonProcessingException {
 String stringValue = "Hello World";
 ObjectWriter writer = new ObjectMapper().writer().withRootValue(instanceOf(JsonNode.class));
 writer.writeValueAsTree(stringValue);
 }
}