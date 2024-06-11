package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link JsonGeneratorImpl}
 */
public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;

 @Before
 public void setUp() {
 IOContext ioContext = new IOContext();
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#JsonGeneratorImpl(IOContext, int, ObjectCodec)}
 */
 @Test
 public void testJsonGeneratorImpl() {
 // Test default constructor
 IOContext ioContext = new IOContext();
 JsonGeneratorImpl jsonGenerator1 = new JsonGeneratorImpl(ioContext, 0, null);

 // Test with specified features
 int features = JsonGenerator.Feature.AUTO_CLOSE_SOURCE.getMask() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask();
 IOContext ioContext2 = new IOContext();
 JsonGeneratorImpl jsonGenerator2 = new JsonGeneratorImpl(ioContext2, features, null);

 assertEquals(features, jsonGenerator2.getFeatures());

 // Test with ObjectCodec
 ObjectCodec codec = new ObjectMapper().getSerializerProvider().getFactory();
 IOContext ioContext3 = new IOContext();
 JsonGeneratorImpl jsonGenerator3 = new JsonGeneratorImpl(ioContext3, 0, codec);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#writeFieldName(String)}
 * @throws IOException
 */
 @Test
 public void testWriteFieldName() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("field1");
 jsonGenerator.writeString("value1");
 jsonGenerator.writeEndObject();
 String writtenJson = jsonGenerator.getOutput().toString();
 assertEquals("{\"field1\":\"value1\"}", writtenJson);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#writeString(String)}
 * @throws IOException
 */
 @Test
 public void testWriteString() throws IOException {
 jsonGenerator.writeString("testString");
 String writtenJson = jsonGenerator.getOutput().toString();
 assertEquals("\"testString\"", writtenJson);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#setHighestNonEscapedChar(int)}
 */
 @Test
 public void testSetHighestNonEscapedChar() {
 jsonGenerator.setHighestNonEscapedChar(128);
 assertEquals(128, jsonGenerator.getHighestEscapedChar());
 }

 /**
 * Test method for {@link JsonGeneratorImpl#setCharacterEscapes(CharacterEscapes)}
 */
 @Test
 public void testSetCharacterEscapes() {
 CharacterEscapes characterEscapes = new CharacterEscapes();
 characterEscapes.putEscapeSequence("\\test", (char) 128);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 String writtenJson = jsonGenerator.getOutput().toString();
 assertEquals("\"\\test\"", writtenJson.substring(writtenJson.lastIndexOf(":"), writtenJson.length()));
 }

 /**
 * Test method for {@link JsonGeneratorImpl#writeFieldName(String) writeString(String)}
 * @throws IOException
 */
 @Test
 public void testWriteFieldNameAndWriteString() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("field1");
 jsonGenerator.writeString("value1");
 jsonGenerator.writeEndObject();
 String writtenJson = jsonGenerator.getOutput().toString();
 assertEquals("{\"field1\":\"value1\"}", writtenJson);
 }
}