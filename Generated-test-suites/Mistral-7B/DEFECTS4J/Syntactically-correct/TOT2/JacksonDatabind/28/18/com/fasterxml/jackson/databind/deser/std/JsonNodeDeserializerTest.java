import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private ObjectMapper mapper;

 @BeforeClass
 public static void setupClass() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetNullValue() throws IOException {
 JsonNode actual = JsonNodeDeserializer.instance.getNullValue(mapper.getDeserializationContext());
 assertTrue(actual.isNull());
 }

 @Test
 public void testDeserializeObject() throws IOException {
 ObjectNode expected = mapper.readValue("{\"name\": \"John\", \"age\": 30}", ObjectNode.class);
 JsonNode actual = JsonNodeDeserializer.instance.deserialize(mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}"), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 ArrayNode expected = mapper.readValue("[1, 2, 3]", ArrayNode.class);
 JsonNode actual = JsonNodeDeserializer.instance.deserialize(mapper.getFactory().createParser("[1, 2, 3]"), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeAny() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
 JsonToken token = parser.nextToken();
 JsonNode actual = JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), mapper.getFactory());
 assertEquals("John", actual.get("name").asText());
 assertEquals(30, actual.get("age").asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\": [\"John\"");
 JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), mapper.getFactory());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullJson() throws IOException {
 JsonParser parser = mapper.getFactory().createParser(null);
 JsonNodeDeserializer.instance.deserializeAny(parser, mapper.getDeserializationContext(), mapper.getFactory());
 }
}