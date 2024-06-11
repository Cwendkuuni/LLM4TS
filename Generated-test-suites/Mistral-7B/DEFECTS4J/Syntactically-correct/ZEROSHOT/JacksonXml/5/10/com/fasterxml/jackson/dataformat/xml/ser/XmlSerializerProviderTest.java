package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 private SerializerFactory serializerFactory;
 private ObjectMapper objectMapper;
 private JsonSerializer<Object> nullSerializer;
 private JsonSerializer<Object> stringSerializer;
 private JsonSerializer<Object> intSerializer;
 private JsonSerializer<Object> arraySerializer;
 private SerializationConfig config;
 private JavaType javaType;
 private QName rootName;
 private ToXmlGenerator toXmlGenerator;
 private JsonGenerator jsonGenerator;
 private DefaultSerializerProvider defaultSerializerProvider;

 @Before
 public void setup() throws Exception {
 serializerFactory = mock(SerializerFactory.class);
 objectMapper = mock(ObjectMapper.class);
 xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(objectMapper));
 config = new SerializationConfig(objectMapper.getFactory());
 javaType = JavaType.construct(int[].class, int.class);
 rootName = new QName("testRoot");
 toXmlGenerator = mock(ToXmlGenerator.class);
 when(toXmlGenerator.isInRoot()).thenReturn(true);
 when(toXmlGenerator.writeStartObject()).thenReturn(toXmlGenerator);
 when(toXmlGenerator.writeFieldName(String.class, "item")).thenReturn(toXmlGenerator);
 when(toXmlGenerator.initGenerator()).thenReturn(toXmlGenerator);
 when(toXmlGenerator.getStaxWriter()).thenReturn(mock(XMLStreamWriter.class));
 when(objectMapper.findValueSerializer(int[].class, null)).thenReturn(arraySerializer);
 when(objectMapper.findValueSerializer(int.class, null)).thenReturn(intSerializer);
 when(objectMapper.findValueSerializer(String.class, null)).thenReturn(stringSerializer);
 when(objectMapper.findValueSerializer(Object.class, null)).thenReturn(nullSerializer);
 when(serializerFactory.findValueSerializer(Object.class, null)).thenReturn(nullSerializer);
 when(objectMapper.getSerializerProvider()).thenReturn(defaultSerializerProvider);

 defaultSerializerProvider = new DefaultSerializerProvider();
 defaultSerializerProvider.setSerializerFactory(serializerFactory);
 xmlSerializerProvider.setConfig(config);
 xmlSerializerProvider.setSerializerFactory(serializerFactory);
 xmlSerializerProvider.init();
 }

 @Test
 public void testSerializeValueWithNull() throws Exception {
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.isEnabled()).thenReturn(true);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 xmlSerializerProvider.serializeValue(gen, null);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("null");
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithString() throws Exception {
 String value = "test";
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.isEnabled()).thenReturn(true);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.writeStartObject()).thenReturn(toXmlGenerator);
 when(gen.writeEndObject()).thenReturn(toXmlGenerator);
 when(gen.writeString(String.class, "testString")).thenReturn(toXmlGenerator);
 xmlSerializerProvider.serializeValue(gen, value);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("testString");
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithInt() throws Exception {
 int value = 5;
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.isEnabled()).thenReturn(true);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.writeStartObject()).thenReturn(toXmlGenerator);
 when(gen.writeEndObject()).thenReturn(toXmlGenerator);
 when(gen.writeNumber(int.class, 5)).thenReturn(toXmlGenerator);
 xmlSerializerProvider.serializeValue(gen, value);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("item");
 verify(gen).writeNumber(int.class, 5);
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithArray() throws Exception {
 int[] value = {1, 2, 3};
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.isEnabled()).thenReturn(true);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.writeStartObject()).thenReturn(toXmlGenerator);
 when(gen.writeEndObject()).thenReturn(toXmlGenerator);
 when(gen.writeFieldName(String.class, "item")).thenReturn(toXmlGenerator);
 when(gen.writeStartArray()).thenReturn(toXmlGenerator);
 when(gen.writeEndArray()).thenReturn(toXmlGenerator);
 when(gen.writeNumber(int.class, 1)).thenReturn(toXmlGenerator);
 when(gen.writeNumber(int.class, 2)).thenReturn(toXmlGenerator);
 when(gen.writeNumber(int.class, 3)).thenReturn(toXmlGenerator);
 xmlSerializerProvider.serializeValue(gen, value);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("item");
 verify(gen).writeStartArray();
 verify(gen, times(3)).writeNumber(int.class, anyInt());
 verify(gen).writeEndArray();
 verify(gen).writeEndObject();
 }

 @Test
 public void testXmlSerializerProviderCopy() throws Exception {
 XmlSerializerProvider xmlSerializerProvider1 = new XmlSerializerProvider(new XmlRootNameLookup(objectMapper));
 XmlSerializerProvider xmlSerializerProvider2 = xmlSerializerProvider1.copy();
 assertEquals(xmlSerializerProvider1, xmlSerializerProvider2);
 }

 @Test
 public void testXmlSerializerProviderCreateInstance() throws Exception {
 XmlSerializerProvider xmlSerializerProvider1 = new XmlSerializerProvider(new XmlRootNameLookup(objectMapper), config, serializerFactory);
 XmlSerializerProvider xmlSerializerProvider2 = xmlSerializerProvider1.createInstance(config, serializerFactory);
 assertEquals(xmlSerializerProvider1, xmlSerializerProvider2);
 }

 @Test
 public void testXmlSerializerProviderCreateInstanceFromXmlSerializerProvider() throws Exception {
 XmlSerializerProvider xmlSerializerProvider1 = new XmlSerializerProvider(new XmlRootNameLookup(objectMapper));
 XmlSerializerProvider xmlSerializerProvider2 = new XmlSerializerProvider(xmlSerializerProvider1);
 assertEquals(xmlSerializerProvider1, xmlSerializerProvider2);
 }

}