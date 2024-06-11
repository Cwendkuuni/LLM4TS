package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlProvider;
 private XmlRootNameLookup xmlRootNameLookup;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 xmlProvider = new XmlSerializerProvider(xmlRootNameLookup = new XmlRootNameLookup());
 objectMapper = new ObjectMapper(new SerializationConfig(xmlProvider));
 }

 @Test
 public void testSerializeValue() throws Exception {
 ObjectNode rootNode = objectMapper.createObjectNode();
 JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeString(value);
 }
 };
 JsonSerializer<Integer> intSerializer = new StdSerializer<Integer>(Integer.class) {
 @Override
 public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeNumber(value);
 }
 };
 xmlProvider.setNullValueSerializer(new StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeFieldName("null");
 gen.writeStartObject();
 gen.writeEndObject();
 }
 });
 xmlProvider.setNullValueSerializer(Integer.class, new StdSerializer<Integer>(Integer.class) {
 @Override
 public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeFieldName("null");
 gen.writeStartObject();
 gen.writeNumber(0);
 gen.writeEndObject();
 }
 });
 JsonNode expectedNullNode = objectMapper.valueToTree("null");
 JsonNode expectedStringNode = objectMapper.valueToTree("Hello World");
 JsonNode expectedIntNode = objectMapper.valueToTree(42);
 JsonSerializer<ObjectNode> objectNodeSerializer = objectMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, false);
 JsonSerializer<Object> defaultSerializer = xmlProvider.getSerializer(Object.class);

 // Test null serialization
 JsonGenerator gen = new TokenBuffer(objectMapper.getFactory());
 JsonNode actualNullNode = testSerializeValue(gen, null, defaultSerializer, Object.class);
 assertEquals(expectedNullNode, actualNullNode);

 // Test string serialization
 gen.reset();
 String testString = "Hello World";
 actualNullNode = testSerializeValue(gen, testString, stringSerializer, String.class);
 assertEquals(expectedStringNode, actualNullNode);

 // Test integer serialization
 gen.reset();
 Integer testInt = 42;
 actualNullNode = testSerializeValue(gen, testInt, intSerializer, Integer.class);
 assertEquals(expectedIntNode, actualNullNode);
 }

 @Test
 public void testSerializeValueWithRootType() throws Exception {
 ObjectNode rootNode = objectMapper.createObjectNode();
 JsonSerializer<ObjectNode> objectNodeSerializer = objectMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, false);
 JsonSerializer<Object> defaultSerializer = xmlProvider.getSerializer(Object.class);

 // Test null serialization
 JsonGenerator gen = new TokenBuffer(objectMapper.getFactory());
 JsonNode actualNullNode = testSerializeValue(gen, null, defaultSerializer, ObjectNode.class, rootNode);
 assertEquals(expectedNullNode, actualNullNode);

 // Test string serialization
 gen.reset();
 String testString = "Hello World";
 actualNullNode = testSerializeValue(gen, testString, stringSerializer, String.class, rootNode);
 assertEquals(expectedStringNode, actualNullNode);

 // Test integer serialization
 gen.reset();
 Integer testInt = 42;
 actualNullNode = testSerializeValue(gen, testInt, intSerializer, Integer.class, rootNode);
 assertEquals(expectedIntNode, actualNullNode);
 }

 private JsonNode testSerializeValue(JsonGenerator gen, Object value, JsonSerializer<?> serializer, Class<?> clazz) throws Exception {
 JsonSerializer<Object> serializerToUse = serializer == null ? xmlProvider.getSerializer(clazz) : serializer;
 xmlProvider.serializeValue(gen, value, serializerToUse);
 return gen.getEncodingWriter().getRootValue();
 }

 private JsonNode testSerializeValue(JsonGenerator gen, Object value, JsonSerializer<?> serializer, Class<?> clazz, JsonNode rootNode) throws Exception {
 JsonSerializer<ObjectNode> objectNodeSerializer = objectMapper.getSerializerProvider().findValueSerializer(ObjectNode.class, false);
 JsonSerializer<Object> serializerToUse = serializer == null ? objectNodeSerializer : serializer;
 xmlProvider.serializeValue(gen, value, serializerToUse, rootNode);
 return gen.getEncodingWriter().getRootValue();
 }

}