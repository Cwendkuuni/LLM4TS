package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest {

 private static final String VALID_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "  <element1 attr1=\"value1\">Content 1</element1>\n" +
         "  <element2 attr2=\"value2\">Content 2</element2>\n" +
         "</root>";

 private static final String INVALID_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "  <element1 attr1=\"value1\">Content 1</element1>\n" +
         "  <element2>\n" +
         "  <element3>\n" +
         "</root>";

 private AbstractLoader loader;

 @Test
 public void testAbstractLoader() throws Exception {
     Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
     loader = new AbstractLoader(publicIdToResourceNameMap);
     assertTrue(loader instanceof AbstractLoader);
 }

 @Test(expected = SAXException.class)
 public void testParseInvalidInput() throws Exception {
     loader = new AbstractLoader(Maps.newHashMap());
     StringReader reader = new StringReader(INVALID_XML);
     loader.parseInputSource(new InputSource(reader));
     fail("SAXException expected but not thrown.");
 }

 @Test
 public void testParseValidInput() throws Exception {
     Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
     publicIdToResourceNameMap.put("DTD_ROOT.dtd", "classpath:valid_dtd.dtd");
     loader = new AbstractLoader(publicIdToResourceNameMap);
     StringReader reader = new StringReader(VALID_XML);
     loader.parseInputSource(new InputSource(reader));
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityInvalidPublicId() throws Exception {
     Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
     loader = new AbstractLoader(publicIdToResourceNameMap);
     StringReader reader = new StringReader(INVALID_XML);
     InputSource inputSource = new InputSource(reader);
     assertNull(loader.resolveEntity("Invalid_Public_ID", ""));
 }

 @Test
 public void testResolveEntityValidPublicId() throws Exception {
     Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
     publicIdToResourceNameMap.put("DTD_ROOT.dtd", "classpath:valid_dtd.dtd");
     loader = new AbstractLoader(publicIdToResourceNameMap);
     StringReader reader = new StringReader(VALID_XML);
     InputSource inputSource = new InputSource(reader);
     InputSource expectedInputSource = new InputSource(getClass().getResourceAsStream("/valid_dtd.dtd"));
     InputSource resultInputSource = loader.resolveEntity("DTD_ROOT.dtd", "");
     assertEquals(expectedInputSource.toString(), resultInputSource.toString());
 }

 @Test(expected = SAXException.class)
 public void testError() throws Exception {
     Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
     loader = new AbstractLoader(publicIdToResourceNameMap);
     StringReader reader = new StringReader(INVALID_XML);
     InputSource inputSource = new InputSource(reader);
     loader.parseInputSource(inputSource);
     fail("SAXException expected but not thrown.");
 }

 @Test
 public void testFatalError() throws Exception {
     Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
     loader = new AbstractLoader(publicIdToResourceNameMap);
     StringReader reader = new StringReader(INVALID_XML);
     InputSource inputSource = new InputSource(reader);
     loader.parseInputSource(inputSource);
     fail("FatalException expected but not thrown.");
 }
}