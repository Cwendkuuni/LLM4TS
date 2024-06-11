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

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.junit.Test;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoader() throws Exception {
        final String publicId = "testPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";
        AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);
        assertNotNull(loader);
        assertEquals(1, loader.publicIdToResourceNameMap.size());
        assertEquals(dtdResourceName, loader.publicIdToResourceNameMap.get(publicId));
    }

    @Test
    public void testAbstractLoaderEmptyMap() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
        assertNotNull(loader);
        assertEquals(0, loader.publicIdToResourceNameMap.size());
    }

    @Test
    public void testParseInputSource() throws Exception {
        final String xmlContent = "<root/>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlContent.getBytes()));
        XMLReader parser = mock(XMLReader.class);
        when(parser.parse(inputSource)).thenReturn(null);
        AbstractLoader loader = new AbstractLoader(null) {
            @Override
            XMLReader getParser() {
                return parser;
            }
        };
        loader.parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntity() throws Exception {
        final String publicId = "testPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1)) {
            @Override
            InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                assertNull(systemId);
                assertEquals(dtdResourceName, super.resolveEntity(publicId, systemId).getSystemId());
                return super.resolveEntity(publicId, systemId);
            }
        };
        InputSource inputSource = loader.resolveEntity(publicId, null);
        assertNotNull(inputSource);
    }

    @Test
    public void testResolveEntityUnknownPublicId() throws Exception {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        InputSource inputSource = loader.resolveEntity("unknownPublicId", null);
        assertNull(inputSource);
    }

    @Test
    public void testResolveEntityNullMap() throws Exception {
        AbstractLoader loader = new AbstractLoader(null);
        InputSource inputSource = loader.resolveEntity("testPublicId", null);
        assertNull(inputSource);
    }
}