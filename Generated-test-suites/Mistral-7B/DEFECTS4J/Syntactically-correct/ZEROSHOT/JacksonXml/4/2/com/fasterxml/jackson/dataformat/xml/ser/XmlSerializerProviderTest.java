package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class XmlSerializerProviderTest {

 private static final ObjectMapper objectMapper = new XmlMapper();
 private static final SerializationConfig config = objectMapper.getSerializationConfig();
 private static final XmlMapper xmlMapper = new XmlMapper(new XmlFactory(config));

 @org.junit.BeforeClass
 public static void init() {
 config.setRootValueSeparator(new PropertyName("root"));
 }

 @org.junit.Test
 public void testSerializeNullValue() throws IOException {
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(null));
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, null);
 assertEquals("<null/>", StaxUtil.toString(gen));
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSerializeObject() throws IOException {
 ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
 rootNode.put("name", "John Doe");
 rootNode.putPOJO("person", new Person("John Doe", 30));

 JsonSerializer<ObjectNode> nodeSerializer = xmlMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, null);
 JsonSerializer<Person> personSerializer = xmlMapper.getSerializerProvider().findValueSerializer(Person.class, null);

 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(null));
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, rootNode);
 String xml = StaxUtil.toString(gen);
 assertEquals("<root><person><name>John Doe</name><age>30</age></person></root>", xml);
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSerializeObjectWithCustomRootName() throws IOException {
 ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
 rootNode.put("name", "John Doe");
 rootNode.putPOJO("person", new Person("John Doe", 30));

 JsonSerializer<ObjectNode> nodeSerializer = xmlMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, null);
 JsonSerializer<Person> personSerializer = xmlMapper.getSerializerProvider().findValueSerializer(Person.class, null);

 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("customRootName")));
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, rootNode);
 String xml = StaxUtil.toString(gen);
 assertEquals("<customRootName><person><name>John Doe</name><age>30</age></person></customRootName>", xml);
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSerializeArray() throws IOException {
 ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
 rootNode.put("items", new ObjectNode(JsonNodeFactory.instance.valueElement("Item1")));
 rootNode.put("items", rootNode.get("items").append(JsonNodeFactory.instance.valueElement("Item2")));

 JsonSerializer<ObjectNode> nodeSerializer = xmlMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, null);

 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(null));
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, rootNode);
 String xml = StaxUtil.toString(gen);
 assertEquals("<root><items><Item1/><Item2/></items></root>", xml);
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSerializeArrayWithCustomRootName() throws IOException {
 ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
 rootNode.put("items", new ObjectNode(JsonNodeFactory.instance.valueElement("Item1")));
 rootNode.put("items", rootNode.get("items").append(JsonNodeFactory.instance.valueElement("Item2")));

 JsonSerializer<ObjectNode> nodeSerializer = xmlMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, null);

 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("customRootName")));
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, rootNode);
 String xml = StaxUtil.toString(gen);
 assertEquals("<customRootName><items><Item1/><Item2/></items></customRootName>", xml);
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSerializeObjectWithFilter() throws IOException {
 ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
 rootNode.put("name", "John Doe");
 rootNode.putPOJO("person", new Person("John Doe", 30));

 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
 SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFilterByName("*", filter);
 JsonMapper mapper = new JsonMapper(config, filterProvider);
 ObjectNode filteredNode = mapper.readValue(xmlMapper.writeValueAsString(rootNode), ObjectNode.class);

 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(null));
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, filteredNode);
 String xml = StaxUtil.toString(gen);
 assertEquals("<root><name>John Doe</name></root>", xml);
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSerializeObjectWithFilterAndCustomRootName() throws IOException {
 ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
 rootNode.put("name", "John Doe");
 rootNode.putPOJO("person", new Person("John Doe", 30));

 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
 SimpleFilterProvider filterProvider = new SimpleFilterProvider().setFilterByName("*", filter);
 JsonMapper mapper = new JsonMapper(config, filterProvider);
 ObjectNode filteredNode = mapper.readValue(xmlMapper.writeValueAsString(rootNode), ObjectNode.class);

 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("customRootName")));
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, filteredNode);
 String xml = StaxUtil.toString(gen);
 assertEquals("<customRootName><name>John Doe</name></customRootName>", xml);
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testInitializeWithRootName() throws IOException {
 ToXmlGenerator gen = null;
 try {
 gen = new ToXmlGenerator(new ByteArrayOutputStream(), new XmlFactory(config).getXmlSerializer());
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("rootName")));
 provider.initWithRootName(gen, new QName("rootName"));
 assertEquals("rootName", gen.getCurrentName());
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testInitializeWithNullRootName() throws IOException {
 ToXmlGenerator gen = null;
 try {
 gen = new ToXmlGenerator(new ByteArrayOutputStream(), new XmlFactory(config).getXmlSerializer());
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(null));
 provider.initWithRootName(gen, null);
 assertEquals("", gen.getCurrentName());
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSetNamespace() throws IOException {
 ToXmlGenerator gen = null;
 try {
 gen = new ToXmlGenerator(new ByteArrayOutputStream(), new XmlFactory(config).getXmlSerializer());
 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("customRootName")));
 provider.initWithRootName(gen, new QName("customRootName"));
 provider.getRootNameLookup().setPrefix("prefix", "http://customNamespace");
 provider.initWithRootName(gen, null);
 assertEquals("prefix:customRootName", gen.getCurrentName().getLocalPart());
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSerializeArrayWithRootName() throws IOException {
 ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
 rootNode.put("items", new ObjectNode(JsonNodeFactory.instance.valueElement("Item1")));
 rootNode.put("items", rootNode.get("items").append(JsonNodeFactory.instance.valueElement("Item2")));

 JsonSerializer<ObjectNode> nodeSerializer = xmlMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, null);

 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("rootName")));
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, rootNode);
 String xml = StaxUtil.toString(gen);
 assertEquals("<rootName><items><Item1/><Item2/></items></rootName>", xml);
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSerializeArrayWithRootNameAndNamespace() throws IOException {
 ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
 rootNode.put("items", new ObjectNode(JsonNodeFactory.instance.valueElement("Item1")));
 rootNode.put("items", rootNode.get("items").append(JsonNodeFactory.instance.valueElement("Item2")));

 JsonSerializer<ObjectNode> nodeSerializer = xmlMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, null);

 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("rootName", "http://customNamespace")));
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, rootNode);
 String xml = StaxUtil.toString(gen);
 assertEquals("<rootName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://customNamespace\"><items><Item1/><Item2/></items></rootName>", xml);
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }

 @org.junit.Test
 public void testSerializeArrayWithRootNameAndCustomRootSerializer() throws IOException {
 ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
 rootNode.put("items", new ObjectNode(JsonNodeFactory.instance.valueElement("Item1")));
 rootNode.put("items", rootNode.get("items").append(JsonNodeFactory.instance.valueElement("Item2")));

 JsonSerializer<ObjectNode> nodeSerializer = xmlMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, null);
 JsonSerializer<ObjectNode> rootSerializer = new JsonSerializer<ObjectNode>() {
 @Override
 public void serialize(ObjectNode node, JsonGenerator gen, SerializerProvider p) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("rootName");
 gen.writeStartArray();
 node.elements().forEachRemaining(child -> {
 try {
 gen.writeStartObject();
 StaxUtil.writeValueAsXml(child.asText(), gen, p);
 gen.writeEndObject();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 });
 gen.writeEndArray();
 gen.writeEndObject();
 }
 };

 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new QName("rootName")));
 provider.setRootValueSerializer(rootSerializer);
 JsonGenerator gen = null;
 try {
 gen = xmlMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 provider.serializeValue(gen, rootNode);
 String xml = StaxUtil.toString(gen);
 assertEquals("<rootName><items><Item1/><Item2/></items></rootName>", xml);
 } finally {
 if (gen != null) {
 gen.close();
 }
 }
 }
}