package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private Map<String, String> publicIdToResourceNameMap;

    @Before
    public void setUp() {
        publicIdToResourceNameMap = new HashMap<String, String>();
        publicIdToResourceNameMap.put("publicId1", "resourceName1.dtd");
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        abstractLoader.parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntityWithPublicId() throws IOException, SAXException {
        InputSource expectedInputSource = createInputSourceFromClasspath("resourceName1.dtd");
        String publicId = "publicId1";
        String systemId = "systemId";

        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);
        assertSame(expectedInputSource, inputSource);
    }

    @Test
    public void testResolveEntityWithSystemId() throws IOException, SAXException {
        InputSource expectedInputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        String publicId = null;
        String systemId = "systemId";

        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);
        assertSame(expectedInputSource, inputSource);
    }

    @Test
    public void testResolveEntityWithNullPublicIdAndSystemId() throws IOException, SAXException {
        InputSource expectedInputSource = null;
        String publicId = null;
        String systemId = null;

        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);
        assertNull(inputSource);
    }

    @Test
    public void testResolveEntityWithNullPublicIdAndValidSystemId() throws IOException, SAXException {
        InputSource expectedInputSource = createInputSourceFromClasspath("validSystemId.dtd");
        String publicId = null;
        String systemId = "validSystemId";

        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);
        assertSame(expectedInputSource, inputSource);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException, IOException {
        SAXParseException exception = mock(SAXParseException.class);
        when(exception.getMessage()).thenReturn("Test error message");
        abstractLoader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException, IOException {
        SAXParseException exception = mock(SAXParseException.class);
        when(exception.getMessage()).thenReturn("Test fatal error message");
        abstractLoader.fatalError(exception);
    }

    private InputSource createInputSourceFromClasspath(String resourceName) {
        ClassLoader classLoader = AbstractLoaderTest.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceName);
        return new InputSource(new InputStreamReader(inputStream));
    }
}