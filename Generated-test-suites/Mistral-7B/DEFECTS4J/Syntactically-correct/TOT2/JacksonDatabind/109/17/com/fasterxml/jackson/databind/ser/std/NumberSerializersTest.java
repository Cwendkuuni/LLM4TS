import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNumberNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NumberSerializersTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer value = 123;
 ObjectNode input = mapper.createObjectNode();
 input.put("value", value);

 JsonNode result = mapper.readTree(new ByteArrayInputStream(mapper.writeValueAsBytes(input)));
 JsonNumberNode expectedNumber = mapper.valueToTree(123);

 assertEquals(expectedNumber, result.get("value"));
 }

 @Test
 public void testIntegerSerializerWithNegativeNumber() throws JsonProcessingException {
 Integer value = -123;
 ObjectNode input = mapper.createObjectNode();
 input.put("value", value);

 JsonNode result = mapper.readTree(new ByteArrayInputStream(mapper.writeValueAsBytes(input)));
 JsonNumberNode expectedNumber = mapper.valueToTree(-123);

 assertEquals(expectedNumber, result.get("value"));
 }

 @Test
 public void testIntegerSerializerWithZero() throws JsonProcessingException {
 Integer value = 0;
 ObjectNode input = mapper.createObjectNode();
 input.put("value", value);

 JsonNode result = mapper.readTree(new ByteArrayInputStream(mapper.writeValueAsBytes(input)));
 JsonNumberNode expectedNumber = mapper.valueToTree(0);

 assertEquals(expectedNumber, result.get("value"));
 }

 @Test(expected = NumberFormatException.class)
 public void testIntegerSerializerWithString() throws JsonProcessingException {
 ObjectNode input = mapper.createObjectNode();
 input.put("value", "abc");

 JsonNode result = mapper.readTree(new ByteArrayInputStream(mapper.writeValueAsBytes(input)));

 assertNull(result.get("value"));
 }

 @Test(expected = IOException.class)
 public void testIntegerSerializerWithNull() throws JsonProcessingException {
 ObjectNode input = mapper.createObjectNode();
 input.put("value", null);

 JsonNode result = mapper.readTree(new ByteArrayInputStream(mapper.writeValueAsBytes(input)));

 assertNull(result.get("value"));
 }

 // Add test cases for other serializers here...
}