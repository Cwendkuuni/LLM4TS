package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

 private AbstractLoader loader;

 @Before
 public void setUp() throws Exception {
 loader = new AbstractLoader(Maps.newHashMap());
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.parseInputSource(inputSource);
 // No specific assertion here, as this method doesn't return any value
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityUnknownPublicId() throws Exception {
 loader.resolveEntity("unknownPublicId", null);
 }

 @Test(expected = IOException.class)
 public void testResolveEntityInvalidSystemId() throws Exception {
 loader.resolveEntity("validPublicId", "invalidSystemId");
 }

 @Test
 public void testResolveEntityValid() throws Exception {
 final String dtdResourceName = "test.dtd";
 loader = new AbstractLoader(Maps.newHashMap());
 loader.publicIdToResourceNameMap.put("validPublicId", dtdResourceName);
 final ClassLoader classLoader = loader.getClass().getClassLoader();
 final InputStream dtdInputStream = classLoader.getResourceAsStream(dtdResourceName);
 final InputSource inputSource = new InputSource(dtdInputStream);
 final InputSource resolvedInputSource = loader.resolveEntity("validPublicId", null);
 assertEquals(inputSource.getInputStream(), resolvedInputSource.getInputStream());
 }

}