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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;

public class AbstractLoaderTest {

    private AbstractLoader loader;
    private EntityResolver entityResolverMock;
    private Map<String, String> publicIdToResourceNameMap;
    private InputStream inputStreamMock;
    private InputSource inputSourceMock;
    private SAXParserFactory saxParserFactoryMock;

    @Before
    public void setUp() throws Exception {
        publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "resourceName1.dtd");

        entityResolverMock = mock(EntityResolver.class);
        saxParserFactoryMock = mock(SAXParserFactory.class);
        parser = mock(Parser.class);

        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            protected Parser createParser() throws SAXException, ParserConfigurationException {
                when(saxParserFactoryMock.newSAXParser()).thenReturn(parser);
                return parser;
            }

            @Override
            public void setEntityResolver(EntityResolver resolver) {
                super.setEntityResolver(resolver);
                entityResolver = resolver;
            }
        };

        inputStreamMock = mock(InputStream.class);
        inputSourceMock = new InputSource(inputStreamMock);
    }

    @Test
    public void testParseInputSourceWithPublicIdAndValidMapping() throws Exception {
        when(entityResolverMock.resolveEntity("publicId1", "")).thenReturn(inputSourceMock);

        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        loader.parseInputSource(inputSource);

        // The InputSource returned by the AbstractLoader should have been replaced by the one resolved from the map
        assertTrue(loader.parser.getInputSource() instanceof InputSource);
        InputSource loadedInputSource = (InputSource) loader.parser.getInputSource();
        assertSame(inputSourceMock, loadedInputSource);
    }

    @Test
    public void testParseInputSourceWithPublicIdAndNoMapping() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        loader.parseInputSource(inputSource);

        // If there is no mapping for the publicId, the super class' resolveEntity method should be called
        assertTrue(loader.parser.getInputSource() instanceof InputSource);
        InputSource loadedInputSource = (InputSource) loader.parser.getInputSource();
        assertNull(loadedInputSource);
    }

    @Test
    public void testParseInputSourceWithSystemId() throws Exception {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        loader.parseInputSource(inputSource);

        // If the publicId is not present in the map, the systemId should be used as is
        assertTrue(loader.parser.getInputSource() instanceof InputSource);
        InputSource loadedInputSource = (InputSource) loader.parser.getInputSource();
        assertSame(inputSource, loadedInputSource);
    }

    @Test
    public void testParseInputSourceWithNullInputSource() throws Exception {
        loader.parseInputSource(null);

        // If the inputSource is null, an SAXException should be thrown
        throwCaughtException(loader, SAXException.class);
    }

    @Test(expected = SAXException.class)
    public void testError() throws Exception {
        SAXParseException exception = new SAXParseException("Test error", null);
        loader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws Exception {
        SAXParseException exception = new SAXParseException("Test error", null);
        loader.fatalError(exception);
    }

    private void throwCaughtException(AbstractLoader loader, Class<? extends Throwable> exceptionType) throws Exception {
        try {
            loader.parseInputSource(inputSourceMock);
        } catch (Throwable e) {
            if (exceptionType.isInstance(e)) {
                throw (Exception) e;
            }
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}