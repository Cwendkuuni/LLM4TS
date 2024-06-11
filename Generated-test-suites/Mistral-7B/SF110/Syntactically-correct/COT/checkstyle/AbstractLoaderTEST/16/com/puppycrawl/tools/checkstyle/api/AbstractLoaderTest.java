package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

 private static final String DTD_RESOURCE_NAME = "test.dtd";
 private static final String PUBLIC_ID = "-//W3C//DTD XML 1.0//EN";

 @Test
 public void testAbstractLoaderWithValidPublicIdAndDtdResourceName() throws Exception {
 AbstractLoader loader = new AbstractLoader(PUBLIC_ID, DTD_RESOURCE_NAME);
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.parseInputSource(inputSource);
 // Add any assertions for this test case if necessary
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithNullInputSource() {
 AbstractLoader loader = new AbstractLoader(PUBLIC_ID, DTD_RESOURCE_NAME);
 loader.parseInputSource(null);
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithInvalidPublicId() throws Exception {
 Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
 publicIdToResourceNameMap.put(PUBLIC_ID, DTD_RESOURCE_NAME);
 AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.parseInputSource(inputSource);
 // Add any assertions for this test case if necessary
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbstractLoaderWithInvalidDtdResourceName() throws Exception {
 Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
 publicIdToResourceNameMap.put(PUBLIC_ID, "nonExistentResourceName.dtd");
 AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.parseInputSource(inputSource);
 // Add any assertions for this test case if necessary
 }

 @Test(expected = SAXParseException.class)
 public void testAbstractLoaderWithInvalidInputSource() throws Exception {
 AbstractLoader loader = new AbstractLoader(PUBLIC_ID, DTD_RESOURCE_NAME);
 InputSource inputSource = null;
 loader.parseInputSource(inputSource);
 }

 @Test
 public void testAbstractLoaderWithValidInputSource() throws Exception {
 Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
 publicIdToResourceNameMap.put(PUBLIC_ID, DTD_RESOURCE_NAME);
 InputStream dtdInputStream = getClass().getResourceAsStream(DTD_RESOURCE_NAME);
 InputSource inputSource = new InputSource(dtdInputStream);
 AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
 loader.parseInputSource(inputSource);
 // Add any assertions for this test case if necessary
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithUnhandledException() throws Exception {
 AbstractLoader loader = new AbstractLoader(PUBLIC_ID, DTD_RESOURCE_NAME) {
 @Override
 public void error(SAXParseException exception) {
 throw new RuntimeException(exception);
 }
 };
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.parseInputSource(inputSource);
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithUnhandledFatalError() throws Exception {
 AbstractLoader loader = new AbstractLoader(PUBLIC_ID, DTD_RESOURCE_NAME) {
 @Override
 public void fatalError(SAXParseException exception) {
 throw new RuntimeException(exception);
 }
 };
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.parseInputSource(inputSource);
 }
}