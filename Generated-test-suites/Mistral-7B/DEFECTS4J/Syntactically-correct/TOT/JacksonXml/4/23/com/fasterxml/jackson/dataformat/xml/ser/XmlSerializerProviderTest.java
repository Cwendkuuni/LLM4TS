package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private static final ObjectMapper MAPPER = new ObjectMapper(new XmlFactory());

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 ObjectNode rootNode = MAPPER.createObjectNode();
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(MAPPER.getXmlRootNameLookup());
 DefaultSerializerProvider defaultProvider = new DefaultSerializerProvider();

 JsonSerializer<Object> xmlSerializer = xmlProvider.findValueSerializer(Object.class, true, null);
 JsonSerializer<Object> defaultSerializer = defaultProvider.findValueSerializer(Object.class, true, null);

 TokenBuffer tokenBuffer = new TokenBuffer(new StaxWriter());
 RootNameResolver rootNameResolver = StaxUtil.getRootNameResolver(tokenBuffer.getRootElementWriter());

 try {
 xmlSerializer.serialize(null, tokenBuffer, xmlProvider);
 DefaultSerializerProvider.SerializerProviderContext context = new DefaultSerializerProvider.SerializerProviderContext(tokenBuffer.getRootElementWriter(), null, null, null, new JavaType(Object.class), null);
 defaultSerializer.serialize(null, tokenBuffer, context);

 String xmlSerializerResult = tokenBuffer.asXml();
 String defaultSerializerResult = defaultProvider.getRootValueSerializer(Object.class).getSerializerProperties().getSerializationConfig().getDefaultRootValueSerializer().serializeValues(new Object[]{null}, new DefaultSerializerProvider(), new JsonGenerator(new StaxWriter()));

 assertEquals(xmlSerializerResult, defaultSerializerResult);
 } finally {
 tokenBuffer.close();
 }
 }

 @Test
 public void testSerializeObjectWithRootName() throws JsonProcessingException {
 ObjectNode rootNode = MAPPER.createObjectNode();
 rootNode.put("name", "John Doe");
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(MAPPER.getXmlRootNameLookup());
 DefaultSerializerProvider defaultProvider = new DefaultSerializerProvider();

 JsonSerializer<Object> xmlSerializer = xmlProvider.findValueSerializer(Object.class, true, null);
 JsonSerializer<Object> defaultSerializer = defaultProvider.findValueSerializer(Object.class, true, null);

 TokenBuffer tokenBuffer = new TokenBuffer(new StaxWriter());
 RootNameResolver rootNameResolver = StaxUtil.getRootNameResolver(tokenBuffer.getRootElementWriter());

 try {
 xmlSerializer.serialize(rootNode, tokenBuffer, xmlProvider);
 DefaultSerializerProvider.SerializerProviderContext context = new DefaultSerializerProvider.SerializerProviderContext(tokenBuffer.getRootElementWriter(), null, null, null, new JavaType(Object.class), null);
 defaultSerializer.serialize(rootNode, tokenBuffer, context);

 String xmlSerializerResult = tokenBuffer.asXml();
 String defaultSerializerResult = defaultProvider.getRootValueSerializer(Object.class).getSerializerProperties().getSerializationConfig().getDefaultRootValueSerializer().serializeValues(new Object[]{rootNode}, new DefaultSerializerProvider(), new JsonGenerator(new StaxWriter()));

 assertEquals(xmlSerializerResult, defaultSerializerResult);
 } finally {
 tokenBuffer.close();
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeObjectWithInvalidRootName() throws JsonProcessingException {
 ObjectNode rootNode = MAPPER.createObjectNode();
 rootNode.put("@name", "John Doe");
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(MAPPER.getXmlRootNameLookup());
 DefaultSerializerProvider defaultProvider = new DefaultSerializerProvider();

 JsonSerializer<Object> xmlSerializer = xmlProvider.findValueSerializer(Object.class, true, null);
 JsonSerializer<Object> defaultSerializer = defaultProvider.findValueSerializer(Object.class, true, null);

 TokenBuffer tokenBuffer = new TokenBuffer(new StaxWriter());
 RootNameResolver rootNameResolver = StaxUtil.getRootNameResolver(tokenBuffer.getRootElementWriter());

 try {
 xmlSerializer.serialize(rootNode, tokenBuffer, xmlProvider);
 DefaultSerializerProvider.SerializerProviderContext context = new DefaultSerializerProvider.SerializerProviderContext(tokenBuffer.getRootElementWriter(), null, null, null, new JavaType(Object.class), null);
 defaultSerializer.serialize(rootNode, tokenBuffer, context);

 fail("JsonMappingException expected but not thrown");
 } catch (JsonMappingException e) {
 // expected
 } finally {
 tokenBuffer.close();
 }
 }
}