package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest {

 private AbstractLoader loader;

 @Before
 public void setUp() throws ParserConfigurationException, SAXException {
 Map<String, String> publicIdToResourceNameMap = new HashMap<>();
 publicIdToResourceNameMap.put("testPublicId", "test.dtd");
 loader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.parseInputSource(inputSource);
 }

 @Test(expected = IOException.class)
 public void testParseInputSourceWithNullInputSource() {
 loader.parseInputSource(null);
 }

 @Test(expected = SAXException.class)
 public void testError() throws IOException, SAXException {
 SAXParseException exception = mock(SAXParseException.class);
 when(exception.getLineNumber()).thenReturn(1);
 when(exception.getColumnNumber()).thenReturn(1);
 when(exception.getMessage()).thenReturn("Test error message.");
 loader.error(exception);
 }

 @Test(expected = SAXException.class)
 public void testFatalError() throws IOException, SAXException {
 SAXParseException exception = mock(SAXParseException.class);
 when(exception.getLineNumber()).thenReturn(1);
 when(exception.getColumnNumber()).thenReturn(1);
 when(exception.getMessage()).thenReturn("Test fatal error message.");
 loader.fatalError(exception);
 }

 @Test
 public void testResolveEntity() throws IOException, SAXException, ParserConfigurationException {
 String testPublicId = "testPublicId";
 String testDtdResourceName = "test.dtd";
 InputStream testDtdIs = mock(InputStream.class);
 when(testDtdIs.available()).thenReturn(10);
 InputSource testInputSource = new InputSource(testDtdIs);
 when(loader.publicIdToResourceNameMap.get(testPublicId)).thenReturn(testDtdResourceName);
 when(loader.getClass().getClassLoader().getResourceAsStream(testDtdResourceName)).thenReturn(testDtdIs);

 InputSource result = loader.resolveEntity(testPublicId, null);
 assertNotNull(result);
 assertNotNull(result.getInputStream());
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithNullPublicId() {
 loader.resolveEntity(null, null);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithNullSystemId() {
 loader.resolveEntity("testPublicId", null);
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithNullInputSource() {
 loader.resolveEntity("testPublicId", mock(InputSource.class));
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithNullInputStream() {
 loader.resolveEntity("testPublicId", new InputSource(mock(InputStream.class)));
 }

 @Test
 public void testResolveEntityWithNonExistentResource()
 throws IOException, SAXException, ParserConfigurationException {
 String nonExistentResourceName = "nonExistent.dtd";
 when(loader.getClass().getClassLoader().getResourceAsStream(nonExistentResourceName)).thenReturn(null);
 InputSource inputSource = loader.resolveEntity("testPublicId", null);
 assertNotNull(inputSource);
 assertNull(inputSource.getInputStream());
 }
}