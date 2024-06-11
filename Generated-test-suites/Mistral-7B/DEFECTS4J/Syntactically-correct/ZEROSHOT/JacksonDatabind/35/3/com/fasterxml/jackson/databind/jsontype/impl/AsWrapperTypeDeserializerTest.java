package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

 private static final String TYPE_ID = "MyCustomTypeId";
 private static final String CLASS_NAME = "com.example.MyCustomClass";

 private ObjectMapper objectMapper;
 private TypeIdResolver typeIdResolver;
 private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule()
 .addDeserializer(Object.class, new BaseDeserializer())
 .addDeserializer(MyCustomClass.class, new MyCustomClassDeserializer())
 .addTypeIdResolver(new TypeIdResolverBase() {
 @Override
 public IdTypeIdResolver getIdResolver(DeserializationConfig config, BeanDescription beanDesc) {
 return new StdTypeIdResolver(beanDesc);
 }
 }));
 typeIdResolver = objectMapper.getTypeIdResolver();
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 JavaType.construct(MyCustomClass.class), typeIdResolver, "_typeProperty", true, MyCustomClass.class);
 }

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 ObjectNode node = createObjectNode();
 node.put("typeId", TYPE_ID);
 JsonNode valueNode = createObjectNode();
 node.set("value", valueNode);
 ObjectReader objectReader = objectMapper.readerForTree(MyCustomClass.class);
 ObjectNode result = objectReader.readTree(new JsonParser(node));
 assertEquals(MyCustomClass.class, result.getClass());
 assertEquals(TYPE_ID, result.get("_class").asText());
 assertEquals("Value from Json", result.get("value").asText());

 JsonParser jsonParser = new JsonParser(node);
 Object deserializedObject = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(MyCustomClass.class, deserializedObject.getClass());
 assertEquals(TYPE_ID, deserializedObject.getClass().getSimpleName() + "$WrapperType");
 assertEquals(TYPE_ID, deserializedObject.toString().split("@")[0]);
 assertEquals(result.get("value").asText(), deserializedObject.toString().split("@")[1]);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 JsonNode jsonNode = createArrayNode();
 jsonNode.add(createObjectNode().put("typeId", TYPE_ID).set("value", createTextNode("Value from Json")));
 ObjectNode result = mapper.readerForTree(MyCustomClass[].class).readValueAs(ObjectNode.class, jsonNode);
 assertEquals(MyCustomClass[].class, result.getClass());
 assertEquals(TYPE_ID, result.get(0).getClass().getName());
 assertEquals("Value from Json", result.get(0).get("value").asText());

 JsonParser jsonParser = new JsonParser(jsonNode);
 Object[] deserializedObjects = (Object[]) asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(MyCustomClass[].class, deserializedObjects.getClass().getComponentType());
 assertEquals(MyCustomClass.class, deserializedObjects[0].getClass().getSuperclass());
 assertEquals(TYPE_ID, deserializedObjects[0].getClass().getSimpleName() + "$WrapperType");
 assertEquals(TYPE_ID, deserializedObjects[0].toString().split("@")[0]);
 assertEquals("Value from Json", deserializedObjects[0].toString().split("@")[1]);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 JsonParser jsonParser = new JsonParser(new StringReader("{\"typeId\":\"" + TYPE_ID + "\",\"value\":\"Value from Json\"}"));
 Object deserializedObject = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, objectMapper.getDeserializationContext());
 assertEquals(MyCustomClass.class, deserializedObject.getClass());
 assertEquals(TYPE_ID, deserializedObject.getClass().getSimpleName() + "$WrapperType");
 assertEquals(TYPE_ID, deserializedObject.toString().split("@")[0]);
 assertEquals("Value from Json", deserializedObject.toString().split("@")[1]);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 JsonParser jsonParser = new JsonParser(new StringReader("{\"typeId\":\"MyOtherTypeId\",\"value\":\"Value from Json\"}"));
 try {
 Object deserializedObject = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, objectMapper.getDeserializationContext());
 fail("Expected IllegalStateException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFindDeserializer() throws Exception {
 JsonDeserializer<Object> deserializer = asWrapperTypeDeserializer.findDeserializer(objectMapper.getDeserializationContext(), TYPE_ID);
 assertNull(deserializer);

 ObjectMapper testMapper = new ObjectMapper();
 testMapper.registerDeserializer(MyCustomClass.class, new MyCustomClassDeserializer());
 JsonDeserializer<Object> testDeserializer = testMapper.findValueDeserializer(MyCustomClass.class, TypeIdResolverBase.getDefaultInstance());
 assertEquals(MyCustomClassDeserializer.class, testDeserializer.getClass());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 ObjectNode node = createObjectNode();
 node.put("typeId", TYPE_ID);
 node.put("value", createObjectNode().put("field1", "Value1").put("field2", "Value2"));
 ObjectReader objectReader = objectMapper.readerForTree(MyCustomClass.class);
 ObjectNode result = objectReader.readTree(new JsonParser(node));
 assertEquals(MyCustomClass.class, result.getClass());
 assertEquals(TYPE_ID, result.get("_class").asText());
 assertEquals("Value1", result.get("field1").asText());
 assertEquals("Value2", result.get("field2").asText());

 JsonParser jsonParser = new JsonParser(node);
 Object deserializedObject = asWrapperTypeDeserializer._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), TYPE_ID);
 assertEquals(MyCustomClass.class, deserializedObject.getClass());
 assertEquals(TYPE_ID, deserializedObject.toString().split("@")[0]);
 assertEquals(result.get("value").get("field1").asText(), deserializedObject.toString().split("@")[1]);
 }
}

 class BaseDeserializer implements JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 throw new IOException("BaseDeserializer should not be invoked");
 }
 }

 class MyCustomClassDeserializer implements JsonDeserializer<MyCustomClass> {
 @Override
 public MyCustomClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new MyCustomClass("Value from Json");
 }
 }

 class MyCustomClass {
 private String value;

 public MyCustomClass(String value) {
 this.value = value;
 }

 @Override
 public String toString() {
 return TYPE_ID + "@" + value;
 }
 }