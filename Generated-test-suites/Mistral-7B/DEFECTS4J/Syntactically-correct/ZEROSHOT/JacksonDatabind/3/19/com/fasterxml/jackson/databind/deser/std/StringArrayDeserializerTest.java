package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for {@link StringArrayDeserializer}
 */
public class StringArrayDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDefaultSerialization() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] input = {"test1", "test2", null};
 JsonNode node = MAPPER.valueToTree(input);
 String[] output = deserializer.deserialize(MAPPER.treeToValue(node, String[].class), new TestContext());
 assertArrayEquals(input, output);
 }

 @Test
 public void testCustomSerialization() throws IOException {
 JsonDeserializer<String> stringDeserializer = new StringDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
 String[] input = {"test1", "test2", null};
 JsonNode node = MAPPER.valueToTree(input);
 String[] output = deserializer.deserialize(MAPPER.treeToValue(node, String[].class), new TestContext());
 assertArrayEquals(input, output);
 }

 @Test
 public void testEmptyArray() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonNode node = MAPPER.valueToTree(new Object());
 String[] output = deserializer.deserialize(MAPPER.treeToValue(node, String[].class), new TestContext());
 assertArrayEquals(new String[0], output);
 }

 @Test
 public void testNullDeserialization() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonNode node = MAPPER.valueToTree(JsonNULL.getInstance());
 String[] output = deserializer.deserialize(MAPPER.treeToValue(node, String[].class), new TestContext());
 assertArrayEquals(new String[0], output);
 }

 @Test
 public void testEmptyStringDeserialization() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonNode node = MAPPER.valueToTree("");
 String[] output = deserializer.deserialize(MAPPER.treeToValue(node, String[].class), new TestContext());
 assertArrayEquals(new String[0], output);
 }

 @Test
 public void testSingleValueDeserialization() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonNode node = MAPPER.valueToTree("test");
 String[] output = deserializer.deserialize(MAPPER.treeToValue(node, String[].class), new TestContext());
 assertEquals(new String[]{"test"}, output);
 }

 @Test
 public void testNonArrayDeserialization() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonNode node = MAPPER.valueToTree(MAPPER.valueToTree(1));
 String[] output = deserializer.deserialize(MAPPER.treeToValue(node, String[].class), new TestContext());
 assertEquals(new String[]{"1"}, output);
 }

 @Test(expected = JsonMappingException.class)
 public void testNonArrayDeserialization_exception() throws IOException {
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 JsonNode node = MAPPER.valueToTree(MAPPER.valueToTree(1));
 deserializer.deserialize(MAPPER.treeToValue(node, String[].class), new TestContext());
 }

 @Test
 public void testContextualDeserialization() throws IOException {
 String[] input = {"test1", "test2", null};
 JsonNode node = MAPPER.valueToTree(input);
 StringDeserializer stringDeserializer = new StringDeserializer();
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule()
 .addDeserializer(String[].class, new ContextualStringArrayDeserializer(stringDeserializer)));
 StringArrayDeserializer deserializer = objectMapper.readValue(node.traverse(), StringArrayDeserializer.class);
 String[] output = deserializer.deserialize(new JsonParser(node), new TestContext());
 assertArrayEquals(input, output);
 }

 @Test
 public void testContextualDeserialization_nullElement() throws IOException {
 String[] input = {"test1", null, "test3"};
 JsonNode node = MAPPER.valueToTree(input);
 StringDeserializer stringDeserializer = new StringDeserializer();
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule()
 .addDeserializer(String[].class, new ContextualStringArrayDeserializer(stringDeserializer)));
 StringArrayDeserializer deserializer = objectMapper.readValue(node.traverse(), StringArrayDeserializer.class);
 String[] output = deserializer.deserialize(new JsonParser(node), new TestContext());
 assertArrayEquals(input, output);
 }

 @Test
 public void testCustomElementDeserialization() throws IOException {
 String[] input = {"test1", "test2", null};
 JsonNode node = MAPPER.valueToTree(input);
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule()
 .addDeserializer(String.class, new CustomStringDeserializer()));
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 String[] output = deserializer.deserialize(new JsonParser(node), new TestContext());
 assertArrayEquals(input, output);
 }

 @Test
 public void testCustomElementDeserialization_nullElement() throws IOException {
 String[] input = {"test1", null, "test3"};
 JsonNode node = MAPPER.valueToTree(input);
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.registerModule(new SimpleModule()
 .addDeserializer(String.class, new CustomStringDeserializer()));
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 StringArrayDeserializer customDeserializer = new StringArrayDeserializer(new CustomStringDeserializer());
 String[] output = deserializer.deserialize(new JsonParser(node), new TestContext());
 assertArrayEquals(input, output);
 String[] customOutput = customDeserializer.deserialize(new JsonParser(node), new TestContext());
 assertArrayEquals(input, customOutput);
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectBuffer leaseObjectBuffer(int sizeHint) {
 return new ObjectBuffer(sizeHint);
 }
 }

 private static class ContextualStringArrayDeserializer
 extends StdDeserializer<String[]>
 implements ContextualDeserializer {
 private final JsonDeserializer<String> _elementDeserializer;

 public ContextualStringArrayDeserializer(JsonDeserializer<String> deserializer) {
 super(String[].class);
 _elementDeserializer = deserializer;
 }

 @Override
 public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
 return _elementDeserializer;
 }
 }

 @JsonDeserialize(using = CustomStringDeserializer.class)
 private static class CustomStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getText().toUpperCase();
 }
 }
}