package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.ToXmlGenerator;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.xml.*", "org.w3c.dom.*"})
public class XmlSerializerProviderTest {

 private XmlMapper mapper;
 private XmlSerializerProvider xmlProvider;
 private SerializationConfig config;
 private SerializerFactory factory;
 private XmlRootNameLookup rootNameLookup;
 private QName rootNameForNull;
 private DefaultSerializerProvider defaultProvider;
 private TokenBuffer tokenBuffer;
 private JsonGenerator gen;
 private JsonSerializer<Object> serializer;
 private PropertyName propertyName;
 private JavaType rootType;

 @Before
 public void setup() {
 mapper = new XmlMapper();
 xmlProvider = mapper.getXmlSerializerProvider();
 config = mapper.getSerializationConfig();
 factory = mapper.getSerializerFactory();
 rootNameLookup = Mockito.mock(XmlRootNameLookup.class);
 rootNameForNull = new QName("null");
 defaultProvider = new DefaultSerializerProvider();
 tokenBuffer = new TokenBuffer();
 gen = PowerMockito.mock(JsonGenerator.class);
 serializer = new StdSerializer<Object>(Object.class) {};
 propertyName = new PropertyName("root");
 rootType = factory.constructType(Object[].class);
 }

 @Test
 public void testCreateInstance() {
 XmlSerializerProvider provider1 = xmlProvider;
 XmlSerializerProvider provider2 = xmlProvider.createInstance(config, factory);
 assertNotSame(provider1, provider2);
 }

 @Test
 public void testSerializeValueNull() throws IOException {
 given(rootNameFromConfig()).willReturn(null);
 given(rootNameLookup.findRootName(Object.class, config)).willReturn(null);
 given(rootNameLookup.findRootName(Object[].class, config)).willReturn(null);
 given(gen.setRootElementName(anyString())).willReturn(gen);

 when(xmlProvider.serializeValue(gen, null)).thenCallRealMethod();
 then(gen).should().writeStartElement("null");
 then(gen).should().writeEndElement();
 }

 @Test
 public void testSerializeValueWithRootNameFromConfig() throws IOException {
 given(rootNameFromConfig()).willReturn(new QName("root"));
 given(gen.setRootElementName(anyString())).willReturn(gen);

 when(xmlProvider.serializeValue(gen, "test")).thenCallRealMethod();
 then(gen).should().writeStartElement("root");
 then(gen).should().writeEndElement();
 then(gen).should().flush();
 }

 @Test
 public void testSerializeValueWithRootNameFromRootType() throws IOException {
 given(rootNameFromConfig()).willReturn(null);
 given(rootNameLookup.findRootName(Object.class, config)).willReturn(null);
 given(rootNameLookup.findRootName(Object[].class, config)).willReturn(null);
 given(rootNameLookup.findRootName(rootType, config)).willReturn(new QName("root"));
 given(gen.setRootElementName(anyString())).willReturn(gen);

 when(xmlProvider.serializeValue(gen, "test", rootType)).thenCallRealMethod();
 then(gen).should().writeStartElement("root");
 then(gen).should().writeEndElement();
 then(gen).should().flush();
 }

 @Test
 public void testSerializeValueWithRootNameAndSerializer() throws IOException {
 given(rootNameFromConfig()).willReturn(null);
 given(rootNameLookup.findRootName(Object.class, config)).willReturn(null);
 given(rootNameLookup.findRootName(Object[].class, config)).willReturn(null);
 given(rootNameLookup.findRootName(rootType, config)).willReturn(new QName("root"));
 given(gen.setRootElementName(anyString())).willReturn(gen);
 given(xmlProvider.findTypedValueSerializer(Object[].class, true, null)).willReturn(serializer);

 when(xmlProvider.serializeValue(gen, new Object[]{"test"}, rootType, serializer)).thenCallRealMethod();
 then(gen).should().writeStartElement("root");
 then(gen).should().writeFieldName("item");
 then(gen).should().writeStartArray();
 then(gen).should().writeStartElement("item");
 then(gen).should().writeValue("test");
 then(gen).should().writeEndElement();
 then(gen).should().writeEndArray();
 then(gen).should().writeEndElement();
 then(gen).should().flush();
 }

 @Test
 public void testCreateInstanceWithRootNameLookup() throws IOException {
 XmlSerializerProvider xmlProvider1 = xmlProvider;
 XmlSerializerProvider xmlProvider2 = new XmlSerializerProvider(rootNameLookup);
 assertNotSame(xmlProvider1, xmlProvider2);
 }

 @Test
 public void testSerializeValueWithRootNameFromConfigAndRootNameLookup() throws IOException {
 given(rootNameFromConfig()).willReturn(new QName("root"));
 given(rootNameLookup.findRootName(Object.class, config)).willReturn(null);
 given(rootNameLookup.findRootName(Object[].class, config)).willReturn(null);
 given(rootNameLookup.findRootName(Object.class, config)).willReturn(new QName("test"));
 given(gen.setRootElementName(anyString())).willReturn(gen);

 when(xmlProvider.serializeValue(gen, "test")).thenCallRealMethod();
 then(gen).should().writeStartElement("test");
 then(gen).should().writeEndElement();
 then(gen).should().flush();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithRootNameAndNullSerializer() throws IOException {
 given(rootNameFromConfig()).willReturn(null);
 given(rootNameLookup.findRootName(Object.class, config)).willReturn(null);
 given(rootNameLookup.findRootName(Object[].class, config)).willReturn(null);
 given(rootNameLookup.findRootName(rootType, config)).willReturn(new QName("root"));
 given(gen.setRootElementName(anyString())).willReturn(gen);
 given(xmlProvider.findTypedValueSerializer(Object[].class, true, null)).willReturn(null);

 when(xmlProvider.serializeValue(gen, new Object[]{"test"}, rootType, null)).shouldThrow(JsonMappingException.class);
 }

 @Test
 public void testSerializeValueWithRootNameAndSerializerFromFactory() throws IOException {
 given(rootNameFromConfig()).willReturn(null);
 given(rootNameLookup.findRootName(Object.class, config)).willReturn(null);
 given(rootNameLookup.findRootName(Object[].class, config)).willReturn(null);
 given(rootNameLookup.findRootName(rootType, config)).willReturn(new QName("root"));
 given(gen.setRootElementName(anyString())).willReturn(gen);
 given(factory.findValueSerializer(javaClass(JsonSerializer.class), Object[].class)).willReturn(serializer);
 given(xmlProvider.getProviderInstance(javaClass(JsonSerializer.class), null)).willReturn(serializer);

 when(xmlProvider.serializeValue(gen, new Object[]{"test"}, rootType, null)).thenCallRealMethod();
 then(gen).should().writeStartElement("root");
 then(gen).should().writeFieldName("item");
 then(gen).should().writeStartArray();
 then(gen).should().writeStartElement("item");
 then(gen).should().writeValue("test");
 then(gen).should().writeEndElement();
 then(gen).should().writeEndArray();
 then(gen).should().writeEndElement();
 then(gen).should().flush();
 }

 @Test
 public void testSerializeValueWithRootNameAndSerializerFromConfig() throws IOException {
 given(rootNameFromConfig()).willReturn(null);
 given(rootNameLookup.findRootName(Object.class, config)).willReturn(null);
 given(rootNameLookup.findRootName(Object[].class, config)).willReturn(null);
 given(rootNameLookup.findRootName(rootType, config)).willReturn(new QName("root"));
 given(gen.setRootElementName(anyString())).willReturn(gen);
 given(config.findValueSerializer(Object[].class)).willReturn(serializer);
 given(xmlProvider.getProviderInstance(javaClass(JsonSerializer.class), null)).willReturn(serializer);

 when(xmlProvider.serializeValue(gen, new Object[]{"test"}, rootType, null)).thenCallRealMethod();
 then(gen).should().writeStartElement("root");
 then(gen).should().writeFieldName("item");
 then(gen).should().writeStartArray();
 then(gen).should().writeStartElement("item");
 then(gen).should().writeValue("test");
 then(gen).should().writeEndElement();
 then(gen).should().writeEndArray();
 then(gen).should().writeEndElement();
 then(gen).should().flush();
 }

 @Test
 public void testSerializeValueWithRootNameAndArray() throws IOException {
 given(rootNameFromConfig()).willReturn(new QName("root"));
 given(rootNameLookup.findRootName(Object[].class, config)).willReturn(null);
 given(gen.setRootElementName(anyString())).willReturn(gen);

 when(xmlProvider.serializeValue(gen, new Object[]{new Object[]{"test1", "test2"}}, rootType)).thenCallRealMethod();
 then(gen).should().writeStartElement("root");
 then(gen).should().writeFieldName("item");
 then(gen).should().writeStartArray();
 then(gen).should().writeStartArray();
 then(gen).should().writeValue("test1");
 then(gen).should().writeValue("test2");
 then(gen).should().writeEndArray();
 then(gen).should().writeEndArray();
 then(gen).should().writeEndElement();
 then(gen).should().flush();
 }

 @Test
 public void testStartRootArray() throws IOException {
 ToXmlGenerator xgen = PowerMockito.mock(ToXmlGenerator.class);
 given(xgen.getRootName()).willReturn(new QName("root"));
 given(xgen.writeStartObject()).willThrow(IOException.class);
 given(xgen.writeFieldName("item")).willThrow(IOException.class);
 given(xgen.writeStartArray()).willThrow(IOException.class);

 when(xmlProvider.serializeValue(gen, null)).thenReturn(xgen);
 when(xgen.setRootName(any(QName.class))).thenCallRealMethod();
 when(xgen.writeStartObject()).thenCallRealMethod();
 when(xgen.writeFieldName("item")).thenCallRealMethod();
 when(xgen.writeStartArray()).thenCallRealMethod();

 then(xmlProvider.serializeValue(gen, null)).should().notThrowAnyException();
 then(xgen).shouldReceive(new MethodCall("setRootName")).withAnyArguments();
 then(xgen).shouldReceive(new MethodCall("writeStartObject")).once();
 then(xgen).shouldReceive(new MethodCall("writeFieldName")).withArguments("item").once();
 then(xgen).shouldReceive(new MethodCall("writeStartArray")).once();
 }

 @Test
 public void testInitWithRootName() throws IOException {
 ToXmlGenerator xgen = PowerMockito.mock(ToXmlGenerator.class);
 given(xgen.getRootName()).willReturn(new QName("root"));
 given(xgen.writeStartElement(anyString())).willThrow(IOException.class);
 given(xgen.setNextNameIfMissing(any(QName.class))).willReturn(false);
 given(xgen.setNextName(anyString())).willThrow(IOException.class);

 when(xmlProvider.serializeValue(gen, null)).thenReturn(xgen);
 when(xmlProvider._initWithRootName(xgen, new QName("root"))).thenCallRealMethod();

 then(xmlProvider._initWithRootName(xgen, new QName("root"))).should().notThrowAnyException();
 then(xgen).shouldReceive(new MethodCall("writeStartElement")).withArguments("root").once();
 }

 @Test
 public void testSerializeValueWithNullRootName() throws IOException {
 ToXmlGenerator xgen = PowerMockito.mock(ToXmlGenerator.class);
 given(xgen.writeStartElement(anyString())).willThrow(IOException.class);
 given(xgen.setNextNameIfMissing(null)).willReturn(false);
 given(xgen.setNextName(anyString())).willThrow(IOException.class);
 given(xgen.getRootName()).willReturn(null);
 given(xgen.writeNull(anyString())).willCallRealMethod();
 given(xgen.getSerializerProvider()).willReturn(defaultProvider);
 given(xmlProvider.serializeValue(gen, null)).thenReturn(xgen);

 when(xmlProvider._serializeXmlNull(gen)).thenCallRealMethod();

 then(xmlProvider._serializeXmlNull(gen)).should().notThrowAnyException();
 then(xgen).shouldReceive(new MethodCall("writeStartElement")).withArguments("null").once();
 }
}