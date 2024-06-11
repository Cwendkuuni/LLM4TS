package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class AbstractLoaderTest {

 private AbstractLoader abstractLoader;
 private XMLReader xmlReaderSpy;
 private InputStream inputStreamMock;
 private InputSource inputSourceMock;
 private EntityResolver entityResolverSpy;

 @Before
 public void setUp() {
 mapAndInitializeAbstractLoader();
 xmlReaderSpy = mock(XMLReader.class);
 inputStreamMock = mock(InputStream.class);
 inputSourceMock = new InputSource(inputStreamMock);
 entityResolverSpy = mock(EntityResolver.class);
 when(entityResolverSpy.resolveEntity(anyString(), anyString())).thenReturn(inputSourceMock);
 }

 private void mapAndInitializeAbstractLoader() {
 Map<String, String> publicIdToResourceNameMap = new HashMap<>();
 publicIdToResourceNameMap.put("publicId1", "resourceName1.dtd");
 abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
 InputSource inputSourceWithData = new InputSource(new ByteArrayInputStream(new byte[0]));
 inputSourceWithData.setSystemId("SystemId");
 when(xmlReaderSpy.parse(inputSourceWithData)).thenReturn(null);
 doReturn(xmlReaderSpy).when(abstractLoader).getParser();

 abstractLoader.parseInputSource(inputSourceWithData);
 verify(xmlReaderSpy, times(1)).parse(inputSourceWithData);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithNoMatchingPublicId() throws IOException, SAXException, ParserConfigurationException {
 when(xmlReaderSpy.getEntityResolver()).thenReturn(entityResolverSpy);
 doReturn(xmlReaderSpy).when(abstractLoader).getParser();

 abstractLoader.resolveEntity("publicIdNotInMap", "systemId");
 }

 @Test
 public void testResolveEntityWithMatchingPublicId() throws IOException, SAXException, ParserConfigurationException {
 doReturn(xmlReaderSpy).when(abstractLoader).getParser();
 doReturn(xmlReaderSpy).when(abstractLoader).getEntityResolver();
 when(xmlReaderSpy.getEntityResolver()).thenReturn(entityResolverSpy);
 when(entityResolverSpy.resolveEntity(eq("publicId1"), anyString())).thenReturn(inputSourceMock);

 InputSource expectedInputSource = inputSourceMock;
 InputSource actualInputSource = abstractLoader.resolveEntity("publicId1", "systemId");
 assertEquals(expectedInputSource, actualInputSource);
 }
}