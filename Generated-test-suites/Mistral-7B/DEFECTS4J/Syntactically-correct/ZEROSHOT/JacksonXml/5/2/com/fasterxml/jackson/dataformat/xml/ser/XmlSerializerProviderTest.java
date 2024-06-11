package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

public class XmlSerializerProviderTest {

 private XmlRootNameLookup _rootNameLookup;
 private SerializerProvider _provider;
 private ObjectMapper _mapper;

 @Before
 public void setup() {
 _rootNameLookup = new XmlRootNameLookup();
 _provider = new XmlSerializerProvider(_rootNameLookup);
 _mapper = new ObjectMapper(new JsonFactory(), _provider);
 }

 @Test
 public void testSerializeNull() throws IOException {
 QName rootName = new QName("null");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator gen = _mapper.getFactory().createGenerator(outputStream);
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(_rootNameLookup);
 xmlProvider.serializeValue(gen, null);
 gen.close();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null/>\n", outputStream.toString());
 }

 @Test
 public void testSerializeValue() throws IOException, JsonProcessingException {
 String input = "{\"name\":\"John\",\"age\":30}";
 Object obj = _mapper.readValue(new ByteArrayInputStream(input.getBytes()), Object.class);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator gen = _mapper.getFactory().createGenerator(outputStream);
 _provider.serializeValue(gen, obj);
 gen.close();
 String expectedXml = "<root><name>John</name><age>30</age></root>";
 assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testSerializeValueWithRootType() throws IOException, JsonProcessingException {
 JavaType rootType = _mapper.getTypeFactory().constructType(Person[].class);
 String input = "{\"name\":[{\"first\":\"John\",\"last\":\"Doe\"},{\"first\":\"Jane\",\"last\":\"Doe\"}]}";
 Object obj = _mapper.readValue(new ByteArrayInputStream(input.getBytes()), Object.class);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator gen = _mapper.getFactory().createGenerator(outputStream);
 _provider.serializeValue(gen, obj, rootType, null);
 gen.close();
 String expectedXml = "<personList><person><first>John</first><last>Doe</last></person><person><first>Jane</first><last>Doe</last></person></personList>";
 assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testSerializeValueWithCustomSerializer() throws IOException, JsonProcessingException {
 JavaType rootType = _mapper.getTypeFactory().constructType(Person[].class);
 StdSerializer<Person> personSerializer = new StdSerializer<Person>(Person.class) {
 @Override
 public void serialize(Person person, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("firstName");
 gen.writeString(person.getFirstName());
 gen.writeFieldName("lastName");
 gen.writeString(person.getLastName());
 gen.writeEndObject();
 }};
 _provider.setSerializer(Person.class, personSerializer);
 String input = "{\"name\":[{\"first\":\"John\",\"last\":\"Doe\"},{\"first\":\"Jane\",\"last\":\"Doe\"}]}";
 Object obj = _mapper.readValue(new ByteArrayInputStream(input.getBytes()), Object.class);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator gen = _mapper.getFactory().createGenerator(outputStream);
 _provider.serializeValue(gen, obj, rootType, null);
 gen.close();
 String expectedXml = "<personList><person><firstName>John</firstName></person><person><firstName>Jane</firstName></person></personList>";
 assertEquals(expectedXml, outputStream.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithInvalidGenerator() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonParser parser = _mapper.getFactory().createParser(new ByteArrayInputStream(new byte[0]));
 _provider.serializeValue(parser, new Object(), null);
 }

 @Test
 public void testSerializeValueWithRootElement() throws IOException {
 String input = "{\"root\":{\"name\":\"John\",\"age\":30}}";
 Object obj = _mapper.readValue(new ByteArrayInputStream(input.getBytes()), Object.class);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator gen = _mapper.getFactory().createGenerator(outputStream);
 SerializerProvider xmlProvider = new XmlSerializerProvider(_rootNameLookup);
 xmlProvider.serializeValue(gen, obj, null, null);
 gen.close();
 String expectedXml = "<root><name>John</name><age>30</age></root>";
 assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testSerializeValueWithRootElementAndRootName() throws IOException {
 String input = "{\"root\":{\"name\":\"John\",\"age\":30}}";
 Object obj = _mapper.readValue(new ByteArrayInputStream(input.getBytes()), Object.class);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator gen = _mapper.getFactory().createGenerator(outputStream);
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(_rootNameLookup);
 QName rootName = new QName("Person");
 xmlProvider.serializeValue(gen, obj, null, null, rootName);
 gen.close();
 String expectedXml = "<Person><name>John</name><age>30</age></Person>";
 assertEquals(expectedXml, outputStream.toString());
 }

 @Test
 public void testSerializeValueWithRootNameFromConfig() throws IOException {
 SerializationConfig config = _mapper.getSerializationConfig();
 config.setRootValueSeparator("<root>");
 Object obj = new Object();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonGenerator gen = _mapper.getFactory().createGenerator(outputStream);
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(_rootNameLookup, config);
 xmlProvider.serializeValue(gen, obj, null, null);
 gen.close();
 String expectedXml = "<root><null/>";
 assertEquals(expectedXml, outputStream.toString());
 }
}

class Person {
 private String firstName;
 private String lastName;

 public String getFirstName() {
 return firstName;
 }

 public void setFirstName(String firstName) {
 this.firstName = firstName;
 }

 public String getLastName() {
 return lastName;
 }

 public void setLastName(String lastName) {
 this.lastName = lastName;
 }
}