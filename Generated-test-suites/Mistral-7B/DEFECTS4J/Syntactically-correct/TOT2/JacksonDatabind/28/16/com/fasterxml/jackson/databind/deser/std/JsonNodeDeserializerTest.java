import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testGetDeserializer() {
 JsonNode expectedObjectNode = objectMapper.valueToTree(new Object());
 JsonNode expectedArrayNode = objectMapper.valueToTree(new int[0]);
 JsonNode expectedNullNode = JsonNodeFactory.instance.nullNode();

 assertEquals(JsonNodeDeserializer.ObjectDeserializer.class,
 JsonNodeDeserializer.getDeserializer(ObjectNode.class));
 assertEquals(JsonNodeDeserializer.ObjectDeserializer.class,
 JsonNodeDeserializer.getDeserializer(Object.class));
 assertEquals(JsonNodeDeserializer.ArrayDeserializer.class,
 JsonNodeDeserializer.getDeserializer(ArrayNode.class));
 assertEquals(JsonNodeDeserializer.class, JsonNodeDeserializer.getDeserializer(JsonNode.class));

 JsonDeserializer<JsonNode> deserializerObjectNode = JsonNodeDeserializer.getDeserializer(expectedObjectNode.getClass());
 JsonDeserializer<JsonNode> deserializerArrayNode = JsonNodeDeserializer.getDeserializer(expectedArrayNode.getClass());
 JsonDeserializer<JsonNode> deserializerNullNode = JsonNodeDeserializer.getDeserializer(expectedNullNode.getClass());

 assertNotNull(deserializerObjectNode);
 assertNotNull(deserializerArrayNode);
 assertNotNull(deserializerNullNode);
 }

 @Test
 public void testGetNullValue() throws IOException {
 JsonNode deserializedNullNode = JsonNodeDeserializer.instance.deserialize(null, null, null);
 assertEquals(JsonNodeDeserializer.instance.getNullValue(null), deserializedNullNode);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 ObjectNode jsonObject = objectMapper.valueToTree(new Object());
 JsonNode deserializedObjectNode = JsonNodeDeserializer.instance.deserialize(jsonObject.traverse(), null, JsonNodeFactory.instance);
 assertEquals(jsonObject, deserializedObjectNode);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 ArrayNode jsonArray = objectMapper.valueToTree(new int[0]);
 JsonNode deserializedArrayNode = JsonNodeDeserializer.instance.deserialize(jsonArray.traverse(), null, JsonNodeFactory.instance);
 assertEquals(jsonArray, deserializedArrayNode);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnexpectedToken() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"abc\":1, }");
 JsonNode deserializedNode = JsonNodeDeserializer.instance.deserialize(jsonParser, null, JsonNodeFactory.instance);
 fail("Expected JsonMappingException to be thrown.");
 }

 @Test
 public void testDeserializeWithDuplicateKey() throws IOException {
 ObjectNode jsonObjectWithDuplicateKey = objectMapper.valueToTree(new Object() {{
 @JsonProperty("name")
 String name = "John";
 @JsonProperty("name")
 String name2 = "Jane";
 }});
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode deserializedObjectNode = deserializer.deserialize(jsonObjectWithDuplicateKey.traverse(), null, JsonNodeFactory.instance);
 assertEquals(2, deserializedObjectNode.size());
 }

 @Test
 public void testDeserializeWithEmptyObject() throws IOException {
 ObjectNode jsonObjectEmpty = objectMapper.createObjectNode();
 JsonNode deserializedObjectNodeEmpty = JsonNodeDeserializer.instance.deserialize(jsonObjectEmpty.traverse(), null, JsonNodeFactory.instance);
 assertEquals(JsonNodeFactory.instance.objectNode(), deserializedObjectNodeEmpty);
 }

 @Test
 public void testDeserializeWithEmptyArray() throws IOException {
 ArrayNode jsonArrayEmpty = objectMapper.createArrayNode();
 JsonNode deserializedArrayNodeEmpty = JsonNodeDeserializer.instance.deserialize(jsonArrayEmpty.traverse(), null, JsonNodeFactory.instance);
 assertEquals(JsonNodeFactory.instance.arrayNode(), deserializedArrayNodeEmpty);
 }
}