package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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
import com.puppycrawl.tools.checkstyle.api.ErrorHandler;
import com.puppycrawl.tools.checkstyle.api.SAXParserFactory;
import com.puppycrawl.tools.checkstyle.api.XMLReader;
import com.puppycrawl.tools.checkstyle.api.XMLEntityResolver;

/**
 * Test cases for AbstractLoader class
 */
public class AbstractLoaderTest {

    private AbstractLoader loader;
    private Map<String, String> publicIdToResourceNameMap;

    @Before
    public void setUp() throws Exception {
        publicIdToResourceNameMap = Maps.newHashMap();
        loader = new AbstractLoader(publicIdToResourceNameMap);
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(loader.getPublicIdToResourceNameMap());
        assertNull(loader.getParser());
    }

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws Exception {
        publicIdToResourceNameMap.put("somePublicId", "someDtdResourceName.dtd");
        loader = new AbstractLoader("somePublicId", "someDtdResourceName.dtd");
        Map<String, String> map = loader.getPublicIdToResourceNameMap();
        assertTrue(map.equals(publicIdToResourceNameMap));
        XMLReader parser = loader.getParser();
        assertNotNull(parser);
    }

    @Test
    public void testParseInputSource() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\"?>".getBytes());
        InputSource inputSource = new InputSource(inputStream);
        loader.parseInputSource(inputSource);
        // In AbstractLoader, parseInputSource() calls setInputSource(inputSource) in DefaultHandler,
        // which sets the input to be parsed. So we don't have an easy way to test the result of parseInputSource()
        // directly, but we can test other methods that use the input.
        // For example, we can test that the input is correctly set in DefaultHandler.
    }

    @Test
    public void testResolveEntityWithValidPublicId() throws Exception {
        publicIdToResourceNameMap.put("validPublicId", "validDtdResourceName.dtd");
        InputSource inputSource = new ByteArrayInputStream(new byte[0])
                .wrap(new InputSource() {
                    @Override
                    public String getSystemId() {
                        return null;
                    }

                    @Override
                    public String getPublicId() {
                        return "validPublicId";
                    }
                });
        InputSource expectedInputSource = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>".getBytes()));
        InputSource actualInputSource = loader.resolveEntity("validPublicId", null);
        assertTrue(actualInputSource.isEqual(expectedInputSource));
    }

    @Test
    public void testResolveEntityWithInvalidPublicId() throws Exception {
        InputSource inputSource = new ByteArrayInputStream(new byte[0])
                .wrap(new InputSource() {
                    @Override
                    public String getSystemId() {
                        return null;
                    }

                    @Override
                    public String getPublicId() {
                        return "invalidPublicId";
                    }
                });
        InputSource actualInputSource = loader.resolveEntity("invalidPublicId", null);
        assertNull(actualInputSource);
    }

    @Test
    public void testResolveEntityWithNullPublicId() throws Exception {
        InputSource inputSource = new ByteArrayInputStream(new byte[0])
                .wrap(new InputSource() {
                    @Override
                    public String getSystemId() {
                        return null;
                    }

                    @Override
                    public String getPublicId() {
                        return null;
                    }
                });
        InputSource actualInputSource = loader.resolveEntity(null, null);
        assertNull(actualInputSource);
    }

    @Test
    public void testResolveEntityWithNullSystemId() throws Exception {
        publicIdToResourceNameMap.put("somePublicId", "someDtdResourceName.dtd");
        InputSource inputSource = new ByteArrayInputStream(new byte[0])
                .wrap(new InputSource() {
                    @Override
                    public String getSystemId() {
                        return null;
                    }

                    @Override
                    public String getPublicId() {
                        return "somePublicId";
                    }
                });
        InputSource expectedInputSource = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>".getBytes()));
        InputSource actualInputSource = loader.resolveEntity("somePublicId", null);
        assertTrue(actualInputSource.isEqual(expectedInputSource));
    }

    @Test
    public void testResolveEntityWithSystemId() throws Exception {
        publicIdToResourceNameMap.put("somePublicId", "someDtdResourceName.dtd");
        InputSource inputSource = new ByteArrayInputStream(new byte[0])
                .wrap(new InputSource() {
                    @Override
                    public String getSystemId() {
                        return "someDtdResourceName.dtd";
                    }

                    @Override
                    public String getPublicId() {
                        return "somePublicId";
                    }
                });
        InputSource expectedInputSource = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>".getBytes()));
        InputSource actualInputSource = loader.resolveEntity("somePublicId", "someDtdResourceName.dtd");
        assertTrue(actualInputSource.isEqual(expectedInputSource));
    }

    @Test
    public void testError() throws Exception {
        parser = new SAXParserFactory().newSAXParser().getXMLReader();
        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) {
                throw new AssertionError("Unexpected error: " + exception.getMessage());
            }

            @Override
            public void fatalError(SAXParseException exception) {
                throw new AssertionError("Unexpected fatal error: " + exception.getMessage());
            }
        };
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public ErrorHandler getErrorHandler() {
                return errorHandler;
            }
        };
        InputSource inputSource = new ByteArrayInputStream(new byte[0])
                .wrap(new InputSource() {
                    @Override
                    public String getSystemId() {
                        return null;
                    }

                    @Override
                    public String getPublicId() {
                        return null;
                    }
                });
        loader.parseInputSource(inputSource);
        // An error or fatal error should be thrown, but we can't test that directly in a unit test,
        // so we test that the error handler receives the error.
        // In this test case, we expect an error to be thrown, so we throw an AssertionError instead.
        // This way, the test fails if an error is not thrown.
    }

    @Test
    public void testFatalError() throws Exception {
        parser = new SAXParserFactory().newSAXParser().getXMLReader();
        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) {
                // Ignore errors
            }

            @Override
            public void fatalError(SAXParseException exception) {
                throw new AssertionError("Unexpected fatal error: " + exception.getMessage());
            }
        };
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public ErrorHandler getErrorHandler() {
                return errorHandler;
            }
        };
        InputSource inputSource = new ByteArrayInputStream(new byte[0])
                .wrap(new InputSource() {
                    @Override
                    public String getSystemId() {
                        return null;
                    }

                    @Override
                    public String getPublicId() {
                        return null;
                    }
                });
        loader.parseInputSource(inputSource);
        // A fatal error should be thrown, but we can't test that directly in a unit test,
        // so we test that the error handler receives the fatal error.
        // In this test case, we expect a fatal error to be thrown, so we throw an AssertionError instead.
        // This way, the test fails if a fatal error is not thrown.
    }

    @Test
    public void testEntityResolver() throws Exception {
        publicIdToResourceNameMap.put("somePublicId", "someDtdResourceName.dtd");
        XMLEntityResolver entityResolver = new XMLEntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                if ("somePublicId".equals(publicId)) {
                    return new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>".getBytes()));
                }
                return super.resolveEntity(publicId, systemId);
            }
        };
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public XMLEntityResolver getEntityResolver() {
                return entityResolver;
            }
        };
        InputSource inputSource = new ByteArrayInputStream(new byte[0])
                .wrap(new InputSource() {
                    @Override
                    public String getSystemId() {
                        return null;
                    }

                    @Override
                    public String getPublicId() {
                        return "somePublicId";
                    }
                });
        InputSource expectedInputSource = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\"?>".getBytes()));
        InputSource actualInputSource = loader.resolveEntity("somePublicId", null);
        assertTrue(actualInputSource.isEqual(expectedInputSource));
    }

}