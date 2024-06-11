package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.IOContext;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test for IntegerSerializer
 */
 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer number = 123;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(123, parser.getValueAsInt());
 }

 /**
 * Test for IntegerSerializer with negative number
 */
 @Test
 public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
 Integer number = -123;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(-123, parser.getValueAsInt());
 }

 /**
 * Test for IntegerSerializer with maximum value
 */
 @Test
 public void testIntegerSerializerMaxValue() throws JsonProcessingException {
 Integer number = Integer.MAX_VALUE;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(Integer.MAX_VALUE, parser.getValueAsInt());
 }

 /**
 * Test for IntegerSerializer with minimum value
 */
 @Test
 public void testIntegerSerializerMinValue() throws JsonProcessingException {
 Integer number = Integer.MIN_VALUE;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(Integer.MIN_VALUE, parser.getValueAsInt());
 }

 /**
 * Test for LongSerializer
 */
 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long number = 123L;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
 assertEquals(123, parser.getValueAsLong());
 }

 /**
 * Test for LongSerializer with negative number
 */
 @Test
 public void testLongSerializerNegativeNumber() throws JsonProcessingException {
 Long number = -123L;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
 assertEquals(-123, parser.getValueAsLong());
 }

 /**
 * Test for LongSerializer with maximum value
 */
 @Test
 public void testLongSerializerMaxValue() throws JsonProcessingException {
 Long number = Long.MAX_VALUE;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
 assertEquals(Long.MAX_VALUE, parser.getValueAsLong());
 }

 /**
 * Test for LongSerializer with minimum value
 */
 @Test
 public void testLongSerializerMinValue() throws JsonProcessingException {
 Long number = Long.MIN_VALUE;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
 assertEquals(Long.MIN_VALUE, parser.getValueAsLong());
 }

 /**
 * Test for FloatSerializer
 */
 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float number = 123.45f;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
 assertEquals(123.45f, parser.getValueAsFloat(), 0.0f);
 }

 /**
 * Test for FloatSerializer with negative number
 */
 @Test
 public void testFloatSerializerNegativeNumber() throws JsonProcessingException {
 Float number = -123.45f;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
 assertEquals(-123.45f, parser.getValueAsFloat(), 0.0f);
 }

 /**
 * Test for FloatSerializer with maximum value
 */
 @Test
 public void testFloatSerializerMaxValue() throws JsonProcessingException {
 Float number = Float.MAX_VALUE;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
 assertEquals(Float.MAX_VALUE, parser.getValueAsFloat(), 0.0f);
 }

 /**
 * Test for FloatSerializer with minimum value
 */
 @Test
 public void testFloatSerializerMinValue() throws JsonProcessingException {
 Float number = Float.MIN_VALUE;
 ObjectWriter writer = objectMapper.writerWithTypeValidator(false).withContext(IOContext.StdIO);
 String json = writer.writeValueAsString(number);
 JsonParser parser = objectMapper.getFactory().createParser(json);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
 assertEquals(Float.MIN_VALUE, parser.getValueAsFloat(), 0.0f);
 }
}