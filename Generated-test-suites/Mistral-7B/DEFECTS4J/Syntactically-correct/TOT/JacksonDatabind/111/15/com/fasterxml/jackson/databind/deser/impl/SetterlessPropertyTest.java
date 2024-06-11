package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

 private ObjectMapper mapper;
 private SetterlessProperty property;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 property = new SetterlessProperty(
 new BeanPropertyDefinition(JsonTestClass.class.getDeclaredField("myList"),
 JsonTypeFactory.numberType(), null),
 mapper.getTypeFactory(), null, null,
 new AnnotatedMethod(JsonTestClass.class.getMethod("getMyList"))) {
 // Do nothing
 };
 }

 @Test
 public void testSetterlessPropertySerialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("myList", mapper.valueToTree(new int[] {1, 2, 3}));
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(json);
 JsonNode root = mapper.readTree(parser);

 Object result = mapper.readValue(root, JsonTestClass.class);
 assertEquals(Arrays.asList(1, 2, 3), result.getMyList());
 }

 @Test
 public void testSetterlessPropertyDeserialization() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("myList", mapper.valueToTree(new int[] {4, 5, 6}));
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(json);
 parser.nextToken(); // Skip [START_OBJECT]
 property.deserializeAndSet(parser, mapper.getDeserializationConfig(), new JsonTestClass());
 JsonNode currentNode = parser.readValueAsTree();
 assertEquals(Arrays.asList(4, 5, 6), currentNode.path("myList"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetterlessPropertySetter() throws Exception {
 Object testInstance = new JsonTestClass();
 property.set(testInstance, new Object());
 fail("Should not call 'set' method on SetterlessProperty");
 }

 @Test
 public void testNullValue() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("myList", mapper.valueToTree(null));
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(json);
 parser.nextToken(); // Skip [START_OBJECT]
 property.deserializeAndSet(parser, mapper.getDeserializationConfig(), new JsonTestClass());
 assertNull(parser.currentNode().path("myList"));
 }

 @Test
 public void testEmptyValue() throws Exception {
 ObjectNode input = mapper.createObjectNode();
 input.put("myList", mapper.createNullNode());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(json);
 parser.nextToken(); // Skip [START_OBJECT]
 property.deserializeAndSet(parser, mapper.getDeserializationConfig(), new JsonTestClass());
 assertTrue(parser.currentNode().path("myList").isMissingNode());
 }

 @Test
 public void testDeserializationFeatureDisableFailOnEmpty() throws Exception {
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
 ObjectNode input = mapper.createObjectNode();
 input.put("myList", mapper.createNullNode());
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(json);
 parser.nextToken(); // Skip [START_OBJECT]
 property.deserializeAndSet(parser, mapper.getDeserializationConfig(), new JsonTestClass());
 assertNotNull(parser.currentNode().path("myList"));
 }

 @Test
 public void testTypeDeserializer() throws Exception {
 ObjectMapper typeMapper = new ObjectMapper();
 TypeDeserializer myTypeDeserializer = new SimpleTypeDeserializer();
 JsonNode input = mapper.createObjectNode();
 input.put("myList", mapper.valueToTree(new int[] {7, 8, 9}));
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 JsonParser parser = mapper.getFactory().createParser(json);
 JsonDeserializer<JsonTestClass> deserializer = mapper.getFactory().findValueDeserializer(JsonTestClass.class, property.getType());
 parser.findAndValidateCurrentToken(JsonToken.START_OBJECT);
 property = new SetterlessProperty(property, deserializer, null) {
 @Override
 public TypeDeserializer _valueTypeDeserializer = myTypeDeserializer;
 };
 property.deserializeAndSet(parser, mapper.getDeserializationConfig(), new JsonTestClass());
 assertEquals(Arrays.asList(7, 8, 9), property.getValue());
 }
}

class SimpleTypeDeserializer extends StdTypeDeserializer<Integer[]> {
 @Override
 public Integer[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new Integer[] {p.nextIntValue(), p.nextIntValue(), p.nextIntValue()};
 }
}

class JsonTestClass {
 private List<Integer> myList;

 public List<Integer> getMyList() {
 return myList;
 }
}