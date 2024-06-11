package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URL;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class AbstractLoaderTest {

 private static final String XML_RESOURCE = "test.xml";
 private static final String DTD_RESOURCE = "test.dtd";

 @Test
 public void testParseValidInput() throws Exception {
  AbstractLoader loader = new AbstractLoader(Maps.newHashMap()) {
   @Override
   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
    if (publicId != null && publicId.equals("DTD_ID")) {
     return new InputSource(getClass().getResourceAsStream(DTD_RESOURCE));
    }
    return null;
   }
  };

  InputSource inputSource = new InputSource(new URL("classpath:" + XML_RESOURCE).openStream());
  loader.parseInputSource(inputSource);
  // Add assertions here to verify the parse result
 }

 @Test(expected = SAXParseException.class)
 public void testParseInvalidInput() throws Exception {
  AbstractLoader loader = new AbstractLoader(Maps.newHashMap()) {
   @Override
   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
    if (publicId != null && publicId.equals("DTD_ID")) {
     throw new SAXParseException("Intentional error", new InputSource(new ByteArrayInputStream("Invalid XML".getBytes())));
    }
    return null;
   }
  };

  InputSource inputSource = new InputSource(new URL("classpath:" + XML_RESOURCE).openStream());
  loader.parseInputSource(inputSource);
 }

 @Test(expected = IOException.class)
 public void testResolveEntityIOException() throws Exception {
  AbstractLoader loader = new AbstractLoader(Maps.newHashMap()) {
   @Override
   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
    throw new IOException("Intentional error");
   }
  };

  String publicId = "DTD_ID";
  String systemId = "classpath:" + DTD_RESOURCE;
  loader.resolveEntity(publicId, systemId);
 }

 @Test(expected = SAXParseException.class)
 public void testUnresolvedEntity() throws Exception {
  AbstractLoader loader = new AbstractLoader(Maps.newHashMap()) {
   // Do not implement resolveEntity method
  };

  String publicId = "DTD_ID";
  String systemId = "classpath:" + DTD_RESOURCE;
  InputSource inputSource = new InputSource(new URL("classpath:" + XML_RESOURCE).openStream());
  loader.parseInputSource(inputSource);
 }
}