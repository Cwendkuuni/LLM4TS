package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.test.Foo"), mapper.getTypeIdResolver(), "_class", true, Foo.class);
 ObjectNode wrapperNode = mapper.createObjectNode();
 wrapperNode.put("_class", "com.test.Bar");
 ObjectNode valueNode = mapper.createObjectNode();
 valueNode.put("name", "test");
 wrapperNode.set("value", valueNode);
 JsonNode jsonNode = mapper.readTree(wrapperNode.toString());
 JsonParser jp = mapper.treeToValues(jsonNode, new TypeReference<ObjectNode>() {});
 Object result = deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
 assertEquals(Bar.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.test.Foo"), mapper.getTypeIdResolver(), "_class", true, Foo.class);
 ObjectNode wrapperNode = mapper.createObjectNode();
 wrapperNode.put("_class", "com.test.Bar");
 ObjectNode valueNode = mapper.createObjectNode();
 valueNode.put("name", "test");
 ObjectNode[] array = { wrapperNode };
 JsonNode jsonNode = mapper.valueToTree(array);
 JsonParser jp = mapper.treeToValues(jsonNode, new TypeReference<ObjectNode[]>() {});
 Object result = deserializer.deserializeTypedFromArray(jp, mapper.getDeserializationContext());
 assertEquals(Bar[].class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.test.Foo"), mapper.getTypeIdResolver(), "_class", true, Foo.class);
 String typeId = "com.test.Bar";
 ObjectNode wrapperNode = mapper.createObjectNode();
 wrapperNode.put("_class", typeId);
 JsonNode jsonNode = wrapperNode;
 JsonParser jp = mapper.treeToValues(jsonNode, new TypeReference<ObjectNode>() {});
 Object result = deserializer.deserializeTypedFromScalar(jp, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.test.Foo"), mapper.getTypeIdResolver(), "_class", true, Foo.class);
 String typeId = "com.test.Bar";
 ObjectNode wrapperNode = mapper.createObjectNode();
 wrapperNode.put("_class", typeId);
 JsonNode jsonNode = wrapperNode;
 JsonParser jp = mapper.treeToValues(jsonNode, new TypeReference<ObjectNode>() {});
 Object result = deserializer.deserializeTypedFromAny(jp, mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testInvalidWrapperObject() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.test.Foo"), mapper.getTypeIdResolver(), "_class", true, Foo.class);
 ObjectNode wrapperNode = mapper.createObjectNode();
 wrapperNode.put("_class", "com.test.Bar");
 wrapperNode.put("invalid", "value");
 JsonNode jsonNode = wrapperNode;
 JsonParser jp = mapper.treeToValues(jsonNode, new TypeReference<ObjectNode>() {});
 deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
 }

 @Test(expected = IOException.class)
 public void testMissingTypeIdWrapperObject() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.test.Foo"), mapper.getTypeIdResolver(), "_class", true, Foo.class);
 ObjectNode wrapperNode = mapper.createObjectNode();
 wrapperNode.put("value", mapper.createObjectNode().put("name", "test"));
 JsonNode jsonNode = wrapperNode;
 JsonParser jp = mapper.treeToValues(jsonNode, new TypeReference<ObjectNode>() {});
 deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
 }

 @Test
 public void testMergeTypeIdWrapperObject() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("com.test.Foo"), mapper.getTypeIdResolver(), "_class", true, Foo.class);
 ObjectNode wrapperNode = mapper.createObjectNode();
 wrapperNode.put("_class", "com.test.Bar");
 wrapperNode.put("_class", "com.test.Baz");
 wrapperNode.set("value", mapper.createObjectNode().put("name", "test"));
 JsonNode jsonNode = wrapperNode;
 JsonParser jp = mapper.treeToValues(jsonNode, new TypeReference<ObjectNode>() {});
 Object result = deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
 assertEquals(Baz.class, result.getClass());
 }
}

class Foo {

}

class Bar extends Foo {

}

class Baz extends Foo {

}