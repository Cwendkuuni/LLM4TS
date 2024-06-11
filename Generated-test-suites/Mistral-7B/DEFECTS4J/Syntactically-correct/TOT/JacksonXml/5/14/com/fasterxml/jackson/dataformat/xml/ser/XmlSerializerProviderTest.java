package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private XmlMapper xmlMapper;
 private XmlSerializerProvider xmlSerializerProvider;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 xmlMapper = new XmlMapper();
 xmlSerializerProvider = xmlMapper.getSerializerProvider();
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 String expectedXml = "<null/>";
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(null);
 ObjectNode actualNode = (ObjectNode) actualXml;
 assertEquals(expectedXml, actualNode.toString());
 }

 @Test
 public void testSerializeObjectWithRootNameFromConfig() throws JsonProcessingException {
 String expectedXml = "<root><value>Hello world</value></root>";
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("root", "value");
 SerializationConfig config = objectMapper.getSerializationConfig();
 config.setRootValueSeparator("root");
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(new String("Hello world"), config);
 ObjectNode actualNode = (ObjectNode) actualXml;
 assertEquals(expectedXml, actualNode.toString());
 }

 @Test
 public void testSerializeObjectWithRootNameFromLookup() throws JsonProcessingException {
 String expectedXml = "<value>Hello world</value>";
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(new String("Hello world"));
 ObjectNode actualNode = (ObjectNode) actualXml;
 assertEquals(expectedXml, actualNode.toString());
 }

 @Test
 public void testSerializeObjectAsArray() throws JsonProcessingException {
 String expectedXml = "<item><value1>Hello world 1</value1><value2>Hello world 2</value2></item>";
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("item", objectMapper.createArrayNode());
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(new String[]{"Hello world 1", "Hello world 2"}, null);
 ObjectNode actualNode = (ObjectNode) actualXml;
 assertEquals(expectedXml, actualNode.toString());
 }

 @Test
 public void testSerializeArrayWithRootNameFromConfig() throws JsonProcessingException {
 String expectedXml = "<root><item><value>Hello world 1</value></item><item><value>Hello world 2</value></item></root>";
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("item", objectMapper.createArrayNode());
 rootNode.put("root", "item");
 SerializationConfig config = objectMapper.getSerializationConfig();
 config.setRootValueSeparator("root");
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(new String[]{"Hello world 1", "Hello world 2"}, config);
 ObjectNode actualNode = (ObjectNode) actualXml;
 assertEquals(expectedXml, actualNode.toString());
 }

 @Test
 public void testSerializeArrayWithRootNameFromLookup() throws JsonProcessingException {
 String expectedXml = "<item><value>Hello world 1</value></item><item><value>Hello world 2</value></item>";
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(new String[]{"Hello world 1", "Hello world 2"}, null);
 ObjectNode actualNode = (ObjectNode) actualXml;
 assertEquals(expectedXml, actualNode.toString());
 }

 @Test
 public void testSerializeObjectWithEmptyRootName() throws JsonProcessingException {
 String expectedXml = "<value>Hello world</value>";
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(new String("Hello world"), null);
 ObjectNode actualNode = (ObjectNode) actualXml;
 assertEquals(expectedXml, actualNode.toString());
 }

 @Test
 public void testSerializeObjectWithEmptyRootNameFromConfig() throws JsonProcessingException {
 String expectedXml = "<value><value>Hello world</value></value>";
 SerializationConfig config = objectMapper.getSerializationConfig();
 config.setRootValueSeparator("value");
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(new String("Hello world"), config);
 ObjectNode actualNode = (ObjectNode) actualXml;
 assertEquals(expectedXml, actualNode.toString());
 }

 @Test
 public void testSerializeObjectWithEmptyRootNameFromLookup() throws JsonProcessingException {
 String expectedXml = "<value>Hello world</value>";
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(new String("Hello world"), null);
 ObjectNode actualNode = (ObjectNode) actualXml;
 assertEquals(expectedXml, actualNode.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeObjectWithEmptyRootNameNotAllowed() throws JsonProcessingException {
 String expectedErrorMessage = "Cannot instantiate value of type [simple type, class java.lang.String] from JSON String";
 SerializationConfig config = new SerializationConfig();
 config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 JsonNode actualXml = xmlSerializerProvider.serializeValueToTree(null, config);
 ObjectNode actualNode = (ObjectNode) actualXml;
 // Should fail with JsonMappingException due to empty root name
 assertNull(actualNode);
 }
}