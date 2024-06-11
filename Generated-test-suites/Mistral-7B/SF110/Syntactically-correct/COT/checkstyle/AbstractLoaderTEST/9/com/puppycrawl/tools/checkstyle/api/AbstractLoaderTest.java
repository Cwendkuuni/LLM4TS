package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest {

 private static final String VALID_DTD_RESOURCE_NAME = "valid.dtd";
 private static final String INVALID_DTD_RESOURCE_NAME = "invalid.dtd";

 @Test
 public void testAbstractLoaderConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test(expected = SAXException.class)
 @Test(expected = ParserConfigurationException.class)
 public void testAbstractLoaderConstructorWithInvalidArguments() throws SAXException, ParserConfigurationException {
  Map<String, String> publicIdToResourceNameMap = null;

  try {
   AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
   fail("Expected SAXException or ParserConfigurationException");
  } catch (SAXException | ParserConfigurationException e) {
   // Expected exception
  }

  // Test with empty Map
  try {
   AbstractLoader abstractLoader = new AbstractLoader(Maps.newHashMap());
   fail("Expected SAXException or ParserConfigurationException");
  } catch (SAXException | ParserConfigurationException e) {
   // Expected exception
  }
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new FileInputStream("test_input.xml"));
  abstractLoader.parseInputSource(inputSource);
 }

 @Test(expected = IOException.class)
 public void testParseInputSourceWithNullInputSource() {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = null;

  try {
   abstractLoader.parseInputSource(inputSource);
   fail("Expected IOException");
  } catch (IOException e) {
   // Expected exception
  }
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithValidPublicId() throws IOException, SAXException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new FileInputStream("test_input.xml"));
  InputSource dtdInputSource = abstractLoader.resolveEntity("testPublicId", null);
  assertNotNull(dtdInputSource);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithInvalidPublicId() throws IOException, SAXException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new FileInputStream("test_input.xml"));
  InputSource dtdInputSource = abstractLoader.resolveEntity("invalidPublicId", null);
  assertNull(dtdInputSource);
 }

 @Test
 public void testResolveEntityWithSystemId() throws IOException, SAXException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new FileInputStream("test_input.xml"));
  InputSource dtdInputSource = abstractLoader.resolveEntity(null, VALID_DTD_RESOURCE_NAME);
  assertNotNull(dtdInputSource);
 }

 @Test
 public void testResolveEntityWithNullPublicIdAndSystemId() throws SAXException, IOException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new FileInputStream("test_input.xml"));
  InputSource dtdInputSource = abstractLoader.resolveEntity(null, null);
  assertNull(dtdInputSource);
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithNullSystemId() throws SAXException, IOException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new FileInputStream("test_input.xml"));
  InputSource dtdInputSource = abstractLoader.resolveEntity("testPublicId", null);
  assertNotNull(dtdInputSource);

  try {
   InputSource dtdInputSource2 = abstractLoader.resolveEntity("testPublicId", null);
   fail("Expected IOException");
  } catch (IOException e) {
   // Expected exception
  }
 }

 @Test(expected = SAXException.class)
 public void testErrorHandler() throws IOException, SAXException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new FileInputStream("test_input.xml"));
  InputSource dtdInputSource = new InputSource(new FileInputStream("test_error_input.xml")); // Contains errors

  try {
   abstractLoader.parseInputSource(inputSource);
   abstractLoader.parseInputSource(dtdInputSource);
  } catch (SAXException | IOException e) {
   fail("Expected no exception for valid input, but got " + e.getClass().getName());
  }

  try {
   abstractLoader.parseInputSource(inputSource);
   abstractLoader.parseInputSource(dtdInputSource);
  } catch (SAXException e) {
   // Expected exception
  }
 }

 @Test(expected = SAXException.class)
 public void testFatalErrorHandler() throws IOException, SAXException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("testPublicId", VALID_DTD_RESOURCE_NAME);

  AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new FileInputStream("test_input.xml"));
  InputSource dtdInputSource = new InputSource(new FileInputStream("test_fatal_error_input.xml")); // Contains fatal errors

  try {
   abstractLoader.parseInputSource(inputSource);
   abstractLoader.parseInputSource(dtdInputSource);
  } catch (SAXException | IOException e) {
   fail("Expected no exception for valid input, but got " + e.getClass().getName());
  }

  try {
   abstractLoader.parseInputSource(inputSource);
   abstractLoader.parseInputSource(dtdInputSource);
  } catch (SAXException e) {
   // Expected exception
  }
 }
}