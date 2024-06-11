package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

 private GeneratorBase generator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws IOException {
 generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), new ObjectMapper.DefaultObjectMapper());
 }

 @Test
 public void testWriteString() throws IOException {
 String str = "Test string";
 generator.writeString(str);
 // Verify output is correct
 assertEquals(str, generator.getCurrentValueAsString());
 }

 @Test
 public void testWriteNull() throws IOException {
 generator.writeNull();
 // Verify null was written correctly
 assertNull(generator.getCurrentValue());
 }

 @Test
 public void testWriteBoolean() throws IOException {
 generator.writeBoolean(true);
 generator.writeBoolean(false);
 // Verify boolean values were written correctly
 assertEquals(Boolean.TRUE.toString(), generator.getCurrentValueAsString());
 assertEquals(Boolean.FALSE.toString(), generator.getCurrentValueAsString());
 }

 @Test
 public void testWriteNumber() throws IOException {
 generator.writeNumber(123);
 generator.writeNumber(456);
 generator.writeNumber(1.23f);
 generator.writeNumber(987.123);
 generator.writeNumber(new BigDecimal("123.45"));
 // Verify numbers were written correctly
 assertEquals(Integer.toString(123), generator.getCurrentValueAsString());
 assertEquals(Integer.toString(456), generator.getCurrentValueAsString());
 assertEquals(Float.toString(1.23f), generator.getCurrentValueAsString());
 assertEquals(Double.toString(987.123), generator.getCurrentValueAsString());
 assertEquals(generator._asString(new BigDecimal("123.45")), generator.getCurrentValueAsString());
 }

 @Test(expected = IOException.class)
 public void testWriteInvalidUtf8() throws IOException {
 generator.writeRawValue("\uD800\uDC00"); // Incomplete surrogate pair
 }

 @Test
 public void testWriteArray() throws IOException {
 int[] arr = {1, 2, 3};
 generator.writeStartArray();
 for (int i : arr) {
 generator.writeNumber(i);
 }
 generator.writeEndArray();
 // Verify array was written correctly
 String json = generator.getOutputContext().getJsonStream().asString();
 assertEquals("[1,2,3]", json);
 }

 @Test
 public void testWriteObject() throws IOException {
 MyClass obj = new MyClass("Test", 123);
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("Test");
 generator.writeFieldName("id");
 generator.writeNumber(123);
 generator.writeEndObject();
 // Verify object was written correctly
 String json = generator.getOutputContext().getJsonStream().asString();
 assertEquals("{\"name\":\"Test\",\"id\":123}", json);
 }

 @Test
 public void testWriteComplexObjectTree() throws IOException {
 MyClass obj = new MyClass("Test", new MyNestedClass("Nested Test", 456));
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("Test");
 generator.writeFieldName("nested");
 generator.writeObject(obj);
 generator.writeEndObject();
 // Verify object tree was written correctly
 String json = generator.getOutputContext().getJsonStream().asString();
 assertEquals("{\"name\":\"Test\",\"nested\":{\"name\":\"Nested Test\",\"id\":456}}", json);
 }

 @Test(expected = IOException.class)
 public void testWriteInvalidJson() throws IOException {
 generator.writeStartArray();
 generator.writeNumber(1);
 generator.writeEndArray();
 generator.writeString("{"); // Invalid JSON: missing comma between array and object
 }

 @Test
 public void testWriteEmptyJson() throws IOException {
 generator.writeStartObject();
 generator.writeEndObject();
 // Verify empty JSON was written correctly
 String json = generator.getOutputContext().getJsonStream().asString();
 assertEquals("{}", json);
 }

 @Test
 public void testCloseGenerator() throws IOException {
 generator.close();
 assertFalse(generator.isOpen());
 }
}

class MyClass {
 String name;
 int id;
 MyNestedClass nested;

 public MyClass(String name, int id) {
 this.name = name;
 this.id = id;
 }

 public MyClass(String name, MyNestedClass nested) {
 this.name = name;
 this.nested = nested;
 }
}

class MyNestedClass {
 String name;
 int id;

 public MyNestedClass(String name, int id) {
 this.name = name;
 this.id = id;
 }
}