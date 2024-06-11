package com.fasterxml.jackson.core.json; // or the package where your test class is located

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private final ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setUp() throws IOException {
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter(), JsonEncoding.UTF8);
 }

 @Test
 public void testWriteStringField() throws IOException {
 String fieldName = "testField";
 String value = "testValue";
 jsonGenerator.writeStringField(fieldName, value);
 String jsonString = jsonGenerator.getOutputString();
 String expectedJson = "{\"testField\":\"testValue\"}";
 assertEquals(expectedJson, jsonString);
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteStringFieldWithNullFieldName() throws IOException {
 String value = "testValue";
 jsonGenerator.writeStringField(null, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWriteStringFieldWithEmptyFieldName() throws IOException {
 String value = "testValue";
 jsonGenerator.writeStringField("", value);
 }

 @Test
 public void testWriteStringFieldWithQuotedFieldName() throws IOException {
 String fieldName = "\"testField\"";
 String value = "testValue";
 jsonGenerator.writeStringField(fieldName, value);
 String jsonString = jsonGenerator.getOutputString();
 String expectedJson = "{\"testField\":\"testValue\"}";
 assertEquals(expectedJson, jsonString);
 }

 @Test
 public void testWriteStringFieldWithEmptyValue() throws IOException {
 String fieldName = "testField";
 jsonGenerator.writeStringField(fieldName, "");
 String jsonString = jsonGenerator.getOutputString();
 String expectedJson = "{\"testField\":\"\"}";
 assertEquals(expectedJson, jsonString);
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 CharacterEscapes customCharacterEscapes = new CharacterEscapes() {
 @Override
 public String escapeByte(int b) {
 return String.format("\\u%04x", b);
 }
 };
 jsonGenerator.setCharacterEscapes(customCharacterEscapes);
 String jsonString = jsonGenerator.getOutputString();
 String expectedJson = "{\"root\":\"\\u0068\\u0065\\u006c\\u006c\\u006f\"}"; // "root" in hex is 0x68 0x65 0x6c 0x6c 0x6f
 jsonGenerator.close();
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter(), JsonEncoding.UTF8);
 }

 @After
 public void tearDown() throws IOException {
 jsonGenerator.close();
 }
}