package com.example.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testForProperty() throws JsonProcessingException {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory());
 assertNull(typeDeserializerBase.forProperty(null));

 BeanProperty property = new BeanProperty.StandardGetterProperty("myProperty", String.class);
 TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
 assertEquals(String.class, forProperty.getType());
 }

 @Test
 public void testGetTypeInclusion() {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory());
 assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws JsonProcessingException {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory());
 String baseTypeName = typeDeserializerBase.baseTypeName();
 assertEquals("java.util.Map", baseTypeName);
 }

 @Test
 public void testToString() throws JsonProcessingException {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory());
 String toString = typeDeserializerBase.toString();
 assertTrue(toString.contains("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyNullBaseType() throws JsonProcessingException {
 new MyTypeDeserializerBase(null, mapper.getTypeFactory());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyNullIdResolver() throws JsonProcessingException {
 new MyTypeDeserializerBase(Map.class, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyNullProperty() throws JsonProcessingException {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory());
 typeDeserializerBase.forProperty(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyNullDefaultImpl() throws JsonProcessingException {
 new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory(), null, false, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForPropertyNullTypePropertyName() throws JsonProcessingException {
 new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory(), null, false, null, null);
 }

 @Test
 public void testFindDeserializer() throws JsonProcessingException {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory());
 String typeId = "MyClass";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId);
 assertNull(deserializer);

 JsonDeserializer<MyClass> myClassDeserializer = mapper.findValueDeserializer(MyClass.class);
 typeDeserializerBase._deserializers.put(typeId, myClassDeserializer);
 deserializer = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId);
 assertEquals(myClassDeserializer, deserializer);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws JsonProcessingException {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory());
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(mapper.getDeserializationContext());
 assertNull(deserializer);

 JsonDeserializer<Object> defaultImplDeserializer = mapper.findValueDeserializer(Object.class);
 typeDeserializerBase._defaultImplDeserializer = defaultImplDeserializer;
 deserializer = typeDeserializerBase._findDefaultImplDeserializer(mapper.getDeserializationContext());
 assertEquals(defaultImplDeserializer, deserializer);
 }

 @Test
 public void testHandleUnknownTypeId() throws JsonProcessingException {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory());
 String typeId = "UnknownTypeId";
 try {
 typeDeserializerBase._handleUnknownTypeId(mapper.getDeserializationContext(), typeId);
 fail();
 } catch (JsonMappingException e) {
 assertEquals("Unknown type id 'UnknownTypeId' for type [simple type, class java.util.Map]", e.getMessage());
 }
 }

 @Test
 public void testHandleMissingTypeId() throws JsonProcessingException {
 TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(Map.class, mapper.getTypeFactory());
 String extraDesc = "Missing type id for type [simple type, class java.util.Map]";
 try {
 typeDeserializerBase._handleMissingTypeId(mapper.getDeserializationContext(), extraDesc);
 fail();
 } catch (JsonMappingException e) {
 assertEquals("Missing type id for type [simple type, class java.util.Map]", e.getMessage());
 }
 }
}

class MyTypeDeserializerBase extends TypeDeserializerBase<Map<String, Object>> {

 public MyTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver) {
 super(baseType, idResolver, null, false, Object.class);
 }

 public MyTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver,
 String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
 super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }
}