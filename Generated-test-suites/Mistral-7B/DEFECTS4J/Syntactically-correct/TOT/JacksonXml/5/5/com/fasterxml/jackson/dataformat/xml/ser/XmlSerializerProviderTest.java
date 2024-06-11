package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlSerializerProviderTest {

 private XmlMapper mapper;
 private SerializationConfig config;
 private ObjectMapper jsonMapper;
 private XmlSerializerProvider serializerProvider;

 @Before
 public void setup() {
 mapper = new XmlMapper();
 config = mapper.getSerializationConfig();
 jacksonObjectMapperSetup(config);
 jsonMapper = new ObjectMapper();
 serializerProvider = mapper.getSerializerProvider();
 }

 @Test
 public void testXmlSerializerProvider_serializeNull() throws Exception {
 String expectedXml = "<null/>";
 JsonGenerator gen = jsonMapper.getFactory().createGenerator(new TokenBuffer(), null);
 serializerProvider.serializeValue(gen, null);
 gen.flush();
 String actualXml = gen.getOutputContent().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXmlSerializerProvider_serializeObject() throws Exception {
 String input = "{\"name\":\"John\", \"age\":30}";
 Object obj = mapper.readValue(input, MyClass.class);
 String expectedXml = "<MyClass><name>John</name><age>30</age></MyClass>";
 JsonGenerator gen = jsonMapper.getFactory().createGenerator(new TokenBuffer(), null);
 serializerProvider.serializeValue(gen, obj);
 gen.flush();
 String actualXml = gen.getOutputContent().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = JsonMappingException.class)
 public void testXmlSerializerProvider_serializeInvalidObject() throws Exception {
 String input = "{\"invalid\":\"JSON\"}";
 Object obj = jsonMapper.readValue(input, MyClass.class); // invalid class
 JsonGenerator gen = jsonMapper.getFactory().createGenerator(new TokenBuffer(), null);
 serializerProvider.serializeValue(gen, obj);
 }

 @Test
 public void testXmlSerializerProvider_serializeArray() throws Exception {
 String input = "[1, 2, 3]";
 Object array = mapper.readValue(input, int[].class);
 String expectedXml = "<int><item>1</item><item>2</item><item>3</item></int>";
 JsonGenerator gen = jsonMapper.getFactory().createGenerator(new TokenBuffer(), null);
 serializerProvider.serializeValue(gen, array);
 gen.flush();
 String actualXml = gen.getOutputContent().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = JsonMappingException.class)
 public void testXmlSerializerProvider_serializeInvalidArray() throws Exception {
 String input = "[1, 2, 3, X]"; // invalid type
 Object array = mapper.readValue(input, int[].class);
 JsonGenerator gen = jsonMapper.getFactory().createGenerator(new TokenBuffer(), null);
 serializerProvider.serializeValue(gen, array);
 }

 @Test
 public void testXmlSerializerProvider_serializeObjectWithRootName() throws Exception {
 String expectedXml = "<MyRootName><MyClass><name>John</name><age>30</age></MyClass></MyRootName>";
 JsonGenerator gen = jsonMapper.getFactory().createGenerator(new TokenBuffer(), null);
 serializerProvider.setRootValueSeparator("MyRootName");
 serializerProvider.serializeValue(gen, new MyClass("John", 30));
 gen.flush();
 String actualXml = gen.getOutputContent().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXmlSerializerProvider_serializeObjectWithCustomSerializer() throws Exception {
 MyClassSerializer serializer = new MyClassSerializer();
 JsonGenerator gen = jsonMapper.getFactory().createGenerator(new TokenBuffer(), null);
 serializerProvider.registerSerializerInstance(MyClass.class, serializer);
 serializerProvider.serializeValue(gen, new MyClass("John", 30), new TypeIdentifier("MyClass"), null);
 gen.flush();
 String actualXml = gen.getOutputContent().toString();
 assertEquals("<MyClass><name>John</name><age>30</age></MyClass>", actualXml);
 }

 private static void jacksonObjectMapperSetup(SerializationConfig config) {
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 config.setSerializationFeature("failOnEmptyBeans", false);
 config.setSerializationFeature("failOnEmptyFields", false);
 config.setSerializationInclusion(JsonInclude.Include.ALWAYS);
 config.setSerializableContent(false);
 config.setRootValueSeparator("");
 config.getDefaultSerializerProvider().setNullValueSerializer(new StdSerializer<Object>(NullSerializer.class));
 }

 // Custom serializer to test serialization with custom root name
 static class MyClassSerializer extends StdSerializer<MyClass> {
 public MyClassSerializer() {
 this(null);
 }

 public MyClassSerializer(Class<MyClass> t) {
 super(t);
 }

 @Override
 public void serialize(MyClass value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("MyRootName");
 gen.writeFieldName("MyClass");
 gen.writeStartObject();
 gen.writeFieldName("name");
 gen.writeString(value.name);
 gen.writeFieldName("age");
 gen.writeNumber(value.age);
 gen.writeEndObject();
 gen.writeEndObject();
 }
 }

 // Custom class to test serialization
 static class MyClass {
 String name;
 int age;

 public MyClass(String name, int age) {
 this.name = name;
 this.age = age;
 }
 }
}