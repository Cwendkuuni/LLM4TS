package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoaderInstanceWithPublicIdAndDtdResourceName()
            throws SAXException, ParserConfigurationException {
        final String publicId = "testPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";
        final AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);

        Map<String, String> publicIdToResourceNameMap = loader.getPublicIdToResourceNameMap();
        assertEquals(publicId, publicIdToResourceNameMap.keySet().iterator().next());
        assertEquals(dtdResourceName, publicIdToResourceNameMap.get(publicId));
    }

    @Test
    public void testAbstractLoaderInstanceWithPublicIdToResourceNameMap()
            throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put("testPublicId1", "testDtdResourceName1.dtd");
        publicIdToResourceNameMap.put("testPublicId2", "testDtdResourceName2.dtd");

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
        Map<String, String> publicIdToResourceNameMapFromLoader = loader.getPublicIdToResourceNameMap();
        assertEquals(publicIdToResourceNameMap, publicIdToResourceNameMapFromLoader);
    }

    @Test(expected = SAXException.class)
    public void testParseInputSourceAndEntityResolutionWithValidPublicId()
            throws IOException, SAXException {
        final String publicId = "testPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";

        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);

        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        loader.parseInputSource(inputSource);
    }

    @Test(expected = SAXException.class)
    public void testResolveEntityWithInvalidPublicId() throws SAXException, IOException {
        final String publicId = "testInvalidPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";

        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);

        loader.resolveEntity("testInvalidPublicId", "");
    }

    @Test(expected = SAXException.class)
    public void testParseInputSourceAndEntityResolutionWithInvalidPublicId()
            throws IOException, SAXException {
        final String publicId = "testInvalidPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";

        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);

        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        loader.parseInputSource(inputSource);
    }

    @Test(expected = IOException.class)
    public void testResolveEntityWithInvalidSystemId() throws SAXException, IOException {
        final String publicId = "testPublicId";
        final String systemId = "testSystemId";

        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put(publicId, "");

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);

        loader.resolveEntity(publicId, systemId);
    }

    @Test
    public void testResolveEntityWithValidPublicIdAndSystemId()
            throws IOException, SAXException {
        final String publicId = "testPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";
        final String systemId = "testSystemId";

        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);

        InputSource expectedInputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        expectedInputSource.setSystemId(systemId);
        InputSource actualInputSource = loader.resolveEntity(publicId, systemId);

        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test
    public void testError() throws SAXException {
        final AbstractLoader loader = new AbstractLoader(Maps.newHashMap());
        try {
            loader.error(new SAXParseException("Test error", new InputSource(""), null, 1, 1));
            fail("Expected SAXException to be thrown");
        } catch (SAXException e) {
            // Expected
        }
    }

    @Test
    public void testFatalError() throws SAXException {
        final AbstractLoader loader = new AbstractLoader(Maps.newHashMap());
        try {
            loader.fatalError(new SAXParseException("Test fatal error", new InputSource(""), null, 1, 1));
            fail("Expected SAXException to be thrown");
        } catch (SAXException e) {
            // Expected
        }
    }
}