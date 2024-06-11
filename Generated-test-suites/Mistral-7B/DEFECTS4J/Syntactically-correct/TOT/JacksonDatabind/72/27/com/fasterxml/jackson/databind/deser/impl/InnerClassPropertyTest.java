package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

 private ObjectMapper mapper;
 private InnerClassProperty property;
 private MyClassWithInnerClass myClass;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 MyClassWithInnerClass.class.getDeclaredField("innerClassProperty").setAccessible(true);
 myClass = mapper.readValue(new ObjectMapperTest.SimpleContextResource("/test-classes/MyClassWithInnerClass.json").getResourceAsStream(), MyClassWithInnerClass.class);
 property = (InnerClassProperty) myClass.getClass().getDeclaredField("innerClassProperty").get(myClass);
 }

 @Test
 public void testDefaultConstructor() throws Exception {
 Object value = property.get("nullValue");
 assertNull(value);
 }

 @Test
 public void testStaticValueDeserializer() throws Exception {
 JsonNode node = mapper.readTree("/test-classes/InnerClassPropertyTestData.json");
 MyClassWithInnerClass deserialized = mapper.readValue(node.traverse(), MyClassWithInnerClass.class);
 MyClassWithInnerClass expected = new MyClassWithInnerClass();
 assertEquals(expected.innerClass, deserialized.innerClass);
 }

 @Test
 public void testNonStaticValueDeserializer() throws Exception {
 JsonNode node = mapper.readTree("/test-classes/InnerClassPropertyTestData.json");
 MyClassWithInnerClass deserialized = mapper.readValue(node.traverse(), MyClassWithInnerClass.class);
 MyClassWithInnerClass expected = new MyClassWithInnerClass();
 expected.innerClass = new InnerClass("value1");
 assertEquals(expected.innerClass, deserialized.innerClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingConstructor() throws Exception {
 property = (InnerClassProperty) MyMissingInnerClassProperty.class.getDeclaredField("innerClassProperty").get(new MyMissingInnerClassProperty());
 property.deserializeAndSet(mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0])), null, new Object());
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testSetAndGet() throws Exception {
 InnerClass innerClass = new InnerClass("value1");
 property.set(myClass, innerClass);
 InnerClass value = (InnerClass) property.get(myClass);
 assertEquals(innerClass, value);
 }

 @Test
 public void testJdkSerialization() throws Exception {
 ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream());
 out.writeObject(myClass);
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 MyClassWithInnerClass deserialized = (MyClassWithInnerClass) in.readObject();
 InnerClass value = (InnerClass) deserialized.getClass().getDeclaredField("innerClassProperty").get(deserialized).get("_delegate").get("value");
 InnerClass expected = new InnerClass("value1");
 assertEquals(expected, value);
 }

 @Test
 public void testErrorHandling() throws Exception {
 try {
 property.deserializeAndSet(mapper.getFactory().createParser("/test-classes/InnerClassPropertyTestDataError.json"), null, new Object());
 fail("Should throw an IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoDefaultConstructor() throws Exception {
 Class<?> clazz = ClassUtil.findCreator(MyNoDefaultConstructorInnerClass.class, null);
 AnnotatedConstructor constructor = mapper.constructorIntrospector().findConstructor(clazz, null);
 InnerClassProperty property = new InnerClassProperty(null, constructor);
 try {
 property.deserializeAndSet(mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0])), null, new Object());
 fail("Should throw an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}

class InnerClass {
 private final String value;

 public InnerClass(String value) {
 this.value = value;
}

 @JsonValue
 public String toString() {
 return value;
}
}

class MyClassWithInnerClass {
 @JsonProperty(value = "innerClass")
 private InnerClass innerClass;
}

class MyMissingInnerClassProperty {
 @JsonProperty(value = "innerClassProperty")
 private InnerClassProperty innerClassProperty;
}

class MyNoDefaultConstructorInnerClass {
 @JsonDeserialize(using = InnerClassDeserializer.class)
 public static class InnerClass {
 @JsonCreator
 public InnerClass(@JsonProperty("value") String value) {
 this.value = value;
}
 private String value;
}

 @JsonProperty(value = "innerClass")
 private InnerClass innerClass;
}

class InnerClassDeserializer extends StdDeserializer<InnerClass> {
 protected InnerClassDeserializer() { this(null); }
 protected InnerClassDeserializer(Class<InnerClass> c) { super(c); }
 @Override
 public InnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new InnerClass(p.getValueAsString());
 }
}