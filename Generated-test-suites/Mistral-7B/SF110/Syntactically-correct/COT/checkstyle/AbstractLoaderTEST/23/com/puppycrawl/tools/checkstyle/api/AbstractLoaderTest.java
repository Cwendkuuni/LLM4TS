package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest {

    private AbstractLoader loader;
    private Map<String, String> publicIdToResourceNameMap;

    @Before
    public void setup() {
        publicIdToResourceNameMap = new HashMap<String, String>(1);
        loader = new AbstractLoader(publicIdToResourceNameMap);
    }

    @Test
    public void testAbstractLoaderConstructorWithPublicIdAndDtdResourceName() throws Exception {
        publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName.dtd");
        loader = new AbstractLoader("testPublicId", "testDtdResourceName.dtd");
        assertNotNull(loader);
    }

    @Test
    public void testAbstractLoaderConstructorWithPublicIdToResourceNameMap() throws Exception {
        publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName.dtd");
        loader = new AbstractLoader(publicIdToResourceNameMap);
        assertNotNull(loader);
    }

    @Test
    public void testParseInputSource() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream("Test Input".getBytes()));
        loader.parseInputSource(inputSource);
    }

    @Test(expected = IOException.class)
    public void testParseInputSourceWithNullInputSource() throws Exception {
        loader.parseInputSource(null);
    }

    @Test(expected = SAXException.class)
    public void testParseInputSourceWithSAXException() throws Exception {
        InputSource inputSource = mock(InputSource.class);
        when(inputSource.getByteStream()).thenThrow(new SAXException("Test SAXException"));
        loader.parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntityWithExistingPublicId() throws Exception {
        publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName.dtd");
        InputStream dtdIs = new ByteArrayInputStream("Test DTD".getBytes());
        InputSource dtdInputSource = new InputSource(dtdIs);
        when(loader.parser.getEntityResolver()).thenReturn(new DefaultHandler() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return dtdInputSource;
            }
        });

        InputSource result = loader.resolveEntity("testPublicId", null);
        assertNotNull(result);
    }

    @Test
    public void testResolveEntityWithNonExistingPublicId() throws Exception {
        InputSource result = loader.resolveEntity("nonExistingPublicId", null);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testResolveEntityWithNullPublicId() throws Exception {
        loader.resolveEntity(null, null);
    }

    @Test(expected = SAXException.class)
    public void testResolveEntityWithSAXException() throws Exception {
        when(loader.parser.getEntityResolver()).thenThrow(new SAXException("Test SAXException"));
        InputSource result = loader.resolveEntity("testPublicId", null);
        assertNull(result);
    }
}