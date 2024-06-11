package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;

public class XmlSerializerProviderTest {

 private ObjectMapper xmlMapper;
 private ObjectMapper jsonMapper;
 private XmlRootNameLookup rootNameLookup;

 @Before
 public void setup() {
 xmlMapper = new XmlMapper();
 jsonMapper = new ObjectMapper();
 rootNameLookup = new XmlRootNameLookup();
 }

 @Test
 public void testXmlSerializerProvider() throws JsonMappingException, IOException {
 // Test empty constructor
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider();
 assertNotNull(xmlProvider);
 assertEquals(XmlSerializerProvider.class, xmlProvider.getClass());

 // Test constructor with rootNameLookup
 XmlSerializerProvider xmlProviderWithRootNameLookup = new XmlSerializerProvider(rootNameLookup);
 assertNotNull(xmlProviderWithRootNameLookup);
 assertEquals(XmlSerializerProvider.class, xmlProviderWithRootNameLookup.getClass());
 assertSame(rootNameLookup, xmlProviderWithRootNameLookup._rootNameLookup);

 // Test copy constructor
 XmlSerializerProvider xmlProviderCopy = new XmlSerializerProvider(xmlProviderWithRootNameLookup);
 assertNotNull(xmlProviderCopy);
 assertEquals(XmlSerializerProvider.class, xmlProviderCopy.getClass());
 assertSame(rootNameLookup, xmlProviderCopy._rootNameLookup);

 // Test serialization of null value
 String xml = xmlMapper.writeValueAsString(null, new XmlFactory());
 JsonNode json = jsonMapper.readTree(xml);
 JsonNode jsonRoot = json.get(0);
 assertEquals("null", jsonRoot.asText());

 // Test serialization of simple class
 ObjectNode jsonObject = jsonMapper.createObjectNode();
 jsonObject.put("name", "John Doe");
 String xmlWithRootName = xmlMapper.writeValueAsString(jsonObject, new XmlFactory());
 JsonNode xmlNode = jsonMapper.readTree(xmlWithRootName);
 JsonNode xmlRoot = xmlNode.get(0);
 assertEquals("JohnDoe", xmlRoot.get("name").asText());

 // Test serialization of class with custom root name
 ObjectNode jsonObjectWithCustomRootName = jsonMapper.createObjectNode();
 jsonObjectWithCustomRootName.put("name", "John Doe");
 SerializerProvider jsonProvider = jsonMapper.getSerializerProvider();
 JsonSerializer<ObjectNode> jsonSerializer = jsonProvider.findValueSerializer(ObjectNode.class, null);
 SerializerFactory jsonSerializerFactory = jsonProvider.getSerializerFactory();
 SerializationConfig jsonConfig = jsonProvider.getConfig();
 SerializationConfig xmlConfig = new SerializationConfig(jsonConfig);
 xmlConfig.setRootValueSerializer(new StdSerializer<ObjectNode>(ObjectNode.class) {
 @Override public void serialize(ObjectNode value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("customRootName");
 provider.writeValue(gen, value);
 gen.writeEndObject();
 }
 });
 xmlConfig.setRootValueSerializerFactory(new BeanSerializerFactory(xmlConfig, xmlSerializerProviderWithRootNameLookup));
 XmlSerializerProvider xmlProviderWithCustomRootName = new XmlSerializerProvider(xmlConfig, jsonSerializerFactory, jsonSerializer);
 String xmlWithCustomRootName = xmlProviderWithCustomRootName.serializeValueAsString(jsonObjectWithCustomRootName);
 JsonNode xmlNodeWithCustomRootName = jsonMapper.readTree(xmlWithCustomRootName);
 JsonNode xmlRootWithCustomRootName = xmlNodeWithCustomRootName.get(0);
 assertEquals("customRootName", xmlRootWithCustomRootName.get("customRootName").get("name").asText());

 // Test serialization of array
 ObjectNode jsonObjectWithArray = jsonMapper.createObjectNode();
 jsonObjectWithArray.put("items", jsonMapper.createValueFactory().asArray());
 jsonObjectWithArray.set("items", jsonObjectWithArray.get("items").elements());
 jsonObjectWithArray.get("items").add(jsonMapper.valueToTree(1));
 jsonObjectWithArray.get("items").add(jsonMapper.valueToTree(2));
 String xmlWithArrayRootName = xmlMapper.writeValueAsString(jsonObjectWithArray, new XmlFactory());
 JsonNode xmlNodeWithArray = jsonMapper.readTree(xmlWithArrayRootName);
 JsonNode xmlRootWithArray = xmlNodeWithArray.get(0);
 ObjectNode xmlArrayRoot = (ObjectNode) xmlRootWithArray;
 assertEquals(2, xmlArrayRoot.size());
 assertTrue(xmlArrayRoot.has("item"));
 assertNotNull(xmlArrayRoot.get("item"));
 JsonNode xmlItem1 = xmlArrayRoot.get("item").get(0);
 assertEquals(1, xmlItem1.asInt());
 JsonNode xmlItem2 = xmlArrayRoot.get("item").get(1);
 assertEquals(2, xmlItem2.asInt());
 }

 @Test
 public void testSerializationWithRootNameLookup() throws JsonMappingException, IOException {
 // Test serialization of simple class with rootNameLookup
 ObjectNode jsonObjectWithRootNameLookup = jsonMapper.createObjectNode();
 jsonObjectWithRootNameLookup.put("name", "John Doe");
 SerializerProvider jsonProviderWithRootNameLookup = jsonProviderWithCustomRootName(rootNameLookup);
 JsonSerializer<ObjectNode> jsonSerializerWithRootNameLookup = jsonProviderWithRootNameLookup.findValueSerializer(ObjectNode.class, null);
 XmlSerializerProvider xmlProviderWithRootNameLookup = new XmlSerializerProvider(jsonConfig, jsonSerializerFactory, jsonSerializerWithRootNameLookup);
 String xmlWithRootNameLookup = xmlProviderWithRootNameLookup.serializeValueAsString(jsonObjectWithRootNameLookup);
 JsonNode xmlNodeWithRootNameLookup = jsonMapper.readTree(xmlWithRootNameLookup);
 JsonNode xmlRootWithRootNameLookup = xmlNodeWithRootNameLookup.get(0);
 assertEquals("JohnDoe", xmlRootWithRootNameLookup.get("name").asText());
 String expectedRootName = "_class";
 String actualRootName = xmlRootWithRootNameLookup.get("@"+expectedRootName).asText();
 assertEquals("com.fasterxml.jackson.databind.JsonNode", actualRootName);

 // Test serialization of class with custom root name using rootNameLookup
 ObjectNode jsonObjectWithCustomRootNameWithRootNameLookup = jsonMapper.createObjectNode();
 jsonObjectWithCustomRootNameWithRootNameLookup.put("name", "John Doe");
 SerializerProvider jsonProviderWithCustomRootNameWithRootNameLookup = jsonProviderWithCustomRootName(rootNameLookup);
 JsonSerializer<ObjectNode> jsonSerializerWithCustomRootNameWithRootNameLookup = jsonProviderWithCustomRootNameWithRootNameLookup.findValueSerializer(ObjectNode.class, null);
 XmlSerializerProvider xmlProviderWithCustomRootNameWithRootNameLookup = new XmlSerializerProvider(jsonConfig, jsonSerializerFactory, jsonSerializerWithCustomRootNameWithRootNameLookup);
 String xmlWithCustomRootNameWithRootNameLookup = xmlProviderWithCustomRootNameWithRootNameLookup.serializeValueAsString(jsonObjectWithCustomRootNameWithRootNameLookup);
 JsonNode xmlNodeWithCustomRootNameWithRootNameLookup = jsonMapper.readTree(xmlWithCustomRootNameWithRootNameLookup);
 JsonNode xmlRootWithCustomRootNameWithRootNameLookup = xmlNodeWithCustomRootNameWithRootNameLookup.get(0);
 assertEquals("customRootName", xmlRootWithCustomRootNameWithRootNameLookup.get("customRootName").get("name").asText());
 }

 // Helper method to create serializer provider with custom root name lookup
 private SerializerProvider jsonProviderWithCustomRootName(XmlRootNameLookup rootNameLookup) {
 SerializationConfig jsonConfig = jsonMapper.getSerializationConfig();
 SerializerFactory jsonSerializerFactory = jsonMapper.getSerializerFactory();
 return new DefaultSerializerProvider(jsonConfig, jsonSerializerFactory, rootNameLookup);
 }
}