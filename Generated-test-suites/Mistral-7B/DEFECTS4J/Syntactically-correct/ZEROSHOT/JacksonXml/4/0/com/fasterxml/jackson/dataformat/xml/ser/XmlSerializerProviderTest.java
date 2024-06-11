package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.util.StandardSerializer;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.impl.ToXmlGenerator;

public class XmlSerializerProviderTest extends TestBase {

 private XmlRootNameLookup rootNameLookup;
 private XmlSerializerProvider xmlSerializerProvider;
 private final static Class<String> STRING_CLASS = String.class;
 private final static Class<Integer> INT_CLASS = Integer.class;
 private final static Class<Integer[]> INT_ARRAY_CLASS = Integer[].class;

 @Before
 public void setUp() throws IOException {
 rootNameLookup = new XmlRootNameLookup();
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 }

 @Test
 public void testSerializeValueNull() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 XmlSerializerProvider testee = xmlSerializerProvider;
 testee.serializeValue(jsonGenerator, null);
 jsonGenerator.flush();
 String xmlOutput = outputStream.toString();
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(new ByteArrayInputStream(xmlOutput.getBytes()));
 assertEquals(XMLStreamConstants.START_ELEMENT, xmlStreamReader.getEventType());
 assertEquals("null", xmlStreamReader.getLocalName());
 xmlStreamReader.nextTag(); // skip "null" element
 assertEquals(XMLStreamConstants.END_ELEMENT, xmlStreamReader.getEventType());
 }

 @Test
 public void testSerializeValueString() throws JsonProcessingException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 XmlSerializerProvider testee = xmlSerializerProvider;
 String testString = "testString";
 testee.serializeValue(jsonGenerator, testString);
 jsonGenerator.flush();
 String xmlOutput = outputStream.toString();
 ObjectMapper objectMapper = new XmlMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(xmlOutput.getBytes()));
 JsonNode rootNode = objectMapper.readTree(jsonParser);
 assertEquals("testString", rootNode.asText());
 }

 @Test
 public void testSerializeValueInteger() throws JsonProcessingException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 XmlSerializerProvider testee = xmlSerializerProvider;
 Integer testInt = 1;
 testee.serializeValue(jsonGenerator, testInt);
 jsonGenerator.flush();
 String xmlOutput = outputStream.toString();
 ObjectMapper objectMapper = new XmlMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(xmlOutput.getBytes()));
 JsonNode rootNode = objectMapper.readTree(jsonParser);
 assertEquals("1", rootNode.asText());
 }

 @Test
 public void testSerializeValueIntegerArray() throws JsonProcessingException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 XmlSerializerProvider testee = xmlSerializerProvider;
 Integer[] testArray = {1, 2, 3};
 testee.serializeValue(jsonGenerator, testArray, INT_ARRAY_CLASS);
 jsonGenerator.flush();
 String xmlOutput = outputStream.toString();
 ObjectMapper objectMapper = new XmlMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(xmlOutput.getBytes()));
 JsonNode rootNode = objectMapper.readTree(jsonParser);
 JsonNode arrayNode = rootNode.get("item");
 assertEquals(3, arrayNode.size());
 assertEquals("1", arrayNode.get(0).asText());
 assertEquals("2", arrayNode.get(1).asText());
 assertEquals("3", arrayNode.get(2).asText());
 }

 @Test
 public void testSerializeValueComplex() throws JsonProcessingException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 XmlSerializerProvider testee = xmlSerializerProvider;
 String testString = "testString";
 Integer testInt = 1;
 testee.serializeValue(jsonGenerator, testString, new PropertyName("testProperty"));
 testee.serializeValue(jsonGenerator, testInt, new PropertyName("testProperty"));
 testee.serializeValue(jsonGenerator, testString, new PropertyName("testProperty"), null);
 testee.serializeValue(jsonGenerator, testInt, new PropertyName("testProperty"), null);
 testee.serializeValue(jsonGenerator, testString, new PropertyName("testProperty"), new StandardSerializer(STRING_CLASS));
 testee.serializeValue(jsonGenerator, testInt, new PropertyName("testProperty"), new StandardSerializer(INT_CLASS));
 jsonGenerator.flush();
 String xmlOutput = outputStream.toString();
 ObjectMapper objectMapper = new XmlMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(xmlOutput.getBytes()));
 JsonNode rootNode = objectMapper.readTree(jsonParser);
 assertEquals("testProperty", rootNode.get("testProperty").get("testString").asText());
 assertEquals("testProperty", rootNode.get("testProperty").get("testInt").asText());
 assertEquals("testString", rootNode.get("testProperty")[0].get("$").asText());
 assertEquals("testInt", rootNode.get("testProperty")[1].get("$").asText());
 assertEquals("testString", rootNode.get("testProperty").get(0).get("_class").asText());
 assertEquals("java.lang.String", rootNode.get("testProperty").get(0).get("_class").asText());
 assertEquals("testInt", rootNode.get("testProperty").get(1).get("_class").asText());
 assertEquals("java.lang.Integer", rootNode.get("testProperty").get(1).get("_class").asText());
 }

 @Test
 public void testSerializeValueWithNullRootName() throws JsonProcessingException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 XmlSerializerProvider testee = xmlSerializerProvider;
 String testString = "testString";
 testee.serializeValue(jsonGenerator, testString, null);
 jsonGenerator.flush();
 String xmlOutput = outputStream.toString();
 ObjectMapper objectMapper = new XmlMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(xmlOutput.getBytes()));
 JsonNode rootNode = objectMapper.readTree(jsonParser);
 assertNull(rootNode.get("_class"));
 assertNull(rootNode.get("_name"));
 }

 @Test
 public void testSerializeValueWithRootName() throws JsonProcessingException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 SerializationConfig config = new SerializationConfig(new ObjectMapper().getConfig());
 config.setRootValueContainer(new PropertyName("test"));
 XmlSerializerProvider testee = new XmlSerializerProvider(xmlSerializerProvider, config);
 String testString = "testString";
 testee.serializeValue(jsonGenerator, testString, new PropertyName("testProperty"));
 jsonGenerator.flush();
 String xmlOutput = outputStream.toString();
 ObjectMapper objectMapper = new XmlMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(xmlOutput.getBytes()));
 JsonNode rootNode = objectMapper.readTree(jsonParser);
 assertEquals("testProperty", rootNode.get("test").get("testProperty").asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithRootNameAndNullSerializer() throws JsonProcessingException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 SerializationConfig config = new SerializationConfig(new ObjectMapper().getConfig());
 config.setRootValueContainer(new PropertyName("test"));
 XmlSerializerProvider testee = new XmlSerializerProvider(xmlSerializerProvider, config);
 String testString = "testString";
 testee.serializeValue(jsonGenerator, testString, new PropertyName("testProperty"), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithRootNameAndWrongSerializerClass() throws JsonProcessingException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 SerializationConfig config = new SerializationConfig(new ObjectMapper().getConfig());
 config.setRootValueContainer(new PropertyName("test"));
 XmlSerializerProvider testee = new XmlSerializerProvider(xmlSerializerProvider, config);
 String testString = "testString";
 testee.serializeValue(jsonGenerator, testString, new PropertyName("testProperty"), new JsonMapperFactory().getMapper().getSerializerProvider().findValueSerializer(String[].class));
 }

 @Test
 public void testCreateInstance() throws IOException {
 XmlMapper xmlMapper = new XmlMapper();
 SerializerProvider provider = xmlMapper.getSerializerProvider();
 XmlSerializerProvider testee = new XmlSerializerProvider(provider.getDefaultSerializerProvider(), xmlMapper.getSerializationConfig(), null);
 }

 @Test
 public void testSerializeValueWithToXmlGenerator() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 ToXmlGenerator toXmlGenerator = new ToXmlGenerator(jsonGenerator);
 XmlSerializerProvider testee = xmlSerializerProvider;
 testee.serializeValue(toXmlGenerator, "testString");
 toXmlGenerator.writeEndObject();
 String xmlOutput = outputStream.toString();
 ObjectMapper objectMapper = new XmlMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(xmlOutput.getBytes()));
 JsonNode rootNode = objectMapper.readTree(jsonParser);
 assertEquals("testString", rootNode.asText());
 }

 @Test
 public void testSerializeValueWithToXmlGeneratorAndNullRootName() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 ToXmlGenerator toXmlGenerator = new ToXmlGenerator(jsonGenerator);
 XmlSerializerProvider testee = xmlSerializerProvider;
 testee.serializeValue(toXmlGenerator, null);
 toXmlGenerator.writeEndObject();
 String xmlOutput = outputStream.toString();
 ObjectMapper objectMapper = new XmlMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(xmlOutput.getBytes()));
 JsonNode rootNode = objectMapper.readTree(jsonParser);
 assertNull(rootNode.get("_class"));
 assertNull(rootNode.get("_name"));
 }

 @Test
 public void testSerializeValueWithToXmlGeneratorAndRootName() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 JsonFactory jsonFactory = new JsonFactory();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
 ToXmlGenerator toXmlGenerator = new ToXmlGenerator(jsonGenerator);
 SerializationConfig config = new SerializationConfig(new ObjectMapper().getConfig());
 config.setRootValueContainer(new PropertyName("test"));
 XmlSerializerProvider testee = new XmlSerializerProvider(xmlSerializerProvider, config);
 testee.serializeValue(toXmlGenerator, "testString", new PropertyName("testProperty"));
 toXmlGenerator.writeEndObject();
 String xmlOutput = outputStream.toString();
 ObjectMapper objectMapper = new XmlMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(xmlOutput.getBytes()));
 JsonNode rootNode = objectMapper.readTree(jsonParser);
 assertEquals("testProperty", rootNode.get("test").get("testProperty").asText());
 }

}