package com.example.json; // replace with your test package name

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testJsonValueSerializerWithValueMethod() throws Exception {
 Class<TestClass> testClassType = TestClass.class;
 JsonValueSerializer serializer = new JsonValueSerializer(
 objectMapper.findMethodValueDeserializer(TestClass.class, "getValueMethod", null), null);
 Object testInstance = new TestClass();
 JsonNode result = objectMapper.treeToNode(serializer, testInstance);
 assertEquals("expectedValue", result.asText());
 }

 @Test
 public void testJsonValueSerializerWithResolvedProperty() throws Exception {
 Class<TestClass> testClassType = TestClass.class;
 BeanProperty property = new BeanProperty.Std("propertyName", TestClass.class, "getPropertyName");
 JsonValueSerializer serializer = new JsonValueSerializer(
 objectMapper.findMethodValueDeserializer(TestClass.class, "getValueMethod", null), property,
 objectMapper.findValueSerializer(String.class, property), true);
 Object testInstance = new TestClass();
 JsonNode result = objectMapper.treeToNode(serializer, testInstance);
 assertEquals("expectedValue", result.asText());
 }

 @Test
 public void testJsonValueSerializerWithCustomValueSerializer() throws Exception {
 Class<TestClass> testClassType = TestClass.class;
 JsonValueSerializer serializer = new JsonValueSerializer(
 objectMapper.findMethodValueDeserializer(TestClass.class, "getValueMethod", null), null,
 objectMapper.getSerializerProvider().findValueSerializer(CustomSerializer.class), true);
 Object testInstance = new TestClass();
 JsonNode result = objectMapper.treeToNode(serializer, testInstance);
 assertEquals("expectedCustomSerializedValue", result.asText());
 }

 @Test
 public void testJsonValueSerializerWithEnumValue() throws Exception {
 Class<TestEnum> testEnumType = TestEnum.class;
 JsonValueSerializer serializer = new JsonValueSerializer(
 objectMapper.findMethodValueDeserializer(TestEnum.class, "getValueMethod", null), null);
 Object testInstance = TestEnum.VALUE1;
 JsonNode result = objectMapper.treeToNode(serializer, testInstance);
 assertEquals("VALUE1_STRING_VALUE", result.asText());
 }

 @Test
 public void testJsonValueSerializerWithNaturalType() throws Exception {
 Class<TestInt> testIntType = TestInt.class;
 JsonValueSerializer serializer = new JsonValueSerializer(
 objectMapper.findMethodValueDeserializer(TestInt.class, "getValueMethod", null), null);
 Object testInstance = 123;
 JsonNode result = objectMapper.treeToNode(serializer, testInstance);
 assertEquals("123", result.asText());
 }

 @JsonValue
 static String getValueMethod(TestClass testClass) {
 return "expectedValue";
 }

 static String getValueMethod(TestEnum testEnum) {
 return testEnum.name();
 }

 static int getValueMethod(TestInt testInt) {
 return testInt;
 }

 static class TestClass {
 @JsonSerialize
 public String getPropertyName() {
 return "propertyName";
 }
 }

 static class TestEnum {
 @JsonValue("VALUE1_STRING_VALUE")
 public String value() {
 return name();
 }

 public static final TestEnum VALUE1 = new TestEnum() {
 };
 }

 static class TestInt {
 }

 static class CustomSerializer {
 }
}