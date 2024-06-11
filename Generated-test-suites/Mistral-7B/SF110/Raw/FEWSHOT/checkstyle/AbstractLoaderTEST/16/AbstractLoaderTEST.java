package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoader() throws Exception {
        final String publicId = "TestPublicId";
        final String dtdResourceName = "TestDTD.dtd";
        AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);
        assertTrue(loader.publicIdToResourceNameMap.containsKey(publicId));
        assertTrue(loader.publicIdToResourceNameMap.get(publicId).equals(dtdResourceName));
    }

    @Test
    public void testParseInputSource() throws Exception {
        final AbstractLoader loader = mock(AbstractLoader.class);
        final InputSource inputSource = mock(InputSource.class);
        when(loader.parser.parse(inputSource)).thenReturn(null);
        loader.parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntity() throws Exception {
        final String publicId = "TestPublicId";
        final String dtdResourceName = "TestDTD.dtd";
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        loader.publicIdToResourceNameMap.put(publicId, dtdResourceName);

        final InputStream expectedDTDStream = new ByteArrayInputStream(new byte[0]);
        final InputSource expectedDTDSource = new InputSource(expectedDTDStream);
        final InputSource inputSource = mock(InputSource.class);
        when(inputSource.getSystemId()).thenReturn("TestSystemID");

        final InputSource actualInputSource = loader.resolveEntity(publicId, null);
        assertEquals(expectedDTDSource, actualInputSource);

        final InputStream actualDTDStream = actualInputSource.getByteStream();
        assertTrue(actualDTDStream.available() == 0);
    }

    @Test
    public void testResolveEntityWithSystemId() throws Exception {
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        final String systemId = "TestSystemID";
        final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        final InputSource actualInputSource = loader.resolveEntity(null, systemId);
        assertNull(actualInputSource);
    }

    @Test(expected = SAXException.class)
    public void testError() throws Exception {
        final AbstractLoader loader = mock(AbstractLoader.class);
        final SAXParseException exception = mock(SAXParseException.class);
        loader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws Exception {
        final AbstractLoader loader = mock(AbstractLoader.class);
        final SAXParseException exception = mock(SAXParseException.class);
        loader.fatalError(exception);
    }

    @Test
    public void testSetContentHandler() throws Exception {
        final AbstractLoader loader = mock(AbstractLoader.class);
        final DefaultHandler handler = mock(DefaultHandler.class);
        loader.setContentHandler(handler);
    }

    @Test
    public void testSetErrorHandler() throws Exception {
        final AbstractLoader loader = mock(AbstractLoader.class);
        final DefaultHandler handler = mock(DefaultHandler.class);
        loader.setErrorHandler(handler);
    }

    @Test
    public void testSetEntityResolver() throws Exception {
        final AbstractLoader loader = mock(AbstractLoader.class);
        final EntityResolver resolver = mock(EntityResolver.class);
        loader.setEntityResolver(resolver);
    }

    private InputSource newInputSource(byte[] data, String systemId) {
        return new InputSource(new ByteArrayInputStream(data), systemId);
    }

    private void assertEquals(InputSource expected, InputSource actual) {
        assertEquals(expected.getByteStream(), actual.getByteStream());
        assertEquals(expected.getSystemId(), actual.getSystemId());
    }

}