import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 // Test for addAll method
 @Test
 public void testAddAll() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 JsonSerializer<?> integerSerializer = allDeserializers.get(Integer.class.getName());
 JsonSerializer<?> integerTypeSerializer = allDeserializers.get(Integer.TYPE.getName());
 JsonSerializer<?> longSerializer = allDeserializers.get(Long.class.getName());
 JsonSerializer<?> longTypeSerializer = allDeserializers.get(Long.TYPE.getName());

 // Basic test for IntegerSerializer and IntegerTypeSerializer
 assertTrue(integerSerializer instanceof IntegerSerializer);
 assertTrue(integerTypeSerializer instanceof IntegerSerializer);

 JsonNode jsonNode = mapper.readTree(mapper.writeValueAsString(new Integer(123)));
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals(123, jsonParser.readValueAs(Integer.class));

 // Test for LongSerializer and LongTypeSerializer
 assertTrue(longSerializer instanceof LongSerializer);
 assertTrue(longTypeSerializer instanceof LongSerializer);

 jsonNode = mapper.readTree(mapper.writeValueAsString(new Long(456)));
 jsonParser = mapper.parserForObject(jsonNode);
 assertEquals(456, jsonParser.readValueAs(Long.class));
 }

 // Test for IntegerSerializer with negative number
 @Test
 public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> integerSerializer = new IntegerSerializer(Integer.class);

 JsonNode jsonNode = mapper.valueToTree(-123);
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals(-123, jsonParser.readValueAs(Integer.class));
 }

 // Test for IntegerSerializer with maximum int value
 @Test
 public void testIntegerSerializerMaxValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> integerSerializer = new IntegerSerializer(Integer.class);

 JsonNode jsonNode = mapper.valueToTree(Integer.MAX_VALUE);
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals(Integer.MAX_VALUE, jsonParser.readValueAs(Integer.class));
 }

 // Test for IntegerSerializer with minimum int value
 @Test
 public void testIntegerSerializerMinValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> integerSerializer = new IntegerSerializer(Integer.class);

 JsonNode jsonNode = mapper.valueToTree(Integer.MIN_VALUE);
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals(Integer.MIN_VALUE, jsonParser.readValueAs(Integer.class));
 }

 // Test for IntLikeSerializer with Byte
 @Test
 public void testIntLikeSerializerWithByte() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> intLikeSerializer = new IntLikeSerializer();

 JsonNode jsonNode = mapper.valueToTree((byte) 127);
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals((byte) 127, jsonParser.readValueAs(Byte.class));
 }

 // Test for IntLikeSerializer with Short
 @Test
 public void testIntLikeSerializerWithShort() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> intLikeSerializer = new IntLikeSerializer();

 JsonNode jsonNode = mapper.valueToTree((short) 12345);
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals((short) 12345, jsonParser.readValueAs(Short.class));
 }

 // Test for LongSerializer with maximum long value
 @Test
 public void testLongSerializerMaxValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> longSerializer = new LongSerializer(Long.class);

 JsonNode jsonNode = mapper.valueToTree(Long.MAX_VALUE);
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals(Long.MAX_VALUE, jsonParser.readValueAs(Long.class));
 }

 // Test for LongSerializer with minimum long value
 @Test
 public void testLongSerializerMinValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> longSerializer = new LongSerializer(Long.class);

 JsonNode jsonNode = mapper.valueToTree(Long.MIN_VALUE);
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals(Long.MIN_VALUE, jsonParser.readValueAs(Long.class));
 }

 // Test for FloatSerializer with maximum float value
 @Test
 public void testFloatSerializerMaxValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> floatSerializer = new FloatSerializer(Float.class);

 JsonNode jsonNode = mapper.valueToTree(Float.MAX_VALUE);
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals(Float.MAX_VALUE, jsonParser.readValueAs(Float.class));
 }

 // Test for FloatSerializer with minimum float value
 @Test
 public void testFloatSerializerMinValue() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> floatSerializer = new FloatSerializer(Float.class);

 JsonNode jsonNode = mapper.valueToTree(Float.MIN_VALUE);
 JsonParser jsonParser = mapper.parserForObject(jsonNode);
 assertEquals(Float.MIN_VALUE, jsonParser.readValueAs(Float.class));
 }
}