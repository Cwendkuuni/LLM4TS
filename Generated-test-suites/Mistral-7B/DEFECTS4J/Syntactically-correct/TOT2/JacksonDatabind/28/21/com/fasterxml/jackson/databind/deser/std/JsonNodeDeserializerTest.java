import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private JsonParser createParser(String json) throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 StringResource.Reader reader = new StringResource.Reader(json);
 return jsonFactory.createParser(reader);
 }

 @Test
 public void testDeserializeNullValue() throws IOException {
 JsonNode expected = JsonNodeFactory.getInstance().nullNode();
 JsonNode actual = new JsonNodeDeserializer().deserialize(createParser(null), new TestContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 String json = "{\"name\": \"John\", \"age\": 30}";
 JsonNode expected = JsonNodeFactory.getInstance().objectNode()
 .put("name", JsonNodeFactory.getInstance().textNode("John"))
 .put("age", JsonNodeFactory.getInstance().numberNode(30));
 JsonNode actual = new JsonNodeDeserializer().deserialize(createParser(json), new TestContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 String json = "[1, 2, 3]";
 JsonNode expected = JsonNodeFactory.getInstance().arrayNode()
 .add(JsonNodeFactory.getInstance().numberNode(1))
 .add(JsonNodeFactory.getInstance().numberNode(2))
 .add(JsonNodeFactory.getInstance().numberNode(3));
 JsonNode actual = new JsonNodeDeserializer().deserialize(createParser(json), new TestContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectWithDuplicateField() throws IOException {
 String json = "{\"name\": \"John\", \"name\": \"Jane\"}";
 JsonNode expected = JsonNodeFactory.getInstance().objectNode()
 .put("name", JsonNodeFactory.getInstance().textNode("Jane"));
 JsonNode actual = new JsonNodeDeserializer().deserialize(createParser(json), new TestContext());
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 String json = "{name: \"John\""; // missing closing brace
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 try {
 JsonNode actual = deserializer.deserialize(createParser(json), new TestContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullJson() throws IOException {
 String json = null;
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 try {
 JsonNode actual = deserializer.deserialize(createParser(json), new TestContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidToken() throws IOException {
 String json = "[1, 2}"; // missing closing bracket
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 try {
 JsonNode actual = deserializer.deserialize(createParser(json), new TestContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnexpectedToken() throws IOException {
 String json = "{name: \"John\"}"; // expected ObjectNode, but got StringNode instead
 JsonParser parser = createParser(json);
 parser.nextToken(); // advance past opening brace
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 try {
 JsonNode actual = deserializer.deserialize(parser, new TestContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnexpectedToken2() throws IOException {
 String json = "{name: \"John\"}"; // expected ObjectNode, but got ArrayNode instead
 JsonParser parser = createParser(json);
 parser.nextToken(); // advance past opening brace
 parser.nextToken(); // advance past opening square bracket
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 try {
 JsonNode actual = deserializer.deserialize(parser, new TestContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
}

 private static class TestContext extends DeserializationContext {
 @Override public JsonNodeFactory nodeFactory() { return JsonNodeFactory.getInstance(); }
 @Override public JsonParser p() { return null; }
 }
}