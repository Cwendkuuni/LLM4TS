package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.XMLInputFactory;
import javax.xml.parsers.XMLParserConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class AbstractLoaderTest {

 private Map<String, String> publicIdToResourceNameMap;
 private XMLReader parser;

 @Test
 public void testAbstractLoaderInstance() throws ParserConfigurationException, SAXException {
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 assertNotNull(abstractLoader);
 assertNotNull(abstractLoader.parser);
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
 InputStream inputStream = mock(InputStream.class);
 when(inputStream.available()).thenReturn(1);
 when(inputStream.read()).thenReturn('X');
 InputSource inputSource = new InputSource(inputStream);
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 abstractLoader.parseInputSource(inputSource);
 verify(abstractLoader.parser, times(1)).parse(inputSource);
 }

 @Test
 public void testResolveEntityWithPublicId() throws IOException, SAXException, ParserConfigurationException {
 String publicId = "publicId";
 String resourceName = "resourceName";
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>() {{
 put(publicId, resourceName);
 }});
 InputStream inputStream = mock(InputStream.class);
 when(inputStream.available()).thenReturn(1);
 when(inputStream.read()).thenReturn('X');
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[]{'X'}));
 InputSource expectedInputSource = new InputSource(inputStream);
 InputSource actualInputSource = abstractLoader.resolveEntity(publicId, null);
 assertEquals(expectedInputSource, actualInputSource);
 }

 @Test
 public void testResolveEntityWithPublicIdAndSystemId() throws IOException, SAXException, ParserConfigurationException {
 String publicId = "publicId";
 String systemId = "systemId";
 String resourceName = "resourceName";
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>() {{
 put(publicId, resourceName);
 }});
 InputStream inputStream = mock(InputStream.class);
 when(inputStream.available()).thenReturn(1);
 when(inputStream.read()).thenReturn('X');
 InputSource expectedInputSource = new InputSource(new ByteArrayInputStream(new byte[]{'X'}));
 InputSource inputSource = new InputSource(inputStream);
 InputSource actualInputSource = abstractLoader.resolveEntity(publicId, systemId);
 assertEquals(expectedInputSource, actualInputSource);
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityWithoutPublicId() throws IOException, SAXException, ParserConfigurationException {
 String systemId = "systemId";
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[]{}));
 InputSource actualInputSource = abstractLoader.resolveEntity(null, systemId);
 fail("Should have thrown SAXParseException");
 }

 @Test
 public void testError() throws IOException, SAXException {
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 SAXParseException expectedException = mock(SAXParseException.class);
 abstractLoader.error(expectedException);
 verify(expectedException, times(1)).printStackTrace();
 }

 @Test
 public void testFatalError() throws IOException, SAXException {
 AbstractLoader abstractLoader = new AbstractLoader(new HashMap<String, String>(1));
 SAXParseException expectedException = mock(SAXParseException.class);
 abstractLoader.fatalError(expectedException);
 verify(expectedException, times(1)).printStackTrace();
 }
}