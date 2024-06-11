package com.example.junit4test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.DefaultHandler;
import com.puppycrawl.tools.checkstyle.api.ErrorHandler;
import com.puppycrawl.tools.checkstyle.api.EntityResolver;
import com.puppycrawl.tools.checkstyle.api.SAXParserFactory;

/**
 * Test cases for {@link AbstractLoader}.
 */
public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private InputStream dtdInputStream;

    @Before
    public void setup() throws Exception {
        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName.dtd");
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                if (publicId != null && publicId.equals("testPublicId")) {
                    return new InputSource(dtdInputStream);
                }
                return super.resolveEntity(publicId, systemId);
            }
        };
        dtdInputStream = new URL("testDtdResourceName.dtd").openStream();
    }

    @Test
    public void testParseInputSourceWithValidDtd() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        abstractLoader.parseInputSource(inputSource);
        verify(abstractLoader, never()).error(any(SAXParseException.class));
        verify(abstractLoader, never()).fatalError(any(SAXParseException.class));
    }

    @Test(expected = SAXException.class)
    public void testParseInputSourceWithInvalidDtd() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[]{'{'}[0]));
        abstractLoader.parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        InputSource expectedInputSource = new InputSource(dtdInputStream);
        when(abstractLoader.resolveEntity("testPublicId", anyString())).thenReturn(expectedInputSource);
        InputSource actualInputSource = abstractLoader.resolveEntity("testPublicId", "testSystemId");
        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws Exception {
        InputSource actualInputSource = abstractLoader.resolveEntity("invalidPublicId", "testSystemId");
        assertNull(actualInputSource);
    }

    @Test(expected = SAXParseException.class)
    public void testErrorDuringParsing() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[]{'<'}[0]));
        abstractLoader.parseInputSource(inputSource);
    }

    @Test(expected = SAXException.class)
    public void testFatalErrorDuringParsing() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[]{'<'}[0]));
        doThrow(new SAXParseException("Test exception")).when(abstractLoader).error(any(SAXParseException.class));
        abstractLoader.parseInputSource(inputSource);
    }
}