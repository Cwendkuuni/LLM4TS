package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.junit.Test;
import org.xml.sax.SAXException;

public class AbstractLoaderTest {

 private Map<String, String> publicIdToResourceNameMap;
 private XMLReader parser;

 @Test
 public void testConstructorWithPublicIdAndDtdResourceName() throws ParserConfigurationException, SAXException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName.dtd");

  AbstractLoader abstractLoader = new AbstractLoader("testPublicId", "testDtdResourceName.dtd");

  assertEquals("testDtdResourceName.dtd", abstractLoader.publicIdToResourceNameMap.get("testPublicId"));
  assertNotNull(abstractLoader.parser);
 }

 @Test
 public void testConstructorWithPublicIdToResourceNameMap() throws ParserConfigurationException, SAXException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId1", "testDtdResourceName1.dtd");
  publicIdToResourceNameMap.put("testPublicId2", "testDtdResourceName2.dtd");

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);

  assertEquals("testDtdResourceName1.dtd", abstractLoader.publicIdToResourceNameMap.get("testPublicId1"));
  assertEquals("testDtdResourceName2.dtd", abstractLoader.publicIdToResourceNameMap.get("testPublicId2"));
  assertNotNull(abstractLoader.parser);
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
  ByteArrayInputStream inputStream = new ByteArrayInputStream("test XML data".getBytes());
  InputSource inputSource = new InputSource(inputStream);

  parser = mock(XMLReader.class);
  when(parser.parse(inputSource)).thenReturn(new Object[0]);

  AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>());
  abstractLoader.setParser(parser);
  abstractLoader.parseInputSource(inputSource);

  // No exception is thrown, so the test is successful
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithPublicIdButNoMapping() throws SAXException, IOException {
  AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>());

  InputSource inputSource = new InputSource(new ByteArrayInputStream("test XML data".getBytes()));

  InputSource result = abstractLoader.resolveEntity("testPublicId", "testSystemId");

  assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithPublicIdAndMappingButNotFound() throws SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName.dtd");

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);

  InputSource inputSource = new InputSource(new ByteArrayInputStream("test XML data".getBytes()));

  InputStream expectedInputstream = mock(InputStream.class);
  when(expectedInputstream.available()).thenReturn(10);

  when(abstractLoader.getClass().getClassLoader().getResourceAsStream("testDtdResourceName.dtd")).thenReturn(expectedInputstream);

  InputSource result = abstractLoader.resolveEntity("testPublicId", "testSystemId");

  assertNull(result);
 }

 @Test
 public void testResolveEntityWithPublicIdAndMapping() throws SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName.dtd");

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);

  InputSource inputSource = new InputSource(new ByteArrayInputStream("test XML data".getBytes()));

  InputStream expectedInputstream = mock(InputStream.class);
  when(expectedInputstream.available()).thenReturn(10);

  when(abstractLoader.getClass().getClassLoader().getResourceAsStream("testDtdResourceName.dtd")).thenReturn(expectedInputstream);

  InputSource result = abstractLoader.resolveEntity("testPublicId", "testSystemId");

  assertEquals(expectedInputstream, result.getInputStream());
 }
}