import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testStandardSerializerStringSerialization() throws JsonProcessingException {
 String testString = "Test string";
 StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
 ObjectNode jsonNode = objectMapper.createObjectNode();
 stringSerializer.serialize(testString, jsonMapper.getFactory().createGenerator(jsonNode), objectMapper.getSerializerProvider());
 String serializedString = jsonNode.get("").asText();
 assertEquals("Test string", serializedString);
 }

 @Test
 public void testStandardSerializerIntegerSerialization() throws JsonProcessingException {
 Integer testInteger = 123;
 StdSerializer<Integer> integerSerializer = objectMapper.findValueSerializer(Integer.class);
 ObjectNode jsonNode = objectMapper.createObjectNode();
 integerSerializer.serialize(testInteger, jsonMapper.getFactory().createGenerator(jsonNode), objectMapper.getSerializerProvider());
 int deserializedInteger = jsonNode.get("").asInt();
 assertEquals(123, deserializedInteger);
 }

 @Test
 public void testStandardSerializerNullSerialization() throws JsonProcessingException {
 StdSerializer<Object> nullSerializer = objectMapper.findValueSerializer(Object.class);
 ObjectNode jsonNode = objectMapper.createObjectNode();
 nullSerializer.serialize(null, jsonMapper.getFactory().createGenerator(jsonNode), objectMapper.getSerializerProvider());
 boolean isNull = jsonNode.get("").isNull();
 assertTrue(isNull);
 }

 @Test(expected = JsonMappingException.class)
 public void testStandardSerializerErrorHandling() throws JsonProcessingException {
 StdSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
 ObjectNode jsonNode = objectMapper.createObjectNode();
 try {
 stringSerializer.serialize(null, jsonMapper.getFactory().createGenerator(jsonNode), objectMapper.getSerializerProvider());
 } catch (JsonMappingException e) {
 // Expected exception
 }
 String errorMessage = e.getMessage();
 assertTrue(errorMessage.contains("Can not serialize null for type String"));
 }
}