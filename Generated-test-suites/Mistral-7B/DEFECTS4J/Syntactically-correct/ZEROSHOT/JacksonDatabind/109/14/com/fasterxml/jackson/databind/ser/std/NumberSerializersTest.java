import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testNumberSerializers() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test Integer serialization
 Integer intValue = 123;
 JsonNode intNode = mapper.valueToTree(intValue);
 assertEquals("123", intNode.asText());

 // Test Integer serialization with JsonFormat
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer formattedInt = 456;
 JsonNode formattedIntNode = mapper.valueToTree(formattedInt);
 assertEquals("\"456\"", formattedIntNode.asText());

 // Test Integer deserialization
 JsonNode intJsonNode = mapper.createObjectNode().putNumber("number", 789);
 Integer deserializedInt = mapper.treeToValue(intJsonNode, Integer.class);
 assertEquals(789, deserializedInt.intValue());

 // Test Integer deserialization with JsonFormat
 JsonNode formattedIntJsonNode = mapper.createObjectNode().put("number", mapper.valueToTree("\"1234\""));
 Integer deserializedFormattedInt = mapper.treeToValue(formattedIntJsonNode, Integer.class);
 assertEquals(1234, deserializedFormattedInt.intValue());

 // Repeat for other types
 Long longValue = 987654321L;
 Float floatValue = 1.23f;
 Double doubleValue = 3.14159265358979323846;

 // ... add more tests for other types like Short, Byte, etc.
 }
}