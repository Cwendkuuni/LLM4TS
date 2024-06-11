package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test class for {@link NumberSerializer}
 */
public class NumberSerializerTest {

 @Test
 public void testSerializeNumber() throws JsonProcessingException, IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 ObjectWriter objectWriter = new ObjectMapper().writer();

 // Test serialization of basic Number types
 Number number = 123;
 String json = objectWriter.writeValueAsString(number);
 JsonParser jsonParser = new ObjectMapper().readTree(json);
 Number deserializedNumber = jsonParser.numberValue();
 assertEquals(number, deserializedNumber);

 number = 123L;
 json = objectWriter.writeValueAsString(number);
 jsonParser = new ObjectMapper().readTree(json);
 number = jsonParser.numberValue();
 assertEquals(number, deserializedNumber);

 number = 123d;
 json = objectWriter.writeValueAsString(number);
 jsonParser = new ObjectMapper().readTree(json);
 number = jsonParser.numberValue();
 assertEquals(number, deserializedNumber, 0.0001);

 number = 123.5f;
 json = objectWriter.writeValueAsString(number);
 jsonParser = new ObjectMapper().readTree(json);
 number = jsonParser.numberValue();
 assertEquals(number, deserializedNumber, 0.0001);

 // Test serialization of BigDecimal and BigInteger
 BigDecimal bigDecimal = new BigDecimal("123.45");
 json = objectWriter.writeValueAsString(bigDecimal);
 jsonParser = new ObjectMapper().readTree(json);
 bigDecimal = jsonParser.numberValueExplicitly(JsonParser.NumberType.BIG_DECIMAL);
 assertEquals(bigDecimal, deserializedNumber);

 BigInteger bigInteger = BigInteger.valueOf(12345678912345678L);
 json = objectWriter.writeValueAsString(bigInteger);
 jsonParser = new ObjectMapper().readTree(json);
 bigInteger = jsonParser.numberValueExplicitly(JsonParser.NumberType.BIG_INTEGER);
 assertEquals(bigInteger, deserializedNumber);
 }

 @Test
 public void testContextualSerialization() throws JsonProcessingException, IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriter = objectMapper.writer();

 // Test that the default serializer is used for Number types
 Number number = 123;
 String json = objectWriter.writeValueAsString(number);
 JsonParser jsonParser = new ObjectMapper().readTree(json);
 Number deserializedNumber = jsonParser.numberValue();
 assertEquals(number, deserializedNumber);

 // Test that ToStringSerializer is used for BigDecimal
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 @JsonSerialize(using = ToStringSerializer.class)
 Number bigDecimalNumber = new BigDecimal("123.45");
 json = objectWriter.writeValueAsString(bigDecimalNumber);
 jsonParser = new ObjectMapper().readTree(json);
 BigDecimal deserializedBigDecimalNumber = new BigDecimal(jsonParser.getText());
 assertEquals(bigDecimalNumber, deserializedBigDecimalNumber);
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializationOfUnsupportedNumberType() throws JsonProcessingException, IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 ObjectWriter objectWriter = new ObjectMapper().writer();

 MyNumber myNumber = new MyNumber();
 myNumber.setValue(new ComplexNumber());
 String json = objectWriter.writeValueAsString(myNumber);
 fail("Expected JsonProcessingException but got: " + json);
 }
}

class MyNumber {
 @JsonSerialize(using = NumberSerializer.class)
 Number number;

 public Number getNumber() {
 return number;
 }

 public void setNumber(Number number) {
 this.number = number;
 }

 public ComplexNumber getComplexNumber() {
 return new ComplexNumber();
 }
}

class ComplexNumber {
 // A complex number representation that is not supported by NumberSerializer
}