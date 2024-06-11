package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class AbstractLoaderTest {

    private AbstractLoader loader;
    private InputStream inputStreamMock;

    @Before
    public void setup() {
        inputStreamMock = mock(InputStream.class);
        loader = new AbstractLoader(Collections.emptyMap()) {
            @Override
            public void parseInputSource(final InputSource inputSource) throws IOException, SAXException {
                // do nothing
            }
        };
    }

    @Test
    public void testParseInputSource() throws Exception {
        when(inputStreamMock.available()).thenReturn(100);
        when(inputStreamMock.read(new byte[100])).thenReturn(new byte[100]);

        final InputSource inputSource = new InputSource(inputStreamMock);
        loader.parseInputSource(inputSource);

        // verify that parseInputSource was called
        loader.parser.parse(inputSource);
    }

    @Test
    public void testResolveEntityWithPublicId() throws Exception {
        final String publicId = "testPublicId";
        final String resourceName = "testResourceName.dtd";
        loader.publicIdToResourceNameMap.put(publicId, resourceName);

        final InputStream expectedInputStream = new ByteArrayInputStream(new byte[0]);
        when(inputStreamMock.available()).thenReturn(0);
        when(inputStreamMock.read(new byte[1])).thenReturn(-1);

        final InputSource inputSource = new InputSource(inputStreamMock);
        final InputSource actualInputSource = loader.resolveEntity(publicId, null);

        // verify that resolveEntity was called with the correct arguments
        loader.parser.setEntityResolver(loader);
        loader.parser.parse(new InputSource(new ByteArrayInputStream(new byte[0])));
        loader.parser.getInputSource().setSystemId("test.xml");
        loader.parser.getInputSource().setPublicId(publicId);
        loader.parser.getInputSource().setByteStream(inputStreamMock);

        assertTrue(loader.parser.getInputSource().isSystemIdResolved());
        assertTrue(loader.parser.getInputSource().isPublicIdResolved());
        assertEquals(expectedInputStream, actualInputSource.getByteStream());
    }

    @Test
    public void testResolveEntityWithoutPublicId() throws Exception {
        final String systemId = "testSystemId.dtd";
        loader.publicIdToResourceNameMap.clear();

        when(inputStreamMock.available()).thenReturn(100);
        when(inputStreamMock.read(new byte[100])).thenReturn(new byte[100]);

        final InputSource inputSource = new InputSource(inputStreamMock);
        final InputSource actualInputSource = loader.resolveEntity(null, systemId);

        // verify that resolveEntity was called with the correct arguments
        loader.parser.setEntityResolver(loader);
        loader.parser.parse(inputSource);
        loader.parser.getInputSource().setSystemId(systemId);

        assertTrue(loader.parser.getInputSource().isSystemIdResolved());
        assertNull(loader.parser.getInputSource().getPublicId());
        assertEquals(inputStreamMock, loader.parser.getInputSource().getByteStream());
    }

    @Test
    public void testResolveEntityWithNullInput() throws Exception {
        final String publicId = "testPublicId";
        loader.publicIdToResourceNameMap.put(publicId, null);

        final InputSource inputSource = null;
        final InputSource actualInputSource = loader.resolveEntity(publicId, null);

        // verify that resolveEntity was not called
        loader.parser.setEntityResolver(loader);
        loader.parser.parse(new InputSource(new ByteArrayInputStream(new byte[0])));
        loader.parser.getInputSource().setPublicId(publicId);
        loader.parser.getInputSource().setSystemId(null);

        assertNull(actualInputSource);
    }

    @Test
    public void testResolveEntityWithEmptyMap() throws Exception {
        loader = new AbstractLoader(Collections.emptyMap()) {
            @Override
            public void parseInputSource(final InputSource inputSource) throws IOException, SAXException {
                // do nothing
            }
        };

        final InputSource actualInputSource = loader.resolveEntity(null, null);

        // verify that resolveEntity was not called
        loader.parser.parse(new InputSource(new ByteArrayInputStream(new byte[0])));

        assertNull(actualInputSource);
    }

    @Test(expected = SAXException.class)
    public void testError() throws Exception {
        final SAXParseException exception = mock(SAXParseException.class);
        loader.parser.setErrorHandler(loader);

        loader.error(exception);

        // verify that error was passed through
        loader.parser.getErrorHandler().error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws Exception {
        final SAXParseException exception = mock(SAXParseException.class);
        loader.parser.setErrorHandler(loader);

        loader.fatalError(exception);

        // verify that fatalError was passed through
        loader.parser.getErrorHandler().fatalError(exception);
    }
}