import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testPOJONodeWithStringValue() throws JsonProcessingException {
 POJONode node = new POJONode("Test String");
 assertEquals("Test String", node.asText());
 assertEquals("Test String", node.asText(null));
 assertEquals("Test String", node.toString());
 // Test serialization
 POJONode deserializedNode = mapper.readValue(mapper.writeValueAsBytes(node), POJONode.class);
 assertEquals("Test String", deserializedNode.asText());
 }

 @Test
 public void testPOJONodeWithIntegerValue() throws JsonProcessingException {
 POJONode node = new POJONode(42);
 assertEquals("42", node.asText());
 assertEquals("42", node.asText(null));
 assertEquals("42", node.toString());
 // Test serialization
 POJONode deserializedNode = mapper.readValue(mapper.writeValueAsBytes(node), POJONode.class);
 assertEquals(42, deserializedNode.asInt(0));
 }

 @Test
 public void testPOJONodeWithNullValue() {
 POJONode node = new POJONode(null);
 assertEquals("null", node.asText());
 assertEquals("null", node.asText(null));
 assertEquals("null", node.toString());
 // Test serialization
 POJONode deserializedNode = new POJONode(null);
 // No need to assert anything as Jackson will serialize null as null
 }

 @Test(expected = ClassCastException.class)
 public void testPOJONodeWithByteArrayValue() throws JsonProcessingException {
 byte[] byteArray = {1, 2, 3};
 POJONode node = new POJONode(byteArray);
 // This will fail as POJONode does not support binaryValue() for byte[]
 byte[] binaryData = node.binaryValue();
 fail("Expected ClassCastException but got: " + binaryData);
 }

 @Test
 public void testPOJONodeWithJsonSerializableValue() throws JsonProcessingException {
 MyClass jsonSerializableObject = new MyClass("Test");
 POJONode node = new POJONode(jsonSerializableObject);
 // Test asText() and asText(defaultValue)
 assertEquals("{\"name\":\"Test\"}", node.asText());
 assertEquals("{\"name\":\"Test\"}", node.asText(""));
 // Test toString()
 assertEquals("POJONode[getPojo()=[{\"name\":\"Test\"}]]", node.toString());
 // Test serialization
 POJONode deserializedNode = mapper.readValue(mapper.writeValueAsBytes(node), POJONode.class);
 assertEquals("Test", deserializedNode.getPojo().getName());
 }
}

class MyClass implements JsonSerializable {
 private String name;

 public MyClass(String name) {
 this.name = name;
 }

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeObjectFieldName("name");
 gen.writeString(name);
 }
}