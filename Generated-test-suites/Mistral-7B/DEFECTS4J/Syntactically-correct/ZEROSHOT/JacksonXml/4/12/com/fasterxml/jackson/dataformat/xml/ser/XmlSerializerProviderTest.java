package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private XmlMapper xmlMapper;
 private SerializationConfig config;
 private XmlSerializerProvider xmlSerializerProvider;
 private JsonMapperFactory jsonMapperFactory;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 xmlMapper = new XmlMapper();
 config = xmlMapper.getSerializationConfig();
 xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(), config);
 jsonMapperFactory = new JsonMapperFactory();
 jsonMapperFactory.setSerializationConfig(config);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonProcessingException, IOException {
 JsonGenerator gen = xmlMapper.getFactory().createGenerator(new TokenBuffer(), config);
 xmlSerializerProvider.serializeValue(gen, null);
 gen.close();
 String xml = ((TokenBuffer) gen.asDelegatingGenerator()).getPrettyValue();
 assertEquals("<null/>", xml);
 }

 @Test
 public void testSerializationWithSimpleValue() throws JsonProcessingException, IOException {
 String value = "Hello World";
 JsonGenerator gen = xmlMapper.getFactory().createGenerator(new TokenBuffer(), config);
 xmlSerializerProvider.serializeValue(gen, value);
 gen.close();
 String xml = ((TokenBuffer) gen.asDelegatingGenerator()).getPrettyValue();
 assertEquals("<string>Hello World</string>", xml);
 }

 @Test
 public void testSerializationWithComplexObject() throws JsonProcessingException, IOException {
 Person person = new Person("John Doe", 30);
 JsonGenerator gen = xmlMapper.getFactory().createGenerator(new TokenBuffer(), config);
 xmlSerializerProvider.serializeValue(gen, person);
 gen.close();
 String xml = ((TokenBuffer) gen.asDelegatingGenerator()).getPrettyValue();
 assertEquals("<person><name>John Doe</name><age>30</age></person>", xml);
 }

 @Test
 public void testSerializationWithComplexObjectAndCustomRootName() throws JsonProcessingException, IOException {
 config.setRootValueSeparator("_");
 Person person = new Person("John Doe", 30);
 JsonGenerator gen = xmlMapper.getFactory().createGenerator(new TokenBuffer(), config);
 xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(), config);
 xmlSerializerProvider.serializeValue(gen, person);
 gen.close();
 String xml = ((TokenBuffer) gen.asDelegatingGenerator()).getPrettyValue();
 assertEquals("<person_name>John Doe</person_name><person_age>30</person_age>", xml);
 }

 @Test
 public void testSerializationWithComplexObjectAndArray() throws JsonProcessingException, IOException {
 Person[] persons = {new Person("John Doe", 30), new Person("Jane Doe", 25)};
 SerializerProvider provider = new DefaultSerializerProvider();
 SerializerFactory factory = xmlMapper.getSerializerFactory();
 SerializationConfig config = xmlMapper.getSerializationConfig();
 provider = new XmlSerializerProvider(provider, config, factory);
 JsonGenerator gen = xmlMapper.getFactory().createGenerator(new TokenBuffer(), config);
 JavaType javaType = jsonMapperFactory.constructType(Person[].class);
 provider.serializeValue(gen, javaType, Arrays.asList(persons));
 gen.close();
 String xml = ((TokenBuffer) gen.asDelegatingGenerator()).getPrettyValue();
 assertTrue(xml.contains("<person>"));
 assertTrue(xml.contains("<person>"));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithException() throws JsonProcessingException, IOException {
 JsonGenerator gen = xmlMapper.getFactory().createGenerator(new TokenBuffer(), config);
 try {
 xmlSerializerProvider.serializeValue(gen, new Exception("This is an exception"));
 } catch (JsonMappingException e) {
 assertEquals("Unexpected type: class java.lang.Exception", e.getMessage());
 }
 gen.close();
 }
}

 class Person {
 private String name;
 private int age;

 public Person(String name, int age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public int getAge() {
 return age;
 }

 public String toString() {
 return "{\"name\":\"" + name + "\",\"age\":\"" + age + "\"}";
 }
}