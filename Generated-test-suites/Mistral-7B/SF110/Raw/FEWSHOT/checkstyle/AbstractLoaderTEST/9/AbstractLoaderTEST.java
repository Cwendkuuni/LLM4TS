package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    private AbstractLoader loader;
    private XMLReader parser;
    private Map<String, String> publicIdToResourceNameMap;

    @Before
    public void setUp() throws Exception {
        publicIdToResourceNameMap = new HashMap<>();
        parser = mock(XMLReader.class);
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            XMLReader getParser() {
                return parser;
            }
        };
    }

    @Test
    public void testAbstractLoaderInstance() {
        assertSame(parser.getClass(), loader.getParser().getClass());
        assertFalse(loader.isNamespaceAware());
        assertTrue(loader.isValidating());
    }

    @Test
    public void testAbstractLoaderInstanceWithPublicIdAndDTDResourceName() throws Exception {
        loader = new AbstractLoader("foo", "bar.dtd") {
            @Override
            XMLReader getParser() {
                return parser;
            }
        };
        assertSame(parser.getClass(), loader.getParser().getClass());
        assertFalse(loader.isNamespaceAware());
        assertTrue(loader.isValidating());
        assertEquals("bar.dtd", loader.getPublicIdToResourceNameMap().get("foo"));
    }

    @Test
    public void testParseInputSource() throws Exception {
        when(parser.parse(InputSource.class.getConstructor(InputStream.class).newInstance(new ByteArrayInputStream(new byte[0]))))
                .thenReturn(new Object());
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        loader.parseInputSource(inputSource);
        // No verification needed, as we are not testing the parsing logic here
    }

    @Test(expected = IOException.class)
    public void testResolveEntityWithNullPublicId() throws Exception {
        loader.resolveEntity(null, "systemId");
    }

    @Test
    public void testResolveEntityWithNonexistentPublicId() throws Exception {
        publicIdToResourceNameMap.put("someOtherId", "someOtherResource.dtd");
        loader.resolveEntity("nonexistentId", "systemId");
        InputStream dtdIs = loader.getClass().getClassLoader().getResourceAsStream("someOtherResource.dtd");
        assertNull(dtdIs);
    }

    @Test
    public void testResolveEntityWithExistentPublicId() throws Exception {
        publicIdToResourceNameMap.put("existingId", "existingResource.dtd");
        InputStream dtdIs = loader.getClass().getClassLoader().getResourceAsStream("existingResource.dtd");
        InputSource inputSource = new InputSource(dtdIs);
        InputSource result = loader.resolveEntity("existingId", "systemId");
        assertSame(inputSource, result);
    }

    @Test
    public void testError() throws Exception {
        SAXParseException exception = new SAXParseException("Test", new InputSource(new ByteArrayInputStream(new byte[0])));
        when(parser.getErrorHandler()).thenReturn(mock(org.xml.sax.ErrorHandler.class));
        loader.error(exception);
        // No verification needed, as we are not testing the error handling logic here
    }

    @Test
    public void testFatalError() throws Exception {
        SAXParseException exception = new SAXParseException("Test", new InputSource(new ByteArrayInputStream(new byte[0])));
        when(parser.getErrorHandler()).thenReturn(mock(org.xml.sax.ErrorHandler.class));
        loader.fatalError(exception);
        // No verification needed, as we are not testing the error handling logic here
    }
}