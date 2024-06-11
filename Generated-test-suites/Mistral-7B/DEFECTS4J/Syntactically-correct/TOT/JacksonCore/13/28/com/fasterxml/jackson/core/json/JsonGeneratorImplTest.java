package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JsonGeneratorImpl}.
 */
public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private CharacterEscapes characterEscapes;
 private IOContext ioContext;

 @Before
 public void setUp() throws Exception {
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
 public void testDefaultConfiguration() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "Alice");
 jsonGenerator.writeEndObject();
 String result = jsonGenerator.getOutput().toString();
 assertEquals("{\"name\":\"Alice\"}", result);
 }

 @Test
 public void testQuoteFieldNames() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null) {
 @Override
 protected boolean _cfgUnqNames() {
 return true;
 }
 };
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "Alice");
 jsonGenerator.writeEndObject();
 String result = jsonGenerator.getOutput().toString();
 assertEquals("{\"name\":Alice}", result);
 }

 @Test
 public void testRootValueSeparator() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null) {
 @Override
 protected SerializableString _rootValueSeparator() {
 return new SerializableString("/");
 }
 };
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "Alice");
 jsonGenerator.writeEndObject();
 String result = jsonGenerator.getOutput().toString();
 assertEquals("{\"name\":\"Alice\"}", result);
 jsonGenerator.writeRawValue("/");
 String result2 = jsonGenerator.getOutput().toString();
 assertEquals("{\"name\":\"Alice\"}/", result2);
 }

 @Test
 public void testEscaping() throws IOException {
 characterEscapes = new CharacterEscapes() {
 @Override
 public String getEscapeSequence(char c) {
 if (c == '_') {
 return "\\_";
 }
 return super.getEscapeSequence(c);
 }
 };
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null) {
 @Override
 protected CharacterEscapes _characterEscapes() {
 return characterEscapes;
 }
 };
 jsonGenerator.setHighestEscapedChar(126);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "Alice\\_Bob");
 jsonGenerator.writeEndObject();
 String result = jsonGenerator.getOutput().toString();
 assertEquals("{\"name\":\"Alice\\_Bob\"}", result);
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 characterEscapes = new CharacterEscapes();
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "Alice\\u0001");
 jsonGenerator.writeEndObject();
 String result = jsonGenerator.getOutput().toString();
 assertEquals("{\"name\":\"Alice\\u0001\"}", result);
 }

 @Test
 public void testConstructorWithFeatures() throws IOException {
 int features = JsonGenerator.Feature.QUOTE_FIELD_NAMES.asInt();
 jsonGenerator = new JsonGeneratorImpl(ioContext, features, null);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "Alice");
 jsonGenerator.writeEndObject();
 String result = jsonGenerator.getOutput().toString();
 assertTrue(Feature.QUOTE_FIELD_NAMES.isEnabled(jsonGenerator.getFeatures()));
 assertEquals("{\"name\":\"Alice\"}", result);
 }

 @Test
 public void testNullOutput() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeNull();
 jsonGenerator.writeEndObject();
 String result = jsonGenerator.getOutput().toString();
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testWriteStringWithNull() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", null);
 jsonGenerator.writeEndObject();
 }

 @Test(expected = IOException.class)
 public void testWriteNullWithNullOutput() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null) {
 @Override
 public void close() throws IOException {
 // do nothing
 }
 };
 jsonGenerator.setOutput(null);
 jsonGenerator.writeNull();
 }
}