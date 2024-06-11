package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;

 @Before
 public void setUp() throws Exception {
 ctxt = new TestDeserializationContext();
 }

 @Test
 public void testBooleanType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object key = keyDeserializer.deserializeKey("true", ctxt);
 assertTrue((Boolean) key);

 keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 key = keyDeserializer.deserializeKey("false", ctxt);
 assertFalse((Boolean) key);
 }

 @Test
 public void testByteType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object key = keyDeserializer.deserializeKey("1", ctxt);
 assertEquals((Byte) new Byte((byte) 1), key);

 keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 key = keyDeserializer.deserializeKey("127", ctxt);
 assertEquals((Byte) new Byte((byte) 127), key);

 keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 try {
 key = keyDeserializer.deserializeKey("256", ctxt);
 fail("Expected JsonMappingException for byte type.");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testShortType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object key = keyDeserializer.deserializeKey("1", ctxt);
 assertEquals((Short) new Short((short) 1), key);

 keyDeserializer = StdKeyDeserializer.forType(Short.class);
 key = keyDeserializer.deserializeKey("-127", ctxt);
 assertEquals((Short) new Short((short) -127), key);

 keyDeserializer = StdKeyDeserializer.forType(Short.class);
 try {
 key = keyDeserializer.deserializeKey("32768", ctxt);
 fail("Expected JsonMappingException for short type.");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testCharType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object key = keyDeserializer.deserializeKey("A", ctxt);
 assertEquals((Character) 'A', key);
 }

 @Test
 public void testIntType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object key = keyDeserializer.deserializeKey("1", ctxt);
 assertEquals((Integer) new Integer(1), key);
 }

 @Test
 public void testLongType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object key = keyDeserializer.deserializeKey("1", ctxt);
 assertEquals((Long) new Long(1), key);

 keyDeserializer = StdKeyDeserializer.forType(Long.class);
 key = keyDeserializer.deserializeKey("9223372036854775807", ctxt);
 assertEquals((Long) new Long(Long.MAX_VALUE), key);
 }

 @Test
 public void testFloatType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object key = keyDeserializer.deserializeKey("1.0", ctxt);
 assertEquals((Float) new Float(1.0f), key);
 }

 @Test
 public void testDoubleType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object key = keyDeserializer.deserializeKey("1.0", ctxt);
 assertEquals((Double) new Double(1.0), key);
 }

 @Test
 public void testLocaleType() throws JsonMappingException, JsonProcessingException, IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object key = keyDeserializer.deserializeKey("en_US", ctxt);
 assertEquals(Locale.US, key);

 keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 key = keyDeserializer.deserializeKey("fr_FR", ctxt);
 assertEquals(Locale.FRENCH, key);
 }

 @Test
 public void testDateType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object key = keyDeserializer.deserializeKey("2019-01-31", ctxt);
 assertEquals(new Date(1548348800000L), key);
 }

 @Test
 public void testCalendarType() throws JsonMappingException, JsonProcessingException, IOException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object key = keyDeserializer.deserializeKey("2019-01-31", ctxt);
 Calendar calendar = GregorianCalendar.getInstance();
 calendar.setTime(new Date(1548348800000L));
 assertEquals(calendar, key);
 }

 @Test
 public void testUUIDType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
 Object key = keyDeserializer.deserializeKey("58c3e6d0-2b2d-4f10-8d7f-44d72e735ede", ctxt);
 assertEquals(new UUID("58c3e6d0-2b2d-4f10-8d7f-44d72e735ede"), key);
 }

 @Test
 public void testURIType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object key = keyDeserializer.deserializeKey("http://example.com", ctxt);
 assertEquals(new URI("http://example.com"), key);
 }

 @Test
 public void testURLType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object key = keyDeserializer.deserializeKey("http://example.com", ctxt);
 assertEquals(new URL("http://example.com"), key);
 }

 @Test
 public void testClassType() throws JsonMappingException, JsonProcessingException, ClassNotFoundException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
 Object key = keyDeserializer.deserializeKey("[Ljava/lang/String;", ctxt);
 assertEquals(new String[0].getClass(), key.getClass());

 keyDeserializer = StdKeyDeserializer.forType(String[].class);
 key = keyDeserializer.deserializeKey("[Ljava/lang/String;{1,2}", ctxt);
 assertEquals(new String[2].getClass(), key.getClass());
 }

 @Test
 public void testEnumType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 Object key = keyDeserializer.deserializeKey("RED", ctxt);
 assertEquals(MyEnum.RED, key);
 }

 @Test
 public void testEnumTypeWithInvalidValue() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 try {
 keyDeserializer.deserializeKey("INVALID", ctxt);
 fail("Expected JsonMappingException for Enum type with invalid value.");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testUnknownType() throws JsonMappingException, JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UnknownType.class);
 try {
 keyDeserializer.deserializeKey("UnknownValue", ctxt);
 fail("Expected JsonMappingException for UnknownType.");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}