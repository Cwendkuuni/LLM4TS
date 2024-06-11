package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest {

 private Map<String, String> publicIdToResourceNameMap;

 @Test
 public void testAbstractLoaderConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
 AbstractLoader abstractLoader = new AbstractLoader("testPublicId", "testDtdResourceName.dtd");
 assertNotNull(abstractLoader);
 }

 @Test
 public void testAbstractLoaderConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
 Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
 publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName.dtd");
 AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
 assertNotNull(abstractLoader);
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
 InputSource inputSource = new InputSource(new URL("testUrl").openStream());
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 abstractLoader.parseInputSource(inputSource);
 // Add assertions here if necessary
 }

 @Test
 public void testResolveEntityWithValidPublicId() throws IOException, SAXException {
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 String dtdResourceName = "testDtdResourceName.dtd";
 InputStream dtdIs = getClass().getResourceAsStream(dtdResourceName);
 publicIdToResourceNameMap = new HashMap<String, String>(1);
 publicIdToResourceNameMap.put("testPublicId", dtdResourceName);
 abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
 InputSource expectedInputSource = new InputSource(dtdIs);
 InputSource actualInputSource = abstractLoader.resolveEntity("testPublicId", null);
 assertEquals(expectedInputSource.getByteStream(), actualInputSource.getByteStream());
 }

 @Test
 public void testResolveEntityWithInvalidPublicId() throws SAXException, IOException {
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 InputSource actualInputSource = abstractLoader.resolveEntity("invalidPublicId", null);
 assertNull(actualInputSource);
 }

 @Test(expected = SAXException.class)
 public void testError() throws SAXException {
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 SAXParseException exception = new SAXParseException("Test error message", null);
 abstractLoader.error(exception);
 }

 @Test(expected = SAXException.class)
 public void testFatalError() throws SAXException {
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 SAXParseException exception = new SAXParseException("Test fatal error message", null);
 abstractLoader.fatalError(exception);
 }

}