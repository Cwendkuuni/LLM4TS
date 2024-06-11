import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for NumberSerializer class.
 */
public class NumberSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 /**
  * Test serialization and deserialization of BigDecimal.
  */
 @Test
 public void testSerializationAndDeserializationOfBigDecimal() throws JsonProcessingException, JsonMappingException {
 BigDecimal bigDecimal = new BigDecimal("123456.789");

 // Serialize BigDecimal to JSON
 String json = mapper.writeValueAsString(bigDecimal);
 System.out.println("JSON representation of BigDecimal: " + json);

 // Deserialize JSON back to BigDecimal
 BigDecimal deserializedBigDecimal = mapper.readValue(json, BigDecimal.class);

 // Check if deserialized BigDecimal is equal to original one
 assertEquals(bigDecimal, deserializedBigDecimal);
 }

 /**
  * Test serialization and deserialization of BigInteger.
  */
 @Test
 public void testSerializationAndDeserializationOfBigInteger() throws JsonProcessingException, JsonMappingException {
 BigInteger bigInteger = new BigInteger("12345678901234567890");

 // Serialize BigInteger to JSON
 String json = mapper.writeValueAsString(bigInteger);
 System.out.println("JSON representation of BigInteger: " + json);

 // Deserialize JSON back to BigInteger
 BigInteger deserializedBigInteger = mapper.readValue(json, BigInteger.class);

 // Check if deserialized BigInteger is equal to original one
 assertEquals(bigInteger, deserializedBigInteger);
 }

 /**
  * Test serialization and deserialization of Integer.
  */
 @Test
 public void testSerializationAndDeserializationOfInteger() throws JsonProcessingException, JsonMappingException {
 Integer integer = 12345;

 // Serialize Integer to JSON
 String json = mapper.writeValueAsString(integer);
 System.out.println("JSON representation of Integer: " + json);

 // Deserialize JSON back to Integer
 Integer deserializedInteger = mapper.readValue(json, Integer.class);

 // Check if deserialized Integer is equal to original one
 assertEquals(integer, deserializedInteger);
 }

 /**
  * Test serialization and deserialization of BigDecimal with invalid value.
  */
 @Test(expected = JsonMappingException.class)
 public void testSerializationAndDeserializationOfInvalidBigDecimal() throws JsonProcessingException, JsonMappingException {
 BigDecimal invalidBigDecimal = new BigDecimal("asdf");

 // Serialize BigDecimal to JSON
 mapper.writeValueAsString(invalidBigDecimal);
 }

 /**
  * Test serialization and deserialization of BigInteger with invalid value.
  */
 @Test(expected = JsonMappingException.class)
 public void testSerializationAndDeserializationOfInvalidBigInteger() throws JsonProcessingException, JsonMappingException {
 BigInteger invalidBigInteger = new BigInteger("asdf");

 // Serialize BigInteger to JSON
 mapper.writeValueAsString(invalidBigInteger);
 }

 /**
  * Test serialization and deserialization of null Number.
  */
 @Test
 public void testSerializationAndDeserializationOfNullNumber() throws JsonProcessingException, JsonMappingException {
 Number nullNumber = null;

 // Serialize null Number to JSON
 String json = mapper.writeValueAsString(nullNumber);
 System.out.println("JSON representation of null Number: " + json);

 // Deserialize JSON back to null Number
 Number deserializedNullNumber = mapper.readValue(json, Number.class);

 // Check if deserialized Number is null
 assertEquals(nullNumber, deserializedNullNumber);
 }

 /**
  * Test serialization and deserialization with custom ObjectMapper configuration.
  */
 @Test
 public void testSerializationAndDeserializationWithCustomObjectMapperConfiguration() throws JsonProcessingException, JsonMappingException {
 ObjectMapper customMapper = new ObjectMapper();
 customMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 BigDecimal bigDecimal = new BigDecimal("123456.789");

 // Serialize BigDecimal to JSON
 String json = customMapper.writeValueAsString(bigDecimal);
 System.out.println("JSON representation of BigDecimal: " + json);

 // Deserialize JSON back to BigDecimal
 BigDecimal deserializedBigDecimal = customMapper.readValue(json, BigDecimal.class);

 // Check if deserialized BigDecimal is equal to original one
 assertEquals(bigDecimal, deserializedBigDecimal);
 }

}