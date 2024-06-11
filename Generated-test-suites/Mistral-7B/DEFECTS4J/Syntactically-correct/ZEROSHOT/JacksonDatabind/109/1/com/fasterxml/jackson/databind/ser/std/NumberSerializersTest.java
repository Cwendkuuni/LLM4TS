import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializersTest {

 @DisplayName("Test NumberSerializers class")
 class NumberSerializersTestClass {

 @BeforeAll
 void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 }

 @DisplayName("Test addAll method")
 @Test
 void testAddAll() throws JsonProcessingException {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 assertEquals(8, allDeserializers.size());
 }

 @DisplayName("Test IntegerSerializer serialization")
 @Test
 void testIntegerSerializerSerialization() throws JsonProcessingException {
 Integer intValue = 123;
 JsonNode actual = mapper.writeValueAsTree(intValue);
 JsonNode expected = mapper.readTree("123");
 assertEquals(expected, actual);
 }

 @DisplayName("Test IntegerSerializer deserialization")
 @Test
 void testIntegerSerializerDeserialization() throws JsonProcessingException {
 JsonNode jsonNode = mapper.readTree("123");
 Integer actual = mapper.treeToValue(jsonNode, Integer.class);
 Integer expected = 123;
 assertEquals(expected, actual);
 }

 @DisplayName("Test LongSerializer serialization")
 @Test
 void testLongSerializerSerialization() throws JsonProcessingException {
 Long longValue = 9876543210L;
 JsonNode actual = mapper.writeValueAsTree(longValue);
 JsonNode expected = mapper.readTree("9876543210");
 assertEquals(expected, actual);
 }

 @DisplayName("Test LongSerializer deserialization")
 @Test
 void testLongSerializerDeserialization() throws JsonProcessingException {
 JsonNode jsonNode = mapper.readTree("9876543210");
 Long actual = mapper.treeToValue(jsonNode, Long.class);
 Long expected = 9876543210L;
 assertEquals(expected, actual);
 }

 @DisplayName("Test ShortSerializer serialization")
 @Test
 void testShortSerializerSerialization() throws JsonProcessingException {
 Short shortValue = 127;
 JsonNode actual = mapper.writeValueAsTree(shortValue);
 JsonNode expected = mapper.readTree("127");
 assertEquals(expected, actual);
 }

 @DisplayName("Test ShortSerializer deserialization")
 @Test
 void testShortSerializerDeserialization() throws JsonProcessingException {
 JsonNode jsonNode = mapper.readTree("127");
 Short actual = mapper.treeToValue(jsonNode, Short.class);
 Short expected = (short) 127;
 assertEquals(expected, actual);
 }

 @DisplayName("Test FloatSerializer serialization")
 @Test
 void testFloatSerializerSerialization() throws JsonProcessingException {
 Float floatValue = 3.14f;
 JsonNode actual = mapper.writeValueAsTree(floatValue);
 JsonNode expected = mapper.readTree("3.14");
 assertEquals(expected, actual);
 }

 @DisplayName("Test FloatSerializer deserialization")
 @Test
 void testFloatSerializerDeserialization() throws JsonProcessingException {
 JsonNode jsonNode = mapper.readTree("3.14");
 Float actual = mapper.treeToValue(jsonNode, Float.class);
 Float expected = 3.14f;
 assertEquals(expected, actual);
 }

 @DisplayName("Test DoubleSerializer serialization")
 @Test
 void testDoubleSerializerSerialization() throws JsonProcessingException {
 Double doubleValue = 1.23456789;
 JsonNode actual = mapper.writeValueAsTree(doubleValue);
 JsonNode expected = mapper.readTree("1.23456789");
 assertEquals(expected, actual);
 }

 @DisplayName("Test DoubleSerializer deserialization")
 @Test
 void testDoubleSerializerDeserialization() throws JsonProcessingException {
 JsonNode jsonNode = mapper.readTree("1.23456789");
 Double actual = mapper.treeToValue(jsonNode, Double.class);
 Double expected = 1.23456789;
 assertEquals(expected, actual);
 }
 }

 private ObjectMapper mapper;
}