package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.XMLInputSource;

public class AbstractLoaderTest {

 private AbstractLoader loader;

 @Before
 public void setUp() {
 try {
 loader = new AbstractLoader(Maps.newHashMap());
 } catch (SAXException | ParserConfigurationException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
 final InputSource inputSource = new XMLInputSource(new ByteArrayInputStream("Test XML".getBytes()));
 loader.parseInputSource(inputSource);
 // Add assertions here if necessary
 }

 @Test
 public void testResolveEntityWithPublicId() throws IOException, SAXException {
 final String publicId = "testPublicId";
 final String dtdResourceName = "testDTD.dtd";
 final Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 try {
 loader = new AbstractLoader(publicIdToResourceNameMap);
 final InputSource inputSource = loader.resolveEntity(publicId, null);
 final InputStream inputStream = inputSource.getByteStream();
 assertTrue(inputStream.available() > 0);
 // Add assertions here if necessary
 } catch (SAXException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityWithPublicIdNotFound() throws IOException, SAXException {
 loader = new AbstractLoader(Maps.newHashMap());
 final SAXParseException exception = (SAXParseException) loader.resolveEntity("testPublicId", null);
 assertEquals("Error resolving entity testPublicId: not found: testPublicId", exception.getMessage());
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityWithSystemId() throws IOException, SAXException {
 loader = new AbstractLoader(Maps.newHashMap());
 final SAXParseException exception = (SAXParseException) loader.resolveEntity(null, new ByteArrayInputStream("Test XML".getBytes()));
 assertEquals("Error resolving entity: null", exception.getMessage());
 }

 @Test
 public void testError() throws SAXException {
 loader = new AbstractLoader(Maps.newHashMap());
 final SAXParseException exception = new SAXParseException("Test error", null);
 loader.error(exception);
 fail("An error should have been thrown but was not.");
 }

 @Test
 public void testFatalError() throws SAXException {
 loader = new AbstractLoader(Maps.newHashMap());
 final SAXParseException exception = new SAXParseException("Test fatal error", null);
 loader.fatalError(exception);
 fail("A fatal error should have caused test failure but didn't.");
 }
}