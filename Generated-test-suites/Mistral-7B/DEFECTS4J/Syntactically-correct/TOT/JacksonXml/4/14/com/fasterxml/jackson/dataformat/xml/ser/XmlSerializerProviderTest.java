package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(XMLConstants.W3C_XML_NAMESPACE_URI));
 objectMapper.setSerializerProvider(xmlSerializerProvider);
 }

 @Test
 public void testNullSerialization() throws JsonProcessingException, XMLStreamException {
 // Given
 QName expectedRootNameForNull = new QName("null");
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 xmlSerializerProvider.initWithRootName(xmlGenerator, expectedRootNameForNull);
 jsonSerializerProvider.setAsRoot(true);

 // When
 xmlSerializerProvider.serializeValue(xmlGenerator, null);

 // Then
 String actualXml = StaxUtil.toXmlString(xmlGenerator);
 String expectedXml = "<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>";
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testEmptyListSerialization() throws JsonProcessingException, XMLStreamException {
 // Given
 JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 xmlSerializerProvider.initWithRootName(xmlGenerator, new QName("items"));
 jsonSerializerProvider.setAsRoot(true);

 // When
 xmlSerializerProvider.serializeValue(xmlGenerator, Collections.emptyList(), javaType);

 // Then
 String actualXml = StaxUtil.toXmlString(xmlGenerator);
 String expectedXml = "<items xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item xsi:type=\"x:Array\"></item></items>";
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test(expected = JsonMappingException.class)
 public void testNonExistingRootNameSerialization() throws JsonProcessingException, XMLStreamException {
 // Given
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 xmlSerializerProvider.initWithRootName(xmlGenerator, new QName("nonExistingRootName"));
 jsonSerializerProvider.setAsRoot(true);

 // When
 xmlSerializerProvider.serializeValue(xmlGenerator, new Object());

 // Then
 // An exception should be thrown
 }

 @Test
 public void testStringSerialization() throws JsonProcessingException, XMLStreamException {
 // Given
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 xmlSerializerProvider.initWithRootName(xmlGenerator, new QName("string"));
 jsonSerializerProvider.setAsRoot(true);
 xmlSerializerProvider.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 // When
 xmlSerializerProvider.serializeValue(xmlGenerator, "Test String");

 // Then
 String actualXml = StaxUtil.toXmlString(xmlGenerator);
 String expectedXml = "<string xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><![CDATA[Test String]]></string>";
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testIntegerSerialization() throws JsonProcessingException, XMLStreamException {
 // Given
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 xmlSerializerProvider.initWithRootName(xmlGenerator, new QName("integer"));
 jsonSerializerProvider.setAsRoot(true);

 // When
 xmlSerializerProvider.serializeValue(xmlGenerator, new Integer(42));

 // Then
 String actualXml = StaxUtil.toXmlString(xmlGenerator);
 String expectedXml = "<integer xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">42</integer>";
 Assert.assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testCustomRootNameSerialization() throws JsonProcessingException, XMLStreamException {
 // Given
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 xmlSerializerProvider.initWithRootName(xmlGenerator, new QName("customRootName", "http://example.com"));
 jsonSerializerProvider.setAsRoot(true);

 // When
 xmlSerializerProvider.serializeValue(xmlGenerator, new Object());

 // Then
 String actualXml = StaxUtil.toXmlString(xmlGenerator);
 String expectedXml = "<customRootName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://example.com\"><item xsi:type=\"x:Object\"></item></customRootName>";
 Assert.assertEquals(expectedXml, actualXml);
 }
}