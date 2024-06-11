package com.fasterxml.jackson.core.base;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

 private JsonGenerator generator;
 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 generator = mapper.getFactory().createGenerator(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
 }

 @Test
 public void testVersion() throws JsonProcessingException {
 JsonGenerator g = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), mapper.getFactory());
 assertEquals("2.12.0", g.version().toString());
 }

 @Test
 public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() throws JsonProcessingException {
 JsonGenerator g = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), mapper.getFactory());
 assertTrue(g.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
 g = new GeneratorBase(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask(), mapper.getFactory());
 assertFalse(g.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
 }

 @Test
 public void testEnable_WRITE_NUMBERS_AS_STRINGS() throws JsonProcessingException {
 JsonGenerator g = new GeneratorBase(0, mapper.getFactory());
 g.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
 assertTrue(g.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
 g.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
 assertFalse(g.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS));
 }

 @Test
 public void testWriteNumber_int() throws JsonProcessingException {
 generator.writeNumber(42);
 assertEquals(42, generator.getCurrentValue().asInt());
 }

 @Test
 public void testWriteNumber_long() throws JsonProcessingException {
 generator.writeNumber(Long.MAX_VALUE);
 assertEquals(Long.MAX_VALUE, generator.getCurrentValue().asLong());
 }

 @Test
 public void testWriteNumber_double() throws JsonProcessingException {
 generator.writeNumber(Math.PI);
 assertEquals(Math.PI, generator.getCurrentValue().asDouble(), 0.0001);
 }

 @Test
 public void testWriteNumber_float() throws JsonProcessingException {
 generator.writeNumber(Float.NaN);
 assertEquals(Float.NaN, generator.getCurrentValue().asFloat());
 }

 @Test
 public void testWriteNumber_BigDecimal() throws JsonProcessingException {
 BigDecimal bd = new BigDecimal("3.14159265358979323846");
 generator.writeNumber(bd);
 assertEquals(bd, generator.getCurrentValue().asText());
 }

 @Test
 public void testWriteBoolean() throws JsonProcessingException {
 generator.writeBoolean(true);
 assertEquals("true", generator.getCurrentValue().asText());
 generator.writeBoolean(false);
 assertEquals("false", generator.getCurrentValue().asText());
 }

 @Test
 public void testWriteNull() throws JsonProcessingException {
 generator.writeNull();
 assertNull(generator.getCurrentValue());
 }

 @Test
 public void testWriteRawValue_String() throws JsonProcessingException {
 String s = "Hello World!";
 generator.writeRawValue(s);
 assertEquals(s, generator.getCurrentValue().asText());
 }

 @Test
 public void testWriteRawValue_charArray() throws JsonProcessingException {
 char[] chars = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
 generator.writeRawValue(chars, 0, chars.length);
 assertEquals(new String(chars), generator.getCurrentValue().asText());
 }

 @Test
 public void testWriteString() throws JsonProcessingException {
 generator.writeString("Hello World!");
 assertEquals("Hello World!", generator.getCurrentValue().asText());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 generator.writeFieldName("name");
 assertEquals("name", generator.getCurrentValue().asText());
 }

 @Test
 public void testWriteRawValue_InvalidUTF8() throws JsonProcessingException {
 byte[] data = new byte[] {(byte) 0xFF, (byte) 0xED}; // Invalid UTF-8 sequence
 generator.writeRawValue(new String(data, 0, data.length));
 assertNull(generator.getCurrentValue());
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteBinary() throws IOException {
 generator.writeBinary(JsonGenerator.Base64Variant.STANDARD, new ByteArrayInputStream("data".getBytes()), "data".length());
 }

 @Test
 public void testWriteObject() throws JsonProcessingException {
 String str = "Hello World!";
 generator.writeObject(str);
 assertEquals(str, generator.getCurrentValue().asText());
 }

 @Test
 public void testUseDefaultPrettyPrinter() throws JsonProcessingException {
 generator.useDefaultPrettyPrinter();
 DefaultPrettyPrinter pp = (DefaultPrettyPrinter) generator.getPrettyPrinter();
 assertEquals(DefaultPrettyPrinter.StandardOutputType.INDENTED_OBJECT, pp.getOutputProperties().getOutputProperty("indent-objects"));
 }

 @Test
 public void testSetCodec() throws JsonProcessingException {
 ObjectCodec oc = mapper.getFactory().createObjectCodec();
 generator.setCodec(oc);
 assertSame(oc, generator.getCodec());
 }

 @Test
 public void testSetPrettyPrinter() throws JsonProcessingException {
 DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
 generator.setPrettyPrinter(pp);
 assertSame(pp, generator.getPrettyPrinter());
 }

 @Test
 public void testFlush() throws JsonProcessingException {
 generator.writeNumber(7);
 generator.flush();
 assertEquals(7, generator.getCurrentValue().asInt());
 generator.close();
 }

 @Test
 public void testClose() throws JsonProcessingException {
 generator.writeNumber(42);
 generator.close();
 assertFalse(generator.isClosed());
 }

 @Test
 public void testReleaseBuffers() throws JsonProcessingException {
 generator.writeNumber(123);
 generator._releaseBuffers();
 generator.writeNumber(456);
 assertEquals(123, generator.getCurrentValue().asInt());
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteAfterClose() throws JsonProcessingException {
 generator.writeNumber(7);
 generator.close();
 generator.writeNumber(42);
 }

 @Test
 public void testWriteArray() throws JsonProcessingException {
 generator.writeStartArray();
 generator.writeNumber(1);
 generator.writeNumber(2);
 generator.writeNumber(3);
 generator.writeEndArray();
 JsonNode root = mapper.readTree(generator.getOutputBuffer().toString());
 JsonNode arr = root.get(0);
 assertEquals(3, arr.size());
 assertEquals(1, arr.get(0).asInt());
 assertEquals(2, arr.get(1).asInt());
 assertEquals(3, arr.get(2).asInt());
 }

 @Test
 public void testWriteObject_Null() throws JsonProcessingException {
 generator.writeStartObject();
 generator.writeNull();
 generator.writeEndObject();
 JsonNode root = mapper.readTree(generator.getOutputBuffer().toString());
 JsonNode obj = root.get(0);
 assertTrue(obj.isNull());
 }

 @Test
 public void testWriteObject_String() throws JsonProcessingException {
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("Hello World!");
 generator.writeEndObject();
 JsonNode root = mapper.readTree(generator.getOutputBuffer().toString());
 JsonNode obj = root.get(0);
 assertEquals("name", obj.get("name").asText());
 }

 @Test
 public void testWriteObject_Number() throws JsonProcessingException {
 generator.writeStartObject();
 generator.writeFieldName("age");
 generator.writeNumber(25);
 generator.writeEndObject();
 JsonNode root = mapper.readTree(generator.getOutputBuffer().toString());
 JsonNode obj = root.get(0);
 assertEquals(25, obj.get("age").asInt());
 }

 @Test
 public void testWriteObject_Boolean() throws JsonProcessingException {
 generator.writeStartObject();
 generator.writeFieldName("isAdmin");
 generator.writeBoolean(true);
 generator.writeEndObject();
 JsonNode root = mapper.readTree(generator.getOutputBuffer().toString());
 JsonNode obj = root.get(0);
 assertEquals(true, obj.get("isAdmin").asBoolean());
 }

 @Test
 public void testWriteObject_Complex() throws JsonProcessingException {
 generator.writeStartObject();
 generator.writeFieldName("employees");
 generator.writeStartArray();
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("Alice");
 generator.writeFieldName("age");
 generator.writeNumber(25);
 generator.writeFieldName("isManager");
 generator.writeBoolean(true);
 generator.writeEndObject();
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("Bob");
 generator.writeFieldName("age");
 generator.writeNumber(30);
 generator.writeFieldName("isManager");
 generator.writeBoolean(false);
 generator.writeEndObject();
 generator.writeEndArray();
 generator.writeEndObject();
 JsonNode root = mapper.readTree(generator.getOutputBuffer().toString());
 JsonNode obj = root.get(0);
 JsonNode employees = obj.get("employees");
 assertEquals(2, employees.size());
 JsonNode alice = employees.get(0);
 assertEquals("name", alice.get("name").asText());
 assertEquals(25, alice.get("age").asInt());
 assertEquals(true, alice.get("isManager").asBoolean());
 JsonNode bob = employees.get(1);
 assertEquals("name", bob.get("name").asText());
 assertEquals(30, bob.get("age").asInt());
 assertEquals(false, bob.get("isManager").asBoolean());
 }

 @Test
 public void testWriteObjectWithMissingStartArrayOrObject() throws JsonProcessingException {
 generator.writeFieldName("employees");
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("Alice");
 generator.writeFieldName("age");
 generator.writeNumber(25);
 generator.writeFieldName("isManager");
 generator.writeBoolean(true);
 generator.writeEndObject();
 // Missing end array
 generator.writeEndObject();
 String json = generator.getOutputBuffer().toString();
 assertEquals(JsonGenerationException.class, mapper.readTree(json).getClass());
 }

 @Test
 public void testWriteObjectWithMissingFieldName() throws JsonProcessingException {
 generator.writeStartObject();
 generator.writeStartArray();
 generator.writeObject(new Object());
 generator.writeEndArray();
 generator.writeFieldName("unknownField");
 generator.writeBoolean(true);
 generator.writeEndObject();
 String json = generator.getOutputBuffer().toString();
 assertEquals(JsonProcessingException.class, mapper.readTree(json).getClass());
 }

 @Test
 public void testWriteObjectWithMissingEndObject() throws JsonProcessingException {
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("Alice");
 generator.writeFieldName("age");
 generator.writeNumber(25);
 generator.writeFieldName("isManager");
 generator.writeBoolean(true);
 // Missing end object
 generator.writeFieldName("unknownField");
 generator.writeBoolean(true);
 String json = generator.getOutputBuffer().toString();
 assertEquals(JsonProcessingException.class, mapper.readTree(json).getClass());
 }

 @Test
 public void testWriteObjectWithInvalidFieldName() throws JsonProcessingException {
 generator.writeStartObject();
 generator.writeFieldName("name");
 generator.writeString("Alice");
 generator.writeFieldName("age");
 generator.writeNumber(25);
 generator.writeFieldName("is\rManager");
 generator.writeBoolean(true);
 generator.writeEndObject();
 String json = generator.getOutputBuffer().toString();
 JsonNode root = mapper.readTree(json);
 JsonNode obj = root.get(0);
 assertEquals(3, obj.size());
 assertEquals("name", obj.get(0).getName());
 assertEquals("Alice", obj.get(0).asText());
 assertEquals("age", obj.get(1).getName());
 assertEquals(25, obj.get(1).asInt());
 assertEquals("is\rManager", obj.get(2).getName());
 assertEquals(true, obj.get(2).asBoolean());
 }
}