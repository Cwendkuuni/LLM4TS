package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.DefaultHandler;
import com.puppycrawl.tools.checkstyle.api.XMLReader;
import com.puppycrawl.tools.checkstyle.api.XMLEntityResolver;
import com.puppycrawl.tools.checkstyle.api.XMLErrorHandler;

public class AbstractLoaderTest {

    private AbstractLoader loader;

    @Before
    public void setup() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put("publicId1", "resource1.dtd");
        loader = new AbstractLoader(publicIdToResourceNameMap);
    }

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        AbstractLoader loader = new AbstractLoader("publicId1", "resource1.dtd");
        assertNotNull(loader.getParser());
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
        assertNotNull(loader.getParser());
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));
        loader.parseInputSource(inputSource);
        // add assertions here if needed
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws SAXException, IOException {
        String publicId = "publicId1";
        String expectedResourceName = "resource1.dtd";
        InputSource expectedInputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));
        InputSource actualInputSource = loader.resolveEntity(publicId, null);
        assertTrue(actualInputSource != null);
        assertTrue(actualInputSource.getSystemId().equals(expectedResourceName));
        assertTrue(actualInputSource.getByteStream().available() > 0);
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws SAXException, IOException {
        String publicId = "nonExistentPublicId";
        InputSource actualInputSource = loader.resolveEntity(publicId, null);
        assertNull(actualInputSource);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        loader.error(new SAXParseException("Test error", new InputSource("test.xml"), 1, 1, null));
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        loader.fatalError(new SAXParseException("Test fatal error", new InputSource("test.xml"), 1, 1, null));
    }
}