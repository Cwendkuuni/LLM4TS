package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing AsPropertyTypeDeserializer class")
public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObjectWithProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 Object jsonObject = "{\"typePropertyName\":{\"typeId\":\"IntegerType\",\"value\":42}}";
 JsonParser jp = mapper.getFactory().createParser(jsonObject);

 assertEquals(42, deserializer.deserializeTypedFromObject(jp, null));
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 Object jsonObjectWithNativeTypeId = "{\"typePropertyName\":{\"@class\":\"com.fasterxml.jackson.core.type.TypeId\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonObjectWithNativeTypeId);
 jp.findAndHandleToken(JsonToken.START_OBJECT);
 jp.nextToken();

 assertEquals(mapper.getTypeFactory().constructType(Integer.class),
 jp.readValueAs(TypeReference.class));

 Object deserializedObject = deserializer.deserializeTypedFromObject(jp, null);
 assertEquals(42, deserializedObject);
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromObjectWithoutProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 Object jsonObjectWithoutProperty = "{\"value\":42}";
 JsonParser jp = mapper.getFactory().createParser(jsonObjectWithoutProperty);

 Object deserializedObject = deserializer.deserializeTypedFromObject(jp, null);
 assertNull(deserializedObject);
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 null,
 As.PROPERTY);

 Object jsonObjectWithDefaultImpl = "{\"value\":\"test\"}";
 JsonParser jp = mapper.getFactory().createParser(jsonObjectWithDefaultImpl);

 assertEquals("test", deserializer.deserializeTypedFromObject(jp, null));
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(List.class),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 Object jsonArray = "[{\"typePropertyName\":{\"typeId\":\"IntegerType\",\"value\":1},\"value\":2},{\"value\":3}]";
 JsonParser jp = mapper.getFactory().createParser(jsonArray);

 assertEquals(java.util.Arrays.asList(1, 3), deserializer.deserialize(jp, null));
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 Object jsonScalar = "42";
 JsonParser jp = mapper.getFactory().createParser(jsonScalar);

 assertEquals(42, deserializer.deserializeTypedFromScalar(jp, null));
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromAnyWithProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 Object jsonObjectWithArrayWrapper = "[{\"typePropertyName\":{\"typeId\":\"IntegerType\",\"value\":42}}]";
 JsonParser jp = mapper.getFactory().createParser(jsonObjectWithArrayWrapper);

 Object deserializedObject = deserializer.deserializeTypedFromAny(jp, null);
 assertEquals(42, deserializedObject);
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromAnyWithoutProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 mapper.getTypeFactory().constructType(Integer.class),
 As.PROPERTY);

 Object jsonObjectWithoutProperty = "{\"value\":42}";
 JsonParser jp = mapper.getFactory().createParser(jsonObjectWithoutProperty);

 Object deserializedObject = deserializer.deserializeTypedFromAny(jp, null);
 assertNull(deserializedObject);
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromAnyWithDefaultImpl() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "typePropertyName",
 true,
 null,
 As.PROPERTY);

 Object jsonObjectWithDefaultImpl = "{\"value\":\"test\"}";
 JsonParser jp = mapper.getFactory().createParser(jsonObjectWithDefaultImpl);

 Object deserializedObject = deserializer.deserializeTypedFromAny(jp, null);
 assertEquals("test", deserializedObject);
 jp.close();
 }

 @AfterEach
 public void cleanup() throws Exception {
 mapper.close();
 }
}