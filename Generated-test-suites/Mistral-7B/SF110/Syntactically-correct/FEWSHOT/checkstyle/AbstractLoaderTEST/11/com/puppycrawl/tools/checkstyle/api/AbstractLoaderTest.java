package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReader;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private XMLReader xmlReader;
    private Map<String, String> publicIdToResourceNameMap;

    @Before
    public void setup() throws Exception {
        publicIdToResourceNameMap = new HashMap<>();
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
        xmlReader = mock(XMLReader.class);
        when(xmlReader.parse(any(InputSource.class))).thenReturn(null);
        when(xmlReader.setContentHandler(any(DefaultHandler.class))).thenReturn(xmlReader);
        when(xmlReader.setEntityResolver(any(EntityResolver.class))).thenReturn(xmlReader);
        when(xmlReader.setErrorHandler(any(ErrorHandler.class))).thenReturn(xmlReader);
        when(xmlReader.getParser()).thenReturn(mock(SAXParser.class));
        abstractLoader.parser = xmlReader;
    }

    @Test
    public void testAbstractLoaderWithPublicIdAndDtdResourceName() throws Exception {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName";
        abstractLoader = new AbstractLoader(publicId, dtdResourceName);
        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        abstractLoader.parseInputSource(inputSource);
        verify(xmlReader, times(1)).parse(inputSource);
        verify(xmlReader, times(1)).setContentHandler(any(DefaultHandler.class));
        verify(xmlReader, times(1)).setEntityResolver(any(EntityResolver.class));
        verify(xmlReader, times(1)).setErrorHandler(any(ErrorHandler.class));
    }

    @Test
    public void testAbstractLoaderWithPublicIdToResourceNameMap() throws Exception {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName";
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        abstractLoader.parseInputSource(inputSource);
        verify(xmlReader, times(1)).parse(inputSource);
        verify(xmlReader, times(1)).setContentHandler(any(DefaultHandler.class));
        verify(xmlReader, times(1)).setEntityResolver(any(EntityResolver.class));
        verify(xmlReader, times(1)).setErrorHandler(any(ErrorHandler.class));
    }

    @Test
    public void testResolveEntity() throws Exception {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName";
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        InputStream dtdIs = mock(InputStream.class);
        when(dtdIs.available()).thenReturn(1024);
        when(dtdIs.read()).thenReturn(-1);
        when(dtdIs.markSupported()).thenReturn(true);
        when(dtdIs.reset()).thenReturn(true);
        when(xmlReader.getEntityResolver()).thenReturn(abstractLoader);
        when(abstractLoader.resolveEntity(eq(publicId), anyString())).thenReturn(new InputSource(dtdIs));

        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        InputSource resolvedEntity = abstractLoader.resolveEntity(publicId, "");
        assertEquals(new InputSource(dtdIs), resolvedEntity);
    }

    @Test
    public void testResolveEntityWhenPublicIdIsNotPresent() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        InputSource resolvedEntity = abstractLoader.resolveEntity("publicId", "");
        assertNull(resolvedEntity);
    }

    @Test(expected = SAXException.class)
    public void testError() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        SAXParseException exception = new SAXParseException("Test exception", null, null, null, null);
        when(xmlReader.getErrorHandler()).thenReturn(abstractLoader);
        abstractLoader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        SAXParseException exception = new SAXParseException("Test exception", null, null, null, null);
        when(xmlReader.getErrorHandler()).thenReturn(abstractLoader);
        abstractLoader.fatalError(exception);
    }
}