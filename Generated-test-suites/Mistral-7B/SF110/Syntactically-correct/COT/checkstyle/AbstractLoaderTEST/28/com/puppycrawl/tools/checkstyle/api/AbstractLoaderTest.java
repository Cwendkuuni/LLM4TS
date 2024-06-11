package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;

public class AbstractLoaderTest {

 private static final String DTD_RESOURCE_NAME = "test.dtd";
 private static final String VALID_INPUT = "validInput.xml";
 private static final String INVALID_INPUT = "invalidInput.xml";
 private static final String MISSING_DTD_INPUT = "missingDTDInput.xml";

 private Map<String, String> publicIdToResourceNameMap;
 private AbstractLoader loader;

 @Test
 public void testAbstractLoaderConstructorWithPublicIdAndDtdResourceName() throws ParserConfigurationException, SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", DTD_RESOURCE_NAME);
  loader = new AbstractLoader(publicIdToResourceNameMap);
  assertNotNull(loader);
 }

 @Test
 public void testAbstractLoaderConstructorWithMap() throws ParserConfigurationException, SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", DTD_RESOURCE_NAME);
  loader = new AbstractLoader(publicIdToResourceNameMap);
  assertNotNull(loader);
 }

 @Test
 public void testParseInputSource() throws ParserConfigurationException, SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", DTD_RESOURCE_NAME);
  loader = new AbstractLoader(publicIdToResourceNameMap);

  InputSource inputSource = new InputSource(new FileInputStream(VALID_INPUT));
  loader.parseInputSource(inputSource);
  // Make assertions on the parsed data if necessary
 }

 @Test(expected = IOException.class)
 public void testParseInputSourceWithNullInputSource() throws ParserConfigurationException, SAXException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", DTD_RESOURCE_NAME);
  loader = new AbstractLoader(publicIdToResourceNameMap);

  InputSource inputSource = null;
  loader.parseInputSource(inputSource);
  fail("Expected IOException but did not receive one");
 }

 @Test(expected = SAXException.class)
 public void testParseInputSourceWithSAXException() throws ParserConfigurationException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", DTD_RESOURCE_NAME);
  loader = new AbstractLoader(publicIdToResourceNameMap);

  InputSource inputSource = new InputSource(new FileInputStream(INVALID_INPUT));
  loader.parseInputSource(inputSource);
  fail("Expected SAXException but did not receive one");
 }

 @Test
 public void testResolveEntityWithValidPublicId() throws ParserConfigurationException, SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", DTD_RESOURCE_NAME);
  loader = new AbstractLoader(publicIdToResourceNameMap);

  InputSource inputSource = new InputSource(new FileInputStream(VALID_INPUT));
  InputSource entity = loader.resolveEntity("testPublicId", null);
  assertNotNull(entity);
 }

 @Test
 public void testResolveEntityWithNullPublicId() throws ParserConfigurationException, SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  publicIdToResourceNameMap.put("testPublicId", DTD_RESOURCE_NAME);
  loader = new AbstractLoader(publicIdToResourceNameMap);

  InputSource inputSource = new InputSource(new FileInputStream(VALID_INPUT));
  InputSource entity = loader.resolveEntity(null, null);
  assertNull(entity);
 }

 @Test
 public void testResolveEntityWithMissingPublicId() throws ParserConfigurationException, SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  loader = new AbstractLoader(new HashMap<>());

  InputSource inputSource = new InputSource(new FileInputStream(MISSING_DTD_INPUT));
  InputSource entity = loader.resolveEntity("testPublicId", null);
  assertNull(entity);
 }

 @Test
 public void testResolveEntityWithInvalidPublicId() throws ParserConfigurationException, SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  loader = new AbstractLoader(new HashMap<>());

  InputSource inputSource = new InputSource(new FileInputStream(VALID_INPUT));
  InputSource entity = loader.resolveEntity("testInvalidPublicId", null);
  assertNull(entity);
 }

 @Test
 public void testError() throws ParserConfigurationException, SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  loader = new AbstractLoader(new HashMap<>());

  InputSource inputSource = new InputSource(new FileInputStream(INVALID_INPUT));
  loader.parseInputSource(inputSource);
 }

 @Test
 public void testFatalError() throws ParserConfigurationException, SAXException, IOException {
  publicIdToResourceNameMap = new HashMap<>();
  loader = new AbstractLoader(new HashMap<>());

  InputSource inputSource = new InputSource(new FileInputStream(INVALID_INPUT));
  loader.parseInputSource(inputSource);
 }
}