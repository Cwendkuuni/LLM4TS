import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testNullDeserialization() throws IOException {
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.parser(), null);
 assertEquals(JsonNode.ValueType.NULL, node.getNodeType());
 }

 @Test
 public void testObjectDeserialization() throws IOException {
 String json = "{\"name\":\"John\",\"age\":30}";
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.parser(json), ObjectNode.class);
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 }

 @Test
 public void testArrayDeserialization() throws IOException {
 String json = "[1,2,3]";
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.parser(json), ArrayNode.class);
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(3, arrNode.size());
 assertEquals(1, arrNode.get(0).asInt());
 assertEquals(2, arrNode.get(1).asInt());
 assertEquals(3, arrNode.get(2).asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidJsonDeserialization() throws IOException {
 String json = "{\"name\":\"John";
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.parser(json), ObjectNode.class);
 fail("Expected JsonMappingException but got JsonNode");
 }

 @Test(expected = JsonMappingException.class)
 public void testUnexpectedTokenDeserialization() throws IOException {
 String json = "[1,2,3,";
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.parser(json), ArrayNode.class);
 fail("Expected JsonMappingException but got JsonNode");
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTokenTypeDeserialization() throws IOException {
 String json = "[1,2,3X]";
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.parser(json), ArrayNode.class);
 fail("Expected JsonMappingException but got JsonNode");
 }

 @Test
 public void testEmptyObjectDeserialization() throws IOException {
 String json = "{}";
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.parser(json), ObjectNode.class);
 assertTrue(node.isEmpty());
 }

 @Test
 public void testEmptyArrayDeserialization() throws IOException {
 String json = "[]";
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.parser(json), ArrayNode.class);
 assertTrue(node.isEmpty());
 }

 @Test
 public void testDuplicateFieldDeserialization() throws IOException {
 String json = "{\"name\":\"John\",\"name\":\"Jane\"}";
 try {
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(mapper.parser(json), ObjectNode.class);
 fail("Expected JsonMappingException but got JsonNode");
 } catch (JsonMappingException e) {
 assertEquals("Duplicate field 'name' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled", e.getMessage());
 }
 }
}