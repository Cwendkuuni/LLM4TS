package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class XmlSerializerProviderTest {

 private XmlSerializerProvider serializerProvider;
 private XmlRootNameLookup rootNameLookup;

 @BeforeAll
 public void setup() {
 rootNameLookup = new XmlRootNameLookup();
 serializerProvider = new XmlSerializerProvider(rootNameLookup);
 }

 @Test
 public void testDefaultSerialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper(new XMLFactory());
 JsonNode node = mapper.readTree("{'name': 'John'}");
 String xml = mapper.writerWithRootValue(new Person("John")).writeValueAsString();

 JsonParser jsonParser = mapper.getFactory().createParser(node.getRawValue());
 JsonParser xmlParser = mapper.getFactory().createParser(new StringReader(xml));

 assertEquals(jsonParser.readValueAs(Person.class), xmlParser.readValueAs(Person.class));

 assertEquals(serializerProvider.getClass(), xmlParser.getSerializerProvider().getClass());
 }

 @Test
 public void testSerializationWithRootNameFromConfig() throws Exception {
 ObjectMapper mapper = new ObjectMapper(new XMLFactory());
 ObjectNode node = mapper.createObjectNode();
 node.put("rootElement", new Person("John"));
 String xml = mapper.writerForRootValue(node)
 .withRootName("rootElement")
 .writeValueAsString();

 JsonNode jsonNode = mapper.readTree(node.getRawValue());
 StringReader jsonReader = new StringReader(jsonNode.getRawValue());
 JsonParser jsonParser = mapper.getFactory().createParser(jsonReader);

 assertEquals(jsonParser.readValueAs(Person.class), xmlParser.readValueAs(Person.class));

 assertEquals(new QName("rootElement"), ((ToXmlGenerator) xmlParser.getSerializerProvider().getGenerator()).getNextName());
 }

 @Test
 public void testSerializationWithRootNameFromLookup() throws Exception {
 ObjectMapper mapper = new ObjectMapper(new XMLFactory());
 ObjectNode node = mapper.createObjectNode();
 node.put("rootElement", new Person("John"));
 String xml = mapper.writerForRootValue(node)
 .writeValueAsString();

 JsonNode jsonNode = mapper.readTree(node.getRawValue());
 StringReader jsonReader = new StringReader(jsonNode.getRawValue());
 JsonParser jsonParser = mapper.getFactory().createParser(jsonReader);

 assertEquals(jsonParser.readValueAs(Person.class), xmlParser.readValueAs(Person.class));

 assertEquals(new QName("person"), ((ToXmlGenerator) xmlParser.getSerializerProvider().getGenerator()).getNextName());
 }

 @Test
 public void testSerializationWithNullRootName() throws Exception {
 ObjectMapper mapper = new ObjectMapper(new XMLFactory());
 ObjectNode node = mapper.createObjectNode();
 node.put("rootElement", null);
 String xml = mapper.writerForRootValue(node)
 .writeValueAsString();

 JsonNode jsonNode = mapper.readTree(node.getRawValue());
 StringReader jsonReader = new StringReader(jsonNode.getRawValue());
 JsonParser jsonParser = mapper.getFactory().createParser(jsonReader);

 assertEquals(jsonParser.readValueAs(Object.class), xmlParser.readValueAs(Object.class));

 assertNull(((ToXmlGenerator) xmlParser.getSerializerProvider().getGenerator()).getNextName());
 }

 @Test
 public void testSerializationWithArray() throws Exception {
 ObjectMapper mapper = new ObjectMapper(new XMLFactory());
 ObjectNode node = mapper.createObjectNode();
 node.put("items", mapper.valueToTree(new int[] {1, 2, 3}));
 String xml = mapper.writerForRootValue(node)
 .writeValueAsString();

 JsonNode jsonNode = mapper.readTree(node.getRawValue());
 StringReader jsonReader = new StringReader(jsonNode.getRawValue());
 JsonParser jsonParser = mapper.getFactory().createParser(jsonReader);
 JsonParser xmlParser = mapper.getFactory().createParser(new StringReader(xml));

 JsonNode jsonResult = jsonParser.readValueAs(JsonNode.class);
 JsonNode xmlResult = xmlParser.readValueAs(JsonNode.class);

 assertEquals(jsonResult, xmlResult);

 List<Integer> jsonList = jsonResult.path("items").asValue().asIntArray();
 List<Integer> xmlList = xmlResult.path("item").asValue().asValue().asIntArray();

 assertArrayEquals(jsonList.toArray(), xmlList.toArray());
 }

 @Test
 public void testSerializationWithRootNameAndArray() throws Exception {
 ObjectMapper mapper = new ObjectMapper(new XMLFactory());
 ObjectNode node = mapper.createObjectNode();
 node.put("rootElement", mapper.valueToTree(new int[] {1, 2, 3}));
 String xml = mapper.writerForRootValue(node)
 .withRootName("rootElement")
 .writeValueAsString();

 JsonNode jsonNode = mapper.readTree(node.getRawValue());
 StringReader jsonReader = new StringReader(jsonNode.getRawValue());
 JsonParser jsonParser = mapper.getFactory().createParser(jsonReader);
 JsonParser xmlParser = mapper.getFactory().createParser(new StringReader(xml));

 JsonNode jsonResult = jsonParser.readValueAs(JsonNode.class);
 JsonNode xmlResult = xmlParser.readValueAs(JsonNode.class);

 assertEquals(jsonResult, xmlResult);

 List<Integer> jsonList = jsonResult.path("items").asValue().asIntArray();
 List<Integer> xmlList = xmlResult.path("item").asValue().asValue().asIntArray();

 assertArrayEquals(jsonList.toArray(), xmlList.toArray());

 assertEquals(new QName("rootElement"), ((ToXmlGenerator) xmlParser.getSerializerProvider().getGenerator()).getNextName());
 }

 @Test
 public void testSerializationWithRootNameAndEmptyArray() throws Exception {
 ObjectMapper mapper = new ObjectMapper(new XMLFactory());
 ObjectNode node = mapper.createObjectNode();
 node.put("rootElement", mapper.valueToTree(new int[] {}));
 String xml = mapper.writerForRootValue(node)
 .withRootName("rootElement")
 .writeValueAsString();

 JsonNode jsonNode = mapper.readTree(node.getRawValue());
 StringReader jsonReader = new StringReader(jsonNode.getRawValue());
 JsonParser jsonParser = mapper.getFactory().createParser(jsonReader);
 JsonParser xmlParser = mapper.getFactory().createParser(new StringReader(xml));

 JsonNode jsonResult = jsonParser.readValueAs(JsonNode.class);
 JsonNode xmlResult = xmlParser.readValueAs(JsonNode.class);

 assertEquals(jsonResult, xmlResult);

 List<Integer> jsonList = jsonResult.path("items").asValue().asIntArray();
 List<Integer> xmlList = xmlResult.path("item").asValue().asValue().asIntArray();

 assertArrayEquals(new int[0], jsonList.toArray());
 assertArrayEquals(new int[0], xmlList.toArray());

 assertEquals(new QName("rootElement"), ((ToXmlGenerator) xmlParser.getSerializerProvider().getGenerator()).getNextName());
 }
}

class Person {
 private final String name;

 Person(String name) {
 this.name = name;
}
}

class XMLFactory extends SimpleModule {
 @Override
 public void setupModule(SerializationConfig config) {
 addSerializer(Person.class, new JsonSerializer<Person>() {
 @Override
 public void serialize(Person value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeStartElement("person");
 gen.writeFieldName("name");
 gen.writeString(value.name);
 gen.writeEndElement();
 }
 });
}
}