package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.DefaultHandler;
import com.puppycrawl.tools.checkstyle.api.EntityResolver;
import com.puppycrawl.tools.checkstyle.api.ErrorHandler;
import com.puppycrawl.tools.checkstyle.api.SAXParserFactory;
import com.puppycrawl.tools.checkstyle.api.XMLReader;

public class AbstractLoaderTest {

    private AbstractLoader loader;
    private Map<String, String> publicIdToResourceNameMap;

    @Before
    public void setUp() {
        publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "resource1.dtd");
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void warning(SAXParseException exception) throws SAXException {}
        };
    }

    @Test
    public void testAbstractLoaderWithValidPublicId() throws Exception {
        String xml = "<xml><tag/></xml>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        InputSource inputSource = new InputSource(inputStream);

        loader.parseInputSource(inputSource);

        verify(loader, times(1)).resolveEntity("publicId1", anyString());
    }

    @Test(expected = SAXException.class)
    public void testAbstractLoaderWithInvalidPublicId() throws Exception {
        String xml = "<xml><tag/></xml>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        InputSource inputSource = new InputSource(inputStream);

        doReturn(null).when(loader).resolveEntity("invalidPublicId", anyString());

        loader.parseInputSource(inputSource);

        fail("Expected SAXException but didn't get one.");
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws Exception {
        String dtd = "<!DOCTYPE xml [<!ENTITY e ENTITY \"entity.xml\">]>";
        InputStream dtdInputStream = new ByteArrayInputStream(dtd.getBytes());
        InputSource dtdInputSource = new InputSource(dtdInputStream);

        when(loader.resolveEntity("publicId1", anyString())).thenReturn(dtdInputSource);

        loader.parseInputSource(new InputSource(new ByteArrayInputStream("".getBytes())));

        verify(loader, times(1)).parseEntity("e");
    }

    @Test(expected = SAXException.class)
    public void testResolveEntityWithInvalidPublicId() throws Exception {
        doThrow(new SAXParseException("Test error", null)).when(loader)
                .parseEntity("e");

        doReturn(null).when(loader).resolveEntity("invalidPublicId", anyString());

        loader.parseInputSource(new InputSource(new ByteArrayInputStream("".getBytes())));

        fail("Expected SAXException but didn't get one.");
    }

    @Test
    public void testParseInputSourceWithNullInputSource() {
        try {
            loader.parseInputSource(null);
            fail("Expected IOException but didn't get one.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test(expected = IOException.class)
    public void testParseInputSourceWithIOException() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        doThrow(new IOException("Test error")).when(inputStream).read();

        InputSource inputSource = new InputSource(inputStream);

        loader.parseInputSource(inputSource);

        fail("Expected IOException but didn't get one.");
    }

    @Test(expected = SAXException.class)
    public void testError() throws Exception {
        SAXParseException exception = new SAXParseException("Test error", null);

        doThrow(exception).when(loader).error(any(SAXParseException.class));

        InputSource inputSource = new InputSource(new ByteArrayInputStream("".getBytes()));
        loader.parseInputSource(inputSource);

        fail("Expected SAXException but didn't get one.");
    }

    @Test
    public void testWarning() throws Exception {
        SAXParseException exception = new SAXParseException("Test warning", null);

        doCallRealMethod().when(loader).warning(exception);

        InputSource inputSource = new InputSource(new ByteArrayInputStream("".getBytes()));
        loader.parseInputSource(inputSource);

        // No assertions here, as this method is abstract and not meant to be called directly.
    }

    @Test
    public void testFatalError() throws Exception {
        SAXParseException exception = new SAXParseException("Test error", null);

        doThrow(exception).when(loader).fatalError(any(SAXParseException.class));

        InputSource inputSource = new InputSource(new ByteArrayInputStream("".getBytes()));
        loader.parseInputSource(inputSource);

        fail("Expected SAXException but didn't get one.");
    }
}