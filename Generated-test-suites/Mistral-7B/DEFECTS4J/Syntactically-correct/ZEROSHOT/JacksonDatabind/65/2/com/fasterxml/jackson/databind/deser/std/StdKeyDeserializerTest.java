package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@JacksonStdImpl
public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;

 @Before
 public void setup() {
 ctxt = new TestContext();
 }

 @Test
 public void testBooleanKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object result = keyDeserializer.deserializeKey("true", ctxt);
 assertEquals(Boolean.TRUE, result);
 result = keyDeserializer.deserializeKey("false", ctxt);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testByteKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = keyDeserializer.deserializeKey("127", ctxt);
 assertEquals((byte) 127, result);
 }

 @Test
 public void testShortKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object result = keyDeserializer.deserializeKey("32767", ctxt);
 assertEquals((short) 32767, result);
 }

 @Test
 public void testCharKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object result = keyDeserializer.deserializeKey("A", ctxt);
 assertEquals('A', result);
 }

 @Test
 public void testIntKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = keyDeserializer.deserializeKey("42", ctxt);
 assertEquals(42, result);
 }

 @Test
 public void testLongKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object result = keyDeserializer.deserializeKey("9223372036854775807", ctxt);
 assertEquals(Long.MAX_VALUE, result);
 }

 @Test
 public void testFloatKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object result = keyDeserializer.deserializeKey("3.14", ctxt);
 assertEquals(3.14f, result);
 }

 @Test
 public void testDoubleKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object result = keyDeserializer.deserializeKey("3.14159265", ctxt);
 assertEquals(3.14159265, result, 0.000001);
 }

 @Test
 public void testLocaleKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = keyDeserializer.deserializeKey("en_US", ctxt);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testUUIDKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
 Object result = keyDeserializer.deserializeKey("2f0f3b0e-9d54-43b3-8b63-ab2e1bb12de4", ctxt);
 assertEquals(new UUID("2f0f3b0e-9d54-43b3-8b63-ab2e1bb12de4"), result);
 }

 @Test
 public void testURILevel1KeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object result = keyDeserializer.deserializeKey("http://www.example.com", ctxt);
 assertEquals(new URI("http://www.example.com"), result);
 }

 @Test
 public void testURILevel2KeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object result = keyDeserializer.deserializeKey("scheme://user:password@host:port/path?query#fragment", ctxt);
 assertEquals(new URI("scheme", "user", "password", "host", "port", "/path", null, null, "fragment"), result);
 }

 @Test
 public void testURLKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object result = keyDeserializer.deserializeKey("http://www.example.com", ctxt);
 assertEquals(new URL("http://www.example.com"), result);
 }

 @Test
 public void testClassNameKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 Object result = keyDeserializer.deserializeKey("java.lang.String", ctxt);
 assertEquals("java.lang.String", result);
 }

 @Test
 public void testEnumKeyDeserializer() throws Exception {
 Enum testEnum = Enum.valueOf(MyEnum.class, "VALUE_1");
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 Object result = keyDeserializer.deserializeKey("VALUE_1", ctxt);
 assertEquals(testEnum, result);
 }

 @Test
 public void testNullKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 Object result = keyDeserializer.deserializeKey(null, ctxt);
 assertNull(result);
 }

 @Test
 public void testInvalidKeyDeserializer() throws Exception {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 Object result = keyDeserializer.deserializeKey("invalid", ctxt);
 assertNull(result);
 }

}