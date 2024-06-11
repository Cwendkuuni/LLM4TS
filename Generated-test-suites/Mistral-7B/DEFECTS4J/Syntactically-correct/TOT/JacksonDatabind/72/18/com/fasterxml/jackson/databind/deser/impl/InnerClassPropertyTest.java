package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

 private ObjectMapper objectMapper;
 private JsonDeserializer<MyInnerClass> innerClassDeserializer;
 private MyInnerClass myInnerClass;
 private MyClassWithInnerClass myClassWithInnerClass;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 innerClassDeserializer = objectMapper.findValueDeserializer(MyInnerClass.class);
 myInnerClass = new MyInnerClass("InnerClassValue");
 myClassWithInnerClass = new MyClassWithInnerClass();
 myClassWithInnerClass.setInnerClassProperty(myInnerClass);
 }

 @Test
 public void testDeserializeInnerClassProperty() throws Exception {
 ObjectReader reader = objectMapper.readerForMap(MyClassWithInnerClass.class);
 JsonNode jsonNode = objectMapper.readTree("{\"innerClassProperty\": {\"class\":\"com.fasterxml.jackson.databind.deser.impl.MyInnerClass\", \"value\":\"InnerClassValue\"}}");
 MyClassWithInnerClass deserializedObject = reader.readValueAs(MyClassWithInnerClass.class);
 assertEquals(myInnerClass, deserializedObject.getInnerClassProperty());
 }

 @Test
 public void testDeserializeInnerClassPropertyWithValueDeserializer() throws Exception {
 ObjectReader reader = objectMapper.readerForMap(MyClassWithInnerClass.class);
 ObjectNode jsonNode = (ObjectNode) objectMapper.createObjectNode();
 ObjectNode innerClassNode = jsonNode.putObject("innerClassProperty");
 ObjectNode innerClassValueNode = innerClassNode.putObject("value");
 ObjectNode innerClassValueDeserNode = innerClassValueNode.putObject("valueDeserializer");
 innerClassValueNode.putPOJO("class", MyOtherInnerClass.class);
 JsonDeserializer<?> innerClassValueDeser = objectMapper.findValueDeserializer(MyOtherInnerClass.class);
 innerClassValueDeserNode.putPOJO("class", innerClassValueDeser.getClass().getName());
 reader.readValueAs(MyClassWithInnerClass.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeInnerClassPropertyMissingConstructor() throws Exception {
 ObjectReader reader = objectMapper.readerForMap(MyClassWithInnerClass.class);
 ObjectNode jsonNode = objectMapper.readTree("{\"innerClassProperty\": {}}");
 reader.readValueAs(MyClassWithInnerClass.class);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testSetAndGetInnerClassProperty() {
 assertEquals(myInnerClass, myClassWithInnerClass.getInnerClassProperty());
 myClassWithInnerClass.setInnerClassProperty(new MyInnerClass("NewInnerClassValue"));
 assertNotEquals(myInnerClass, myClassWithInnerClass.getInnerClassProperty());
 }

 @Test
 public void testToString() throws Exception {
 ObjectWriter writer = objectMapper.writerForValueType(MyClassWithInnerClass.class);
 writer.writeValue(System.out, myClassWithInnerClass);
 String toString = writer.writeValueAsString(myClassWithInnerClass);
 JsonParser parser = objectMapper.getFactory().createParser(toString);
 Object readValue = parser.readValueAs(MyClassWithInnerClass.class);
 assertEquals(myClassWithInnerClass, readValue);
 }

 @Test
 public void testWriteReplace() throws Exception {
 MyClassWithInnerClass original = myClassWithInnerClass;
 MyClassWithInnerClass newInstance = (MyClassWithInnerClass) original.writeReplace();
 assertNotSame(original, newInstance);
 assertEquals(original.getInnerClassProperty(), newInstance.getInnerClassProperty());
 }

 @Test
 public void testReadResolve() throws Exception {
 MyClassWithInnerClass original = myClassWithInnerClass;
 MyClassWithInnerClass newInstance = (MyClassWithInnerClass) original.readResolve();
 assertNotSame(original, newInstance);
 assertEquals(original.getInnerClassProperty(), newInstance.getInnerClassProperty());
 }
}

class MyInnerClass {
 private String value;

 public MyInnerClass(String value) {
 this.value = value;
 }

 @Override
 public String toString() {
 return ToStringSerializer.instance.serializeValueAsString(this);
 }
}

class MyClassWithInnerClass {
 private InnerClassProperty innerClassProperty;

 public InnerClassProperty getInnerClassProperty() {
 return innerClassProperty;
 }

 public void setInnerClassProperty(InnerClassProperty innerClassProperty) {
 this.innerClassProperty = innerClassProperty;
 }
}