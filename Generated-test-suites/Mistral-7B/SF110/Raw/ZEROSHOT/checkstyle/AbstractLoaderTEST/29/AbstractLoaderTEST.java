package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import junit.framework.TestCase;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest extends TestCase {

 private static final String PUBLIC_ID = "testPublicId";
 private static final String DTD_RESOURCE_NAME = "test.dtd";
 private static final String INPUT = "<test/>";

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 final Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>();
 publicIdToResourceNameMap.put(PUBLIC_ID, DTD_RESOURCE_NAME);
 final AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
 this.parser = new SAXParser();
 this.parser.setContentHandler(loader);
 this.parser.setErrorHandler(loader);
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(INPUT.getBytes()));
 loader.parseInputSource(inputSource);
 // Add assertions here to check the state of the loader after parsing
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityInvalidPublicId() throws IOException, SAXException {
 loader.resolveEntity("invalidPublicId", null);
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityInvalidSystemId() throws IOException, SAXException {
 loader.resolveEntity(PUBLIC_ID, "invalidSystemId");
 }

 @Test
 public void testResolveEntityValid() throws IOException, SAXException {
 final ClassLoader loader = this.getClass().getClassLoader();
 final InputStream dtdIs = loader.getResourceAsStream(DTD_RESOURCE_NAME);
 final InputSource expectedInputSource = new InputSource(dtdIs);
 final InputSource actualInputSource = loader.resolveEntity(PUBLIC_ID, null);
 assertEquals("Expected input source to be the same", expectedInputSource, actualInputSource);
 }

 @Test(expected = SAXParseException.class)
 public void testError() throws IOException, SAXException {
 loader.error(new SAXParseException("Test error", null, 1, 1, null));
 }

 @Test(expected = SAXParseException.class)
 public void testFatalError() throws IOException, SAXException {
 loader.fatalError(new SAXParseException("Test fatal error", null, 1, 1, null));
 }

 @After
 public void tearDown() throws Exception {
 this.parser = null;
 loader = null;
 }

 private SAXParser parser;
 private AbstractLoader loader;

}