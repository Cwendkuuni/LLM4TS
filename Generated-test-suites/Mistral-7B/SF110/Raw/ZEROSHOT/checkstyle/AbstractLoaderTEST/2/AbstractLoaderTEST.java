package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

    private AbstractLoader loader;
    private Map<String, String> publicIdToResourceNameMap;

    @Before
    public void setUp() {
        publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put("publicId1", "test-dtd1.dtd");
        loader = new AbstractLoader(publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSourceWithValidInput() throws IOException, SAXException {
        InputStream inputStream = new ByteArrayInputStream("Valid XML content".getBytes());
        InputSource inputSource = new InputSource(inputStream);

        loader.parseInputSource(inputSource);
        verify(loader, times(1)).handleStartElement(anyString(), anyString(), anyInt(), anyMapOf(String.class, String.class));
        verify(loader, times(1)).handleEndElement(anyString(), anyString());
        verify(loader, times(1)).handleCharacters(anyCharArray());
    }

    @Test(expected = SAXException.class)
    public void testResolveEntityWithValidPublicId() throws IOException, SAXException {
        InputSource inputSource = new InputSource(new ByteArrayInputStream("Test data".getBytes()));
        when(loader.resolveEntity("publicId1", anyString())).thenReturn(inputSource);

        loader.parseInputSource(new InputSource(new ByteArrayInputStream("Test data".getBytes())));

        loader.resolveEntity("publicId1", "test-dtd1.dtd");
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws IOException, SAXException {
        when(loader.resolveEntity("invalidPublicId", anyString())).thenCallRealMethod();

        InputSource inputSource = new InputSource(new ByteArrayInputStream("Test data".getBytes()));
        loader.parseInputSource(new InputSource(inputSource));

        InputSource result = loader.resolveEntity("invalidPublicId", "test-dtd1.dtd");
        assertNull(result);
    }

    @Test(expected = SAXException.class)
    public void testErrorHandler() throws IOException, SAXException {
        SAXParseException exception = mock(SAXParseException.class);
        when(loader.parseInputSource(any(InputSource.class))).thenThrow(exception);

        loader.parseInputSource(new InputSource(new ByteArrayInputStream("Test data".getBytes())));

        loader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalErrorHandler() throws IOException, SAXException {
        SAXParseException exception = mock(SAXParseException.class);
        when(loader.parseInputSource(any(InputSource.class))).thenThrow(exception);

        loader.parseInputSource(new InputSource(new ByteArrayInputStream("Test data".getBytes())));

        loader.fatalError(exception);
    }

    @Test
    public void testEmptyPublicIdToResourceNameMap() throws IOException, SAXException {
        loader = new AbstractLoader(Collections.emptyMap());

        InputStream inputStream = new ByteArrayInputStream("Valid XML content".getBytes());
        InputSource inputSource = new InputSource(inputStream);

        loader.parseInputSource(inputSource);
        verify(loader, times(1)).resolveEntity(anyString(), anyString());
    }
}