package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.DefaultHandler;
import com.puppycrawl.tools.checkstyle.api.XMLReader;
import com.puppycrawl.tools.checkstyle.api.XMLEntityResolver;
import com.puppycrawl.tools.checkstyle.api.XMLErrorHandler;

public class AbstractLoaderTest {

    @SuppressWarnings("unchecked")
    private Map<String, String> getDefaultPublicIdToResourceNameMap() {
        return Maps.newHashMap((Map<?, ?>) new AbstractLoader(Maps.newHashMap()).getPublicIdToResourceNameMap());
    }

    @Test
    public void testAbstractLoaderConstructionWithPublicIdAndDtdResourceName() throws Exception {
        final String publicId = "somePublicId";
        final String dtdResourceName = "someDtdResourceName.dtd";
        final AbstractLoader abstractLoader = new AbstractLoader(publicId, dtdResourceName);

        final Map<String, String> publicIdToResourceNameMap = abstractLoader.getPublicIdToResourceNameMap();
        assertEquals(1, publicIdToResourceNameMap.size());
        assertEquals(dtdResourceName, publicIdToResourceNameMap.get(publicId));
    }

    @Test
    public void testAbstractLoaderConstructionWithPublicIdToResourceNameMap() throws Exception {
        final Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put("somePublicId", "someDtdResourceName.dtd");

        final AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);

        final Map<String, String> map = abstractLoader.getPublicIdToResourceNameMap();
        assertEquals(1, map.size());
        assertEquals(publicIdToResourceNameMap.get("somePublicId"), map.get("somePublicId"));
    }

    @Test
    public void testParseInputSourceWithValidInputSource() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap());

        final InputSource inputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));
        abstractLoader.parseInputSource(inputSource);

        // TODO: add assertions to verify the state of the AbstractLoader instance after parsing
    }

    @Test(expected = IOException.class)
    public void testParseInputSourceWithNullInputSource() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap());

        abstractLoader.parseInputSource(null);
    }

    @Test(expected = SAXException.class)
    public void testParseInputSourceWithSAXParseException() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap()) {
            @Override
            public void error(final SAXParseException exception) {
                throw new RuntimeException(exception);
            }

            @Override
            public void fatalError(final SAXParseException exception) {
                throw new RuntimeException(exception);
            }
        };

        final InputSource inputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));
        abstractLoader.parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntityWithKnownPublicId() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap());

        final String publicId = "somePublicId";
        final String dtdResourceName = "someDtdResourceName.dtd";
        final InputSource expectedInputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));

        final InputSource inputSource = abstractLoader.resolveEntity(publicId, null);
        assertEquals("expected InputStream should be returned", expectedInputSource.getByteStream(), inputSource.getByteStream());
    }

    @Test
    public void testResolveEntityWithUnknownPublicId() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap());

        final String publicId = "unknownPublicId";
        final InputSource expectedNullInputSource = null;

        final InputSource inputSource = abstractLoader.resolveEntity(publicId, null);
        assertNull("null InputStream should be returned", inputSource.getByteStream());
    }

    @Test(expected = IOException.class)
    public void testResolveEntityWithNullPublicId() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap());

        abstractLoader.resolveEntity(null, null);
    }

    @Test(expected = SAXException.class)
    public void testResolveEntityWithSAXParseException() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap()) {
            @Override
            public void error(final SAXParseException exception) {
                throw new RuntimeException(exception);
            }

            @Override
            public void fatalError(final SAXParseException exception) {
                throw new RuntimeException(exception);
            }
        };

        abstractLoader.resolveEntity("somePublicId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveEntityWithNullSystemId() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap());

        abstractLoader.resolveEntity("somePublicId", null);
    }

    @Test
    public void testError() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap()) {
            @Override
            public void error(final SAXParseException exception) {
                // TODO: add assertions to verify the state of the AbstractLoader instance and the exception
                throw exception;
            }
        };

        final InputSource inputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));
        abstractLoader.parseInputSource(inputSource);
    }

    @Test
    public void testFatalError() throws Exception {
        final AbstractLoader abstractLoader = new AbstractLoader(getDefaultPublicIdToResourceNameMap()) {
            @Override
            public void fatalError(final SAXParseException exception) {
                // TODO: add assertions to verify the state of the AbstractLoader instance and the exception
                throw exception;
            }
        };

        final InputSource inputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));
        abstractLoader.parseInputSource(inputSource);
    }
}