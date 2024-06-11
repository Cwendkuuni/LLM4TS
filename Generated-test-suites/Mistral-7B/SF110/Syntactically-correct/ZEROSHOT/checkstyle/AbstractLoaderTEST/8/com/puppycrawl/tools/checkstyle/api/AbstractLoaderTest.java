package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import junit.framework.TestCase;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest extends TestCase {

 private static final String VALID_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
         + "<root>\n<element>Hello World</element>\n</root>";

 private AbstractLoader loader;

 @Before
 public void setUp() throws Exception {
     Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
     publicIdToResourceNameMap.put("publicId1", "dtdResource1.dtd");
     loader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test
 public void testParseInputSourceValid() throws Exception {
     InputSource inputSource = new InputSource(new ByteArrayInputStream(VALID_XML.getBytes()));
     loader.parseInputSource(inputSource);
 }

 @Test(expected = IOException.class)
 public void testParseInputSourceInvalid() throws Exception {
     InputSource inputSource = new InputSource(new ByteArrayInputStream("invalid xml".getBytes()));
     loader.parseInputSource(inputSource);
 }

 @Test
 public void testResolveEntityValid() throws Exception {
     InputSource inputSource = loader.resolveEntity("publicId1", null);
     assertEquals("class path resource: dtdResource1.dtd", inputSource.toString());
 }

 @Test
 public void testResolveEntityInvalid() throws Exception {
     InputSource inputSource = loader.resolveEntity("publicId2", null);
     assertNull(inputSource);
 }

 @Test
 public void testErrorFatal() throws Exception {
     InputSource inputSource = new InputSource(new ByteArrayInputStream("Invalid XML".getBytes()));
     try {
         loader.parseInputSource(inputSource);
         fail("Expected SAXException");
     } catch (SAXException e) {
         // expected
     }
 }

 @Test
 public void testErrorNonFatal() throws Exception {
     InputSource inputSource = new InputSource(new ByteArrayInputStream("Invalid XML".getBytes()));
     try {
         loader.parseInputSource(inputSource);
     } catch (SAXException e) {
         // expected
     }
 }

}