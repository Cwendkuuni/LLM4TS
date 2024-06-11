package com.example.jackson.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link StdKeyDeserializer}.
 */
public class StdKeyDeserializerTest {

 private ObjectMapper mapper;

 @Test
 public void testBoolean() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(Boolean.TRUE, kd.deserializeKey("true", ctxt));
 assertEquals(Boolean.FALSE, kd.deserializeKey("false", ctxt));

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testByte() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals((byte) 12, kd.deserializeKey("12", ctxt));
 assertEquals((byte) -12, kd.deserializeKey("-12", ctxt));

 // test invalid case with negative number
 assertNull(kd.deserializeKey("-128", ctxt)); // byte can only hold values from -128 to 127
 }

 @Test
 public void testShort() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals((short) 12345, kd.deserializeKey("12345", ctxt));
 assertEquals((short) -12345, kd.deserializeKey("-12345", ctxt));

 // test invalid case with overflow
 assertNull(kd.deserializeKey("123456", ctxt)); // short can only hold values from -32768 to 32767
 }

 @Test
 public void testChar() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals('A', kd.deserializeKey("A", ctxt));
 assertEquals('\\u00E9', kd.deserializeKey("\u00E9", ctxt));

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testInt() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(12345, kd.deserializeKey("12345", ctxt));
 assertEquals(-12345, kd.deserializeKey("-12345", ctxt));

 // test invalid case with overflow
 assertNull(kd.deserializeKey("123456", ctxt)); // int can only hold values from -2,147,483,648 to 2,147,483,647
 }

 @Test
 public void testLong() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(12345678912345L, kd.deserializeKey("12345678912345", ctxt));
 assertEquals(-12345678912345L, kd.deserializeKey("-12345678912345", ctxt));

 // test invalid case with overflow
 assertNull(kd.deserializeKey("12345678912346", ctxt)); // long can only hold values from -9,223,372,036,854,775,807 to 9,223,372,036,854,775,807
 }

 @Test
 public void testDouble() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(123.45, kd.deserializeKey("123.45", ctxt), 0.0);
 assertEquals(-123.45, kd.deserializeKey("-123.45", ctxt), 0.0);

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testLocale() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.READ_UNKNOWN_LOCALE_AS_DEFAULT, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(Locale.US, kd.deserializeKey("en_US", ctxt));
 assertEquals(Locale.UK, kd.deserializeKey("en_GB", ctxt));

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testDate() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(new Date(1234567891234L), kd.deserializeKey("2022-01-01T01:01:01.123Z", ctxt));

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testCalendar() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(Calendar.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Calendar calendar = Calendar.getInstance();
 assertEquals(calendar, kd.deserializeKey("2022-01-01T01:01:01.123Z", ctxt));

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testUUID() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(UUID.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(UUID.fromString("123e4567-789b-11d1-a0b4-00c0apef1234"), kd.deserializeKey("123e4567-789b-11d1-a0b4-00c0apef1234", ctxt));

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testURI() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(new URI("http://example.com/path/to/resource"), kd.deserializeKey("http://example.com/path/to/resource", ctxt));

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testURL() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // test valid cases
 KeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(new URL("http://example.com/path/to/resource"), kd.deserializeKey("http://example.com/path/to/resource", ctxt));

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testClass() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

 // test valid case
 AnnotatedClass ac = new AnnotatedClass(String[].class, mapper.getTypeFactory());
 JsonDeserialize annotation = ac.getAnnotation(JsonDeserialize.class);
 KeyDeserializer kd = new DelegatingKD(String[].class, mapper.findValueDeserializer(annotation, null));
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(String[].class, kd.deserializeKey("[1, 2]", ctxt));

 // test invalid case
 assertNull(kd.deserializeKey("invalid", ctxt));
 }
}