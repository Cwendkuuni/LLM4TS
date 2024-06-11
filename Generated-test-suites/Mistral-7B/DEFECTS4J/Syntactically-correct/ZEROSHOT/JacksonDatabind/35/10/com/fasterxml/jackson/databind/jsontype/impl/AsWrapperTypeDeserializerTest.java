package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper objectMapper;
 private TypeIdResolver typeIdResolver;
 private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

 @Before
 public void setup() throws IOException {
 objectMapper = new ObjectMapper();
 typeIdResolver = objectMapper.getTypeIdResolver();
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 ObjectMapperUtil.constructType(Foo.class), typeIdResolver, "_typeId", true, FooImpl.class);
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser("{\"_typeId\":\"FooSubType\"}"), new TestContext());
 assertNotNull(actual);
 assertEquals(FooSubType.class, actual.getClass());
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromArray(objectMapper.getFactory().createParser("[{\"_typeId\":\"FooSubType\"}]"), new TestContext());
 assertNotNull(actual);
 assertEquals(FooSubType[].class, actual.getClass());
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromScalar(objectMapper.getFactory().createParser("\"FooSubType\""), new TestContext());
 assertNotNull(actual);
 assertEquals(FooSubType.class, actual.getClass());
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromAny(objectMapper.getFactory().createParser("{\"_typeId\":\"FooSubType\", \"name\":\"John\"}"), new TestContext());
 assertNotNull(actual);
 assertEquals(FooSubType.class, actual.getClass());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser("{\"_class\":\"com.fasterxml.jackson.databind.jsontype.impl.FooSubType\"}"), new TestContext());
 assertNotNull(actual);
 assertEquals(FooSubType.class, actual.getClass());
 }

 @Test(expected = IOException.class)
 public void testWrongTokenException() throws IOException {
 asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser("{\"name\":\"John\"}"), new TestContext());
 }

 @Test
 public void testWrongTypeIdException() throws IOException {
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 ObjectMapperUtil.constructType(Foo.class), typeIdResolver, "_typeId", true, FooSubType2.class);
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser("{\"_typeId\":\"FooSubType\"}"), new TestContext());
 assertNotNull(actual);
 assertEquals(FooSubType2.class, actual.getClass());
 }

 @Test(expected = IOException.class)
 public void testWrongTypeIdException2() throws IOException {
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 ObjectMapperUtil.constructType(Foo.class), typeIdResolver, "_typeId", true, FooSubType.class);
 Object actual = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser("{\"_typeId\":\"FooSubType2\"}"), new TestContext());
 }

 @Test
 public void testMergeTypeId() throws IOException {
 ObjectMapper objectMapperWithTypeId = new ObjectMapper();
 objectMapperWithTypeId.setDefaultTyping(DefaultTyping.OBJECT_AND_WRAPPER_ARRAY);
 ObjectMapperUtil.configureDefaultTypes(objectMapperWithTypeId, Foo.class, FooSubType.class, FooSubType2.class);
 AsWrapperTypeDeserializer asWrapperTypeDeserializerWithTypeId = new AsWrapperTypeDeserializer(
 ObjectMapperUtil.constructType(Foo.class), typeIdResolver, "_typeId", true, FooSubType.class);
 Object actual = asWrapperTypeDeserializerWithTypeId.deserializeTypedFromObject(objectMapperWithTypeId.getFactory().createParser("{\"_typeId\":\"FooSubType\", \"_class\":\"com.fasterxml.jackson.databind.jsontype.impl.FooSubType2\"}"), new TestContext());
 assertNotNull(actual);
 assertEquals(FooSubType2.class, actual.getClass());
 }

 @Test
 public void testMergeTypeId2() throws IOException {
 ObjectMapper objectMapperWithTypeId = new ObjectMapper();
 objectMapperWithTypeId.setDefaultTyping(DefaultTyping.OBJECT_AND_WRAPPER_ARRAY);
 ObjectMapperUtil.configureDefaultTypes(objectMapperWithTypeId, Foo.class, FooSubType.class, FooSubType2.class);
 AsWrapperTypeDeserializer asWrapperTypeDeserializerWithTypeId = new AsWrapperTypeDeserializer(
 ObjectMapperUtil.constructType(Foo.class), typeIdResolver, "_typeId", true, FooSubType.class);
 ObjectMapperUtil.configureDefaultTypes(objectMapperWithTypeId, FooSubType.class, FooSubType2.class);
 Object actual = asWrapperTypeDeserializerWithTypeId.deserializeTypedFromObject(objectMapperWithTypeId.getFactory().createParser("{\"_typeId\":\"FooSubType\", \"_class\":\"com.fasterxml.jackson.databind.jsontype.impl.FooSubType2\"}"), new TestContext());
 assertNotNull(actual);
 assertEquals(FooSubType2.class, actual.getClass());
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectMapper _defaultObjectMapper() {
 return null;
 }
 }

 static class Foo {
 }

 static class FooImpl implements Foo {
 }

 static class FooSubType extends Foo {
 }

 static class FooSubType2 extends Foo {
 }
}