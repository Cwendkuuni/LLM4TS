package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;

 @Before
 public void setup() {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 }

 @Test
 public void testDefaultConfiguration() {
 // Test default configuration values
 assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
 assertNull(jsonGenerator._characterEscapes);
 assertNull(jsonGenerator._rootValueSeparator);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 CharacterEscapes characterEscapes = new StandardCharacterEscapes();
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertSame(characterEscapes, jsonGenerator._characterEscapes);
 assertArrayEquals(characterEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);
 }

 @Test
 public void testSetRootValueSeparator() {
 SerializableString rootValueSeparator = new SerializableString("test_separator");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
 }

 @Test
 public void testSetHighestNonEscapedChar() {
 jsonGenerator.setHighestNonEscapedChar(255);
 assertEquals(255, jsonGenerator.getHighestEscapedChar());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetHighestNonEscapedCharNegative() {
 jsonGenerator.setHighestNonEscapedChar(-1);
 }

 @Test
 public void testEnableQuoteFieldNames() {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 assertTrue(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testDisableQuoteFieldNames() {
 jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGenerator._cfgUnqNames);
 }
}