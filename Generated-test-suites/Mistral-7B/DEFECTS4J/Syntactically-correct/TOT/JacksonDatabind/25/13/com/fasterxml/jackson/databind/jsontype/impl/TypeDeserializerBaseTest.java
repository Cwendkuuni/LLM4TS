package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializationWithDefaultImpl() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, MyDefaultImpl.class);

 ObjectNode node = MAPPER.createObjectNode();
 node.put("typeId", "MyDefaultImpl");
 Object result = typeDeserializer.deserialize(MAPPER.getFactory().createParser(MAPPER.writeValueAsString(node)), null, null);

 assertEquals(MyDefaultImpl.class, result.getClass());
 }

 @Test
 public void testDeserializationWithCustomDeserializer() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);
 JsonDeserializer<Map<String, Object>> customDeserializer = new BaseMapDeserializer();
 typeDeserializer.setDeserializers(new ConcurrentHashMap<>(Map.of("customTypeId", customDeserializer)));

 ObjectNode node = MAPPER.createObjectNode();
 node.put("typeId", "customTypeId");
 Object result = typeDeserializer.deserialize(MAPPER.getFactory().createParser(MAPPER.writeValueAsString(node)), null, null);

 assertEquals(Map.class, result.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);

 ObjectNode node = MAPPER.createObjectNode();
 node.put("typeId", "UnknownTypeId");
 typeDeserializer.deserialize(MAPPER.getFactory().createParser(MAPPER.writeValueAsString(node)), null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithMissingTypeId() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);

 ObjectNode node = MAPPER.createObjectNode();
 Object result = typeDeserializer.deserialize(MAPPER.getFactory().createParser(MAPPER.writeValueAsString(node)), null, null);

 fail("Expected JsonMappingException but got " + result.getClass());
 }

 @Test
 public void testToString() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);
 String result = typeDeserializer.toString();
 assertTrue(result.startsWith("[TypeDeserializerBase"));
 }

 @Test
 public void testForProperty() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);
 BeanProperty property = MAPPER.findProperty("typePropertyName", Map.class);
 TypeDeserializerBase newTypeDeserializer = (TypeDeserializerBase) typeDeserializer.forProperty(property);
 assertNotSame(typeDeserializer, newTypeDeserializer);
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);
 assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeDeserializer.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);
 assertEquals(Map.class.getName(), typeDeserializer.baseTypeName());
 }

 @Test
 public void testGetPropertyName() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);
 assertEquals("typePropertyName", typeDeserializer.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolver() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);
 assertNotNull(typeDeserializer.getTypeIdResolver());
 }

 @Test
 public void testGetDefaultImpl() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, MyDefaultImpl.class);
 assertNull(typeDeserializer.getDefaultImpl());
 }

 @Test
 public void testFindDeserializer() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);
 String typeId = "MyCustomDeserializer";
 JsonDeserializer<Map<String, Object>> customDeserializer = new BaseMapDeserializer();
 typeDeserializer.setDeserializers(new ConcurrentHashMap<>(Map.of(typeId, customDeserializer)));
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typeId", typeId);
 JsonDeserializer<Object> resultDeserializer = typeDeserializer.findDeserializer(MAPPER.getConfiguration(), typeId);
 assertEquals(customDeserializer.getClass(), resultDeserializer.getClass());
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, MyDefaultImpl.class);
 Object resultDeserializer = typeDeserializer.findDefaultImplDeserializer(MAPPER.getConfiguration());
 assertEquals(NullifyingDeserializer.class, resultDeserializer.getClass());
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializer = new MyTypeDeserializerBase(Map.class, null, null, false, null);
 String unknownTypeId = "UnknownTypeId";
 TypeIdResolver typeIdResolver = MAPPER.getTypeIdResolver();
 try {
 typeDeserializer.handleUnknownTypeId(MAPPER.getConfiguration(), unknownTypeId, typeIdResolver, Map.class);
 fail("Expected JsonMappingException but got null");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 class MyTypeDeserializerBase extends TypeDeserializerBase<Map<String, Object>> {
 MyTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
 super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }
 }

 class MyDefaultImpl {
 }

 class BaseMapDeserializer extends JsonDeserializer<Map<String, Object>> {
 @Override
 public Map<String, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 Map<String, Object> map = new HashMap<>();
 while (p.nextToken() != null) {
 String key = p.getCurrentName();
 p.nextValue();
 map.put(key, p.getValueAsString());
 p.nextToken();
 }
 return map;
 }
 }
}