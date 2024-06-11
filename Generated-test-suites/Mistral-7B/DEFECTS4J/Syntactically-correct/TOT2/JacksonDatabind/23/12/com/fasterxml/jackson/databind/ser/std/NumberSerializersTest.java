import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

private ObjectMapper objectMapper;

@Before
public void setup() {
objectMapper = new ObjectMapper();
}

// Test for addAll method
@Test
public void testAddAll() throws JsonProcessingException {
Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
NumberSerializers.addAll(allDeserializers);
assertEquals(11, allDeserializers.size());
}

// Test for IntegerSerializer - positive number
@Test
public void testIntegerSerializerPositive() throws JsonProcessingException {
IntegerSerializer integerSerializer = new IntegerSerializer();
JsonNode jsonNode = objectMapper.treeToJson(5, integerSerializer);
assertEquals(5, jsonNode.asInt());
}

// Test for IntegerSerializer - negative number
@Test
public void testIntegerSerializerNegative() throws JsonProcessingException {
IntegerSerializer integerSerializer = new IntegerSerializer();
JsonNode jsonNode = objectMapper.treeToJson(-5, integerSerializer);
assertEquals(-5, jsonNode.asInt());
}

// Test for IntegerSerializer - maximum value
@Test(expected = JsonProcessingException.class)
public void testIntegerSerializerMaxValue() throws JsonProcessingException {
IntegerSerializer integerSerializer = new IntegerSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Integer.MAX_VALUE, integerSerializer);
fail("Maximum value should not be serialized");
}

// Test for IntegerSerializer - minimum value
@Test(expected = JsonProcessingException.class)
public void testIntegerSerializerMinValue() throws JsonProcessingException {
IntegerSerializer integerSerializer = new IntegerSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Integer.MIN_VALUE, integerSerializer);
fail("Minimum value should not be serialized");
}

// Test for IntLikeSerializer - positive number
@Test
public void testIntLikeSerializerPositive() throws JsonProcessingException {
IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
JsonNode jsonNode = objectMapper.treeToJson(5, intLikeSerializer);
assertEquals(5, jsonNode.asInt());
}

// Test for IntLikeSerializer - negative number
@Test
public void testIntLikeSerializerNegative() throws JsonProcessingException {
IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
JsonNode jsonNode = objectMapper.treeToJson(-5, intLikeSerializer);
assertEquals(-5, jsonNode.asInt());
}

// Test for IntLikeSerializer - maximum value
@Test(expected = JsonProcessingException.class)
public void testIntLikeSerializerMaxValue() throws JsonProcessingException {
IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Integer.MAX_VALUE, intLikeSerializer);
fail("Maximum value should not be serialized");
}

// Test for IntLikeSerializer - minimum value
@Test(expected = JsonProcessingException.class)
public void testIntLikeSerializerMinValue() throws JsonProcessingException {
IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Integer.MIN_VALUE, intLikeSerializer);
fail("Minimum value should not be serialized");
}

// Test for LongSerializer - positive number
@Test
public void testLongSerializerPositive() throws JsonProcessingException {
LongSerializer longSerializer = new LongSerializer();
JsonNode jsonNode = objectMapper.treeToJson(5L, longSerializer);
assertEquals(5, jsonNode.asInt());
}

// Test for LongSerializer - negative number
@Test
public void testLongSerializerNegative() throws JsonProcessingException {
LongSerializer longSerializer = new LongSerializer();
JsonNode jsonNode = objectMapper.treeToJson(-5L, longSerializer);
assertEquals(-5, jsonNode.asInt());
}

// Test for LongSerializer - maximum value
@Test(expected = JsonProcessingException.class)
public void testLongSerializerMaxValue() throws JsonProcessingException {
LongSerializer longSerializer = new LongSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Long.MAX_VALUE, longSerializer);
fail("Maximum value should not be serialized");
}

// Test for LongSerializer - minimum value
@Test(expected = JsonProcessingException.class)
public void testLongSerializerMinValue() throws JsonProcessingException {
LongSerializer longSerializer = new LongSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Long.MIN_VALUE, longSerializer);
fail("Minimum value should not be serialized");
}

// Test for FloatSerializer - positive number
@Test
public void testFloatSerializerPositive() throws JsonProcessingException {
FloatSerializer floatSerializer = new FloatSerializer();
JsonNode jsonNode = objectMapper.treeToJson(5.0f, floatSerializer);
assertEquals(5.0f, jsonNode.get(0).asFloat(), 0.001f);
}

// Test for FloatSerializer - negative number
@Test
public void testFloatSerializerNegative() throws JsonProcessingException {
FloatSerializer floatSerializer = new FloatSerializer();
JsonNode jsonNode = objectMapper.treeToJson(-5.0f, floatSerializer);
assertEquals(-5.0f, jsonNode.get(0).asFloat(), 0.001f);
}

// Test for FloatSerializer - maximum value
@Test
public void testFloatSerializerMaxValue() throws JsonProcessingException {
FloatSerializer floatSerializer = new FloatSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Float.MAX_VALUE, floatSerializer);
assertEquals(Float.MAX_VALUE, jsonNode.get(0).asFloat(), 0.001f);
}

// Test for FloatSerializer - minimum value
@Test
public void testFloatSerializerMinValue() throws JsonProcessingException {
FloatSerializer floatSerializer = new FloatSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Float.MIN_VALUE, floatSerializer);
assertEquals(Float.MIN_VALUE, jsonNode.get(0).asFloat(), 0.001f);
}

// Test for DoubleSerializer - positive number
@Test
public void testDoubleSerializerPositive() throws JsonProcessingException {
DoubleSerializer doubleSerializer = new DoubleSerializer();
JsonNode jsonNode = objectMapper.treeToJson(5.0, doubleSerializer);
assertEquals(5.0, jsonNode.get(0).asDouble(), 0.001);
}

// Test for DoubleSerializer - negative number
@Test
public void testDoubleSerializerNegative() throws JsonProcessingException {
DoubleSerializer doubleSerializer = new DoubleSerializer();
JsonNode jsonNode = objectMapper.treeToJson(-5.0, doubleSerializer);
assertEquals(-5.0, jsonNode.get(0).asDouble(), 0.001);
}

// Test for DoubleSerializer - maximum value
@Test
public void testDoubleSerializerMaxValue() throws JsonProcessingException {
DoubleSerializer doubleSerializer = new DoubleSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Double.MAX_VALUE, doubleSerializer);
assertEquals(Double.MAX_VALUE, jsonNode.get(0).asDouble(), 0.001);
}

// Test for DoubleSerializer - minimum value
@Test
public void testDoubleSerializerMinValue() throws JsonProcessingException {
DoubleSerializer doubleSerializer = new DoubleSerializer();
JsonNode jsonNode = objectMapper.treeToJson(Double.MIN_VALUE, doubleSerializer);
assertEquals(Double.MIN_VALUE, jsonNode.get(0).asDouble(), 0.001);
}
}