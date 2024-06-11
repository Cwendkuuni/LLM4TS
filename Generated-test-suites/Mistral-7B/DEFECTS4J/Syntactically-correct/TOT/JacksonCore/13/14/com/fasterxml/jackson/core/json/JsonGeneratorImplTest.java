package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class JsonGeneratorImplTest {

 private JsonGenerator jsonGenerator;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 ObjectCodec objectCodec = JsonFactory.getInstance().getCodec();
 jsonGenerator = objectCodec.getJsonGenerator(new StringWriter());
 objectMapper = new ObjectMapper(objectCodec);
 }

 @After
 public void tearDown() throws Exception {
 jsonGenerator.close();
 }

 @Test
 public void testWriteStringField() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual);
 }

 @Test
 public void testWriteStringFieldWithEmptyName() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{}", actual);
 }

 @Test
 public void testWriteStringFieldWithNullValue() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", null);
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertNull(actual);
 }

 @Test(expected = IOException.class)
 public void testWriteStringFieldWithIllegalCharacter() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("\uD800", "John Doe");
 }

 @Test
 public void testSetRootValueSeparator() throws IOException {
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter(), new DefaultPrettyPrinter().withRootValueSeparator(":"));
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual);
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter(), new CustomCharacterEscapes());
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual); // No custom escapes should have been applied
 }

 @Test
 public void testSetCharacterEscapesWithCustomEscapes() throws IOException {
 CharacterEscapes characterEscapes = new CustomCharacterEscapes();
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter(), characterEscapes);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual); // No custom escapes should have been applied
 }

 @Test
 public void testSetCharacterEscapesWithCustomEscapesApplied() throws IOException {
 CharacterEscapes characterEscapes = new CustomCharacterEscapes();
 jsonGenerator.setCharacterEscapes(characterEscapes);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual); // No custom escapes should have been applied
 }

 @Test
 public void testSetHighestNonEscapedChar() throws IOException {
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator.setHighestNonEscapedChar(Character.MAX_VALUE);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "\uD800John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertThrows(IOException.class, () -> objectMapper.readValue(jsonGenerator.getOutputStream(), String.class));
 }

 @Test
 public void testSetMaximumNonEscapedCharToZero() throws IOException {
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator.setHighestNonEscapedChar(0);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual);
 }

 @Test
 public void testSetMaximumNonEscapedCharTo127() throws IOException {
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator.setHighestNonEscapedChar(127);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual);
 }

 @Test
 public void testSetMaximumNonEscapedCharTo128() throws IOException {
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator.setHighestNonEscapedChar(128);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual);
 }

 @Test
 public void testSetMaximumNonEscapedCharTo129() throws IOException {
 jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator.setHighestNonEscapedChar(129);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String actual = objectMapper.readValue(jsonGenerator.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual);
 }

 @Test
 public void testDisableQuoteFieldNames() throws IOException {
 JsonGenerator jsonGenerator1 = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator1.writeStartObject();
 jsonGenerator1.writeStringField("name", "John Doe");
 jsonGenerator1.writeEndObject();
 String actual1 = objectMapper.readValue(jsonGenerator1.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual1);

 JsonGenerator jsonGenerator2 = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator2.disable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator2.writeStartObject();
 jsonGenerator2.writeStringField("name", "John Doe");
 jsonGenerator2.writeEndObject();
 String actual2 = objectMapper.readValue(jsonGenerator2.getOutputStream(), String.class);
 assertEquals("{\"name\":John Doe}", actual2);
 }

 @Test
 public void testEnableQuoteFieldNames() throws IOException {
 JsonGenerator jsonGenerator1 = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator1.writeStartObject();
 jsonGenerator1.writeStringField("name", "John Doe");
 jsonGenerator1.writeEndObject();
 String actual1 = objectMapper.readValue(jsonGenerator1.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual1);

 JsonGenerator jsonGenerator2 = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator2.disable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator2.writeStartObject();
 jsonGenerator2.disable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator2.writeStringField("name", "John Doe");
 jsonGenerator2.writeEndObject();
 String actual2 = objectMapper.readValue(jsonGenerator2.getOutputStream(), String.class);
 assertEquals("{\"name\":John Doe}", actual2);

 JsonGenerator jsonGenerator3 = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator3.disable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator3.writeStartObject();
 jsonGenerator3.enable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator3.writeStringField("name", "John Doe");
 jsonGenerator3.writeEndObject();
 String actual3 = objectMapper.readValue(jsonGenerator3.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual3);

 JsonGenerator jsonGenerator4 = objectMapper.getFactory().createGenerator(new StringWriter());
 jsonGenerator4.enable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator4.writeStartObject();
 jsonGenerator4.writeStringField("name", "John Doe");
 jsonGenerator4.writeEndObject();
 String actual4 = objectMapper.readValue(jsonGenerator4.getOutputStream(), String.class);
 assertEquals("{\"name\":\"John Doe\"}", actual4);
 }

 @Test
 public void testVersion() throws IOException {
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
 Version version = jsonGenerator.version();
 assertEquals(Version.unknownVersion(), version);
 }
}

class CustomCharacterEscapes extends CharacterEscapes {
 @Override
 protected Map<Integer, String> getEscapeCodesForAscii() {
 Map<Integer, String> map = new HashMap<>();
 map.put(128, "\\u0080");
 return map;
 }
}