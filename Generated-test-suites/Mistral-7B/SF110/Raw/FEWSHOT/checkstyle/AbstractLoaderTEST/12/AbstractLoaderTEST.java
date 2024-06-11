package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public class AbstractLoaderTest {

    private XMLReader parser;
    private AbstractLoader abstractLoader;

    @Before
    public void setUp() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "resourceName1.dtd");
        parser = mock(XMLReader.class);
        when(parser.getXMLReader()).thenReturn(parser);
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            XMLReader getParser() {
                return parser;
            }
        };
    }

    @Test
    public void testParseInputSource() throws Exception {
        final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        abstractLoader.parseInputSource(inputSource);
    }

    @Test(expected = IOException.class)
    public void testParseInputSourceWithNullInputSource() throws Exception {
        abstractLoader.parseInputSource(null);
    }

    @Test(expected = SAXException.class)
    public void testParseInputSourceWithSAXException() throws Exception {
        final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        when(parser.parse(inputSource)).thenThrow(new SAXParseException("Test SAXException"));

        abstractLoader.parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntityWithExistingPublicId() throws Exception {
        final String publicId = "publicId1";
        final String resourceName = "resourceName1.dtd";

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        InputSource expectedInputSource = new InputSource(inputStream);

        when(parser.getInputSource()).thenReturn(expectedInputSource);
        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, null);

        assertNotNull(actualInputSource);
        assertEquals(expectedInputSource.getInputStream(), actualInputSource.getInputStream());
    }

    @Test
    public void testResolveEntityWithNonExistingPublicId() throws Exception {
        final String publicId = "nonExistingPublicId";

        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, null);

        assertNull(actualInputSource);
    }

    @Test
    public void testResolveEntityWithNullPublicId() throws Exception {
        final String publicId = null;

        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, null);

        assertNull(actualInputSource);
    }

    @Test
    public void testResolveEntityWithNullSystemId() throws Exception {
        final String publicId = "publicId1";

        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, null);

        assertNull(actualInputSource);
    }

    @Test
    public void testResolveEntityWithSystemId() throws Exception {
        final String publicId = "publicId1";
        final String systemId = "systemId1.dtd";
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        InputSource expectedInputSource = new InputSource(inputStream);

        when(parser.getInputSource()).thenReturn(expectedInputSource);

        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, systemId);

        assertNotNull(actualInputSource);
        assertEquals(expectedInputSource.getInputStream(), actualInputSource.getInputStream());
    }
}