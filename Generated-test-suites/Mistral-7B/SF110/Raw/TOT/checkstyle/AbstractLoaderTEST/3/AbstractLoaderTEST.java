package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSourceWithValidPublicId() throws IOException, SAXException {
        String publicId = "testPublicId";
        String dtdResourceName = "test.dtd";
        AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);
        InputSource inputSource = createTestInputSource("<?xml-model href=\"test.dtd\"?>");
        loader.parseInputSource(inputSource);
        // Add assertions based on the behavior of the AbstractLoader after parsing
    }

    @Test
    public void testParseInputSourceWithInvalidPublicId() throws IOException, SAXException {
        String publicId = "InvalidPublicId";
        String dtdResourceName = "test.dtd";
        AbstractLoader loader = new AbstractLoader(Maps.newHashMap());
        loader.publicIdToResourceNameMap.put(publicId, dtdResourceName);
        InputSource inputSource = createTestInputSource("<?xml-model href=\"test.dtd\"?>");
        try {
            loader.parseInputSource(inputSource);
            fail("Expected SAXException when publicId is not present in the map");
        } catch (SAXException e) {
            // Expected exception, no need to assert
        }
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws IOException, SAXException {
        String publicId = "testPublicId";
        String dtdResourceName = "test.dtd";
        AbstractLoader loader = new AbstractLoader(Maps.newHashMap());
        loader.publicIdToResourceNameMap.put(publicId, dtdResourceName);
        InputStream dtdIs = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
        InputSource dtdInputSource = new InputSource(dtdIs);
        InputSource expectedInputSource = new InputSource(dtdIs);
        InputSource actualInputSource = loader.resolveEntity(publicId, null);
        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws IOException, SAXException {
        String publicId = "InvalidPublicId";
        AbstractLoader loader = new AbstractLoader(Maps.newHashMap());
        InputSource actualInputSource = loader.resolveEntity(publicId, null);
        assertNull(actualInputSource);
    }

    private InputSource createTestInputSource(String xmlContent) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());
        return new InputSource(inputStream);
    }
}