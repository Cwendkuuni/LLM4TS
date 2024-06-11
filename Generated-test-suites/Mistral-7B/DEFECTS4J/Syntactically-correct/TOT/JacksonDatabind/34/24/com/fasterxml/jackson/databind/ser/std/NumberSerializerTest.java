import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

 private ObjectMapper objectMapper = new JsonMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(NumberSerializer.class).createContext(null).getSerializerProvider().getGenerator();
 BigDecimal bigDecimal = new BigDecimal("123.45");
 numberSerializer.serialize(bigDecimal, jsonGenerator, null);
 String expected = "\"123.45\"";
 String actual = jsonGenerator.getOutputBuffer().toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(NumberSerializer.class).createContext(null).getSerializerProvider().getGenerator();
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 numberSerializer.serialize(bigInteger, jsonGenerator, null);
 String expected = "12345678901234567890";
 String actual = jsonGenerator.getOutputBuffer().toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSerializeInteger() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(NumberSerializer.class).createContext(null).getSerializerProvider().getGenerator();
 Integer integer = 123;
 numberSerializer.serialize(integer, jsonGenerator, null);
 String expected = "123";
 String actual = jsonGenerator.getOutputBuffer().toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSerializeUnsupportedNumberType() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(NumberSerializer.class).createContext(null).getSerializerProvider().getGenerator();
 Byte byteValue = 127;
 numberSerializer.serialize(byteValue, jsonGenerator, null);
 String expected = "127";
 String actual = jsonGenerator.getOutputBuffer().toString();
 Assert.assertEquals(expected, actual);
 // although it actually serializes int value, this test passes because we expect the same output
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeUnsupportedClass() throws JsonGenerationException, IOException {
 NumberSerializer numberSerializer = new NumberSerializer(String.class);
 JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(NumberSerializer.class).createContext(null).getSerializerProvider().getGenerator();
 String stringValue = "Hello";
 numberSerializer.serialize(stringValue, jsonGenerator, null);
 }

 @Test
 public void testDeserializeBigDecimal() throws IOException {
 ObjectMapper objectMapper = new JsonMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "\"123.45\"";
 Number number = objectMapper.readValue(json, Number.class);
 Assert.assertNotNull(number);
 Assert.assertTrue(number instanceof BigDecimal);
 BigDecimal bigDecimal = (BigDecimal) number;
 Assert.assertEquals("123.45", bigDecimal.toString());
 }

 @Test
 public void testDeserializeBigInteger() throws IOException {
 ObjectMapper objectMapper = new JsonMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "12345678901234567890";
 Number number = objectMapper.readValue(json, Number.class);
 Assert.assertNotNull(number);
 Assert.assertTrue(number instanceof BigInteger);
 BigInteger bigInteger = (BigInteger) number;
 Assert.assertEquals("12345678901234567890", bigInteger.toString());
 }

 @Test
 public void testDeserializeInteger() throws IOException {
 ObjectMapper objectMapper = new JsonMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "123";
 Number number = objectMapper.readValue(json, Number.class);
 Assert.assertNotNull(number);
 Assert.assertTrue(number instanceof Integer);
 Integer integer = (Integer) number;
 Assert.assertEquals(123, integer.intValue());
 }

 @Test
 public void testDeserializeUnsupportedNumberType() throws IOException {
 ObjectMapper objectMapper = new JsonMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "127";
 Number number = objectMapper.readValue(json, Number.class);
 Assert.assertNotNull(number);
 Assert.assertTrue(number instanceof Integer);
 Integer integer = (Integer) number;
 Assert.assertEquals(127, integer.intValue());
 // although it is deserialized as an integer, this test passes because we expect the same value
 }
}