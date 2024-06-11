import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;

public class NumberSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 BigDecimal bigDecimal = new BigDecimal("12345.6789");
 numberSerializer.serialize(bigDecimal, jsonGenerator, null);

 String expectedJson = "{\"@type\":\"Number\",\"numberValue\":12345.6789}";
 String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 BigInteger bigInteger = new BigInteger("98765432109876543210");
 numberSerializer.serialize(bigInteger, jsonGenerator, null);

 String expectedJson = "{\"@type\":\"Number\",\"numberValue\":98765432109876543210}";
 String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 Integer integer = 123;
 numberSerializer.serialize(integer, jsonGenerator, null);

 String expectedJson = "123";
 String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 Long longNumber = 1234567890L;
 numberSerializer.serialize(longNumber, jsonGenerator, null);

 String expectedJson = "1234567890";
 String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 Double doubleNumber = 123.45;
 numberSerializer.serialize(doubleNumber, jsonGenerator, null);

 String expectedJson = "123.45";
 String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 Float floatNumber = 123.45f;
 numberSerializer.serialize(floatNumber, jsonGenerator, null);

 String expectedJson = "123.45";
 String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 Byte byteNumber = (byte) 123;
 numberSerializer.serialize(byteNumber, jsonGenerator, null);

 String expectedJson = "123";
 String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 Short shortNumber = (short) 123;
 numberSerializer.serialize(shortNumber, jsonGenerator, null);

 String expectedJson = "123";
 String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testSerializeString() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 String stringNumber = "123";
 numberSerializer.serialize(stringNumber, jsonGenerator, null);

 String expectedJson = "\"123\"";
 String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws JsonProcessingException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGenerator();

 Number nullNumber = null;
 numberSerializer.serialize(nullNumber, jsonGenerator, null);
 }

 @Test
 public void testDeserializeBigDecimal() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "{\"@type\":\"Number\",\"numberValue\":12345.6789}";
 Number deserializedNumber = objectMapper.readValue(json, Number.class);

 BigDecimal expectedBigDecimal = new BigDecimal("12345.6789");
 BigDecimal actualBigDecimal = (BigDecimal) deserializedNumber;
 Assert.assertEquals(expectedBigDecimal, actualBigDecimal);
 }

 @Test
 public void testDeserializeBigInteger() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "{\"@type\":\"Number\",\"numberValue\":98765432109876543210}";
 Number deserializedNumber = objectMapper.readValue(json, Number.class);

 BigInteger expectedBigInteger = new BigInteger("98765432109876543210");
 BigInteger actualBigInteger = (BigInteger) deserializedNumber;
 Assert.assertEquals(expectedBigInteger, actualBigInteger);
 }

 @Test
 public void testDeserializeInteger() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "123";
 Number deserializedNumber = objectMapper.readValue(json, Number.class);

 Integer expectedInteger = 123;
 Integer actualInteger = (Integer) deserializedNumber;
 Assert.assertEquals(expectedInteger, actualInteger);
 }

 @Test
 public void testDeserializeLong() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "1234567890";
 Number deserializedNumber = objectMapper.readValue(json, Number.class);

 Long expectedLong = 1234567890L;
 Long actualLong = (Long) deserializedNumber;
 Assert.assertEquals(expectedLong, actualLong);
 }

 @Test
 public void testDeserializeDouble() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "123.45";
 Number deserializedNumber = objectMapper.readValue(json, Number.class);

 Double expectedDouble = 123.45;
 Double actualDouble = (Double) deserializedNumber;
 Assert.assertEquals(expectedDouble, actualDouble);
 }

 @Test
 public void testDeserializeFloat() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "123.45";
 Number deserializedNumber = objectMapper.readValue(json, Number.class);

 Float expectedFloat = 123.45f;
 Float actualFloat = (Float) deserializedNumber;
 Assert.assertEquals(expectedFloat, actualFloat);
 }

 @Test
 public void testDeserializeByte() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "123";
 Number deserializedNumber = objectMapper.readValue(json, Number.class);

 Byte expectedByte = (byte) 123;
 Byte actualByte = (Byte) deserializedNumber;
 Assert.assertEquals(expectedByte, actualByte);
 }

 @Test
 public void testDeserializeShort() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "123";
 Number deserializedNumber = objectMapper.readValue(json, Number.class);

 Short expectedShort = (short) 123;
 Short actualShort = (Short) deserializedNumber;
 Assert.assertEquals(expectedShort, actualShort);
 }

 @Test
 public void testDeserializeString() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "\"123\"";
 Number deserializedNumber = objectMapper.readValue(json, Number.class);

 String expectedString = "123";
 String actualString = deserializedNumber.toString();
 Assert.assertEquals(expectedString, actualString);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNull() throws JsonProcessingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = null;
 Number deserializedNumber = objectMapper.readValue(json, Number.class);
 }
}