package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerBase;
import com.fasterxml.jackson.databind.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private XmlSerializerProvider xmlSerializerProvider;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addSerializer(String.class, new SimpleStringSerializer());
 mapper.registerModule(module);
 this.objectMapper = mapper;
 this.xmlSerializerProvider = objectMapper.getSerializerProvider();
 }

 @Test
 public void testSerializeNull() throws IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, null);
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<null/>", xml);
 }

 @Test
 public void testSerializeBoolean() throws IOException {
 boolean value = true;
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, value);
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<boolean>true</boolean>", xml);
 }

 @Test
 public void testSerializeInteger() throws IOException {
 int value = 123;
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, value);
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<int>123</int>", xml);
 }

 @Test
 public void testSerializeString() throws IOException {
 String value = "Hello, World!";
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, value);
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<string>Hello, World!</string>", xml);
 }

 @Test
 public void testSerializeObject() throws IOException {
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setAge(42);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, obj);
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<myClass><name>Test</name><age>42</age></myClass>", xml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeException() throws IOException {
 Throwable exception = new IOException("Test Exception");
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 try {
 xmlSerializerProvider.serializeValue(gen, exception);
 } catch (JsonMappingException e) {
 assertEquals("Unexpected value: class java.io.IOException", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testSerializeArray() throws IOException {
 int[] values = {1, 2, 3};
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider.serializeValue(gen, values);
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<int>[1, 2, 3]</int>", xml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullRootName() throws IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 try {
 xmlSerializerProvider.serializeValue(gen, new Object(), null);
 } catch (JsonMappingException e) {
 assertEquals("No root name configured for serialization", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testSerializeCustomRootName() throws IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 SimpleBeanPropertySerializerBase serializer = new CustomRootNameSerializer();
 JsonSerializer<Object> ser = objectMapper.findValueSerializer(Object.class, null, serializer);
 xmlSerializerProvider.setSerializerInstance(Object.class, ser);
 xmlSerializerProvider.serializeValue(gen, new Object(), new QName("test"));
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<test></test>", xml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnknownRootName() throws IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 try {
 xmlSerializerProvider.serializeValue(gen, new Object(), new QName("unknown"));
 } catch (JsonMappingException e) {
 assertEquals("No serializer found for value class: class java.lang.Object", e.getMessage());
 throw e;
 }
 }

 @Test(expected = XMLStreamException.class)
 public void testSerializeInvalidNamespace() throws IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider = new XmlSerializerProvider(null);
 try {
 xmlSerializerProvider.serializeValue(gen, new Object(), new QName("", ""));
 } catch (XMLStreamException e) {
 throw e;
 }
 }

 @Test
 public void testSerializeArrayWithRootName() throws IOException {
 int[] values = {1, 2, 3};
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("array")));
 xmlSerializerProvider.serializeValue(gen, values, new QName("item"));
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<array><item><int>1</int></item><item><int>2</int></item><item><int>3</int></item></array>", xml);
 }

 @Test
 public void testSerializeObjectWithRootName() throws IOException {
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setAge(42);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("myClass")));
 xmlSerializerProvider.serializeValue(gen, obj, new QName("name"));
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<myClass><name>Test</name></myClass>", xml);
 }

 @Test
 public void testSerializeObjectWithCustomRootName() throws IOException {
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setAge(42);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 SimpleBeanPropertySerializerBase serializer = new CustomRootNameSerializer();
 JsonSerializer<Object> ser = objectMapper.findValueSerializer(Object.class, null, serializer);
 xmlSerializerProvider.setSerializerInstance(Object.class, ser);
 xmlSerializerProvider.serializeValue(gen, obj, new QName("test"));
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<test><name>Test</name></test>", xml);
 }

 @Test
 public void testSerializeObjectWithEmptyRootName() throws IOException {
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setAge(42);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider = new XmlSerializerProvider(null);
 xmlSerializerProvider.serializeValue(gen, obj, new QName(""));
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<>", xml);
 }

 @Test
 public void testSerializeObjectWithNullRootName() throws IOException {
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setAge(42);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(new TokenBuffer(), null);
 xmlSerializerProvider = new XmlSerializerProvider(null);
 xmlSerializerProvider.serializeValue(gen, obj, null);
 gen.flush();
 String xml = gen.getOutputContent();
 assertEquals("<null/>", xml);
 }
}

class MyClass {
 private String name;
 private int age;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }
}

class CustomRootNameSerializer extends JsonSerializer<Object> {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeStartElement("test");
 gen.writeEndElement();
 }
}