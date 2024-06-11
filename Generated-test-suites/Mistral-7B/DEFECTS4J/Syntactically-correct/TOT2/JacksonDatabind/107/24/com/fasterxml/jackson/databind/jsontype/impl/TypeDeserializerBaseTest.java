package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for TypeDeserializerBase class.
 */
public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 String typePropertyName = "type";
 JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
 TypeDeserializerBase testee = new TypeDeserializerBase(baseType, idResolver, typePropertyName, true, null);
 this.testee = testee;
 }

 /**
 * Test for forProperty method.
 * @throws IOException
 */
 @Test
 public void testForProperty() throws IOException {
 BeanProperty property = new BeanProperty(null, "testProperty");
 TypeDeserializer forProperty = testee.forProperty(property);
 assertEquals(TypeDeserializer.class, forProperty.getClass());
 }

 /**
 * Test for getTypeInclusion method.
 * @throws IOException
 */
 @Test
 public void testgetTypeInclusion() throws IOException {
 JsonTypeInfo.As typeInclusion = testee.getTypeInclusion();
 assertTrue(typeInclusion.isExplicit());
 }

 /**
 * Test for baseTypeName method.
 * @throws IOException
 */
 @Test
 public void testBaseTypeName() throws IOException {
 String baseTypeName = testee.baseTypeName();
 assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", baseTypeName);
 }

 /**
 * Test for getPropertyName method.
 * @throws IOException
 */
 @Test
 public void testgetPropertyName() throws IOException {
 String propertyName = testee.getPropertyName();
 assertEquals("type", propertyName);
 }

 /**
 * Test for getTypeIdResolver method.
 * @throws IOException
 */
 @Test
 public void testgetTypeIdResolver() throws IOException {
 TypeIdResolver typeIdResolver = testee.getTypeIdResolver();
 assertNotNull(typeIdResolver);
 }

 /**
 * Test for getDefaultImpl method.
 * @throws IOException
 */
 @Test
 public void testgetDefaultImpl() throws IOException {
 JavaType defaultImpl = testee.getDefaultImpl();
 assertNull(defaultImpl);
 }

 /**
 * Test for _findDeserializer method with known type id.
 * @throws IOException
 */
 @Test
 public void testFindDeserializerWithKnownTypeId() throws IOException {
 String typeId = "com.example.MyClass";
 JsonDeserializer<Object> deserializer = testee._findDeserializer(null, typeId);
 assertNotNull(deserializer);
 }

 /**
 * Test for _findDeserializer method with unknown type id.
 * @throws IOException
 */
 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerWithUnknownTypeId() throws IOException {
 String typeId = "unknown.type";
 testee._findDeserializer(null, typeId);
 }

 /**
 * Test for _handleMissingTypeId method.
 * @throws IOException
 */
 @Test
 public void testHandleMissingTypeId() throws IOException {
 String extraDesc = "missing type id";
 JavaType javaType = testee._handleMissingTypeId(null, extraDesc);
 assertNull(javaType);
 }

 /**
 * Test for _deserializeWithNativeTypeId method with known type id.
 * @throws IOException
 */
 @Test
 public void testDeserializeWithNativeTypeIdWithKnownTypeId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = mapper.getFactory().createParser("[{\"type\":\"com.example.MyClass\"}]");
 Object result = testee._deserializeWithNativeTypeId(jsonParser, null);
 assertNotNull(result);
 }

 /**
 * Test for _deserializeWithNativeTypeId method with unknown type id.
 * @throws IOException
 */
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNativeTypeIdWithUnknownTypeId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonParser jsonParser = mapper.getFactory().createParser("[{\"type\":\"unknown.type\"}]");
 testee._deserializeWithNativeTypeId(jsonParser, null);
 }
}