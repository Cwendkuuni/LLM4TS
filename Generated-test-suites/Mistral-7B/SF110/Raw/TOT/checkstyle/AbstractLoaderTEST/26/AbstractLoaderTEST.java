package com.puppycrawl.tools.checkstyle.api.test;

import com.google.common.io.Resources;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class AbstractLoaderTest {

 private static final String DTD_RESOURCE_NAME = "com/puppycrawl/tools/checkstyle/api/test/sample.dtd";

 @Test
 public void testParseValidInputSource() throws IOException, SAXException {
 AbstractLoader loader = new AbstractLoader("sample.publicId", DTD_RESOURCE_NAME);
 InputSource inputSource = Resources.asCharSource(getClass().getClassLoader(), "testInput.xml");
 loader.parseInputSource(inputSource);
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityWithMissingPublicId() throws IOException, SAXException {
 AbstractLoader loader = new AbstractLoader(new HashMap<>());
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root/>"));
 loader.parseInputSource(inputSource);
 loader.resolveEntity("missingPublicId", null);
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithMissingResource() throws SAXException, IOException {
 AbstractLoader loader = new AbstractLoader("sample.publicId", "nonExistentResourceName");
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root/>"));
 loader.parseInputSource(inputSource);
 loader.resolveEntity("sample.publicId", null);
 }

 @Test(expected = SAXParseException.class)
 public void testParseInvalidInputSource() throws IOException, SAXException {
 AbstractLoader loader = new AbstractLoader("sample.publicId", DTD_RESOURCE_NAME);
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root><tag error=\"error\"></tag>"));
 loader.parseInputSource(inputSource);
 }

 @Test
 public void testParseWithValidPublicId() throws IOException, SAXException {
 AbstractLoader loader = new AbstractLoader("sample.publicId", DTD_RESOURCE_NAME);
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\" system=\"testInput.xml\"?>\n<root/>"));
 loader.parseInputSource(inputSource);
 loader.resolveEntity("sample.publicId", null);
 }

 @Test
 public void testParseWithEmptyPublicId() throws IOException, SAXException {
 AbstractLoader loader = new AbstractLoader(new HashMap<>());
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\" system=\"testInput.xml\"?>\n<root/>"));
 loader.parseInputSource(inputSource);
 loader.resolveEntity("", null);
 }
}