package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

 private static final String TEST_DTD_PUBLIC_ID = "test-dtd";
 private static final String TEST_DTD_RESOURCE_NAME = "test-dtd.dtd";

 @Test
 public void testAbstractLoader() throws SAXException, ParserConfigurationException {
  // Test constructor with publicId and dtdResourceName
  AbstractLoader loader = new AbstractLoader(TEST_DTD_PUBLIC_ID, TEST_DTD_RESOURCE_NAME);
  Map<String, String> publicIdToResourceNameMap = loader.getPublicIdToResourceNameMap();
  assertEquals(TEST_DTD_PUBLIC_ID, publicIdToResourceNameMap.keySet().iterator().next());
  assertEquals(TEST_DTD_RESOURCE_NAME, publicIdToResourceNameMap.get(TEST_DTD_PUBLIC_ID));

  // Test constructor with predefined map
  Map<String, String> predefinedMap = Maps.newHashMap();
  predefinedMap.put(TEST_DTD_PUBLIC_ID, TEST_DTD_RESOURCE_NAME);
  loader = new AbstractLoader(predefinedMap);
  publicIdToResourceNameMap = loader.getPublicIdToResourceNameMap();
  assertEquals(TEST_DTD_PUBLIC_ID, publicIdToResourceNameMap.keySet().iterator().next());
  assertEquals(TEST_DTD_RESOURCE_NAME, publicIdToResourceNameMap.get(TEST_DTD_PUBLIC_ID));
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityWithPublicId() throws SAXException, ParserConfigurationException {
  AbstractLoader loader = new AbstractLoader();
  InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
  loader.parseInputSource(inputSource);
  loader.resolveEntity(TEST_DTD_PUBLIC_ID, null);
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityWithPublicIdAndSystemId() throws SAXException, ParserConfigurationException, IOException {
  AbstractLoader loader = new AbstractLoader();
  InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
  loader.parseInputSource(inputSource);
  InputSource result = loader.resolveEntity(TEST_DTD_PUBLIC_ID, TEST_DTD_RESOURCE_NAME);
  fail("Unexpected result.");
 }

 @Test(expected = SAXParseException.class)
 public void testError() throws SAXException, ParserConfigurationException, IOException {
  AbstractLoader loader = new AbstractLoader() {
   @Override
   public void error(SAXParseException exception) {
    super.error(exception);
    // Silence the exception
   }
  };
  InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
  loader.parseInputSource(inputSource);
  loader.error(new SAXParseException("Test error", null, 0, 0, null));
 }

 @Test(expected = SAXParseException.class)
 public void testFatalError() throws SAXException, ParserConfigurationException, IOException {
  AbstractLoader loader = new AbstractLoader() {
   @Override
   public void fatalError(SAXParseException exception) {
    super.fatalError(exception);
    // Silence the exception
   }
  };
  InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
  loader.parseInputSource(inputSource);
  loader.fatalError(new SAXParseException("Test error", null, 0, 0, null));
 }
}