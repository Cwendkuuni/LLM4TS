package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;

 @Before
 public void setUp() {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 }

 @After
 public void tearDown() throws IOException {
 jsonGenerator.close();
 }

 @Test
 public void testConstructor() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 assertNotNull(jsonGenerator);
 }

 @Test
 public void testEnableFeature() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testDisableFeature() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
 assertTrue(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testSetHighestNonEscapedChar() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setHighestNonEscapedChar(128);
 assertEquals(128, jsonGenerator.getHighestNonEscapedChar());
 }

 @Test
 public void testSetCharacterEscapes() {
 CharacterEscapes characterEscapes = new CharacterEscapes();
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertSame(characterEscapes, jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testSetRootValueSeparator() {
 SerializableString rootValueSeparator = new SerializableString("root");
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetRootValueSeparatorNull() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setRootValueSeparator(null);
 }

 @Test
 public void testGetCharacterEscapes() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 CharacterEscapes characterEscapes = jsonGenerator.getCharacterEscapes();
 assertNull(characterEscapes);
 }

 @Test
 public void testVersion() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 Version version = jsonGenerator.version();
 assertNotNull(version);
 }
}