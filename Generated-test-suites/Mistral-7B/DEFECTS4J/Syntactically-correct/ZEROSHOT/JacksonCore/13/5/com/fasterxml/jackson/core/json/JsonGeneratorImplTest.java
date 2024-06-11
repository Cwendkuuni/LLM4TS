package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;

 @Before
 public void setUp() {
 JsonFactory factory = new JsonFactory();
 jsonGenerator = factory.createGenerator(new StringWriter());
 }

 @Test
 public void testDefaultConstructor() {
 assertEquals(JsonGenerator.Feature.AUTO_CLOSE_TARGET, jsonGenerator.getFeatures().get(JsonGenerator.Feature.AUTO_CLOSE_TARGET.id()));
 assertFalse(jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
 assertNull(jsonGenerator.getCharacterEscapes());
 assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
 }

 @Test
 public void testSetUnquoteFieldNames() {
 jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
 assertTrue(jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
 jsonGenerator.disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_names));
 }

 @Test
 public void testSetHighestNonEscapedChar() {
 jsonGenerator.setHighestNonEscapedChar(128);
 assertEquals(128, jsonGenerator.getHighestEscapedChar());
 jsonGenerator.setHighestNonEscapedChar(0);
 assertEquals(0, jsonGenerator.getHighestEscapedChar());
 }

 @Test
 public void testSetCharacterEscapes() {
 CharacterEscapes characterEscapes = new StandardCharacterEscapes();
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertSame(characterEscapes, jsonGenerator.getCharacterEscapes());
 assertNotEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
 }

 @Test
 public void testSetRootValueSeparator() {
 SerializableString rootValueSeparator = new SerializableString("root");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
 }
}