package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import com.puppycrawl.tools.checkstyle.api.DefaultHandler;
import com.puppycrawl.tools.checkstyle.api.ErrorHandler;
import com.puppycrawl.tools.checkstyle.api.EntityResolver;
import com.puppycrawl.tools.checkstyle.api.SAXParserFactory;
import com.puppycrawl.tools.checkstyle.api.XMLReader;

public class AbstractLoaderTest extends TestCase {

    public static Test suite() {
        return new TestSuite(AbstractLoaderTest.class);
    }

    private AbstractLoader loader;
    private final static String TEST_PUBLIC_ID = "TEST_PUBLIC_ID";
    private final static String TEST_DTD_RESOURCE_NAME = "test.dtd";

    @Override
    protected void setUp() throws Exception {
        loader = new AbstractLoader(TEST_PUBLIC_ID, TEST_DTD_RESOURCE_NAME);
    }

    public void testAbstractLoaderConstructionWithPublicIdAndDtdResourceName() throws Exception {
        // Test case for AbstractLoader constructor that takes publicId and dtdResourceName as arguments.
        assertNotNull(loader);
    }

    public void testAbstractLoaderConstructionWithPublicIdToResourceNameMap() throws Exception {
        // Test case for AbstractLoader constructor that takes a Map of publicIdToResourceName as an argument.
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(TEST_PUBLIC_ID, TEST_DTD_RESOURCE_NAME);
        AbstractLoader loaderWithMap = new AbstractLoader(publicIdToResourceNameMap);
        assertNotNull(loaderWithMap);
    }

    public void testParseInputSource() throws Exception {
        // Test case for parseInputSource method.
        InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        loader.parseInputSource(inputSource);
    }

    public void testResolveEntity() throws Exception {
        // Test case for resolveEntity method.
        String actualPublicId = "TEST_OTHER_PUBLIC_ID";
        String actualSystemId = "test_other.dtd";
        InputStream expectedDtdIs = new ByteArrayInputStream(new byte[0]);
        InputSource expectedInputSource = new InputSource(expectedDtdIs);

        InputStream inputStreamSpy = mock(InputStream.class);
        when(inputStreamSpy.available()).thenReturn(0);

        XMLReader xmlReaderSpy = mock(XMLReader.class);
        when(xmlReaderSpy.getEntityResolver()).thenReturn(this);

        SAXParserFactory saxParserFactorySpy = mock(SAXParserFactory.class);
        when(saxParserFactorySpy.newSAXParser()).thenReturn(mock(Parser.class));
        when(saxParserFactorySpy.getXMLReader()).thenReturn(xmlReaderSpy);

        InputSource inputSource = new InputSource(inputStreamSpy);
        String publicId = actualPublicId;
        String systemId = actualSystemId;

        AbstractLoader loaderSpy = spy(loader);
        doReturn(expectedInputSource).when(loaderSpy).parseInputSource(eq(inputSource));

        InputSource result = loaderSpy.resolveEntity(publicId, systemId);

        verify(loaderSpy).parseInputSource(eq(inputSource));
        assertEquals(expectedInputSource, result);
    }

    public void testResolveEntityWhenPublicIdIsNotInMap() throws Exception {
        // Test case for resolveEntity method when publicId is not in the map.
        String publicId = "TEST_OTHER_PUBLIC_ID";
        String systemId = "test_other.dtd";

        InputStream inputStreamSpy = mock(InputStream.class);
        InputSource inputSource = new InputSource(inputStreamSpy);

        AbstractLoader loaderSpy = spy(loader);

        InputSource result = loaderSpy.resolveEntity(publicId, systemId);

        verify(loaderSpy).resolveEntity(eq(publicId), eq(systemId));
        assertNotNull(result);
    }

    public void testErrorHandler() throws Exception {
        // Test case for error and fatalError methods.
        SAXParseException exception = new SAXParseException("Test error", null);

        loader.error(exception);
        loader.fatalError(exception);
    }

    public void testEntityResolver() throws Exception {
        // Test case for entityResolver method.
        String publicId = "TEST_PUBLIC_ID";
        String systemId = "test.dtd";
        InputStream dtdIs = new ByteArrayInputStream(new byte[0]);
        InputSource expectedInputSource = new InputSource(dtdIs);

        InputStream inputStreamSpy = mock(InputStream.class);
        when(inputStreamSpy.available()).thenReturn(0);

        XMLReader xmlReaderSpy = mock(XMLReader.class);
        when(xmlReaderSpy.getEntityResolver()).thenReturn(this);

        SAXParserFactory saxParserFactorySpy = mock(SAXParserFactory.class);
        when(saxParserFactorySpy.newSAXParser()).thenReturn(mock(Parser.class));
        when(saxParserFactorySpy.getXMLReader()).thenReturn(xmlReaderSpy);

        InputSource inputSource = new InputSource(inputStreamSpy);
        AbstractLoader loaderSpy = spy(loader);

        doReturn(expectedInputSource).when(loaderSpy).parseInputSource(eq(inputSource));

        InputSource result = loaderSpy.resolveEntity(publicId, systemId);

        verify(loaderSpy).parseInputSource(eq(inputSource));
        assertEquals(expectedInputSource, result);
    }
}