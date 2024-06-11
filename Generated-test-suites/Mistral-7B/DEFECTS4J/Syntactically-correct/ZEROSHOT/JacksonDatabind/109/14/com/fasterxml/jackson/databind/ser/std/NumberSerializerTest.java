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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializationTest;
import com.fasterxml.jackson.databind.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NumberSerializerTest extends SerializationTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testNumberSerialization() throws JsonProcessingException, JsonGenerationException {
 NumberSerializer serializer = NumberSerializer.instance;

 // Test serialization of Number subclasses
 BigDecimal bd = new BigDecimal("123.45");
 BigInteger bi = BigInteger.valueOf(123L);
 assertNull(serializer.findFormatOverrides(null, null, Number.class));
 assertEquals("123.45", mapper.writeValueAsString(bd));
 assertEquals("123", mapper.writeValueAsString(bi));

 // Test serialization of primitive types
 int i = 123;
 float f = 123.45f;
 double d = 123.45;
 assertEquals("123", mapper.writeValueAsString(i));
 assertEquals("123.45", mapper.writeValueAsString(f));
 assertEquals("123.45", mapper.writeValueAsString(d));

 // Test serialization with JsonFormat annotation
 @JsonFormat(shape = Shape.STRING)
 Integer intAnnotation = 123;
 Number intAnnotated = intAnnotation;
 assertEquals("123", mapper.writeValueAsString(intAnnotated));
 }

 @Test
 public void testNumberSerializationWithFormatVisitors() throws JsonProcessingException, JsonGenerationException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 ObjectNode node = mapper.createObjectNode();
 JsonFormatVisitorWrapper formatVisitor = mapper.schemaFactory().getSchemaProvider().getSchemaFactory(ObjectNode.class)
 .acceptJsonFormatVisitor(node, NumberSerializer.class);

 // Test serialization of Number subclasses
 BigDecimal bd = new BigDecimal("123.45");
 BigInteger bi = BigInteger.valueOf(123L);
 formatVisitor.expectNumberFormat(NumberSerializer.class, JsonParser.NumberType.BIG_INTEGER);
 formatVisitor.visitIntFormat(NumberSerializer.class, Number.class, JsonParser.NumberType.BIG_INTEGER);
 formatVisitor.visitNumberFormat(NumberSerializer.class, Number.class, JsonParser.NumberType.BIG_DECIMAL);

 formatVisitor.acceptNumberFormat(bd, null);
 formatVisitor.acceptNumberFormat(bi, null);
 formatVisitor.acceptEnd();
 ObjectNode result = node.deepClone();
 assertEquals("{\"numberType\":\"bigDecimal\",\"baseType\":\"NUMBER\",\"schemaType\":\"NUMBER\"}", result.toString());
 }

 @Test
 public void testContextualSerialization() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectWriter writer = mapper.writer();

 // Test serialization of Number subclasses
 BigDecimal bd = new BigDecimal("123.45");
 BigInteger bi = BigInteger.valueOf(123L);
 writeTest("123.45", bd, writer);
 writeTest("123", bi, writer);

 // Test serialization of primitive types
 int i = 123;
 float f = 123.45f;
 double d = 123.45;
 writeTest("123", i, writer);
 writeTest("123.45", f, writer);
 writeTest("123.45", d, writer);
 }

 private void writeTest(String expected, Number number, ObjectWriter writer) throws JsonProcessingException {
 String actual = writer.writeValueAsString(number);
 assertEquals(expected, actual);
 }
}