import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer intValue = 123;
 ObjectMapper mapper = new ObjectMapper();

 // Test serialization of an integer
 JsonNode serializedInt = mapper.writeValueAsTree(intValue);
 ObjectNode intNode = (ObjectNode) serializedInt;
 assertEquals("123", intNode.getTextValue());

 // Test deserialization of an integer
 Integer deserializedInt = mapper.readValue(serializedInt.toString(), Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 @Test
 public void testIntegerSerializerWithNegativeValue() throws JsonProcessingException {
 Integer intValue = -123;
 ObjectMapper mapper = new ObjectMapper();

 // Test serialization of a negative integer
 JsonNode serializedInt = mapper.writeValueAsTree(intValue);
 ObjectNode intNode = (ObjectNode) serializedInt;
 assertEquals("-123", intNode.getTextValue());

 // Test deserialization of a negative integer
 Integer deserializedInt = mapper.readValue(serializedInt.toString(), Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 @Test(expected = JsonProcessingException.class)
 public void testIntegerSerializerWithStringInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String input = "abc";

 // Test serialization should fail with a JsonProcessingException
 mapper.readValue(input, Integer.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerWithNullInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer input = null;

 // Test deserialization should fail with a JsonMappingException
 mapper.readValue(mapper.writeValueAsString(input), Integer.class);
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short shortValue = 127;
 ObjectMapper mapper = new ObjectMapper();

 // Test serialization of a short
 JsonNode serializedShort = mapper.writeValueAsTree(shortValue);
 ObjectNode shortNode = (ObjectNode) serializedShort;
 assertEquals("127", shortNode.getTextValue());

 // Test deserialization of a short
 Short deserializedShort = mapper.readValue(serializedShort.toString(), Short.class);
 assertEquals(shortValue, deserializedShort);
 }

 // ... Continue testing for LongSerializer, FloatSerializer, and DoubleSerializer

}