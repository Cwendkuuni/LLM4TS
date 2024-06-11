package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(PowerMockRunner.class)
@PrepareForTest(XmlSerializerProvider.class)
public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private XmlRootNameLookup xmlRootNameLookup;
 private XmlSerializerProvider xmlSerializerProvider;
 private JsonSerializerProvider jsonSerializerProvider;
 private SerializationConfig config;
 private SerializerFactory serializerFactory;
 private JsonGenerator jsonGenerator;
 private TokenBuffer tokenBuffer;
 private QName rootName;

 @Before
 public void setup() {
 xmlRootNameLookup = Mockito.mock(XmlRootNameLookup.class);
 objectMapper = Mockito.spy(new ObjectMapper());
 config = new SerializationConfig(objectMapper.getFactory());
 serializerFactory = Mockito.mock(SerializerFactory.class);
 jsonSerializerProvider = new DefaultSerializerProvider();
 xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 jsonGenerator = Mockito.mock(JsonGenerator.class);
 tokenBuffer = Mockito.mock(TokenBuffer.class);
 }

 @Test
 public void testDefaultConstructor() {
 XmlSerializerProvider provider = new XmlSerializerProvider();
 assertEquals(XmlSerializerProvider.class, provider.getClass());
 }

 @Test
 public void testCopyConstructor() throws Exception {
 XmlSerializerProvider src = new XmlSerializerProvider(xmlRootNameLookup);
 XmlSerializerProvider newProvider = new XmlSerializerProvider(src);
 assertEquals(src, newProvider);
 }

 @Test
 public void testCreateInstance() throws Exception {
 XmlSerializerProvider src = new XmlSerializerProvider(xmlRootNameLookup);
 SerializationConfig config = new SerializationConfig(objectMapper.getFactory());
 SerializerFactory serializerFactory = Mockito.mock(SerializerFactory.class);
 XmlSerializerProvider newProvider = new XmlSerializerProvider(src, config, serializerFactory);
 assertEquals(src, newProvider);
 }

 @Test
 public void testInitWithRootName() throws Exception {
 ToXmlGenerator toXmlGenerator = Mockito.mock(ToXmlGenerator.class);
 QName rootName = new QName("root", "namespace");
 PowerMockito.whenNew(ToXmlGenerator.class).withArguments(jsonGenerator).thenReturn(toXmlGenerator);
 xmlSerializerProvider._initWithRootName(toXmlGenerator, rootName);
 PowerMockito.verifyNew(ToXmlGenerator.class);
 PowerMockito.verifyZeroInteractions(jsonGenerator);
 PowerMockito.verify(toXmlGenerator).initGenerator();
 PowerMockito.verify(toXmlGenerator).setNextName(rootName);
 }

 @Test
 public void testFindRootName() throws Exception {
 Class<?> clazz = String.class;
 JavaType javaType = null;
 QName expectedRootName = new QName("root");
 QName actualRootName = xmlSerializerProvider._rootNameFromConfig();
 if (actualRootName == null) {
 actualRootName = xmlSerializerProvider._rootNameLookup.findRootName(clazz, config);
 }
 assertEquals(expectedRootName, actualRootName);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindRootNameInvalidType() throws Exception {
 Class<?> clazz = Integer.class;
 xmlSerializerProvider._rootNameFromConfig();
 }

 @Test
 public void testSerializeValue() throws Exception {
 String value = "Test Value";
 QName rootName = new QName("root");
 PowerMockito.whenNew(ToXmlGenerator.class).withArguments(jsonGenerator).thenReturn(Mockito.mock(ToXmlGenerator.class));PowerMockito.when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);PowerMockito.when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(Mockito.mock(ToXmlGenerator.class));XMLEventWriter xmlEventWriter = Mockito.mock(XMLEventWriter.class);PowerMockito.when(jsonGenerator.getXmlOutput()).thenReturn(xmlEventWriter);TokenBuffer tokenBuffer = Mockito.mock(TokenBuffer.class);PowerMockito.when(jsonGenerator.getBufferedWriterAsTokenBuffer()).thenReturn(tokenBuffer);JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class); PowerMockito.when(jsonSerializerProvider.findValueSerializer(clazz, false, null)).thenReturn(serializer); xmlSerializerProvider.serializeValue(jsonGenerator, value); PowerMockito.verify(jsonGenerator, times(1)).writeStartObject(); PowerMockito.verify(jsonGenerator, times(1)).writeFieldName("root"); PowerMockito.verify(jsonGenerator, times(1)).writeString(value); PowerMockito.verify(jsonGenerator, times(1)).writeEndObject(); PowerMockito.verify(jsonGenerator, times(1)).close(); PowerMockito.verify(xmlEventWriter, times(1)).setDefaultNamespace("root"); }

 @Test
 public void testSerializeValueWithRootType() throws Exception {
 JavaType rootType = Mockito.mock(JavaType.class); PowerMockito.when(rootType.getRawClass()).thenReturn(String[].class); String[] value = {"Test Value 1", "Test Value 2"}; QName rootName = new QName("root"); PowerMockito.whenNew(ToXmlGenerator.class).withArguments(jsonGenerator).thenReturn(Mockito.mock(ToXmlGenerator.class));PowerMockito.when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);PowerMockito.when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(Mockito.mock(ToXmlGenerator.class));XMLEventWriter xmlEventWriter = Mockito.mock(XMLEventWriter.class);PowerMockito.when(jsonGenerator.getXmlOutput()).thenReturn(xmlEventWriter);TokenBuffer tokenBuffer = Mockito.mock(TokenBuffer.class);PowerMockito.when(jsonGenerator.getBufferedWriterAsTokenBuffer()).thenReturn(tokenBuffer); JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class); PowerMockito.when(jsonSerializerProvider.findValueSerializer(JavaType.construct(String[].class), false, null)).thenReturn(serializer); xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType, serializer); PowerMockito.verify(jsonGenerator, times(1)).writeStartObject(); PowerMockito.verify(jsonGenerator, times(1)).writeFieldName("item"); PowerMockito.verify(jsonGenerator, times(2)).writeArrayElements(value[0], value[1]); PowerMockito.verify(jsonGenerator, times(1)).writeEndObject(); PowerMockito.verify(jsonGenerator, times(1)).close(); PowerMockito.verify(xmlEventWriter, times(1)).setDefaultNamespace("root"); }

 @Test
 public void testSerializeNullValue() throws Exception {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup); JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class); XMLEventWriter xmlEventWriter = Mockito.mock(XMLEventWriter.class);PowerMockito.when(jsonGenerator.getXmlOutput()).thenReturn(xmlEventWriter);PowerMockito.when(jsonGenerator.getBufferedWriterAsTokenBuffer()).thenReturn(Mockito.mock(TokenBuffer.class));jsonSerializerProvider._serializeXmlNull(jsonGenerator); PowerMockito.verify(jsonGenerator, times(1)).writeStartObject(); PowerMockito.verify(jsonGenerator, times(1)).writeFieldName(XmlSerializerProvider.ROOT_NAME_FOR_NULL.getLocalPart()); PowerMockito.verify(jsonGenerator, times(1)).writeEndObject(); PowerMockito.verify(jsonGenerator, times(1)).close(); }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithInvalidGenerator() throws Exception {
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class); PowerMockito.whenNew(ToXmlGenerator.class).withArguments(jsonGenerator).thenThrow(new InstantiationException()); xmlSerializerProvider.serializeValue(jsonGenerator, "Test Value"); }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithNullGenerator() throws Exception {
 JsonGenerator jsonGenerator = null; xmlSerializerProvider.serializeValue(jsonGenerator, "Test Value"); }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithNullType() throws Exception {
 JavaType javaType = null; xmlSerializerProvider.serializeValue(jsonGenerator, "Test Value", javaType, serializer); }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithNullSerializer() throws Exception {
 JsonSerializer<Object> serializer = null; xmlSerializerProvider.serializeValue(jsonGenerator, "Test Value", null, serializer); }
}