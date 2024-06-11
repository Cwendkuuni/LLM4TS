import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer input = 123;
 String output = mapper.writeValueAsString(input);
 JsonNode jsonNode = mapper.readTree(output);
 assertEquals("123", jsonNode.asText());
 }

 @Test
 public void testIntegerSerializerWithNegativeValue() throws JsonProcessingException {
 Integer input = -123;
 String output = mapper.writeValueAsString(input);
 JsonNode jsonNode = mapper.readTree(output);
 assertEquals("-123", jsonNode.asText());
 }

 @Test
 public void testIntegerSerializerWithMinValue() throws JsonProcessingException {
 Integer input = Integer.MIN_VALUE;
 String output = mapper.writeValueAsString(input);
 JsonNode jsonNode = mapper.readTree(output);
 assertEquals("-2147483648", jsonNode.asText());
 }

 @Test
 public void testIntegerSerializerWithMaxValue() throws JsonProcessingException {
 Integer input = Integer.MAX_VALUE;
 String output = mapper.writeValueAsString(input);
 JsonNode jsonNode = mapper.readTree(output);
 assertEquals("2147483647", jsonNode.asText());
 }

 @Test
 public void testIntegerSerializerWithNonIntegerValue() throws JsonProcessingException {
 Float nonIntegerValue = 123.45f;
 String output = mapper.writeValueAsString(nonIntegerValue);
 try {
 JsonNode jsonNode = mapper.readTree(output);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short input = 127;
 String output = mapper.writeValueAsString(input);
 JsonNode jsonNode = mapper.readTree(output);
 assertEquals("127", jsonNode.asText());
 }

 // ... Add similar test cases for other serializers (LongSerializer, FloatSerializer, etc.)

}