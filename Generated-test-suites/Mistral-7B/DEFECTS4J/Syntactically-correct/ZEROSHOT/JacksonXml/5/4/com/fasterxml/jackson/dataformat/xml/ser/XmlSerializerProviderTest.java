package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.XmlSerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.XmlSerializerProvider.ToXmlGenerator;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private XmlRootNameLookup xmlRootNameLookup;
 private JsonGenerator jsonGenerator;
 private XmlSerializerProvider xmlSerializerProvider;
 private SerializationConfig serializationConfig;
 private DefaultSerializerProvider defaultSerializerProvider;
 private SerializerFactory serializerFactory;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 xmlRootNameLookup = mock(XmlRootNameLookup.class);
 jsonGenerator = mock(JsonGenerator.class);
 serializationConfig = new SerializationConfig(objectMapper.getConfig());
 defaultSerializerProvider = new DefaultSerializerProvider();
 serializerFactory = mock(SerializerFactory.class);
 xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 xmlSerializerProvider.setConfig(serializationConfig);
 xmlSerializerProvider.setFactory(serializerFactory);
 }

 @Test
 public void testCopy() throws JsonProcessingException {
 XmlSerializerProvider newXmlSerializerProvider = xmlSerializerProvider.copy();
 assertSame(xmlSerializerProvider, newXmlSerializerProvider);
 }

 @Test
 public void testCreateInstance() throws JsonProcessingException {
 SerializationConfig configuration = new SerializationConfig(objectMapper.getConfig());
 XmlSerializerProvider newXmlSerializerProvider = xmlSerializerProvider.createInstance(configuration, serializerFactory);
 assertFalse(newXmlSerializerProvider == xmlSerializerProvider);
 }

 @Test
 public void testSerialization() throws JsonProcessingException {
 String value = "test";
 JavaType javaType = objectMapper.getTypeFactory().constructValueType(String.class);
 when(xmlRootNameLookup.findRootName(javaType, serializationConfig)).thenReturn(new QName("test"));
 when(serializerFactory.createSerializer(javaType)).thenReturn(mock(JsonSerializer.class));
 when(jsonGenerator.writeStartObject()).thenReturn(jsonGenerator);
 when(jsonGenerator.writeFieldName("item")).thenReturn(jsonGenerator);
 when(jsonGenerator.writeValue(value)).thenReturn(jsonGenerator);
 when(jsonGenerator.writeEndObject()).thenReturn(jsonGenerator);

 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 xmlSerializerProvider.setConfig(serializationConfig);
 xmlSerializerProvider.setFactory(serializerFactory);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 TokenBuffer tokenBuffer = mock(TokenBuffer.class);
 when(serializerFactory.createGenerator(TokenBuffer.class, serializationConfig)).thenReturn(tokenBuffer);
 when(tokenBuffer.getAsXmlGenerator()).thenReturn(jsonGenerator);

 xmlSerializerProvider.serializeValue(jsonGenerator, value);
 verify(jsonGenerator).writeStartObject();
 verify(jsonGenerator).writeFieldName("item");
 verify(jsonGenerator).writeValue(value);
 verify(jsonGenerator).writeEndObject();
 }

 @Test
 public void testSerializationWithRootName() throws JsonProcessingException {
 String value = "test";
 JavaType javaType = objectMapper.getTypeFactory().constructValueType(String.class);
 when(xmlRootNameLookup.findRootName(javaType, serializationConfig)).thenReturn(new QName("test"));
 when(serializerFactory.createSerializer(javaType)).thenReturn(mock(JsonSerializer.class));
 when(jsonGenerator.writeStartObject()).thenReturn(jsonGenerator);
 when(jsonGenerator.writeFieldName("item")).thenReturn(jsonGenerator);
 when(jsonGenerator.writeValue(value)).thenReturn(jsonGenerator);
 when(jsonGenerator.writeEndObject()).thenReturn(jsonGenerator);

 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 xmlSerializerProvider.setConfig(serializationConfig);
 xmlSerializerProvider.setFactory(serializerFactory);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
 when(serializerFactory.createGenerator(JsonGenerator.class, serializationConfig)).thenReturn(toXmlGenerator);
 when(toXmlGenerator.setNextNameIfMissing(new QName("test"))).thenReturn(false);
 when(toXmlGenerator.initGenerator()).thenReturn(toXmlGenerator);
 when(toXmlGenerator.getStaxWriter()).thenReturn(jsonGenerator);

 xmlSerializerProvider.serializeValue(toXmlGenerator, value);
 verify(jsonGenerator).writeStartObject();
 verify(jsonGenerator).writeFieldName("item");
 verify(jsonGenerator).writeValue(value);
 verify(jsonGenerator).writeEndObject();
 }

 @Test
 public void testSerializationWithNull() throws JsonProcessingException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 xmlSerializerProvider.setConfig(serializationConfig);
 xmlSerializerProvider.setFactory(serializerFactory);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
 when(serializerFactory.createGenerator(JsonGenerator.class, serializationConfig)).thenReturn(toXmlGenerator);
 when(toXmlGenerator.setNextNameIfMissing(null)).thenReturn(false);
 when(toXmlGenerator.initGenerator()).thenReturn(toXmlGenerator);
 when(toXmlGenerator.getStaxWriter()).thenReturn(jsonGenerator);
 when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(null);
 when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(new QName("null"));

 xmlSerializerProvider.serializeValue(toXmlGenerator, null);
 verify(jsonGenerator).writeStartObject();
 verify(jsonGenerator).writeStartElement("null", null);
 verify(jsonGenerator).writeEndObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidGenerator() throws JsonProcessingException {
 String value = "test";
 JavaType javaType = objectMapper.getTypeFactory().constructValueType(String.class);
 when(xmlRootNameLookup.findRootName(javaType, serializationConfig)).thenReturn(new QName("test"));
 when(serializerFactory.createSerializer(javaType)).thenReturn(mock(JsonSerializer.class));
 when(jsonGenerator.writeStartObject()).thenThrow(new IOException());

 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 xmlSerializerProvider.setConfig(serializationConfig);
 xmlSerializerProvider.setFactory(serializerFactory);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 TokenBuffer tokenBuffer = mock(TokenBuffer.class);
 when(serializerFactory.createGenerator(TokenBuffer.class, serializationConfig)).thenReturn(tokenBuffer);
 when(tokenBuffer.getAsXmlGenerator()).thenReturn(jsonGenerator);

 xmlSerializerProvider.serializeValue(jsonGenerator, value);
 }

 @Test
 public void testInitializeWithRootName() throws IOException, XMLStreamException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 XmlSerializerProvider newXmlSerializerProvider = new XmlSerializerProvider(xmlSerializerProvider);

 QName rootName = new QName("test");
 when(xmlSerializerProvider._rootNameLookup.findRootName(Object.class, serializationConfig)).thenReturn(rootName);
 newXmlSerializerProvider._initWithRootName(jsonGenerator, rootName);

 assertSame(rootName, jsonGenerator.getNamespaceURI());
 }

 @Test
 public void testInitializeWithEmptyRootName() throws IOException, XMLStreamException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 XmlSerializerProvider newXmlSerializerProvider = new XmlSerializerProvider(xmlSerializerProvider);

 when(xmlSerializerProvider._rootNameLookup.findRootName(Object.class, serializationConfig)).thenReturn(null);
 when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(null);
 newXmlSerializerProvider._initWithRootName(jsonGenerator, null);

 assertNull(jsonGenerator.getNamespaceURI());
 }

 @Test
 public void testStartRootArray() throws IOException, XMLStreamException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 XmlSerializerProvider newXmlSerializerProvider = new XmlSerializerProvider(xmlSerializerProvider);

 ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
 when(toXmlGenerator.writeStartObject()).thenThrow(new IOException());
 newXmlSerializerProvider._startRootArray(toXmlGenerator, null);

 verify(toXmlGenerator).writeStartObject();
 }
}