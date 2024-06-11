package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.DefaultErrorReporter;
import com.puppycrawl.tools.checkstyle.api.ErrorManager;
import com.puppycrawl.tools.checkstyle.api.InputSource;
import com.puppycrawl.tools.checkstyle.api.SAXParseError;
import com.puppycrawl.tools.checkstyle.api.XmlParser;

public class AbstractLoaderTest {

    private AbstractLoader loader;
    private ErrorManager errorManager;

    @Before
    public void setUp() {
        errorManager = new DefaultErrorReporter();
        loader = new AbstractLoader(Maps.newHashMap()) {
            @Override
            public ErrorManager getErrorManager() {
                return errorManager;
            }
        };
    }

    @Test
    public void testDefaultConstructor() throws Exception {
        loader = new AbstractLoader();
        assertNull(loader.getPublicIdToResourceNameMap());
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws Exception {
        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put("publicId1", "resource1.dtd");
        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
        assertEquals(publicIdToResourceNameMap, loader.getPublicIdToResourceNameMap());
    }

    @Test
    public void testParseInputSourceWithNullInputSource() throws Exception {
        try {
            loader.parseInputSource(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseInputSourceWithEmptyInputSource() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        loader.parseInputSource(inputSource);
        assertTrue(errorManager.hasErrors());
    }

    @Test
    public void testParseInputSourceWithValidInputSource() throws Exception {
        String xml = "<root/>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
        loader.parseInputSource(inputSource);
        assertTrue(errorManager.isEmpty());
    }

    @Test
    public void testResolveEntityWithNullPublicId() throws Exception {
        String dtd = "<!DOCTYPE root [ <!ENTITY e EntityPublic \"entity.xml\"> ]>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(dtd.getBytes()));
        loader.parseInputSource(inputSource);

        InputSource result = loader.resolveEntity(null, null);
        assertNull(result);
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws Exception {
        String publicId = "publicId1";
        String dtdResourceName = "resource1.dtd";
        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        loader = new AbstractLoader(publicIdToResourceNameMap);

        String dtd = "<!DOCTYPE root [ <!ENTITY e EntityPublic \"%" + publicId + "\"> ]>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(dtd.getBytes()));
        loader.parseInputSource(inputSource);

        InputSource result = loader.resolveEntity(publicId, null);
        InputStream expectedIs = AbstractLoaderTest.class.getResourceAsStream(dtdResourceName);
        InputSource expectedInputSource = new InputSource(expectedIs);
        assertEquals(expectedInputSource, result);
    }

    @Test
    public void testResolveEntityWithNullSystemId() throws Exception {
        String publicId = "publicId1";
        String dtdResourceName = "resource1.dtd";
        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        loader = new AbstractLoader(publicIdToResourceNameMap);

        String dtd = "<!DOCTYPE root [ <!ENTITY e EntityPublic \"%" + publicId + "\"> ]>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(dtd.getBytes()));
        loader.parseInputSource(inputSource);

        InputSource result = loader.resolveEntity(publicId, null);
        InputStream expectedIs = AbstractLoaderTest.class.getResourceAsStream(dtdResourceName);
        InputSource expectedInputSource = new InputSource(expectedIs);
        assertEquals(expectedInputSource, result);
    }

    @Test
    public void testResolveEntityWithValidPublicIdAndSystemId() throws Exception {
        String publicId = "publicId1";
        String dtdResourceName = "resource1.dtd";
        String expectedSystemId = "expectedSystemId";
        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        loader = new AbstractLoader(publicIdToResourceNameMap);

        String dtd = "<!DOCTYPE root [ <!ENTITY e EntityPublic \"%" + publicId + " systemId \"%" + expectedSystemId + "\"> ]>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(dtd.getBytes()));
        loader.parseInputSource(inputSource);

        InputSource result = loader.resolveEntity(publicId, expectedSystemId);
        InputStream expectedIs = AbstractLoaderTest.class.getResourceAsStream(dtdResourceName);
        InputSource expectedInputSource = new InputSource(expectedIs);
        assertEquals(expectedInputSource, result);
    }

    @Test
    public void testResolveEntityWithUnknownPublicId() throws Exception {
        String publicId = "publicId1";
        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        loader = new AbstractLoader(publicIdToResourceNameMap);

        String dtd = "<!DOCTYPE root [ <!ENTITY e EntityPublic \"%" + publicId + "\"> ]>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(dtd.getBytes()));
        loader.parseInputSource(inputSource);

        InputSource result = loader.resolveEntity(publicId, null);
        assertNull(result);
    }

    @Test
    public void testErrorHandler() throws Exception {
        String xml = "<root><error/></root>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));

        loader = spy(new AbstractLoader(Maps.newHashMap()) {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                // do nothing
            }
        });

        doCallRealMethod(() -> loader.parseInputSource(inputSource));
        verify(loader).error(any(SAXParseException.class));
    }

    @Test
    public void testFatalErrorHandler() throws Exception {
        String xml = "<root><fatalError/></root>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));

        loader = spy(new AbstractLoader(Maps.newHashMap()) {
            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                // do nothing
            }
        });

        doCallRealMethod(() -> loader.parseInputSource(inputSource));
        verify(loader).fatalError(any(SAXParseException.class));
    }
}