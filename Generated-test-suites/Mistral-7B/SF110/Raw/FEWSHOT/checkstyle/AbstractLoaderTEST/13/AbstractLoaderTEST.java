package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public class AbstractLoaderTest {

 private AbstractLoader abstractLoader;
 private DefaultHandler mockDefaultHandler;
 private EntityResolver mockEntityResolver;
 private ErrorHandler mockErrorHandler;
 private InputStream mockDtdResource;
 private InputSource mockInputSource;
 private SAXParserFactory mockSaxParserFactory;

 @Before
 public void setUp() throws Exception {
 mockDefaultHandler = mock(DefaultHandler.class);
 mockEntityResolver = mock(EntityResolver.class);
 mockErrorHandler = mock(ErrorHandler.class);
 mockSaxParserFactory = mock(SAXParserFactory.class);

 abstractLoader = new AbstractLoader(Collections.emptyMap()) {
 @Override
 public DefaultHandler getContentHandler() {
 return mockDefaultHandler;
 }

 @Override
 public EntityResolver getEntityResolver() {
 return mockEntityResolver;
 }

 @Override
 public ErrorHandler getErrorHandler() {
 return mockErrorHandler;
 }
 };

 when(mockSaxParserFactory.newSAXParser().getXMLReader()).thenReturn(abstractLoader.parser);
 when(mockSaxParserFactory.newSAXParser().getXMLReader()).thenReturn(abstractLoader);
 when(mockSaxParserFactory.setValidating(Boolean.TRUE)).thenReturn(mockSaxParserFactory);
 when(mockSaxParserFactory.setNamespaceAware(Boolean.TRUE)).thenReturn(mockSaxParserFactory);
 }

 @Test
 public void testParseInputSourceWithEmptyPublicIdToResourceNameMap() throws IOException, SAXException {
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 abstractLoader.parseInputSource(inputSource);

 // Verify no calls to resolveEntity
 verify(mockEntityResolver, never()).resolveEntity(anyString(), anyString());
 }

 @Test
 public void testParseInputSourceWithPublicIdToResourceNameMap() throws IOException, SAXException {
 final String publicId = "testPublicId";
 final String dtdResourceName = "testDtdResourceName.dtd";
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));

 abstractLoader = new AbstractLoader(Maps.newHashMap()) {
 @Override
 public Map<String, String> getPublicIdToResourceNameMap() {
 return Collections.singletonMap(publicId, dtdResourceName);
 }
 };

 when(mockEntityResolver.resolveEntity(publicId, anyString())).thenReturn(mockInputSource);
 when(mockInputSource.getByteStream()).thenReturn(mockDtdResource);

 abstractLoader.parseInputSource(inputSource);

 // Verify resolveEntity was called once with the correct publicId and null systemId
 verify(mockEntityResolver).resolveEntity(publicId, null);

 // Verify the InputStream of the DTD resource was read
 final byte[] dtdContent = new byte[10];
 mockDtdResource.read(dtdContent);
 // Verify the InputSource of the DTD resource was passed to the content handler
 verify(mockDefaultHandler).startElement(anyString(), anyString(), anyString(), anyMapOfStrings());
 verify(mockDefaultHandler).characters(anyArrayOfBytes());
 }

 @Test
 public void testResolveEntityWithEmptyPublicIdToResourceNameMap() throws IOException, SAXException {
 final String publicId = "testPublicId";
 final String systemId = "testSystemId";

 abstractLoader.resolveEntity(publicId, systemId);

 // Verify no calls to getClass().getResourceAsStream()
 verify(mockEntityResolver, never()).resolveEntity(publicId, systemId);
 }

 @Test
 public void testResolveEntityWithPublicIdToResourceNameMap() throws IOException, SAXException {
 final String publicId = "testPublicId";
 final String dtdResourceName = "testDtdResourceName.dtd";
 final ClassLoader classLoader = getClass().getClassLoader();
 final InputStream dtdResource = classLoader.getResourceAsStream(dtdResourceName);
 final InputSource inputSource = new InputSource(dtdResource);

 abstractLoader = new AbstractLoader(Maps.newHashMap()) {
 @Override
 public Map<String, String> getPublicIdToResourceNameMap() {
 return Collections.singletonMap(publicId, dtdResourceName);
 }
 };

 when(mockEntityResolver.resolveEntity(publicId, anyString())).thenReturn(inputSource);

 abstractLoader.resolveEntity(publicId, null);

 // Verify getClass().getResourceAsStream() was called once with the correct dtdResourceName
 verify(classLoader).getResourceAsStream(dtdResourceName);
 }

 @Test
 public void testError() throws SAXException {
 final SAXParseException exception = new SAXParseException("Test error", null);
 abstractLoader.error(exception);

 // Verify the error was thrown
 verify(mockErrorHandler).error(exception);
 }

 @Test
 public void testFatalError() throws SAXException {
 final SAXParseException exception = new SAXParseException("Test fatal error", null);
 abstractLoader.fatalError(exception);

 // Verify the fatal error was thrown
 verify(mockErrorHandler).fatalError(exception);
 }
}