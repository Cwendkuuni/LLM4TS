package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class JsonGeneratorImplTest {

 JsonGeneratorImpl jsonGeneratorImpl;
 IOContext ioContext;
 ObjectCodec objectCodec;
 CharacterEscapes characterEscapes;
 CharacterEscapes standardCharacterEscapes;

 @Before
 public void setUp() {
 ioContext = mock(IOContext.class);
 objectCodec = mock(ObjectCodec.class);
 characterEscapes = mock(CharacterEscapes.class);
 standardCharacterEscapes = mock(CharacterEscapes.class);
 jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, objectCodec) {
 @Override
 protected CharacterEscapes _characterEscapes = standardCharacterEscapes;
 };
 }

 @Test
 public void testDefaultConfiguration() throws IOException {
 // Test default configuration
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 // Verify defaults
 assertEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
 assertNull(jsonGenerator._characterEscapes);
 assertNull(jsonGenerator._rootValueSeparator);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testEnableQuoteFieldNames() throws IOException {
 // Test enabling quote field names
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 assertTrue(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testDisableQuoteFieldNames() throws IOException {
 // Test disabling quote field names
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testSetHighestNonEscapedChar() throws IOException {
 // Test setting highest non-escaped char
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setHighestNonEscapedChar(128);
 assertEquals(128, jsonGenerator._maximumNonEscapedChar);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeHighestNonEscapedChar() throws IOException {
 // Test setting negative highest non-escaped char
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setHighestNonEscapedChar(-1);
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 // Test setting custom character escapes
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertEquals(characterEscapes, jsonGenerator._characterEscapes);
 }

 @Test
 public void testGetCharacterEscapes() throws IOException {
 // Test getting custom character escapes
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testSetRootValueSeparator() throws IOException {
 // Test setting root value separator
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setRootValueSeparator(new SerializableString("rootValueSeparator"));
 assertEquals(new SerializableString("rootValueSeparator"), jsonGenerator._rootValueSeparator);
 }
}