import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
 Integer number = 123;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("123", result.get("@numberType").asText());
 assertEquals("123", result.get("@").asText());
 assertEquals(123, mapper.readValue(result.toString(), Integer.class));
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 Integer number = -123;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("123", result.get("@numberType").asText());
 assertEquals("-123", result.get("@").asText());
 assertEquals(-123, mapper.readValue(result.toString(), Integer.class));
 }

 @Test
 public void testIntegerSerializerWithBigInteger() throws JsonProcessingException {
 java.math.BigInteger bigNumber = new java.math.BigInteger("12345678901234567890");
 ObjectNode result = mapper.writeValueAsTree(bigNumber);
 assertEquals("bigint", result.get("@numberType").asText());
 assertEquals("12345678901234567890", result.get("@").asText());
 assertEquals(bigNumber, mapper.readValue(result.toString(), java.math.BigInteger.class));
 }

 @Test(expected = JsonProcessingException.class)
 public void testIntegerSerializerWithString() throws JsonProcessingException {
 String str = "hello";
 ObjectNode result = mapper.writeValueAsTree(str);
 fail("Expected JsonProcessingException when trying to serialize a String using IntegerSerializer");
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long number = 123L;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("123", result.get("@numberType").asText());
 assertEquals("123", result.get("@").asText());
 assertEquals(123L, mapper.readValue(result.toString(), Long.class));
 }

 @Test
 public void testLongSerializerWithNegativeNumber() throws JsonProcessingException {
 Long number = -123L;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("123", result.get("@numberType").asText());
 assertEquals("-123", result.get("@").asText());
 assertEquals(-123L, mapper.readValue(result.toString(), Long.class));
 }

 @Test
 public void testLongSerializerWithBigInteger() throws JsonProcessingException {
 java.math.BigInteger bigNumber = new java.math.BigInteger("12345678901234567890");
 ObjectNode result = mapper.writeValueAsTree(bigNumber.longValue());
 assertEquals("bigint", result.get("@numberType").asText());
 assertEquals("12345678901234567890", result.get("@").asText());
 assertEquals(bigNumber.longValue(), mapper.readValue(result.toString(), Long.class));
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float number = 123.45f;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("number", result.get("@numberType").asText());
 assertEquals("123.45", result.get("@").asText());
 assertEquals(123.45f, mapper.readValue(result.toString(), Float.class));
 }

 @Test
 public void testFloatSerializerWithNaN() throws JsonProcessingException {
 Float number = Float.NaN;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("number", result.get("@numberType").asText());
 assertEquals("NaN", result.get("@").asText());
 assertEquals(number, mapper.readValue(result.toString(), Float.class));
 }

 @Test
 public void testFloatSerializerWithInfinity() throws JsonProcessingException {
 Float number = Float.POSITIVE_INFINITY;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("number", result.get("@numberType").asText());
 assertEquals("Infinity", result.get("@").asText());
 assertEquals(number, mapper.readValue(result.toString(), Float.class));
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double number = 123.45;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("number", result.get("@numberType").asText());
 assertEquals("123.45", result.get("@").asText());
 assertEquals(123.45, mapper.readValue(result.toString(), Double.class));
 }

 @Test
 public void testDoubleSerializerWithNaN() throws JsonProcessingException {
 Double number = Double.NaN;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("number", result.get("@numberType").asText());
 assertEquals("NaN", result.get("@").asText());
 assertEquals(number, mapper.readValue(result.toString(), Double.class));
 }

 @Test
 public void testDoubleSerializerWithInfinity() throws JsonProcessingException {
 Double number = Double.POSITIVE_INFINITY;
 ObjectNode result = mapper.writeValueAsTree(number);
 assertEquals("number", result.get("@numberType").asText());
 assertEquals("Infinity", result.get("@").asText());
 assertEquals(number, mapper.readValue(result.toString(), Double.class));
 }
}