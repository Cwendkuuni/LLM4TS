package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringKD;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerializable;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StdKeyDeserializerTest {

 @Test
 public void testForType() throws Exception {
 Class<?> clazz = String.class;
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);
 Assert.assertEquals(keyDeserializer.getClass(), StringKD.class);

 clazz = UUID.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Integer.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Long.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Date.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Calendar.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Boolean.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Byte.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Character.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Short.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Float.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Double.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = URI.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = URL.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Locale.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Currency.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNotNull(keyDeserializer);

 clazz = Enum.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertNull(keyDeserializer);
 }

 @Test
 public void testDeserializeKey() throws Exception {
 Class<?> clazz = String.class;
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(clazz);
 DeserializationContext ctxt = createContext();
 String key = "test";
 Object result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, key);

 clazz = UUID.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertNotNull(result);
 Assert.assertTrue(result instanceof UUID);

 clazz = Integer.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "123";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Integer.valueOf(123));

 clazz = Long.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "456";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Long.valueOf(456));

 clazz = Date.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertNotNull(result);
 Assert.assertTrue(result instanceof Date);

 clazz = Calendar.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertNotNull(result);
 Assert.assertTrue(result instanceof Calendar);

 clazz = Boolean.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "true";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Boolean.TRUE);
 key = "false";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Boolean.FALSE);

 clazz = Byte.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "127";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Byte.valueOf((byte) 127));

 clazz = Character.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "A";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Character.valueOf('A'));

 clazz = Short.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "32767";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Short.valueOf((short) 32767));

 clazz = Float.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "3.14159265";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Float.valueOf((float) 3.14159265));

 clazz = Double.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "3.14159265358979323846";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Double.valueOf(3.14159265358979323846));

 clazz = URI.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "https://www.google.com";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, new URI(key));

 clazz = URL.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 key = "https://www.google.com";
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, new URL(key));

 clazz = Locale.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 keyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(Locale.class, FromStringDeserializer.class);
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, new Locale("en", "US"));

 clazz = Currency.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 keyDeserializer = (StdKeyDeserializer) StdKeyDeserializer.forType(Currency.class, FromStringDeserializer.class);
 result = keyDeserializer.deserializeKey(key, ctxt);
 Assert.assertEquals(result, Currency.getInstance("USD"));

 clazz = Enum.class;
 Enum<?> enumType = Enum.class.getField("VALUE").getAnnotatedType();
 keyDeserializer = StdKeyDeserializer.forType(enumType);
 Enum[] enumValues = enumType.getEnumConstants();
 String enumKey = enumValues[0].name();
 result = keyDeserializer.deserializeKey(enumKey, ctxt);
 Assert.assertEquals(result, enumValues[0]);
 }

 @Test
 public void testGetKeyClass() throws Exception {
 Class<?> clazz = String.class;
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = UUID.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Integer.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Long.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Date.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Calendar.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Boolean.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Byte.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Character.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Short.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Float.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Double.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = URI.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = URL.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Locale.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Currency.class;
 keyDeserializer = StdKeyDeserializer.forType(clazz);
 Assert.assertEquals(keyDeserializer.getKeyClass(), clazz);

 clazz = Enum.class;
 Enum<?> enumType = Enum.class.getField("VALUE").getAnnotatedType();
 keyDeserializer = StdKeyDeserializer.forType(enumType);
 Assert.assertEquals(keyDeserializer.getKeyClass(), enumType);
 }

 private DeserializationContext createContext() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 return mapper.getDeserializationContext();
 }
}