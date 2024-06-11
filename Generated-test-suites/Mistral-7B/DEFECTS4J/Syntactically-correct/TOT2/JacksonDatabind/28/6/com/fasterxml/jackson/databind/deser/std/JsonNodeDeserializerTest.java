import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private final JsonFactory jsonFactory = new JsonFactory();
 private final ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

 @Test
 public void testNullValueDeserialization() throws JsonProcessingException {
 JsonNode actual = JsonNodeDeserializer.getDeserializer(JsonNode.class).deserialize(jsonFactory, null, null);
 JsonNode expected = JsonNode.Instance.getNullValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObject() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", jsonFactory.createTextNode("John Doe"));
 jsonObject.put("age", jsonFactory.createNumberNode(30));
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.put("name", jsonFactory.createTextNode("John Doe"));
 expected.put("age", jsonFactory.createNumberNode(30));
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeEmptyObject() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 assertTrue(actual.isMissingNode());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeObjectWithInvalidFieldName() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("field_name$#%^&", jsonFactory.createTextNode("value"));
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 try {
 JsonNode actual = deserializer.deserialize(jsonFactory.createParser(jsonObject.toString()), null);
 fail("Expected JsonMappingException but got " + actual.getClass().getName());
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeObjectWithDuplicateField() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", jsonFactory.createTextNode("John Doe"));
 jsonObject.put("name", jsonFactory.createTextNode("Jane Doe"));
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 try {
 JsonNode actual = deserializer.deserialize(jsonFactory.createParser(jsonObject.toString()), null);
 fail("Expected JsonMappingException but got " + actual.getClass().getName());
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeArray() throws JsonProcessingException {
 ArrayNode jsonArray = jsonFactory.createArrayNode();
 jsonArray.add(jsonFactory.createNumberNode(1));
 jsonArray.add(jsonFactory.createNumberNode(2));
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonArray.toString()), null);
 ArrayNode expected = jsonFactory.createArrayNode();
 expected.add(jsonFactory.createNumberNode(1));
 expected.add(jsonFactory.createNumberNode(2));
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeEmptyArray() throws JsonProcessingException {
 ArrayNode jsonArray = jsonFactory.createArrayNode();
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ArrayNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonArray.toString()), null);
 assertTrue(actual.isMissingNode());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeArrayWithInvalidToken() throws JsonProcessingException {
 ArrayNode jsonArray = jsonFactory.createArrayNode();
 jsonArray.add(jsonFactory.createNumberNode(1));
 jsonArray.add(JsonToken.START_OBJECT);
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 try {
 JsonNode actual = deserializer.deserialize(jsonFactory.createParser(jsonArray.toString()), null);
 fail("Expected JsonMappingException but got " + actual.getClass().getName());
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeObjectWithEmptyStringValue() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", jsonFactory.createTextNode(""));
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.put("name", jsonFactory.createTextNode(""));
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectWithNullValue() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", null);
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.putNull("name");
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectWithBooleanValue() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", jsonFactory.createBooleanNode(true));
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.put("name", jsonFactory.createBooleanNode(true));
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectWithIntegerValue() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", jsonFactory.createNumberNode(1));
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.put("name", jsonFactory.createNumberNode(1));
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectWithLongValue() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", jsonFactory.createNumberNode(Long.MAX_VALUE));
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.put("name", jsonFactory.createNumberNode(Long.MAX_VALUE));
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectWithStringValue() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", jsonFactory.createTextNode("Value"));
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.put("name", jsonFactory.createTextNode("Value"));
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectWithArrayValue() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", jsonFactory.createArrayNode());
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.put("name", JsonNodeFactory.instance.arrayNode());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectWithEmptyArrayValue() throws JsonProcessingException {
 ObjectNode jsonObject = jsonFactory.createObjectNode();
 jsonObject.put("name", JsonNodeFactory.instance.arrayNode().get(0));
 JsonNode actual = JsonNodeDeserializer.getDeserializer(ObjectNode.class).deserialize(jsonFactory, jsonFactory.createParser(jsonObject.toString()), null);
 ObjectNode expected = jsonFactory.createObjectNode();
 expected.put("name", JsonNodeFactory.instance.missingNode());
 assertEquals(expected, actual);
 }

}