import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer intValue = 123;
 ObjectWriter writer = new ObjectMapper().writer();
 String json = writer.writeValueAsString(intValue);
 ObjectReader reader = new ObjectMapper().reader();
 Integer deserializedInt = reader.readValue(JsonFactory.getInstance().parse(json), Integer.class);
 assertEquals(intValue, deserializedInt);
 }

 @Test
 public void testInvalidIntegerInput() throws JsonProcessingException {
 String invalidJson = "{\"int\":abc}";
 ObjectReader reader = new ObjectMapper().reader();
 try {
 reader.readValue(JsonFactory.getInstance().parse(invalidJson), Integer.class);
 fail("Expected JsonProcessingException but no exception was thrown");
 } catch (JsonMappingException e) {
 String expectedMessage = "Cannot deserialize value of type [simple type, class java.lang.Integer] from String \"abc\": not a valid representation";
 assertTrue(e.getMessage().contains(expectedMessage));
 }
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long longValue = 1234567890L;
 ObjectWriter writer = new ObjectMapper().writer();
 String json = writer.writeValueAsString(longValue);
 ObjectReader reader = new ObjectMapper().reader();
 Long deserializedLong = reader.readValue(JsonFactory.getInstance().parse(json), Long.class);
 assertEquals(longValue, deserializedLong);
 }

 @Test
 public void testInvalidLongInput() throws JsonProcessingException {
 String invalidJson = "{\"long\":xyz}";
 ObjectReader reader = new ObjectMapper().reader();
 try {
 reader.readValue(JsonFactory.getInstance().parse(invalidJson), Long.class);
 fail("Expected JsonProcessingException but no exception was thrown");
 } catch (JsonMappingException e) {
 String expectedMessage = "Cannot deserialize value of type [simple type, class java.lang.Long] from String \"xyz\": not a valid representation";
 assertTrue(e.getMessage().contains(expectedMessage));
 }
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short shortValue = (short) 123;
 ObjectWriter writer = new ObjectMapper().writer();
 String json = writer.writeValueAsString(shortValue);
 ObjectReader reader = new ObjectMapper().reader();
 Short deserializedShort = reader.readValue(JsonFactory.getInstance().parse(json), Short.class);
 assertEquals(shortValue, deserializedShort);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float floatValue = 123.45F;
 ObjectWriter writer = new ObjectMapper().writer();
 String json = writer.writeValueAsString(floatValue);
 ObjectReader reader = new ObjectMapper().reader();
 Float deserializedFloat = reader.readValue(JsonFactory.getInstance().parse(json), Float.class);
 assertEquals(floatValue, deserializedFloat, 0.01f); // allow for small floating point error
 }

 @Test
 public void testInvalidFloatInput() throws JsonProcessingException {
 String invalidJson = "{\"float\":xyz}";
 ObjectReader reader = new ObjectMapper().reader();
 try {
 reader.readValue(JsonFactory.getInstance().parse(invalidJson), Float.class);
 fail("Expected JsonProcessingException but no exception was thrown");
 } catch (JsonMappingException e) {
 String expectedMessage = "Cannot deserialize value of type [simple type, class java.lang.Float] from String \"xyz\": not a valid representation";
 assertTrue(e.getMessage().contains(expectedMessage));
 }
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double doubleValue = 123.456789;
 ObjectWriter writer = new ObjectMapper().writer();
 String json = writer.writeValueAsString(doubleValue);
 ObjectReader reader = new ObjectMapper().reader();
 Double deserializedDouble = reader.readValue(JsonFactory.getInstance().parse(json), Double.class);
 assertEquals(doubleValue, deserializedDouble, 0.0001); // allow for small floating point error
 }

 @Test
 public void testInvalidDoubleInput() throws JsonProcessingException {
 String invalidJson = "{\"double\":xyz}";
 ObjectReader reader = new ObjectMapper().reader();
 try {
 reader.readValue(JsonFactory.getInstance().parse(invalidJson), Double.class);
 fail("Expected JsonProcessingException but no exception was thrown");
 } catch (JsonMappingException e) {
 String expectedMessage = "Cannot deserialize value of type [simple type, class java.lang.Double] from String \"xyz\": not a valid representation";
 assertTrue(e.getMessage().contains(expectedMessage));
 }
 }
}