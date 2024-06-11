package com.fasterxml.jackson.core.json; // or the package where the class is located

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;

 @Before
 public void setUp() {
 try {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(),
 Feature.AUTO_CLOSE_SOURCE | Feature.ESCAPE_NON_ASCII, new SimpleObjectMapper());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 /**
 * Test writing a simple JSON object with string fields.
 */
 @Test
 public void testSimpleJsonObject() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field1", "value1");
 jsonGenerator.writeStringField("field2", "value2");
 jsonGenerator.writeEndObject();
 String json = jsonGenerator.getOutputStr();
 assertEquals("{\"field1\":\"value1\",\"field2\":\"value2\"}", json);
 }

 /**
 * Test writing a JSON object with null value for a field.
 */
 @Test
 public void testJsonObjectWithNullField() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field1", "value1");
 jsonGenerator.writeNullField("field2");
 jsonGenerator.writeEndObject();
 String json = jsonGenerator.getOutputStr();
 assertEquals("{\"field1\":\"value1\",\"field2\":null}", json);
 }

 /**
 * Test writing an empty JSON object.
 */
 @Test
 public void testEmptyJsonObject() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeEndObject();
 String json = jsonGenerator.getOutputStr();
 assertEquals("{}", json);
 }

 /**
 * Test handling of non-ASCII characters.
 */
 @Test
 public void testHandlingOfNonAsciiCharacters() throws IOException {
 jsonGenerator.setHighestNonEscapedChar(128); // set a non-ASCII character as non-escaped
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field1", "Mööse");
 jsonGenerator.writeEndObject();
 String json = jsonGenerator.getOutputStr();
 assertEquals("{\"field1\":\"M\"ö\"ose\"}", json); // non-ASCII characters should be escaped
 }

 /**
 * Test setting custom character escapes.
 */
 @Test
 public void testSettingCustomCharacterEscapes() throws IOException {
 jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
 @Override
 public int getEscapeCodeForChar(int c) {
 return c == '\\' ? '\\' * 2 : super.getEscapeCodeForChar(c);
 }
 });
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field1", "value\\");
 jsonGenerator.writeEndObject();
 String json = jsonGenerator.getOutputStr();
 assertEquals("{\"field1\":\"value\\\\\"}", json); // backslash character should be escaped twice
 }
}