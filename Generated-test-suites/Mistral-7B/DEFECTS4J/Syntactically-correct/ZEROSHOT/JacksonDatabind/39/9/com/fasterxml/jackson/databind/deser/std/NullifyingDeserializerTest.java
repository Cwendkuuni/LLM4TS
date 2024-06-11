package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

/**
 * Tests for {@link NullifyingDeserializer}.
 */
public class NullifyingDeserializerTest {

 private static ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserialize() throws Exception {
 Object input = new Object();
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("test", input);

 StdSerializer<Object> standardSerializer = new StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
 jgen.writeObject(value);
 }
 };

 standardSerializer.setNullValueSerializer(NullifyingDeserializer.instance);
 objectMapper.setSerializerForClass(Object.class, standardSerializer);

 JsonParser jsonParser = objectMapper.getFactory().createParser(node.toString());
 Object result = NullifyingDeserializer.instance.deserialize(jsonParser, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType() throws Exception {
 Object input = new Object();
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("test", input);

 TypeDeserializer typeDeserializer = new StdTypeDeserializer();
 Object result = NullifyingDeserializer.instance.deserializeWithType(objectMapper.createParser(node.toString()), new TestContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_whenStartArrayOrObject() throws Exception {
 Object input = new Object();
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("test", JsonNodeFactory.instance.arrayNode(new Object[]{input}));

 TypeDeserializer typeDeserializer = new StdTypeDeserializer();
 Object result = NullifyingDeserializer.instance.deserializeWithType(objectMapper.createParser(node.toString()), new TestContext(), typeDeserializer);
 assertNull(result);

 node = JsonNodeFactory.instance.objectNode();
 node.put("test", JsonNodeFactory.instance.objectNode());
 result = NullifyingDeserializer.instance.deserializeWithType(objectMapper.createParser(node.toString()), new TestContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType_whenFieldName() throws Exception {
 Object input = new Object();
 JsonNode node = JsonNodeFactory.instance.objectNode();
 node.put("test", JsonNodeFactory.instance.objectNode().set("name", JsonNodeFactory.instance.textNode("Test")));

 TypeDeserializer typeDeserializer = new StdTypeDeserializer();
 Object result = NullifyingDeserializer.instance.deserializeWithType(objectMapper.createParser(node.toString()), new TestContext(), typeDeserializer);
 assertNotNull(result);
 }

 /**
 * Test context for JUnit tests.
 */
 private static class TestContext extends DeserializationContext {
 @Override
 public final BeanDescription _valueDescriber() {
 throw new UnsupportedOperationException();
 }

 @Override
 public final JavaType _currentType() {
 throw new UnsupportedOperationException();
 }

 @Override
 public final void handleUnexpectedToken(JsonParser p, int tokenId) throws IOException {
 // Do nothing as we don't care about unexpected tokens.
 }

 @Override
 public final void handleMissingValue(JsonParser p, DeserializationContext ctxt) throws IOException {
 // Do nothing as we don't care about missing values.
 }
 }

}