import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 // Test case for addAll method
 @Test
 public void testAddAll() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 JsonSerializer<?> intSerializer = allDeserializers.get(Integer.class.getName());
 JsonSerializer<?> longSerializer = allDeserializers.get(Long.class.getName());

 assertEquals(Integer.class, intSerializer.getClass());
 assertEquals(Long.class, longSerializer.getClass());
 }

 // Test case for IntegerSerializer
 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 JsonNode node = mapper.readValue("{\"num\":123}", JsonNode.class);
 Integer actual = mapper.treeToValue(node.path("num"), Integer.class);
 Integer expected = 123;
 assertEquals(expected, actual);
 }

 // Test case for IntegerSerializer with negative number
 @Test
 public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 JsonNode node = mapper.readValue("{\"num\":-123}", JsonNode.class);
 Integer actual = mapper.treeToValue(node.path("num"), Integer.class);
 Integer expected = -123;
 assertEquals(expected, actual);
 }

 // Test case for IntegerSerializer with max value
 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerMaxValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
 JsonNode node = mapper.readValue("{\"num\":Integer.MAX_VALUE}", JsonNode.class);
 integerSerializer.serialize(Integer.MAX_VALUE, null, null); // Throws exception in production
 }

 // Test case for IntLikeSerializer
 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
 JsonNode node = mapper.readValue("{\"num\":456}", JsonNode.class);
 Number actual = mapper.treeToValue(node.path("num"), Number.class);
 Number expected = 456;
 assertEquals(expected, actual);
 }

 // Test case for LongSerializer
 @Test
 public void testLongSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
 JsonNode node = mapper.readValue("{\"num\":987654321}", JsonNode.class);
 Long actual = mapper.treeToValue(node.path("num"), Long.class);
 Long expected = 987654321L;
 assertEquals(expected, actual);
 }

 // Test case for LongSerializer with max value
 @Test(expected = JsonMappingException.class)
 public void testLongSerializerMaxValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
 JsonNode node = mapper.readValue("{\"num\":Long.MAX_VALUE}", JsonNode.class);
 longSerializer.serialize(Long.MAX_VALUE, null, null); // Throws exception in production
 }

 // Test case for FloatSerializer
 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
 JsonNode node = mapper.readValue("{\"num\":3.14159265}", JsonNode.class);
 Float actual = mapper.treeToValue(node.path("num"), Float.class);
 Float expected = 3.14159265f;
 assertEquals(expected, actual, 0.0001f); // Allow some tolerance for float comparisons
 }

 // Test case for DoubleSerializer
 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
 JsonNode node = mapper.readValue("{\"num\":1.23456789}", JsonNode.class);
 Double actual = mapper.treeToValue(node.path("num"), Double.class);
 Double expected = 1.23456789;
 assertEquals(expected, actual, 0.000001); // Allow some tolerance for double comparisons
 }
}