package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testSerializeNumber() throws JsonGenerationException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;

 // Test serialization for basic numbers
 Number[] numbers = new Number[] { 1, 1L, 1.0F, 1.0, -1, -1L, -1.0F, -1.0, new BigDecimal("1"), new BigDecimal("1.0"), new BigInteger("1"), new BigInteger("1"), new Byte((byte) 1), new Short((short) 1) };
 for (Number number : numbers) {
 JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(System.out);
 serializer.serialize(number, jsonGenerator, MAPPER.getSerializerProvider());
 jsonGenerator.flush();
 }

 // Test serialization for BigDecimal and BigInteger
 BigDecimal bigDecimal = new BigDecimal("1234567890.1234567890");
 BigInteger bigInteger = new BigInteger("98765432109876543210");
 JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(System.out);
 serializer.serialize(bigDecimal, jsonGenerator, MAPPER.getSerializerProvider());
 jsonGenerator.writeNumber("."); // Separator for BigDecimal
 serializer.serialize(bigInteger, jsonGenerator, MAPPER.getSerializerProvider());
 jsonGenerator.flush();
 }

 @Test
 public void testDeserializeNumber() throws IOException {
 JsonParser jsonParser = MAPPER.getFactory().createParser("1");
 assertEquals(1, JsonSerialization.readValue(JsonToken.VALUE_NUMBER, JsonNode.class, jsonParser).numberValue().longValue());

 JsonParser jsonParserBigDecimal = MAPPER.getFactory().createParser("1234567890.1234567890");
 assertEquals(new BigDecimal("1234567890.1234567890"), JsonSerialization.readValue(JsonToken.VALUE_NUMBER_INT, JsonNode.class, jsonParserBigDecimal));

 JsonParser jsonParserBigInteger = MAPPER.getFactory().createParser("98765432109876543210");
 assertEquals(new BigInteger("98765432109876543210"), JsonSerialization.readValue(JsonToken.VALUE_NUMBER_INT, JsonNode.class, jsonParserBigInteger));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = jsonFactory.constructDefaultInputFormatVisitor(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, null);

 NumberSerializer numberSerializer = NumberSerializer.instance;
 numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, Object.class.getType());

 assertEquals("number", jsonFormatVisitorWrapper.getSchemaName());
 assertTrue(jsonFormatVisitorWrapper.canHandleNumberType(NumberType.NUMBER));
 }
}