package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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
import java.util.concurrent.ConcurrentHashMap;

public class AbstractLoaderTest {

    private XMLReader parser;
    private AbstractLoader loader;

    @Before
    public void setup() throws Exception {
        final Map<String, String> publicIdToResourceNameMap = new ConcurrentHashMap<>(1);
        parser = mock(XMLReader.class);
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public XMLReader getParser() {
                return parser;
            }
        };
    }

    @Test
    public void testAbstractLoaderConstructionWithPublicIdAndDtdResourceName() throws Exception {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName";
        loader = new AbstractLoader(publicId, dtdResourceName);
        assertTrue(loader.getPublicIdToResourceNameMap().containsKey(publicId));
        assertEquals(dtdResourceName, loader.getPublicIdToResourceNameMap().get(publicId));
    }

    @Test
    public void testAbstractLoaderConstructionWithPublicIdToResourceNameMap() throws Exception {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName";
        AbstractLoader loader = new AbstractLoader(Collections.singletonMap(publicId, dtdResourceName));
        assertTrue(loader.getPublicIdToResourceNameMap().containsKey(publicId));
        assertEquals(dtdResourceName, loader.getPublicIdToResourceNameMap().get(publicId));
    }

    @Test
    public void testResolveEntityWithPublicId() throws Exception {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName";
        loader.getPublicIdToResourceNameMap().put(publicId, dtdResourceName);
        InputStream expectedInputStream = new ByteArrayInputStream("expectedData".getBytes());
        when(parser.getInputSource(expectedInputStream)).thenReturn(new InputSource(expectedInputStream));
        InputSource actualInputSource = loader.resolveEntity(publicId, null);
        InputStream actualInputStream = actualInputSource.getByteStream();
        InputStream expectedInputStream1 = loader.getParser().getInputSource(expectedInputStream);
        InputStream expectedInputStream2 = loader.resolveEntity(publicId, null);
        assertSame(expectedInputStream, actualInputStream);
        assertSame(expectedInputStream1, expectedInputStream2);
    }

    @Test
    public void testResolveEntityWithoutPublicId() throws Exception {
        InputSource actualInputSource = loader.resolveEntity(null, null);
        assertNull(actualInputSource);
    }

    @Test
    public void testParseInputSource() throws Exception {
        String data = "<root><child/></root>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(data.getBytes()));
        loader.parseInputSource(inputSource);
        // Add your assertions here for testing the behavior of the parseInputSource method.
    }

    @Test
    public void testError() throws Exception {
        SAXParseException exception = mock(SAXParseException.class);
        loader.error(exception);
        // Add your assertions here for testing the behavior of the error method.
    }

    @Test
    public void testFatalError() throws Exception {
        SAXParseException exception = mock(SAXParseException.class);
        loader.fatalError(exception);
        // Add your assertions here for testing the behavior of the fatalError method.
    }
}