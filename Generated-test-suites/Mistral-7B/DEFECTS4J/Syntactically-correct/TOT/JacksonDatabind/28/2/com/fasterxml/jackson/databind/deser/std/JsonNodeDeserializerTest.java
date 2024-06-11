package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class JsonNodeDeserializerTest {

 @BeforeAll
 static void setUpClass() {
 // Set up any static resources or initializations if needed
 }

 @AfterAll
 static void tearDownClass() {
 // Tear down any static resources or initializations if needed
 }

 @BeforeEach
 void setUp() {
 // Set up any instance variables or initializations for each test
 }

 @AfterEach
 void tearDown() {
 // Tear down any instance variables or resources used in each test
 }

 @Test
 void testGetDeserializerNullNodeClass() {
 assertNull(JsonNodeDeserializer.getDeserializer(null));
 }

 @Test
 void testGetDeserializerObjectNode() {
 JsonDeserializer<ObjectNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 assertInstanceOf(ObjectDeserializer.class, deserializer);
 }

 @Test
 void testGetDeserializerArrayNode() {
 JsonDeserializer<ArrayNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 assertInstanceOf(ArrayDeserializer.class, deserializer);
 }

 @Test
 void testGetDeserializerNonSupportedNodeType() {
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(String.class);
 assertNotNull(deserializer);
 }

 @Test
 void testDeserializeNull() throws IOException {
 JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertEquals(JsonNode.Value.nullValue(), node);
 }

 @Test
 void testDeserializeEmptyObject() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{}");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertInstanceOf(ObjectNode.class, node);
 assertEquals(0, node.size());
 }

 @Test
 void testDeserializeEmptyArray() throws IOException {
 JsonParser parser = new JsonFactory().createParser("[{}]");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertInstanceOf(ArrayNode.class, node);
 assertEquals(0, node.size());
 }

 @Test
 void testDeserializeSimpleObject() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{\"name\":\"John\", \"age\":30}");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertInstanceOf(ObjectNode.class, node);
 assertEquals("John", node.get("name").asText());
 assertEquals(30, node.get("age").asInt());
 }

 @Test
 void testDeserializeSimpleArray() throws IOException {
 JsonParser parser = new JsonFactory().createParser("[1, 2, 3]");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertInstanceOf(ArrayNode.class, node);
 assertEquals(3, node.size());
 assertEquals(1, node.get(0).asInt());
 assertEquals(2, node.get(1).asInt());
 assertEquals(3, node.get(2).asInt());
 }

 @Test
 void testDeserializeComplexObject() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{" +
 "\"name\":\"John\"," +
 "\"age\":30," +
 "\"address\":{" +
 "\"street\":\"123 Main St\"," +
 "\"city\":\"Anytown\"," +
 "\"state\":\"CA\"," +
 "\"zip\":\"12345\"" +
 "}," +
 "\"phoneNumbers\":[" +
 "{" +
 "\"type\":\"home\"," +
 "\"number\":\"555-1234\"" +
 "}," +
 "{" +
 "\"type\":\"work\"," +
 "\"number\":\"555-5678\"" +
 "}," +
 "{" +
 "\"type\":\"mobile\"," +
 "\"number\":\"555-9012\"" +
 "}" +
 "]" +
 "}");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertInstanceOf(ObjectNode.class, node);
 assertEquals("John", node.get("name").asText());
 assertEquals(30, node.get("age").asInt());
 assertInstanceOf(ObjectNode.class, node.get("address"));
 assertEquals("123 Main St", node.get("address").get("street").asText());
 assertEquals("Anytown", node.get("address").get("city").asText());
 assertEquals("CA", node.get("address").get("state").asText());
 assertEquals("12345", node.get("address").get("zip").asText());
 assertEquals(3, node.get("phoneNumbers").size());
 assertInstanceOf(ObjectNode.class, node.get("phoneNumbers").get(0));
 assertEquals("home", node.get("phoneNumbers").get(0).get("type").asText());
 assertEquals("555-1234", node.get("phoneNumbers").get(0).get("number").asText());
 assertInstanceOf(ObjectNode.class, node.get("phoneNumbers").get(1));
 assertEquals("work", node.get("phoneNumbers").get(1).get("type").asText());
 assertEquals("555-5678", node.get("phoneNumbers").get(1).get("number").asText());
 assertInstanceOf(ObjectNode.class, node.get("phoneNumbers").get(2));
 assertEquals("mobile", node.get("phoneNumbers").get(2).get("type").asText());
 assertEquals("555-9012", node.get("phoneNumbers").get(2).get("number").asText());
 }

 @Test
 void testDeserializeComplexArray() throws IOException {
 JsonParser parser = new JsonFactory().createParser("[{" +
 "\"name\":\"John\"," +
 "\"age\":30," +
 "\"address\":{" +
 "\"street\":\"123 Main St\"," +
 "\"city\":\"Anytown\"," +
 "\"state\":\"CA\"," +
 "\"zip\":\"12345\"" +
 "}" +
 "}," +
 "{" +
 "\"name\":\"Jane\"," +
 "\"age\":25," +
 "\"address\":{" +
 "\"street\":\"456 Elm St\"," +
 "\"city\":\"Othertown\"," +
 "\"state\":\"NY\"," +
 "\"zip\":\"67890\"" +
 "}" +
 "}]");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertInstanceOf(ArrayNode.class, node);
 assertEquals(2, node.size());
 assertInstanceOf(ObjectNode.class, node.get(0));
 assertEquals("John", node.get(0).get("name").asText());
 assertEquals(30, node.get(0).get("age").asInt());
 assertInstanceOf(ObjectNode.class, node.get(0).get("address"));
 assertEquals("123 Main St", node.get(0).get("address").get("street").asText());
 assertEquals("Anytown", node.get(0).get("address").get("city").asText());
 assertEquals("CA", node.get(0).get("address").get("state").asText());
 assertEquals("12345", node.get(0).get("address").get("zip").asText());
 assertInstanceOf(ObjectNode.class, node.get(1));
 assertEquals("Jane", node.get(1).get("name").asText());
 assertEquals(25, node.get(1).get("age").asInt());
 assertInstanceOf(ObjectNode.class, node.get(1).get("address"));
 assertEquals("456 Elm St", node.get(1).get("address").get("street").asText());
 assertEquals("Othertown", node.get(1).get("address").get("city").asText());
 assertEquals("NY", node.get(1).get("address").get("state").asText());
 assertEquals("67890", node.get(1).get("address").get("zip").asText());
 }

 @Test
 void testDeserializeEmptyObjectWithNullValueFeature() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{}");
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonNode node = mapper.readTree(parser);
 assertInstanceOf(ObjectNode.class, node);
 assertNull(node);
 }

 @Test
 void testDeserializeEmptyArrayWithNullValueFeature() throws IOException {
 JsonParser parser = new JsonFactory().createParser("[{}]");
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonNode node = mapper.readTree(parser);
 assertInstanceOf(ArrayNode.class, node);
 assertEquals(0, node.size());
 }

 @Test
 void testDeserializeNullValue() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{ \"name\": null }");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertInstanceOf(ObjectNode.class, node);
 assertNull(node.get("name"));
 }

 @Test
 void testDeserializeInvalidJson() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{ \"name\": \"John\" { \"age\": 30 }");
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertNull(node);
 }

 @Test
 void testDeserializeReadPastEnd() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{ \"name\": \"John\" }");
 parser.nextToken();
 parser.nextToken(); // read past end
 JsonNode node = JsonNodeDeserializer.instance.deserialize(parser, new TestContext());
 assertNull(node);
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public JsonNodeFactory getNodeFactory() {
 return new StandardJsonNodeFactory();
 }
 }
}