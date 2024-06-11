package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.StdSerializerProviders;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.JacksonXmlTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlSerializerProviderTest extends JacksonXmlTestBase {

 private XmlSerializerProvider xmlSerializerProvider;
 private SimpleBeanPropertySerializerMapper propertyMapper;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 objectMapper = new XmlMapper();
 propertyMapper = new SimpleBeanPropertySerializerMapper(objectMapper.getSerializationConfig());
 xmlSerializerProvider = new XmlSerializerProvider(objectMapper.getRootValueFactory(), objectMapper.getSerializationConfig(), propertyMapper);
 }

 @Test
 public void testSerializeNull() throws IOException {
 JsonGenerator jsonGenerator = null;
 try {
 jsonGenerator = createJsonGenerator();
 xmlSerializerProvider.serializeValue(jsonGenerator, null);
 jsonGenerator.flush();
 String xml = readText(jsonGenerator.asOutputProtectedStream());
 Assert.assertEquals("<null/>", xml);
 } finally {
 if (jsonGenerator != null) {
 jsonGenerator.close();
 }
 }
 }

 @Test
 public void testSerializeObjectWithDefaultRootName() throws IOException {
 ObjectNode objectNode = createObjectNode();
 objectNode.putPOJOIn("person", new Person());
 JsonGenerator jsonGenerator = createJsonGenerator();
 xmlSerializerProvider.serializeValue(jsonGenerator, objectNode);
 jsonGenerator.flush();
 String xml = readText(jsonGenerator.asOutputProtectedStream());
 Assert.assertEquals("<person><name>John Doe</name><age>30</age></person>", xml);
 }

 @Test
 public void testSerializeObjectWithCustomRootName() throws IOException {
 SimpleModule module = new SimpleModule();
 module.setSerializerProvider(xmlSerializerProvider);
 ObjectMapper objectMapper = new XmlMapper(module);
 ObjectNode objectNode = createObjectNode();
 objectNode.putPOJOIn("person", new Person());
 JsonGenerator jsonGenerator = createJsonGenerator();
 xmlSerializerProvider.setRootNameLookup(objectMapper.getSerializationConfig().getXmlRootNameLookup());
 xmlSerializerProvider.serializeValue(jsonGenerator, objectNode, Person.class);
 jsonGenerator.flush();
 String xml = readText(jsonGenerator.asOutputProtectedStream());
 Assert.assertEquals("<person><name>John Doe</name><age>30</age></person>", xml);
 }

 @Test
 public void testSerializeArray() throws IOException {
 ObjectNode objectNode = createObjectNode();
 objectNode.put("numbers", createNumberArrayNode(new Long[]{1L, 2L, 3L, 4L}));
 JsonGenerator jsonGenerator = createJsonGenerator();
 xmlSerializerProvider.serializeValue(jsonGenerator, objectNode, Long[].class);
 jsonGenerator.flush();
 String xml = readText(jsonGenerator.asOutputProtectedStream());
 Assert.assertEquals("<numbers><number>1</number><number>2</number><number>3</number><number>4</number></numbers>", xml);
 }

 @Test
 public void testSerializeArrayWithCustomRootName() throws IOException {
 SimpleModule module = new SimpleModule();
 module.setSerializerProvider(xmlSerializerProvider);
 ObjectMapper objectMapper = new XmlMapper(module);
 ObjectNode objectNode = createObjectNode();
 objectNode.put("numbers", createNumberArrayNode(new Long[]{1L, 2L, 3L, 4L}));
 JsonGenerator jsonGenerator = createJsonGenerator();
 xmlSerializerProvider.setRootNameLookup(objectMapper.getSerializationConfig().getXmlRootNameLookup());
 xmlSerializerProvider.serializeValue(jsonGenerator, objectNode, "ArrayOfNumber.xml", Long[].class);
 xmlSerializerProvider.getSerializers().findValueSerializer(Long[].class, null, null);
 JsonSerializer<Long[]> serializer = (JsonSerializer<Long[]>) xmlSerializerProvider.getSerializers().findValueSerializer(Long[].class, null, null);
 serializer.serialize(new Long[]{1L, 2L, 3L, 4L}, jsonGenerator, xmlSerializerProvider);
 jsonGenerator.flush();
 String xml = readText(jsonGenerator.asOutputProtectedStream());
 Assert.assertEquals("<ArrayOfNumber xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><number>1</number><number>2</number><number>3</number><number>4</number></ArrayOfNumber>", xml);
 }

 @Test
 public void testSerializeObjectWithEmptyStringRootName() throws IOException {
 ObjectNode objectNode = createObjectNode();
 objectNode.putPOJOIn("person", new Person());
 JsonGenerator jsonGenerator = createJsonGenerator();
 xmlSerializerProvider.setRootNameLookup(new XmlRootNameLookup(new PropertyName("")));
 xmlSerializerProvider.serializeValue(jsonGenerator, objectNode, Person.class);
 jsonGenerator.flush();
 String xml = readText(jsonGenerator.asOutputProtectedStream());
 Assert.assertEquals("<></person>", xml);
 }

 @Test
 public void testSerializeObjectWithInvalidRootName() throws IOException {
 ObjectNode objectNode = createObjectNode();
 objectNode.putPOJOIn("person", new Person());
 JsonGenerator jsonGenerator = createJsonGenerator();
 xmlSerializerProvider.setRootNameLookup(new XmlRootNameLookup(new PropertyName("person")));
 try {
 xmlSerializerProvider.serializeValue(jsonGenerator, objectNode, Person.class);
 failure("Expected JsonMappingException, but got none");
 } catch (JsonMappingException e) {
 Assert.assertEquals("Root name 'person' does not match expected '': Cannot construct root name from value ['person']", e.getMessage());
 }
 }

 @Test
 public void testSerializeObjectWithInvalidRootNamespace() throws IOException {
 ObjectNode objectNode = createObjectNode();
 objectNode.putPOJOIn("person", new Person());
 JsonGenerator jsonGenerator = createJsonGenerator();
 xmlSerializerProvider.setRootNameLookup(new XmlRootNameLookup(new PropertyName("person"), "http://example.com/"));
 try {
 xmlSerializerProvider.serializeValue(jsonGenerator, objectNode, Person.class);
 failure("Expected JsonMappingException, but got none");
 } catch (JsonMappingException e) {
 Assert.assertEquals("Root name 'person' does not match expected '': Cannot construct root name from value ['person']", e.getMessage());
 }
 }

 @Test
 public void testSerializeObjectWithNullRootNameLookup() throws IOException {
 ObjectNode objectNode = createObjectNode();
 objectNode.putPOJOIn("person", new Person());
 JsonGenerator jsonGenerator = createJsonGenerator();
 ObjectMapper objectMapper = new XmlMapper();
 SerializerProvider serializerProvider = StdSerializerProviders.emptyProvider();
 xmlSerializerProvider = new XmlSerializerProvider(objectMapper.getRootValueFactory(), objectMapper.getSerializationConfig(), serializerProvider);
 try {
 xmlSerializerProvider.serializeValue(jsonGenerator, objectNode, Person.class);
 failure("Expected JsonMappingException, but got none");
 } catch (JsonMappingException e) {
 Assert.assertEquals("No XML root name lookup provided", e.getMessage());
 }
 }

 private static ObjectNode createObjectNode() throws IOException {
 return objectMapper.readTree("{\"person\":{}}");
 }

 private static JsonParser createJsonParser() throws IOException {
 return objectMapper.getFactory().createParser(new ByteArrayInputStream("".getBytes()));
 }

 private static JsonGenerator createJsonGenerator() throws IOException {
 return objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 }

 private static ObjectNode createNumberArrayNode(Object[] values) throws IOException {
 ObjectNode arrayNode = objectMapper.createObjectNode();
 for (int i = 0; i < values.length; i++) {
 ObjectNode numberNode = objectMapper.createObjectNode();
 numberNode.put("value", values[i]);
 arrayNode.set("number[" + i + "]", numberNode);
 }
 return arrayNode;
 }

}