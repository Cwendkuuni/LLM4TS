package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import junit.framework.TestCase;

public class AbstractLoaderTest extends TestCase {

 private static final String PUBLIC_ID = "testPublicId";
 private static final String DTD_RESOURCE_NAME = "testDTD.dtd";
 private static final String DTD_CONTENT = "<!DOCTYPE root [ <!ATTLIST elem attr CDATA #IMPLIED> ]>";

 private AbstractLoader loader;

 @Before
 public void setUp() throws Exception {
 loader = new AbstractLoader(PUBLIC_ID, DTD_RESOURCE_NAME);
 }

 @Test
 public void testConstructorWithPublicIdAndDtdResourceName() throws Exception {
 Map<String, String> publicIdToResourceNameMap = Maps.newHashMap(loader.getPublicIdToResourceNameMap());
 assertEquals(PUBLIC_ID, publicIdToResourceNameMap.keySet().iterator().next());
 assertEquals(DTD_RESOURCE_NAME, publicIdToResourceNameMap.get(PUBLIC_ID));
 }

 @Test
 public void testConstructorWithMap() throws Exception {
 loader = new AbstractLoader(Maps.newHashMap(Maps.singletonMap(PUBLIC_ID, DTD_RESOURCE_NAME)));
 Map<String, String> publicIdToResourceNameMap = loader.getPublicIdToResourceNameMap();
 assertEquals(PUBLIC_ID, publicIdToResourceNameMap.keySet().iterator().next());
 assertEquals(DTD_RESOURCE_NAME, publicIdToResourceNameMap.get(PUBLIC_ID));
 }

 @Test
 public void testParseInputSource() throws Exception {
 InputStream dtdInputStream = new ByteArrayInputStream(DTD_CONTENT.getBytes());
 InputSource inputSource = new InputSource(dtdInputStream);
 loader.parseInputSource(inputSource);
 // Add assertions here if needed
 }

 @Test
 public void testResolveEntity() throws Exception {
 InputStream dtdInputStream = new ByteArrayInputStream(DTD_CONTENT.getBytes());
 InputSource dtdInputSource = new InputSource(dtdInputStream);
 InputSource resolvedInputSource = loader.resolveEntity(PUBLIC_ID, null);
 assertEquals(dtdInputSource.getByteStream(), resolvedInputSource.getByteStream());
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityWithUnknownPublicId() throws Exception {
 InputSource resolvedInputSource = loader.resolveEntity("unknownPublicId", null);
 fail("SAXParseException expected but not thrown.");
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithUnknownSystemId() throws Exception {
 InputSource inputSource = new InputSource(new ByteArrayInputStream(DTD_CONTENT.getBytes()));
 InputSource resolvedInputSource = loader.resolveEntity(PUBLIC_ID, "unknownSystemId");
 fail("IOException expected but not thrown.");
 }
}