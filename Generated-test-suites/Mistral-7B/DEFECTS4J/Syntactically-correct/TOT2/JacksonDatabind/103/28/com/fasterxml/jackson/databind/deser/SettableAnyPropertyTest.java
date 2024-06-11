package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

 private SettableAnyProperty _property;

 @Before
 public void setup() {
 ObjectNode jsonNode = JsonFactory.instance.createObjectNode();
 jsonNode.put("name", "John Doe");
 jsonNode.put("age", 30);
 jsonNode.put("hobbies", JsonFactory.instance.createValueNode("reading, swimming"));
 ObjectNode propertyNode = JsonFactory.instance.createObjectNode();
 propertyNode.put("_class", MapperFeature.getDefaultMapper().getSerializers().findValueSerializer(Map.class).getClass());
 propertyNode.set("setter", JsonFactory.instance.createValueNode("setHobbies"));
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
 KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
 JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
 TypeDeserializer typeDeserializer = null;
 _property = new SettableAnyProperty(new BeanProperty(propertyNode, "_class"), new AnnotatedMethod(SettableAnyProperty.class, "setHobbies", Map.class), javaType, keyDeserializer, valueDeserializer, typeDeserializer);
 }

 @Test
 public void testSettableAnyPropertyBasic() throws IOException {
 Object pojo = new HashMap<>();
 _property.deserializeAndSet(JsonFactory.instance.createParser(JsonFactory.instance.createValueNode("{\"hobbies\":[\"reading\",\"swimming\"]}")), ObjectMapper.getDefaultInstance(), pojo, "_properties");
 Map<Object, Object> actualMap = (Map<Object, Object>) pojo;
 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("hobbies", new String[]{"reading", "swimming"});
 assertEquals(expectedMap, actualMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyNullValue() throws IOException {
 Object pojo = new HashMap<>();
 _property.deserializeAndSet(JsonFactory.instance.createParser(JsonFactory.instance.createValueNode(null)), ObjectMapper.getDefaultInstance(), pojo, "_properties");
 fail("Expected JsonMappingException");
 }

 @Test
 public void testSettableAnyPropertyEmptyValue() throws IOException {
 Object pojo = new HashMap<>();
 _property.deserializeAndSet(JsonFactory.instance.createParser(JsonFactory.instance.createValueNode("{}")), ObjectMapper.getDefaultInstance(), pojo, "_properties");
 assertTrue(pojo instanceof Map);
 assertTrue(pojo.getClass().isInstance(new HashMap<>()));
 assertEquals(new HashMap<>(), pojo);
 }

 @Test
 public void testSettableAnyPropertyUnknownProperty() throws IOException {
 Object pojo = new HashMap<>();
 _property.deserializeAndSet(JsonFactory.instance.createParser(JsonFactory.instance.createValueNode("{\"unknownProperty\":\"value\"}")), ObjectMapper.getDefaultInstance(), pojo, "_properties");
 assertTrue(pojo instanceof Map);
 assertTrue(pojo.getClass().isInstance(new HashMap<>()));
 Map<String, Object> actualMap = (Map<String, Object>) pojo;
 assertTrue(actualMap.isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyUnexpectedType() throws IOException {
 Object pojo = new HashMap<>();
 _property.deserializeAndSet(JsonFactory.instance.createParser(JsonFactory.instance.createValueNode(123)), ObjectMapper.getDefaultInstance(), pojo, "_properties");
 fail("Expected JsonMappingException");
 }

 @Test
 public void testSettableAnyPropertyNestedMap() throws IOException {
 Object pojo = new HashMap<>();
 _property.deserializeAndSet(JsonFactory.instance.createParser(JsonFactory.instance.createObjectNode().set("innerMap", JsonFactory.instance.createObjectNode())), ObjectMapper.getDefaultInstance(), pojo, "_properties");
 Map<String, Object> actualMap = (Map<String, Object>) pojo;
 Map<String, Object> expectedMap = new HashMap<>();
 expectedMap.put("innerMap", new HashMap<>());
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testSettableAnyPropertySetField() throws IOException {
 Object pojo = new MyPojo();
 _property.deserializeAndSet(JsonFactory.instance.createParser(JsonFactory.instance.createValueNode("{\"hobbies\":[\"reading\",\"swimming\"]}")), ObjectMapper.getDefaultInstance(), pojo, "_hobbiesField");
 MyPojo actualPojo = (MyPojo) pojo;
 MyPojo expectedPojo = new MyPojo();
 expectedPojo.setHobbies(new String[]{"reading", "swimming"});
 assertEquals(expectedPojo, actualPojo);
 }

 private static class MyPojo {
 private Map<String, Object> _hobbiesField;

 public Map<String, Object> getHobbiesField() {
 return _hobbiesField;
 }

 public void setHobbiesField(Map<String, Object> hobbiesField) {
 _hobbiesField = hobbiesField;
 }
 }

}