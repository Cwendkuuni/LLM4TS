package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class AbstractLoaderTest {

 private AbstractLoader abstractLoader;
 private Map<String, String> publicIdToResourceNameMap;

 @Before
 public void setUp() throws Exception {
 publicIdToResourceNameMap = Maps.newHashMap();
 abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test
 public void testAbstractLoaderWithValidPublicId() throws Exception {
 final String publicId = "testPublicId";
 final String dtdResourceName = "testDtdResourceName.dtd";
 abstractLoader = new AbstractLoader(publicId, dtdResourceName);

 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 abstractLoader.parseInputSource(inputSource);

 assertEquals(dtdResourceName, abstractLoader.publicIdToResourceNameMap.get(publicId));
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithInvalidPublicId() throws Exception {
 final String publicId = "testInvalidPublicId";
 final String dtdResourceName = "testDtdResourceName.dtd";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);
 abstractLoader = new AbstractLoader(publicIdToResourceNameMap);

 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 abstractLoader.parseInputSource(inputSource);

 fail("SAXException expected but not thrown.");
 }

 @Test(expected = SAXException.class)
 @Test(expected = IOException.class)
 public void testResolveEntityWithUnknownPublicId() throws Exception {
 final String publicId = "testUnknownPublicId";
 final String systemId = "testSystemId";
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));

 abstractLoader.publicIdToResourceNameMap.put(publicId, systemId);
 final InputStream dtdIs = new ByteArrayInputStream(new byte[0]);
 abstractLoader.parser.setInputSource(inputSource);
 abstractLoader.parser.parse(new InputSource(dtdIs));

 InputSource result = abstractLoader.resolveEntity(publicId, systemId);
 assertNull(result);
 }

 @Test(expected = SAXException.class)
 @Test(expected = IOException.class)
 public void testResolveEntityWithUnknownSystemId() throws Exception {
 final String publicId = "testPublicId";
 final String systemId = "testSystemId";
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));

 abstractLoader.parser.setInputSource(inputSource);
 abstractLoader.parseInputSource(inputSource);

 InputSource result = abstractLoader.resolveEntity(publicId, systemId);
 assertNull(result);
 }

 @Test(expected = SAXException.class)
 public void testErrorHandler() throws Exception {
 final String publicId = "testPublicId";
 final String dtdResourceName = "testDtdResourceName.dtd";
 final String errorMessage = "testErrorMessage";
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));

 publicIdToResourceNameMap.put(publicId, dtdResourceName);
 abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
 @Override
 public void error(final SAXParseException exception) throws SAXException {
 throw new SAXException(errorMessage);
 }
 };

 abstractLoader.parser.setInputSource(inputSource);
 abstractLoader.parseInputSource(inputSource);

 fail("SAXException with expected message not thrown.");
 }
}