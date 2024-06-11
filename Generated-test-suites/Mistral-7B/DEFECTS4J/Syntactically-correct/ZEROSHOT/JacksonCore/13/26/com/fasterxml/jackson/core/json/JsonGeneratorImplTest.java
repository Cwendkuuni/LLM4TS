package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;

 @Before
 public void setUp() {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new SimpleObjectCodec()) {
 @Override
 public void close() throws IOException {
 // empty implementation
 }
 };
 }

 @Test
 public void testSetHighestNonEscapedChar() throws IOException {
 jsonGenerator.setHighestNonEscapedChar(160);
 assertEquals(160, jsonGenerator.getHighestEscapedChar());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeHighestNonEscapedChar() throws IOException {
 jsonGenerator.setHighestNonEscapedChar(-1);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test
 public void testGetCharacterEscapes() throws IOException {
 assertNull(jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 CharacterEscapes escapes = new StandardCharacterEscapes();
 jsonGenerator.setCharacterEscapes(escapes);
 assertEquals(escapes, jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testSetRootValueSeparator() throws IOException {
 JsonGeneratorImpl jsonGeneratorWithRootValueSeparator = new JsonGeneratorImpl(new IOContext(), 0, new SimpleObjectCodec()) {
 @Override
 public void close() throws IOException {
 // empty implementation
 }
 }.setRootValueSeparator(new SerializableString(":"));
 assertEquals(":", jsonGeneratorWithRootValueSeparator.getRootValueSeparator());
 }

 @Test
 public void testConfigureQuoteFieldNames() throws IOException {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGenerator._cfgUnqNames);
 jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
 assertTrue(jsonGenerator._cfgUnqNames);
 }
}