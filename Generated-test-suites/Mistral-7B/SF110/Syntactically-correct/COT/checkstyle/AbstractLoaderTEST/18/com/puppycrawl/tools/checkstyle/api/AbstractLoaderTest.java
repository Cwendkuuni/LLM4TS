package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.DefaultHandler;
import com.puppycrawl.tools.checkstyle.api.EntityResolver;
import com.puppycrawl.tools.checkstyle.api.ErrorHandler;
import com.puppycrawl.tools.checkstyle.api.SAXParserFactory;
import com.puppycrawl.tools.checkstyle.api.XMLReader;
import com.puppycrawl.tools.checkstyle.api.XMLEntityResolver;

public class AbstractLoaderTest {

 private AbstractLoader loader;
 private Map<String, String> publicIdToResourceNameMap;

 @Before
 public void setUp() throws Exception {
 publicIdToResourceNameMap = new HashMap<String, String>(1);
 loader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test
 public void testConstructorWithPublicIdAndDtdResourceName() throws Exception {
 String publicId = "publicId";
 String dtdResourceName = "dtdResourceName";
 loader = new AbstractLoader(publicId, dtdResourceName);
 assertTrue(publicIdToResourceNameMap.containsKey(publicId));
 assertEquals(dtdResourceName, publicIdToResourceNameMap.get(publicId));
 }

 @Test
 public void testConstructorWithPublicIdToResourceNameMap() throws Exception {
 Map<String, String> publicIdToResourceNameMapInput = new HashMap<String, String>(1);
 publicIdToResourceNameMapInput.put("publicId", "dtdResourceName");
 loader = new AbstractLoader(publicIdToResourceNameMapInput);
 assertTrue(publicIdToResourceNameMap.equals(publicIdToResourceNameMapInput));
 }

 @Test
 public void testParseInputSource() throws Exception {
 InputStream inputStream = new ByteArrayInputStream("inputSourceContent".getBytes());
 InputSource inputSource = new InputSource(inputStream);
 loader.parseInputSource(inputSource);
 // Add assertions here if needed
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithPublicIdInMap() throws Exception {
 String publicId = "publicId";
 String dtdResourceName = "dtdResourceName";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);
 String systemId = "systemId";
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 InputSource expectedResult = new InputSource(new ByteArrayInputStream("dtdResourceName content".getBytes()));
 InputSource actualResult = loader.resolveEntity(publicId, systemId);
 assertEquals(expectedResult.getByteStream(), actualResult.getByteStream());
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithPublicIdNotInMap() throws Exception {
 String publicId = "publicId";
 String systemId = "systemId";
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 InputSource actualResult = loader.resolveEntity(publicId, systemId);
 assertNull(actualResult);
 }

 @Test
 public void testError() throws Exception {
 SAXParseException exception = new SAXParseException("Test error", null);
 loader.error(exception);
 fail("An exception should have been thrown.");
 }

 @Test
 public void testFatalError() throws Exception {
 SAXParseException exception = new SAXParseException("Test fatal error", null);
 loader.fatalError(exception);
 fail("An exception should have been thrown.");
 }

 @Test
 public void testSetErrorHandler() throws Exception {
 ErrorHandler errorHandlerMock = mock(ErrorHandler.class);
 loader.setErrorHandler(errorHandlerMock.Mock());
 SAXParseException exception = new SAXParseException("Test error", null);
 loader.error(exception);
 verify(errorHandlerMock).error(exception);
 }

 @Test
 public void testSetEntityResolver() throws Exception {
 EntityResolver entityResolverMock = mock(EntityResolver.class);
 loader.setEntityResolver(entityResolverMock.Mock());
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 String publicId = "publicId";
 String systemId = "systemId";
 String resourceName = "resourceName";
 when(entityResolverMock.resolveEntity(publicId, systemId)).thenReturn(new InputSource(new ByteArrayInputStream(resourceName.getBytes())));
 InputSource actualResult = loader.resolveEntity(publicId, systemId);
 InputSource expectedResult = new InputSource(new ByteArrayInputStream(resourceName.getBytes()));
 assertEquals(expectedResult.getByteStream(), actualResult.getByteStream());
 }
}