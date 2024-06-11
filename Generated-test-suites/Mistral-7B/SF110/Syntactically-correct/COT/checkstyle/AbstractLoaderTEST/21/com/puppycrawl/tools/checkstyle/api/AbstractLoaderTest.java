package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

 private AbstractLoader abstractLoader;

 @Test
 public void testAbstractLoaderConstructionWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("publicId", "dtdResourceName");
  abstractLoader = new AbstractLoader(publicIdToResourceNameMap.get("publicId"), "dtdResourceName");
  assertNotNull(abstractLoader);
 }

 @Test
 public void testAbstractLoaderConstructionWithMap() throws SAXException, ParserConfigurationException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("publicId", "dtdResourceName");
  abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  assertNotNull(abstractLoader);
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("publicId", "dtdResourceName");
  abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new ByteArrayInputStream("XML data".getBytes()));
  abstractLoader.parseInputSource(inputSource);
  // Add assertions for verifying the parsed XML data here
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithNullPublicId() throws SAXException, ParserConfigurationException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("publicId", "dtdResourceName");
  abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  abstractLoader.resolveEntity(null, "systemId");
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithNullSystemId() throws SAXException, ParserConfigurationException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("publicId", "dtdResourceName");
  abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  abstractLoader.resolveEntity("publicId", null);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithUnknownPublicId() throws IOException, ParserConfigurationException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("anotherPublicId", "dtdResourceName");
  abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new ByteArrayInputStream("XML data".getBytes()));
  abstractLoader.parseInputSource(inputSource);
  abstractLoader.resolveEntity("publicId", "systemId");
 }

 @Test
 public void testError() throws SAXException, IOException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("publicId", "dtdResourceName");
  abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new ByteArrayInputStream("XML data with errors".getBytes()));
  try {
   abstractLoader.parseInputSource(inputSource);
  } catch (SAXParseException exception) {
   assertEquals("expected error message", exception.getMessage());
  }
 }

 @Test
 public void testFatalError() throws SAXException, IOException {
  Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
  publicIdToResourceNameMap.put("publicId", "dtdResourceName");
  abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
  InputSource inputSource = new InputSource(new ByteArrayInputStream("XML data with fatal errors".getBytes()));
  try {
   abstractLoader.parseInputSource(inputSource);
  } catch (SAXParseException | SAXException e) {
   fail("Expected a SAXException of type SAXNotRecognizedException");
  } catch (SAXNotRecognizedException e) {
   // Add assertions for verifying the fatal error message here
  }
 }

}