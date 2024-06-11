import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StdDelegatingSerializerTest {

 private ObjectMapper mapper;
 private StdDelegatingSerializer serializer;
 private Converter<String, Integer> converter;

 @BeforeAll
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 converter = mapper.getTypeFactory().constructConverter(String.class, Integer.class);
 serializer = new StdDelegatingSerializer(converter);
 }

 @Test
 public void testSerializationOfStringToInteger() throws JsonProcessingException {
 String input = "42";
 ObjectNode output = mapper.writeValueAsTree(serializer, input);
 assertEquals(42, output.get(0).asInt());
 }

 @Test
 public void testSerializationOfNullString() throws JsonProcessingException {
 String input = null;
 ObjectNode output = mapper.writeValueAsTree(serializer, input);
 assertNull(output);
 }

 @Test
 public void testSerializationOfNonConvertibleType() throws JsonProcessingException {
 Object input = new Object() {
 @Override
 public String toString() {
 return "not a number";
 }
 };
 assertThrows(JsonGenerationException.class, () -> mapper.writeValueAsString(serializer, input));
 }

 @Test
 public void testSerializationWithDelegateSerializer() throws JsonProcessingException {
 ObjectMapper delegateMapper = new ObjectMapper();
 JsonSerializer<String> delegateSerializer = delegateMapper.findValueSerializer(String.class);
 serializer = serializer.withDelegate(converter, String.class, delegateSerializer);
 String input = "42";
 ObjectNode output = mapper.writeValueAsTree(serializer, input);
 assertTrue(output.isContainerNode());
 assertEquals(delegateSerializer.getClass().getName(), output.get(0).getClass().getName());
 assertEquals(delegateSerializer.getClass().getName(), serializer.getDelegatee().getClass().getName());
 }

 @Test
 public void testSerializationWithTypeSerialization() throws JsonProcessingException {
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 ObjectNode output = mapper.writeValueAsTree(serializer, "42");
 Object intNode = output.get(0);
 ObjectNode intTypeNode = (ObjectNode) output.get(1);
 assertNotNull(intNode);
 assertNotNull(intTypeNode);
 assertEquals("int", intTypeNode.get("typeName").asText());
 assertEquals("42", intNode.asText());
 }

 @Test
 public void testSerializationWithOptionalSchema() throws JsonProcessingException {
 ObjectMapper delegateMapper = new ObjectMapper();
 JsonSerializer<String> delegateSerializer = delegateMapper.findValueSerializer(String.class);
 ObjectNode schemaNode = mapper.readerForTree(ObjectNode.class).readValue("{\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"}}}");
 serializer = serializer.withDelegate(converter, String.class, delegateSerializer);
 JsonNode result = mapper.readTree(mapper.writeValueAsString(serializer, "42"));
 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), String.class, true);
 assertEquals(schemaNode, schema);
 }

 @Test
 public void testSerializationWithContextualization() throws JsonProcessingException {
 ObjectMapper delegateMapper = new ObjectMapper();
 JsonSerializer<Integer> delegateSerializer = delegateMapper.findValueSerializer(Integer.class);
 serializer = serializer.withDelegate(converter, Integer.class, delegateSerializer);
 String input = "42";
 ObjectNode output = mapper.writeValueAsTree(serializer, input);
 assertEquals(42, output.get(0).asInt());
 }

 @Test
 public void testSerializationWithDynamicLookup() throws JsonProcessingException {
 serializer = new StdDelegatingSerializer(converter, Object.class, null);
 String input = "42";
 ObjectNode output = mapper.writeValueAsTree(serializer, input);
 Object delegateSerializer = output.get(0).get("_delegateSerializer");
 assertNotNull(delegateSerializer);
 assertTrue(delegateSerializer instanceof JsonSerializer);
 }

 @Test
 public void testSerializationWithEmptyValue() throws JsonProcessingException {
 Object input = null;
 ObjectNode output = mapper.writeValueAsTree(serializer, input);
 assertNull(output);
 }

 @Test
 public void testSerializationWithEmptySerializerProvider() throws JsonProcessingException {
 SerializerProvider emptyProvider = new StandardSerializerProvider();
 ObjectNode output = mapper.writeValueAsTree(serializer, "42");
 assertNull(output);
 }
}