package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.xml.sax.*;
import java.io.*;
import org.xml.sax.helpers.*;
import com.google.common.collect.*;
import javax.xml.parsers.*;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoader() throws Exception {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName.dtd";
        AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);
        assertNotNull(loader);
        assertEquals(1, loader.publicIdToResourceNameMap.size());
        assertEquals(dtdResourceName, loader.publicIdToResourceNameMap.get(publicId));
    }

    @Test(expected = SAXException.class)
    public void testParseInputSourceNullInputSource() throws Exception {
        AbstractLoader loader = new AbstractLoader();
        try {
            loader.parseInputSource(null);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        fail("Expected SAXException");
    }

    @Test(expected = SAXException.class)
    public void testParseInputSourceIOException() throws Exception {
        AbstractLoader loader = new AbstractLoader();
        InputSource inputSource = new InputSource(new FileInputStream("nonExistentFile.xml"));
        try {
            loader.parseInputSource(inputSource);
        } catch (IOException e) {
            fail("Unexpected IOException", e);
        } catch (SAXException e) {
            fail("Unexpected SAXException");
        }
        fail("Expected SAXException for IOException");
    }

    @Test
    public void testResolveEntityNullPublicId() throws Exception {
        AbstractLoader loader = new AbstractLoader();
        InputSource inputSource = new InputSource(new StringReader(""));
        InputSource expectedInputSource = new InputSource(new StringReader(""));
        InputSource actualInputSource = loader.resolveEntity(null, "systemId");
        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test
    public void testResolveEntityEmptyPublicIdToResourceNameMap() throws Exception {
        AbstractLoader loader = new AbstractLoader(Maps.<String, String>newHashMap());
        InputSource inputSource = new InputSource(new StringReader(""));
        InputSource expectedInputSource = new InputSource(new StringReader(""));
        InputSource actualInputSource = loader.resolveEntity("", "systemId");
        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test
    public void testResolveEntityPublicIdMatches() throws Exception {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName.dtd";
        AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);
        InputSource inputSource = new InputSource(new StringReader(""));
        InputSource expectedInputSource = new InputSource(new FileInputStream(dtdResourceName));
        InputSource actualInputSource = loader.resolveEntity(publicId, "systemId");
        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test
    public void testResolveEntityPublicIdMismatch() throws Exception {
        final String publicId = "publicId";
        final String dtdResourceName = "dtdResourceName.dtd";
        AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);
        InputSource inputSource = new InputSource(new StringReader(""));
        InputSource expectedInputSource = null;
        InputSource actualInputSource = loader.resolveEntity("otherPublicId", "systemId");
        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test(expected = SAXException.class)
    public void testErrorSAXParseException() throws Exception {
        AbstractLoader loader = new AbstractLoader();
        SAXParseException exception = new SAXParseException("Error message");
        loader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalErrorSAXParseException() throws Exception {
        AbstractLoader loader = new AbstractLoader();
        SAXParseException exception = new SAXParseException("Error message");
        loader.fatalError(exception);
    }
}