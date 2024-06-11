package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.45");
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
 jsonGenerator.flush();

 Object readValue = objectMapper.readValue(System.in, Number.class);
 assertEquals(bigDecimal, readValue);
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(bigInteger, jsonGenerator, serializerProvider);
 jsonGenerator.flush();

 Object readValue = objectMapper.readValue(System.in, Number.class);
 assertEquals(bigInteger, readValue);
 }

 @Test
 public void testSerializeInteger() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Integer integer = 123;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(integer, jsonGenerator, serializerProvider);
 jsonGenerator.flush();

 Object readValue = objectMapper.readValue(System.in, Number.class);
 assertEquals(integer, readValue);
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Long longNumber = 1234567890123456789L;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(longNumber, jsonGenerator, serializerProvider);
 jsonGenerator.flush();

 Object readValue = objectMapper.readValue(System.in, Number.class);
 assertEquals(longNumber, readValue);
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Double doubleValue = 123.45;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(doubleValue, jsonGenerator, serializerProvider);
 jsonGenerator.flush();

 Object readValue = objectMapper.readValue(System.in, Number.class);
 assertEquals(doubleValue, readValue, 0.001);
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Float floatValue = 123.45f;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(floatValue, jsonGenerator, serializerProvider);
 jsonGenerator.flush();

 Object readValue = objectMapper.readValue(System.in, Number.class);
 assertEquals(floatValue, readValue, 0.001f);
 }

 @Test
 public void testSerializeOtherNumberTypes() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 Byte byteValue = 123;
 Short shortValue = 123;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 serializer.serialize(byteValue, jsonGenerator, serializerProvider);
 jsonGenerator.flush();

 Object readByteValue = objectMapper.readValue(System.in, Number.class);
 assertEquals(byteValue.intValue(), readByteValue.intValue());

 jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(shortValue, jsonGenerator, serializerProvider);
 jsonGenerator.flush();

 Object readShortValue = objectMapper.readValue(System.in, Number.class);
 assertEquals(shortValue.intValue(), readShortValue.intValue());
 }

 @Test
 public void testDeserializeNumberTypes() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper deserializerObjectMapper = new ObjectMapper();
 deserializerObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 deserializerObjectMapper.setDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

 ObjectReader reader = deserializerObjectMapper.getObjectReader();
 JavaType javaType = reader.getType(Number.class);

 JsonParser jsonParser = objectMapper.getFactory().createParser("123");
 Number deserializedNumber = reader.readValue(jsonParser, javaType);
 assertEquals(123, deserializedNumber.intValue());

 jsonParser = objectMapper.getFactory().createParser("123.45");
 deserializedNumber = reader.readValue(jsonParser, javaType);
 assertEquals(123.45, deserializedNumber.doubleValue(), 0.001);

 jsonParser = objectMapper.getFactory().createParser("123.45e0");
 deserializedNumber = reader.readValue(jsonParser, javaType);
 assertEquals(123.45, deserializedNumber.doubleValue(), 0.001);

 jsonParser = objectMapper.getFactory().createParser("1.23E+10");
 deserializedNumber = reader.readValue(jsonParser, javaType);
 assertEquals(123000000000.0, deserializedNumber.doubleValue(), 0.001);

 jsonParser = objectMapper.getFactory().createParser("12345678901234567890");
 deserializedNumber = reader.readValue(jsonParser, javaType);
 assertEquals(new BigInteger("12345678901234567890"), deserializedNumber);

 jsonParser = objectMapper.getFactory().createParser("123.45");
 deserializedNumber = reader.readValue(jsonParser, javaType);
 assertEquals(new BigDecimal("123.45"), deserializedNumber);
 }

 @Test
 public void testDeserializeInvalidNumber() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper deserializerObjectMapper = new ObjectMapper();
 deserializerObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 deserializerObjectMapper.setDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

 ObjectReader reader = deserializerObjectMapper.getObjectReader();
 JavaType javaType = reader.getType(Number.class);

 JsonParser jsonParser = objectMapper.getFactory().createParser("abc");
 try {
 reader.readValue(jsonParser, javaType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }

 jsonParser = objectMapper.getFactory().createParser("true");
 try {
 reader.readValue(jsonParser, javaType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test
 public void testDeserializeFloatTypeNumber() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper deserializerObjectMapper = new ObjectMapper();
 deserializerObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 deserializerObjectMapper.setDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

 ObjectReader reader = deserializerObjectMapper.getObjectReader();
 JavaType javaType = reader.getType(Number.class);

 JsonParser jsonParser = objectMapper.getFactory().createParser("123.45");
 Number deserializedNumber = reader.readValue(jsonParser, javaType);
 assertEquals(123.45, deserializedNumber.doubleValue(), 0.001);

 TypeSerializer typeSerializer = objectMapper.getTypeSerializer(javaType);
 JsonParser jsonParserWithType = objectMapper.getFactory().createParser("123.45");
 jsonParserWithType.nextTokenId(); // Consume START_OBJECT token
 jsonParserWithType.nextValue("number");
 jsonParserWithType.nextTokenId(); // Consume COLON token
 jsonParserWithType.nextValue("float");
 jsonParserWithType.nextTokenId(); // Consume COMMA token
 jsonParserWithType.nextValue("123.45");
 JsonParser jsonParserForDeserialization = jsonParserWithType;

 Number deserializedNumberWithType = reader.readValue(jsonParserForDeserialization, javaType);
 assertEquals(123.45, deserializedNumberWithType.floatValue(), 0.001);
 }

 @Test
 public void testDeserializeIntTypeNumber() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper deserializerObjectMapper = new ObjectMapper();
 deserializerObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 deserializerObjectMapper.setDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

 ObjectReader reader = deserializerObjectMapper.getObjectReader();
 JavaType javaType = reader.getType(Number.class);

 JsonParser jsonParser = objectMapper.getFactory().createParser("123");
 Number deserializedNumber = reader.readValue(jsonParser, javaType);
 assertEquals(123, deserializedNumber.intValue());

 TypeSerializer typeSerializer = objectMapper.getTypeSerializer(javaType);
 JsonParser jsonParserWithType = objectMapper.getFactory().createParser("123");
 jsonParserWithType.nextTokenId(); // Consume START_OBJECT token
 jsonParserWithType.nextValue("number");
 jsonParserWithType.nextTokenId(); // Consume COLON token
 jsonParserWithType.nextValue("integer");
 jsonParserWithType.nextTokenId(); // Consume COMMA token
 jsonParserWithType.nextValue("123");
 JsonParser jsonParserForDeserialization = jsonParserWithType;

 Number deserializedNumberWithType = reader.readValue(jsonParserForDeserialization, javaType);
 assertEquals(123, deserializedNumberWithType.intValue());
 }
}