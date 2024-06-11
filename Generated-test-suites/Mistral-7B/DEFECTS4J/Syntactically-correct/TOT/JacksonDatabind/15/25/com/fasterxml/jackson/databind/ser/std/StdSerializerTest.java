import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StdSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testStringSerializer() throws JsonProcessingException {
 StdSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
 String value = "Hello, World!";
 ObjectNode result = mapper.createObjectNode();
 stringSerializer.serialize(value, result, new SerializerProvider() {});
 String serializedValue = result.get("@type") + ":" + result.get("value").asText();
 assertEquals("string:Hello, World!", serializedValue);
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 StdSerializer<Integer> integerSerializer = mapper.findValueSerializer(Integer.class);
 Integer value = 42;
 ObjectNode result = mapper.createObjectNode();
 integerSerializer.serialize(value, result, new SerializerProvider() {});
 String serializedValue = result.get("@type") + ":" + result.get("value").asInt();
 assertEquals("number:42", serializedValue);
 }

 @Test
 public void testNullSerializer() throws JsonProcessingException {
 StdSerializer<Object> nullSerializer = mapper.findValueSerializer(Object.class);
 ObjectNode result = mapper.createObjectNode();
 nullSerializer.serialize(null, result, new SerializerProvider() {});
 String serializedValue = result.get("@type") + ":" + result.get("nullValue").asText();
 assertEquals("null", serializedValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeSerializer() throws JsonProcessingException {
 StdSerializer<Long> longSerializer = mapper.findValueSerializer(Integer.class);
 Integer value = 42;
 LongNode result = mapper.createLongNode();
 longSerializer.serialize(value, result, new SerializerProvider() {});
 }

 @Test
 public void testSchemaAwareSerializer() throws JsonProcessingException {
 StdSerializer<Boolean> booleanSerializer = new StdSerializer<Boolean>(Boolean.class) {
 @Override
 public JsonNode getSchema(SerializerProvider provider, Type typeHint)
 throws JsonMappingException {
 return createSchemaNode("boolean");
 }
 };
 ObjectNode result = mapper.createObjectNode();
 booleanSerializer.serialize(true, result, new SerializerProvider() {});
 String schema = result.get("schema").asText();
 assertEquals("{\"type\":\"boolean\"}", schema);
 }
}