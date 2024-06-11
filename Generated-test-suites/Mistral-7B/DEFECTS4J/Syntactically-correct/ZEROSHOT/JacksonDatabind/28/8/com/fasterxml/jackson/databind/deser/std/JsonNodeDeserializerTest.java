import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Test;

public class JsonNodeDeserializerTest {

 private JsonFactory jsonFactory = new JsonFactory();

 @Test
 public void testJsonNodeDeserialization() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"a\":1,\"b\":[2,3]}");
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonNode rootNode = deserializer.deserialize(jsonParser, new ObjectMapper().getDeserializationContext());

 Assert.assertEquals(JsonNodeType.OBJECT, rootNode.getNodeType());
 ObjectNode objectNode = (ObjectNode) rootNode;
 Assert.assertEquals("{\"a\":1,\"b\":[2,3]}", objectNode.toString());

 Assert.assertEquals(JsonNodeType.NUMBER, objectNode.get("a").getNodeType());
 Assert.assertEquals(1, objectNode.get("a").asNumber().doubleValue());

 Assert.assertEquals(JsonNodeType.ARRAY, objectNode.get("b").getNodeType());
 ArrayNode arrayNode = (ArrayNode) objectNode.get("b");
 Assert.assertEquals(2, arrayNode.size());

 Assert.assertEquals(JsonNodeType.NUMBER, arrayNode.get(0).getNodeType());
 Assert.assertEquals(2, arrayNode.get(0).asNumber().doubleValue());

 Assert.assertEquals(JsonNodeType.NUMBER, arrayNode.get(1).getNodeType());
 Assert.assertEquals(3, arrayNode.get(1).asNumber().doubleValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonNodeDeserializationWithInvalidJson() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("Invalid JSON");
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 deserializer.deserialize(jsonParser, new ObjectMapper().getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonNodeDeserializationWithMissingRootNode() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("[]");
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 deserializer.deserialize(jsonParser, new ObjectMapper().getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonNodeDeserializationWithUnexpectedToken() throws IOException {
 JsonParser jsonParser = jsonFactory.createParser("{\"a\":1,\"b\":1}");
 JsonParser.NextToken nextToken = jsonParser.nextToken();
 if (nextToken.afterEndOfStream()) {
 throw new AssertionError("Unexpected End of Stream");
 }

 if (nextToken.id() != JsonToken.FIELD_NAME) {
 JsonParser.CurrentLocation location = jsonParser.getCurrentLocation();
 throw new JsonMappingException("Unexpected token [" + nextToken + "] at [" + location + "]");
 }

 JsonParser.FieldName currentName = jsonParser.getCurrentName();
 jsonParser.nextToken();
 if (jsonParser.getCurrentToken().id() != JsonToken.START_ARRAY) {
 throw new AssertionError("Expected START_ARRAY but was: " + jsonParser.getCurrentToken());
 }

 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 deserializer.deserialize(jsonParser, new ObjectMapper().getDeserializationContext());
 }
}