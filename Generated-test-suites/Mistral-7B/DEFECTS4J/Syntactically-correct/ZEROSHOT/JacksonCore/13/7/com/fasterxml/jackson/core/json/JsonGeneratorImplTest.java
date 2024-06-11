package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.base.TestGeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private IOContext ioContext;
 private CharacterEscapes characterEscapes;

 @Before
 public void setup() {
 ioContext = new IOContext();
 characterEscapes = new CharacterEscapes();
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null) {
 @Override
 protected CharacterEscapes _characterEscapes() {
 return characterEscapes;
 }
 };
 }

 @Test
 public void testDefaults() throws IOException, JsonProcessingException {
 assertEquals(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
 assertEquals(JsonGenerator.Feature.CANONICAL_FORMAT.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.CANONICAL_FORMAT));
 assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
 assertEquals(JsonGenerator.Feature.FAIL_ON_EMPTY_BEFORE_ARRAY.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.FAIL_ON_EMPTY_BEFORE_ARRAY));
 assertEquals(JsonGenerator.Feature.FAIL_ON_EMPTY_BEFORE_CONTENT.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.FAIL_ON_EMPTY_BEFORE_CONTENT));
 assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES.getDefaultValue(), jsonGenerator.isQuotingNames());
 assertEquals(JsonGenerator.Feature.WRITE_BODY_BEFORE_START_OBJECT.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.WRITE_BODY_BEFORE_START_OBJECT));
 assertEquals(0, jsonGenerator.getHighestEscapedChar());
 assertNull(jsonGenerator.getCharacterEscapes());
 assertNotNull(jsonGenerator.getDefaultPrettyPrinter());
 }

 @Test
 public void testQuotingNames() throws IOException, JsonProcessingException {
 jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
 String json = jsonGenerator.getCodec().writeValueAsString(new Object(){ String name = "test"; });
 assertTrue(json.contains("\"test\":"));
 jsonGenerator.disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
 json = jsonGenerator.getCodec().writeValueAsString(new Object(){ String name = "test"; });
 assertFalse(json.contains("\"test\":"));
 }

 @Test
 public void testEscapeNonAscii() throws IOException, JsonProcessingException {
 characterEscapes.putEscapeSequence("\\u00A0", "\u00A0");
 jsonGenerator.setCharacterEscapes(characterEscapes);
 String json = jsonGenerator.getCodec().writeValueAsString(new Object(){ String str = "\u00A0test"; });
 assertTrue(json.contains("\\u00A0test"));
 }

 @Test
 public void testSetHighestNonEscapedChar() throws IOException, JsonProcessingException {
 jsonGenerator.setHighestNonEscapedChar(127);
 String json = jsonGenerator.getCodec().writeValueAsString(new Object(){ String str = "test\u0080"; });
 assertEquals("test\\u0080", json);
 jsonGenerator.setHighestNonEscapedChar(0);
 json = jsonGenerator.getCodec().writeValueAsString(new Object(){ String str = "test\u0080"; });
 assertEquals("test\\u0080", json);
 jsonGenerator.setHighestNonEscapedChar(128);
 json = jsonGenerator.getCodec().writeValueAsString(new Object(){ String str = "test\u0080"; });
 assertEquals("test\\u0080", json);
 jsonGenerator.setHighestNonEscapedChar(129);
 json = jsonGenerator.getCodec().writeValueAsString(new Object(){ String str = "test\u0080"; });
 assertEquals("test\\u0080", json);
 jsonGenerator.setHighestNonEscapedChar(130);
 json = jsonGenerator.getCodec().writeValueAsString(new Object(){ String str = "test\u0080"; });
 assertNotEquals("test\\u0080", json);
 }

 @Test
 public void testRootValueSeparator() throws IOException, JsonProcessingException {
 jsonGenerator.setRootValueSeparator(new TextFormat(": "));
 String json = jsonGenerator.getCodec().writeValueAsString(new Object(){ String str = "test"; });
 assertEquals(": test", json);
 }
}