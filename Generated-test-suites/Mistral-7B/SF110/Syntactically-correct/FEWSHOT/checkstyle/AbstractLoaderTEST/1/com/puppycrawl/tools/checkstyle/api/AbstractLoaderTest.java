package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
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

    private AbstractLoader abstractLoader;
    private XMLReader xmlReader;

    @Before
    public void setup() throws Exception {
        Map<String, String> publicIdToResourceNameMap = Collections.singletonMap("publicId", "resourceName.dtd");
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
        xmlReader = mock(XMLReader.class);
        when(xmlReader.parse(any(InputSource.class))).thenReturn(null);
        abstractLoader.setContentHandler(xmlReader);
    }

    @Test
    public void testParseInputSourceWithValidInputSource() throws Exception {
        final InputSource inputSource = new InputSource(new ByteArrayInputStream("Valid XML".getBytes()));
        abstractLoader.parseInputSource(inputSource);
        // Verify that the content handler received the input
        // This depends on the implementation of the content handler
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws Exception {
        final InputSource inputSource = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>\n".getBytes()));
        when(xmlReader.parse(any(InputSource.class))).thenReturn(null);

        final String publicId = "publicId";
        final String dtdResourceName = "resourceName.dtd";
        InputStream expectedInputStream = getClass().getClassLoader().getResourceAsStream(dtdResourceName);
        InputSource expectedInputSource = new InputSource(expectedInputStream);

        InputSource inputSourceToResolve = new InputSource(new ByteArrayInputStream(("<?xml version=\"1.0\"?>\n<!DOCTYPE root [<!ENTITY % entity SYSTEM \"" + publicId + "\">]>\n<root/>").getBytes()));
        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, null);

        assertEquals(expectedInputSource.getByteStream(), actualInputSource.getByteStream());
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws Exception {
        final InputSource inputSource = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>\n".getBytes()));
        when(xmlReader.parse(any(InputSource.class))).thenReturn(null);

        InputSource actualInputSource = abstractLoader.resolveEntity("invalidPublicId", null);
        assertNull(actualInputSource);
    }

    @Test
    public void testResolveEntityWithNullPublicId() throws Exception {
        final InputSource inputSource = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>\n".getBytes()));
        when(xmlReader.parse(any(InputSource.class))).thenReturn(null);

        InputSource actualInputSource = abstractLoader.resolveEntity(null, null);
        assertNull(actualInputSource);
    }

    @Test
    public void testResolveEntityWithNullSystemId() throws Exception {
        final InputSource inputSource = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>\n".getBytes()));
        when(xmlReader.parse(any(InputSource.class))).thenReturn(null);

        final String publicId = "publicId";
        InputSource actualInputSource = abstractLoader.resolveEntity(publicId, null);
        InputSource expectedInputSource = new InputSource(getClass().getClassLoader().getResourceAsStream(publicIdToResourceNameMap.get(publicId)));
        assertEquals(expectedInputSource.getByteStream(), actualInputSource.getByteStream());
    }

    @Test
    public void testResolveEntityWithValidSystemId() throws Exception {
        final InputSource inputSource = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>\n".getBytes()));
        when(xmlReader.parse(any(InputSource.class))).thenReturn(null);

        final String systemId = "systemId.dtd";
        InputStream expectedInputStream = getClass().getClassLoader().getResourceAsStream(systemId);
        InputSource expectedInputSource = new InputSource(expectedInputStream);

        InputSource actualInputSource = abstractLoader.resolveEntity(null, systemId);

        assertEquals(expectedInputSource.getByteStream(), actualInputSource.getByteStream());
    }
}