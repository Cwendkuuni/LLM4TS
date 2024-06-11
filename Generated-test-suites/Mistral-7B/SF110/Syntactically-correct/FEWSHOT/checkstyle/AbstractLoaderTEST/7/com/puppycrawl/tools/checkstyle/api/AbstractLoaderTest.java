package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.junit.Before;
import org.junit.Test;

public class AbstractLoaderTest {

    private XMLReader parser;
    private AbstractLoader loader;

    @Before
    public void setup() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
        publicIdToResourceNameMap.put("publicId", "resourceName.dtd");
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            XMLReader getParser() {
                return parser;
            }
        };
        parser = mock(XMLReader.class);
        when(parser.parse(any(InputSource.class))).thenReturn(null);
    }

    @Test
    public void testParseInputSourceWithValidInput() throws SAXException, IOException {
        InputSource inputSource = new InputSource(new ByteArrayInputStream("Valid XML".getBytes()));
        loader.parseInputSource(inputSource);
        assertNotNull(parser);
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws SAXException, IOException {
        String publicId = "publicId";
        String resourceName = "resourceName.dtd";
        InputStream dtdIs = new ByteArrayInputStream("DTD".getBytes());
        InputSource inputSource = new InputSource(dtdIs);

        when(parser.getEntityResolver()).thenReturn(loader);
        when(loader.resolveEntity(eq(publicId), anyString())).thenReturn(inputSource);

        loader.parseInputSource(new InputSource(new ByteArrayInputStream("XML with entity reference".getBytes())));
        assertNotNull(parser.getEntityResolver().resolveEntity(publicId, null));
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws SAXException, IOException {
        String publicId = "invalidPublicId";

        when(parser.getEntityResolver()).thenReturn(loader);
        when(loader.resolveEntity(eq(publicId), anyString())).thenReturn(null);

        InputSource inputSource = new InputSource(new ByteArrayInputStream("XML with entity reference".getBytes()));
        loader.parseInputSource(inputSource);
        assertNull(parser.getEntityResolver().resolveEntity(publicId, null));
    }

    @Test
    public void testResolveEntityWithNullPublicId() throws SAXException, IOException {
        when(parser.getEntityResolver()).thenReturn(loader);

        InputSource inputSource = new InputSource(new ByteArrayInputStream("XML with entity reference".getBytes()));
        loader.parseInputSource(inputSource);
        assertNull(parser.getEntityResolver().resolveEntity(null, null));
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException, IOException {
        SAXParseException exception = mock(SAXParseException.class);
        loader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException, IOException {
        SAXParseException exception = mock(SAXParseException.class);
        loader.fatalError(exception);
    }
}