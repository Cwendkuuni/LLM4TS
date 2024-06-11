package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.dataformat.xml.DefaultXmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private XmlRootNameLookup rootNameLookup;
 private ObjectMapper mapper;
 private XmlSerializerProvider xmlSerializerProvider;
 private JsonGenerator jsonGeneratorMock;
 private SerializationConfig serializationConfigMock;
 private SerializerFactory serializerFactoryMock;
 private JavaType javaTypeMock;
 private TypeSerializer typeSerializerMock;

 @Before
 public void setup() {
 rootNameLookup = mock(XmlRootNameLookup.class);
 mapper = new DefaultXmlMapper();
 mapper.getSerializerProvider().setRootNameFilter(
 new SimpleFilterProvider().filterMap(new HashMap<PropertyName, SimpleBeanPropertyFilter>() {
 {
 put(PropertyName.construct("testProperty"), new SimpleBeanPropertyFilter("testProperty", null, null, null, null, null, null));
 }
 }));
 serializationConfigMock = mock(SerializationConfig.class);
 when(serializationConfigMock.getRootValueSerializer()).thenReturn(null);
 when(serializationConfigMock.getConfig(ClassUtil.getSimpleName(XmlSerializerProvider.class)))
 .thenReturn(mapper.getSerializationConfig());
 when(serializationConfigMock.getRootName()).thenReturn(null);
 when(serializationConfigMock.getRootName(ClassUtil.getSimpleName(XmlSerializerProvider.class)))
 .thenReturn(new QName("rootName"));
 when(serializationConfigMock.getRootName(eq(XML_ROOT_TYPE)))
 .thenReturn(new QName("xmlRootType"));
 serializerFactoryMock = mock(SerializerFactory.class);
 jsonGeneratorMock = mock(JsonGenerator.class);
 when(jsonGeneratorMock.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(jsonGeneratorMock.setDefaultNamespace(anyString()))
 .thenReturn(jsonGeneratorMock);
 when(jsonGeneratorMock.setPropertyFilter(any()))
 .thenReturn(jsonGeneratorMock);
 javaTypeMock = mock(JavaType.class);
 typeSerializerMock = mock(TypeSerializer.class);
 }

 @Test
 public void testXmlSerializerProvider() throws JsonProcessingException, IOException {
 // Test default constructor
 XmlSerializerProvider xmlSerializerProviderDefault = new XmlSerializerProvider(null);
 assertEquals(XmlSerializerProvider.class, xmlSerializerProviderDefault.getClass());
 assertEquals(DefaultSerializerProvider.class, xmlSerializerProviderDefault.getProviderBase());
 assertNull(xmlSerializerProviderDefault._rootNameLookup);

 // Test constructor with XmlRootNameLookup
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 assertEquals(XmlSerializerProvider.class, xmlSerializerProvider.getClass());
 assertEquals(DefaultSerializerProvider.class, xmlSerializerProvider.getProviderBase());
 assertEquals(rootNameLookup, xmlSerializerProvider._rootNameLookup);

 // Test constructor with source, config and factory
 XmlSerializerProvider xmlSerializerProviderSource = new XmlSerializerProvider(
 xmlSerializerProvider, serializationConfigMock, serializerFactoryMock);
 assertEquals(XmlSerializerProvider.class, xmlSerializerProviderSource.getClass());
 assertEquals(xmlSerializerProvider, xmlSerializerProviderSource.getProviderBase());
 assertEquals(rootNameLookup, xmlSerializerProviderSource._rootNameLookup);

 // Test serializeValue method with null value
 when(jsonGeneratorMock.writeNull()).thenCallRealMethod();
 xmlSerializerProvider.serializeValue(jsonGeneratorMock, null);
 assertEquals(ROOT_NAME_FOR_NULL.getLocalPart(), jsonGeneratorMock.getCurrentName());
 assertEquals("", jsonGeneratorMock.getOutputValue());

 // Test serializeValue method with non-null value
 String nonNullValue = "nonNullValue";
 when(jsonGeneratorMock.writeStartObject()).thenCallRealMethod();
 when(jsonGeneratorMock.writeFieldName("testProperty")).thenCallRealMethod();
 jsonSerializerProvider.serializeValue(jsonGeneratorMock, new Object() {
 @Override
 public String getTestProperty() {
 return nonNullValue;
 }
 }, javaTypeMock, typeSerializerMock);
 when(jsonGeneratorMock.writeEndObject()).thenCallRealMethod();
 assertEquals("testProperty", jsonGeneratorMock.getCurrentName());
 assertEquals(nonNullValue, jsonGeneratorMock.getOutputValue());

 // Test serializeValue method with non-null value and config rootName
 when(jsonGeneratorMock.writeStartObject("xmlRootType")).thenCallRealMethod();
 when(jsonGeneratorMock.writeFieldName("testProperty")).thenCallRealMethod();
 xmlSerializerProvider.serializeValue(jsonGeneratorMock, new Object() {
 @Override
 public String getTestProperty() {
 return nonNullValue;
 }
 }, javaTypeMock, typeSerializerMock, serializationConfigMock);
 when(jsonGeneratorMock.writeEndObject()).thenCallRealMethod();
 assertEquals("xmlRootType", jsonGeneratorMock.getCurrentName());

 // Test _asXmlGenerator method
 assertNull(xmlSerializerProvider._asXmlGenerator(null));
 ToXmlGenerator toXmlGeneratorMock = mock(ToXmlGenerator.class);
 when(jsonGeneratorMock.asXmlGenerator()).thenReturn(toXmlGeneratorMock);
 ToXmlGenerator toXmlGenerator = xmlSerializerProvider._asXmlGenerator(jsonGeneratorMock);
 assertEquals(toXmlGeneratorMock, toXmlGenerator);
 }

 private static final Class<XmlSerializerProvider> XML_ROOT_TYPE = XmlSerializerProvider.class;
 private static final QName ROOT_NAME_FOR_NULL = new QName("null");
}