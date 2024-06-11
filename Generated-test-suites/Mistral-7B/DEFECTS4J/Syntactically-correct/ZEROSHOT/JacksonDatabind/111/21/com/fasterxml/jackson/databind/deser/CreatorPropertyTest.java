package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testCreatorProperty() throws Exception {
 // Define a test class
 class TestClass {
 @JsonCreator
 public TestClass(@JsonProperty("str") String str, @JsonProperty("num") int num) {
 this.str = str;
 this.num = num;
 }

 @JsonProperty
 public String str;
 @JsonProperty
 public int num;
 }

 // Create a test instance
 TestClass testInstance = new TestClass("hello", 123);

 // Create a CreatorProperty instance
 JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
 CreatorProperty property = mapper.findProperty("str", javaType);

 // Test getters and setters
 assertEquals("hello", property.getName());
 assertEquals(String.class, property.getType());
 assertEquals(TypeDeserializer.none, property.getTypeDeserializer());
 assertNull(property._annotated);
 assertNull(property._injectableValueId);
 assertNull(property._fallbackSetter);
 assertFalse(property.isIgnorable());

 // Test injectValue and inject methods
 Object injectedValue = new Object();
 DeserializationContext context = mapper.getSerializationConfig().withContextResolver(new JacksonContextResolver());
 context.setInjectableValues(new StandardInjectableValues());
 context.findInjectableValue(property.getInjectableValueId(), TestClass.class, testInstance);
 property.inject(context, testInstance);
 assertEquals(injectedValue, testInstance.str);

 // Test deserializeAndSet and deserializeSetAndReturn methods
 String input = "{\"str\":\"world\"}";
 JsonParser parser = mapper.getFactory().createParser(input);
 Object result = property.deserializeAndSet(parser, context, testInstance);
 assertEquals("world", testInstance.str);
 assertEquals(injectedValue, testInstance.num);
 assertEquals(TestClass.class, result.getClass());

 Object result2 = property.deserializeSetAndReturn(parser, context, testInstance);
 assertEquals("world", testInstance.str);
 assertEquals(injectedValue, testInstance.num);
 assertEquals(TestClass.class, result2.getClass());

 // Test setter and getter methods
 property.set(testInstance, "newStr");
 assertEquals("newStr", testInstance.str);
 String getStr = property.get(testInstance);
 assertEquals("newStr", getStr);

 // Test markAsIgnorable method
 property.markAsIgnorable();
 assertTrue(property.isIgnorable());
 }

 @Test(expected = InvalidDefinitionException.class)
 public void testMissingSetter() throws Exception {
 // Create a CreatorProperty instance with a missing setter
 JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
 CreatorProperty property = mapper.findProperty("num", javaType);
 property.inject(null, null); // should not throw an exception here

 // Try to call deserializeAndSet method
 String input = "{\"num\":42}";
 JsonParser parser = mapper.getFactory().createParser(input);
 property.deserializeAndSet(parser, null, null);
 fail("Expected InvalidDefinitionException");
 }
}