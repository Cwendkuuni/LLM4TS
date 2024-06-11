package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoader() throws ParserConfigurationException, SAXException {
        final Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>(1);
        final AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
        assertNull(loader.getPublicIdToResourceNameMap());
        assertNull(loader.getParser());

        publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName.dtd");
        loader = new AbstractLoader(publicIdToResourceNameMap);
        assertSame(publicIdToResourceNameMap, loader.getPublicIdToResourceNameMap());
        assertNotNull(loader.getParser());
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        loader.parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntityWithPublicId() throws IOException, SAXException, ParserConfigurationException {
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        inputSource.setSystemId("testSystemId.dtd");
        final String publicId = "testPublicId";

        final InputSource expected = new InputSource(new ByteArrayInputStream(new byte[0]));
        final InputSource result = loader.resolveEntity(publicId, "testSystemId.dtd");

        assertTrue(expected.equals(result));
    }

    @Test
    public void testResolveEntityWithPublicIdAndDefinedMapping() throws IOException, SAXException, ParserConfigurationException {
        final String dtdResourceName = "testDtdResourceName.dtd";
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        loader.getPublicIdToResourceNameMap().put("testPublicId", dtdResourceName);
        final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        inputSource.setSystemId("testSystemId.dtd");

        final InputStream expectedDtdIs = getClass().getResourceAsStream(dtdResourceName);
        final InputSource expected = new InputSource(expectedDtdIs);
        final InputSource result = loader.resolveEntity("testPublicId", "testSystemId.dtd");

        assertTrue(expected.equals(result));
    }

    @Test
    public void testResolveEntityWithSystemId() throws IOException, SAXException, ParserConfigurationException {
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        inputSource.setPublicId("testPublicId");
        final String systemId = "testSystemId.dtd";

        final InputStream expectedDtdIs = getClass().getResourceAsStream(systemId);
        final InputSource expected = new InputSource(expectedDtdIs);
        final InputSource result = loader.resolveEntity("testPublicId", systemId);

        assertFalse(expected.equals(result));
        assertTrue(result.getSystemId().equals(systemId));
    }

    @Test(expected = SAXParseException.class)
    public void testError() throws ParserConfigurationException, SAXException {
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        loader.error(new SAXParseException("Test error", null));
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws ParserConfigurationException, SAXException {
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        loader.fatalError(new SAXParseException("Test fatal error", null));
    }
}