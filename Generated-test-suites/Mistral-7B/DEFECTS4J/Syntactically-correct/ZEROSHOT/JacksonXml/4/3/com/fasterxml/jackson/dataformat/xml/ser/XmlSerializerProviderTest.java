package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.TestUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;

public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private XmlMapper xmlMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 xmlMapper = new XmlMapper();
 }

 @Test
 public void testXmlSerializerProviderCreation() {
 // Test default constructor
 XmlSerializerProvider provider = new XmlSerializerProvider(null);
 assertNotNull(provider);

 // Test constructor with config
 SerializationConfig config = new SerializationConfig(objectMapper.getConfig());
 XmlSerializerProvider providerWithConfig = new XmlSerializerProvider(new XmlRootNameLookup(config), config, null);
 assertNotNull(providerWithConfig);

 // Test constructor with source and config
 XmlSerializerProvider providerWithSrc = new XmlSerializerProvider(new XmlSerializerProvider(), config, null);
 assertNotNull(providerWithSrc);
 }

 @Test
 public void testCreateInstance() throws JsonProcessingException {
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 SerializerProvider providerInstance = provider.createInstance(xmlMapper.getSerializationConfig(), null);
 assertSame(provider, providerInstance);
 }

 @Test
 public void testSerializeValueWithNullValue() throws IOException {
 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 provider.serializeValue(gen, null);
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<null/>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithObject() throws JsonProcessingException {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("name", "John Doe");
 rootNode.put("age", 30);
 rootNode.put("city", "New York");

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 provider.serializeValue(gen, rootNode);
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<root>");
 expectedXml.add("<name>John Doe</name>");
 expectedXml.add("<age>30</age>");
 expectedXml.add("<city>New York</city>");
 expectedXml.add("</root>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithObjectAndRootType() throws JsonProcessingException {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("name", "John Doe");
 rootNode.put("age", 30);
 rootNode.put("city", "New York");

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 provider.serializeValue(gen, rootNode, JsonTypeFactory.valueToType(rootNode, ObjectNode.class));
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<root>");
 expectedXml.add("<name>John Doe</name>");
 expectedXml.add("<age>30</age>");
 expectedXml.add("<city>New York</city>");
 expectedXml.add("</root>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithObjectAndRootTypeAndSerializer() throws JsonProcessingException {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("name", "John Doe");
 rootNode.put("age", 30);
 rootNode.put("city", "New York");

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);
 serializer.serialize(rootNode, gen, provider);
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<root>");
 expectedXml.add("<name>John Doe</name>");
 expectedXml.add("<age>30</age>");
 expectedXml.add("<city>New York</city>");
 expectedXml.add("</root>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithArray() throws JsonProcessingException {
 ArrayNode arrayNode = objectMapper.createArrayNode();
 arrayNode.add(1);
 arrayNode.add(2);
 arrayNode.add(3);

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 provider.serializeValue(gen, arrayNode);
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<items>");
 expectedXml.add("<int>1</int>");
 expectedXml.add("<int>2</int>");
 expectedXml.add("<int>3</int>");
 expectedXml.add("</items>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithArrayAndRootType() throws JsonProcessingException {
 ArrayNode arrayNode = objectMapper.createArrayNode();
 arrayNode.add(1);
 arrayNode.add(2);
 arrayNode.add(3);

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 provider.serializeValue(gen, arrayNode, JsonTypeFactory.valueToType(arrayNode, ArrayNode.class));
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<items>");
 expectedXml.add("<int>1</int>");
 expectedXml.add("<int>2</int>");
 expectedXml.add("<int>3</int>");
 expectedXml.add("</items>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithArrayAndRootTypeAndSerializer() throws JsonProcessingException {
 ArrayNode arrayNode = objectMapper.createArrayNode();
 arrayNode.add(1);
 arrayNode.add(2);
 arrayNode.add(3);

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 JsonSerializer<ArrayNode> serializer = provider.findValueSerializer(ArrayNode.class, null);
 serializer.serialize(arrayNode, gen, provider);
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<items>");
 expectedXml.add("<int>1</int>");
 expectedXml.add("<int>2</int>");
 expectedXml.add("<int>3</int>");
 expectedXml.add("</items>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithFilter() throws JsonProcessingException {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("name", "John Doe");
 rootNode.put("age", 30);
 rootNode.put("city", "New York");

 SimpleFilterProvider filterProvider = new SimpleFilterProvider();
 filterProvider.setFilterInstance(SimpleBeanPropertyFilter.filterOutAllExcept("name"));

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 provider.setSerializerProvider(provider);
 provider.setFilterProvider(filterProvider);
 provider.serializeValue(gen, rootNode);
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<root>");
 expectedXml.add("<name>John Doe</name>");
 expectedXml.add("</root>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithFilterAndRootType() throws JsonProcessingException {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("name", "John Doe");
 rootNode.put("age", 30);
 rootNode.put("city", "New York");

 SimpleFilterProvider filterProvider = new SimpleFilterProvider();
 filterProvider.setFilterInstance(SimpleBeanPropertyFilter.filterOutAllExcept("name"));

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 provider.setFilterProvider(filterProvider);
 provider.serializeValue(gen, rootNode, JsonTypeFactory.valueToType(rootNode, ObjectNode.class));
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<root>");
 expectedXml.add("<name>John Doe</name>");
 expectedXml.add("</root>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithFilterAndRootTypeAndSerializer() throws JsonProcessingException {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("name", "John Doe");
 rootNode.put("age", 30);
 rootNode.put("city", "New York");

 SimpleFilterProvider filterProvider = new SimpleFilterProvider();
 filterProvider.setFilterInstance(SimpleBeanPropertyFilter.filterOutAllExcept("name"));

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 JsonSerializer<ObjectNode> serializer = provider.findValueSerializer(ObjectNode.class, null);
 provider.setFilterProvider(filterProvider);
 serializer.serialize(rootNode, gen, provider);
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<root>");
 expectedXml.add("<name>John Doe</name>");
 expectedXml.add("</root>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }

 @Test
 public void testSerializeValueWithFilterAndRootTypeAndSerializerAndWriteRootName() throws JsonProcessingException, IOException {
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("name", "John Doe");
 rootNode.put("age", 30);
 rootNode.put("city", "New York");

 SimpleFilterProvider filterProvider = new SimpleFilterProvider();
 filterProvider.setFilterInstance(SimpleBeanPropertyFilter.filterOutAllExcept("name"));

 JsonGenerator gen = xmlMapper.getXmlFactory().createGenerator(TestUtil.createTempFile("xmlSerializerProviderTest.xml"));
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper.getSerializationConfig()), xmlMapper, null);
 provider.setFilterProvider(filterProvider);
 provider.serializeValue(gen, rootNode, JsonTypeFactory.valueToType(rootNode, ObjectNode.class), new QName("test"));
 gen.close();

 List<String> expectedXml = new ArrayList<>();
 expectedXml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 expectedXml.add("<test>");
 expectedXml.add("<name>John Doe</name>");
 expectedXml.add("</test>");
 TestUtil.assertXmlEquals(expectedXml, xmlMapper.readTree(TestUtil.createTempFile("xmlSerializerProviderTest.xml")));
 }
}