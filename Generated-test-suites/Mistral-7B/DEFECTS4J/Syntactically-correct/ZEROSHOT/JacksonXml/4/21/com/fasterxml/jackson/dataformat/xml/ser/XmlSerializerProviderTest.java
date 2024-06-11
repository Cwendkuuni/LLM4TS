package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.javapojo.NopSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 private XmlRootNameLookup rootNameLookup;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 rootNameLookup = new XmlRootNameLookup();
 objectMapper = new ObjectMapper();
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 }

 @Test
 public void testCreateInstance() throws IOException {
 SerializationConfig config = new SerializationConfig();
 SerializerFactory factory = objectMapper.getSerializerFactory();
 XmlSerializerProvider newXmlSerializerProvider = xmlSerializerProvider.createInstance(config, factory);
 assertEquals(xmlSerializerProvider.getClass(), newXmlSerializerProvider.getClass());
 }

 @Test
 public void testSerializeValueNull() throws IOException {
 TokenBuffer tokenBuffer = new TokenBuffer();
 JsonGenerator gen = new StaxUtils().createJsonGenerator(tokenBuffer, "UTF-8");
 xmlSerializerProvider.serializeValue(gen, null);
 String xml = tokenBuffer.asXml();
 assertEquals("<null/>", xml);
 }

 @Test
 public void testSerializeValueString() throws IOException {
 String input = "hello";
 String expectedXml = "<string>hello</string>";
 TokenBuffer tokenBuffer = new TokenBuffer();
 JsonGenerator gen = new StaxUtils().createJsonGenerator(tokenBuffer, "UTF-8");
 xmlSerializerProvider.serializeValue(gen, input);
 String xml = tokenBuffer.asXml();
 assertEquals(expectedXml, xml);
 }

 @Test
 public void testSerializeValueInteger() throws IOException {
 int input = 123;
 String expectedXml = "<int>123</int>";
 TokenBuffer tokenBuffer = new TokenBuffer();
 JsonGenerator gen = new StaxUtils().createJsonGenerator(tokenBuffer, "UTF-8");
 xmlSerializerProvider.serializeValue(gen, input);
 String xml = tokenBuffer.asXml();
 assertEquals(expectedXml, xml);
 }

 @Test
 public void testSerializeValueBoolean() throws IOException {
 boolean input = true;
 String expectedXml = "<boolean>true</boolean>";
 TokenBuffer tokenBuffer = new TokenBuffer();
 JsonGenerator gen = new StaxUtils().createJsonGenerator(tokenBuffer, "UTF-8");
 xmlSerializerProvider.serializeValue(gen, input);
 String xml = tokenBuffer.asXml();
 assertEquals(expectedXml, xml);
 }

 @Test
 public void testSerializeValueArrayOfInt() throws IOException {
 int[] input = { 1, 2, 3 };
 String expectedXml = "<int-array><int>1</int><int>2</int><int>3</int></int-array>";
 TokenBuffer tokenBuffer = new TokenBuffer();
 JsonGenerator gen = new StaxUtils().createJsonGenerator(tokenBuffer, "UTF-8");
 xmlSerializerProvider.serializeValue(gen, input);
 String xml = tokenBuffer.asXml();
 assertEquals(expectedXml, xml);
 }

 @Test
 public void testSerializeValueBean() throws IOException {
 Person person = new Person("Alice", 25);
 String expectedXml = "<person><name>Alice</name><age>25</age></person>";
 TokenBuffer tokenBuffer = new TokenBuffer();
 JsonGenerator gen = new StaxUtils().createJsonGenerator(tokenBuffer, "UTF-8");
 ObjectWriter writer = objectMapper.writerWithView(new SimpleBeanPropertyFilter(){{
 filterOutAllExcept("name", "age"); }});
 xmlSerializerProvider = new XmlSerializerProvider(xmlSerializerProvider, writer.getConfig(), objectMapper.getSerializerFactory());
 xmlSerializerProvider.serializeValue(gen, person);
 String xml = tokenBuffer.asXml();
 assertEquals(expectedXml, xml);
 }

 @Test
 public void testSerializeValueBeanWithNullProperty() throws IOException {
 Person person = new Person("Alice", null);
 String expectedXml = "<person><name>Alice</name><age>null</age></person>";
 TokenBuffer tokenBuffer = new TokenBuffer();
 JsonGenerator gen = new StaxUtils().createJsonGenerator(tokenBuffer, "UTF-8");
 ObjectWriter writer = objectMapper.writerWithView(new SimpleBeanPropertyFilter(){{
 filterOutAllExcept("name", "age"); }});
 xmlSerializerProvider = new XmlSerializerProvider(xmlSerializerProvider, writer.getConfig(), objectMapper.getSerializerFactory());
 xmlSerializerProvider.findValueSerializer(Person.class).setNullSerializer(new NopSerializer());
 xmlSerializerProvider.serializeValue(gen, person);
 String xml = tokenBuffer.asXml();
 assertEquals(expectedXml, xml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueBeanWithUnsupportedType() throws IOException {
 Object input = new Object();
 TokenBuffer tokenBuffer = new TokenBuffer();
 JsonGenerator gen = new StaxUtils().createJsonGenerator(tokenBuffer, "UTF-8");
 xmlSerializerProvider.serializeValue(gen, input);
 fail("Expected JsonMappingException");
 }
}

class Person {
 private String name;
 private Integer age;

 public Person(String name, Integer age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public Integer getAge() {
 return age;
 }
}