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
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    private XMLReader parser;
    private AbstractLoader loader;

    @Before
    public void setup() throws SAXException, ParserConfigurationException {
        final Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "resourceName1.dtd");
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public XMLReader getParser() {
                return parser;
            }
        };
        parser = mock(XMLReader.class);
    }

    @Test
    public void testParseInputSourceWithValidInput() throws IOException, SAXException {
        final InputStream inputStream = new ByteArrayInputStream("valid xml".getBytes());
        when(parser.getInputSourceFactory()).thenReturn(new DefaultHandler.InputSourceFactory() {
            @Override
            public InputSource createInputSource(InputStream stream) {
                return new InputSource(stream);
            }
        });
        loader.parseInputSource(new InputSource(inputStream));
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws SAXException, IOException {
        final String publicId = "publicId1";
        final String expectedResourceName = "resourceName1.dtd";
        final InputStream inputStream = new ByteArrayInputStream("valid xml".getBytes());
        when(parser.getInputSourceFactory()).thenReturn(new DefaultHandler.InputSourceFactory() {
            @Override
            public InputSource createInputSource(InputStream stream) {
                return new InputSource(stream);
            }
        });
        when(parser.getEntityResolver()).thenReturn(loader);

        final InputSource inputSource = loader.resolveEntity(publicId, null);
        assertNotNull(inputSource);
        final InputStream actualInputStream = inputSource.getByteStream();
        final byte[] expectedBytes = "valid dtd".getBytes();
        final byte[] actualBytes = new byte[actualInputStream.available()];
        actualInputStream.read(actualBytes);
        assertNotNull(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
        assertNull(loader.publicIdToResourceNameMap.get("anotherPublicId"));
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws SAXException, IOException {
        final String publicId = "anotherPublicId";
        when(parser.getEntityResolver()).thenReturn(loader);
        loader.parseInputSource(new InputSource(new ByteArrayInputStream("valid xml".getBytes())));

        final InputSource inputSource = loader.resolveEntity(publicId, null);
        assertNull(inputSource);
    }

    @Test
    public void testError() throws SAXException {
        final SAXParseException expectedException = new SAXParseException("Test Error", null);
        loader.error(expectedException);
        assertNotNull(expectedException.getException());
    }

    @Test
    public void testFatalError() throws SAXException {
        final SAXParseException expectedException = new SAXParseException("Test Error", null);
        loader.fatalError(expectedException);
        assertNotNull(expectedException.getException());
    }
}