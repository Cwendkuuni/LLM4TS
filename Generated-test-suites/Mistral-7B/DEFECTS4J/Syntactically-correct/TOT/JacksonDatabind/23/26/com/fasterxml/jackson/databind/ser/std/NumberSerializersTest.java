import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer intValue = 123;
 String jsonString = "123";

 // Test serialization
 JsonNode serializedNode = mapper.writeValueAsTree(intValue);
 assertEquals(jsonString, serializedNode.asText());

 // Test deserialization
 Integer deserializedInt = mapper.readValue(jsonString, Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 @Test
 public void testIntegerSerializerInvalidInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String jsonString = "abcd";

 // Test serialization with invalid input
 try {
 JsonNode serializedNode = mapper.writeValueAsTree(new Integer(-1));;
 fail("Expected JsonProcessingException for invalid input");
 } catch (JsonProcessingException e) {
 // Expected
 }

 // Test deserialization with invalid input
 try {
 Object deserializedObj = mapper.readValue(jsonString, Integer.class);
 fail("Expected JsonMappingException for invalid input");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testIntegerSerializerNullInput() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer intValue = null;
 String jsonString = "null";

 // Test serialization
 JsonNode serializedNode = mapper.writeValueAsTree(intValue);
 assertEquals(jsonString, serializedNode.asText());

 // Test deserialization
 Integer deserializedInt = mapper.readValue(jsonString, Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 // Test other serializers similarly...

}