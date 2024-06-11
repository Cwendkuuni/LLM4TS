package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Maps;

import junit.framework.TestCase;

public class AbstractLoaderTest extends TestCase {

 private AbstractLoader loader;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
 publicIdToResourceNameMap.put("publicId1", "dtdResourceName1.dtd");
 loader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
 String xmlString = "<root></root>";
 InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlString.getBytes()));
 loader.parseInputSource(inputSource);
 // Add assertions here if necessary
 }

 @Test
 public void testResolveEntityWithValidPublicId() throws IOException, SAXException {
 String dtdResourceName = "dtdResourceName1.dtd";
 InputSource expectedInputSource = new InputSource(loader.getClass().getResourceAsStream(dtdResourceName));
 StringWriter stringWriter = new StringWriter();
 DefaultHandler defaultHandler = new DefaultHandler() {
 @Override
 public void characters(char[] ch, int start, int length) throws SAXException {
 stringWriter.append(new String(ch, start, length));
 }
 };
 XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
 xmlReader.setContentHandler(defaultHandler);
 xmlReader.parse(loader.resolveEntity("publicId1", null));
 assertEquals(expectedInputSource.getCharacterStream().toString(), stringWriter.toString());
 }

 @Test
 public void testResolveEntityWithInvalidPublicId() throws IOException, SAXException {
 assertNull(loader.resolveEntity("invalidPublicId", null));
 }

 @Test(expected = SAXParseException.class)
 public void testError() throws IOException, SAXException {
 String xmlStringWithError = "<root><error/></root>";
 InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlStringWithError.getBytes()));
 loader.parseInputSource(inputSource);
 }

 @Test(expected = SAXException.class)
 public void testFatalError() throws IOException, SAXException {
 String xmlStringWithFatalError = "<root><fatalError/></root>";
 InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlStringWithFatalError.getBytes()));
 loader.parseInputSource(inputSource);
 }
}