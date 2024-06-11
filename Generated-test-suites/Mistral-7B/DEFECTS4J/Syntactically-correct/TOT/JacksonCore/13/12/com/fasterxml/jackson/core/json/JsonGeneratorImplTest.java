package com.fasterxml.jackson.core.json; // or the package where the given class is located

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link JsonGeneratorImpl} class
 */
public class JsonGeneratorImplTest {

 private static final JsonFactory JSON_FACTORY = new JsonFactory();

 @Test
 public void testWriteStringField() throws IOException {
 JsonGenerator jsonGenerator = createJsonGenerator();
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String jsonString = jsonGenerator.getOutputStr();
 assertEquals("{\"name\":\"John Doe\"}", jsonString);
 }

 @Test
 public void testWriteFieldNameWithSpecialChars() throws IOException {
 JsonGenerator jsonGenerator = createJsonGenerator();
 jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
 @Override
 public int[] getEscapeCodesForAscii() {
 return new int[] { 0x10, 0x11, 0x7F, 0 }; // adding some custom escapes
 }
 });
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field\\tname", "value");
 jsonGenerator.writeEndObject();
 String jsonString = jsonGenerator.getOutputStr();
 assertEquals("{\"field\\tname\":\"value\"}", jsonString);
 }

 @Test(expected = IOException.class)
 public void testWriteFieldNameWithoutQuotes() throws IOException {
 JsonGenerator jsonGenerator = createJsonGenerator();
 jsonGenerator.setCfgUnqNames(true); // Unquoted field names
 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("fieldname");
 jsonGenerator.writeString("value");
 jsonGenerator.writeEndObject();
 }

 @Test(expected = IOException.class)
 public void testWriteInvalidEscapeSequence() throws IOException {
 JsonGenerator jsonGenerator = createJsonGenerator();
 jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
 @Override
 public int[] getEscapeCodesForAscii() {
 return new int[] { 0x12, 0 }; // invalid escape sequence
 }
 });
 jsonGenerator.writeString("{\"field\":\\\"value\"}"); // Double quote should be escaped
 }

 private JsonGenerator createJsonGenerator() {
 return JSON_FACTORY.createGenerator(new StringWriter());
 }

}