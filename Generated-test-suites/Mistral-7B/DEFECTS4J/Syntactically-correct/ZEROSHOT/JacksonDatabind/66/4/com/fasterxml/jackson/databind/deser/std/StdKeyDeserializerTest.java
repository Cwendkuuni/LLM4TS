package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonAnnotationsScan
public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testForTypeBoolean() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 Object result = keyDeserializer.deserializeKey("true", objectMapper.getDeserializationContext());
 assertEquals(Boolean.TRUE, result);

 result = keyDeserializer.deserializeKey("false", objectMapper.getDeserializationContext());
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testForTypeByte() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 Object result = keyDeserializer.deserializeKey("127", objectMapper.getDeserializationContext());
 assertEquals((byte) 127, result);
 }

 @Test
 public void testForTypeShort() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 Object result = keyDeserializer.deserializeKey("32767", objectMapper.getDeserializationContext());
 assertEquals((short) 32767, result);
 }

 @Test
 public void testForTypeChar() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 Object result = keyDeserializer.deserializeKey("A", objectMapper.getDeserializationContext());
 assertEquals('A', result);
 }

 @Test
 public void testForTypeInt() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 Object result = keyDeserializer.deserializeKey("100", objectMapper.getDeserializationContext());
 assertEquals(100, result);
 }

 @Test
 public void testForTypeLong() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 Object result = keyDeserializer.deserializeKey("1000000000000000001", objectMapper.getDeserializationContext());
 assertEquals(1000000000000000001L, result);
 }

 @Test
 public void testForTypeFloat() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
 Object result = keyDeserializer.deserializeKey("3.14159265", objectMapper.getDeserializationContext());
 assertEquals(3.14159265F, result);
 }

 @Test
 public void testForTypeDouble() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
 Object result = keyDeserializer.deserializeKey("3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128481117450284102701938521105559642337312816526050888757288655143042019241594520034273389053246656130496989272351821615758715606708595677034316496152428801671193145555667027294138953556773472302125200105548813176542029253362728022585141061386512322302158013049694820000000000000000000", objectMapper.getDeserializationContext());
 assertEquals(3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128481117450284102701938521105559642337312816526050888757288655143042019241594520034273389053246656130496989272351821615758715606708595677034316496152428801671193145555667027294138953556773472302125200105548813176542029253362728022585141061386512322302158013049694820000000000000000000L, result);
 }

 @Test
 public void testForTypeLocale() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
 Object result = keyDeserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
 assertEquals(Locale.US, result);
 }

 @Test
 public void testForTypeDate() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 Object result = keyDeserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
 assertEquals(new Date(2022, 11, 31), result);
 }

 @Test
 public void testForTypeCalendar() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Object result = keyDeserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
 assertEquals(Calendar.getInstance().set(2022, 11, 31), result);
 }

 @Test
 public void testForTypeUri() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 Object result = keyDeserializer.deserializeKey("https://example.com", objectMapper.getDeserializationContext());
 assertEquals(new URI("https://example.com"), result);
 }

 @Test
 public void testForTypeUrl() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 Object result = keyDeserializer.deserializeKey("https://example.com", objectMapper.getDeserializationContext());
 assertEquals(new URL("https://example.com"), result);
 }

 @Test
 public void testForTypeClass() throws JsonProcessingException {
 StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
 Object result = keyDeserializer.deserializeKey("[Ljava.lang.String;", objectMapper.getDeserializationContext());
 assertEquals(new String[0], result);
 }

 @Test
 public void testForTypeEnum() throws JsonProcessingException {
 Enum testEnum = MyEnum.FOO;
 EnumResolver enumResolver = new SimpleEnumResolver(MyEnum.class);
 StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, MyEnum.class, enumResolver);
 Object result = keyDeserializer.deserializeKey(testEnum.name(), objectMapper.getDeserializationContext());
 assertEquals(testEnum, result);
 }
}