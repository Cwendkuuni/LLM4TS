package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoader() throws ParserConfigurationException, SAXException, IOException {
        Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);

        InputSource inputSource = new InputSource(new StringReader("Test XML data"));
        loader.parseInputSource(inputSource);

        // Test entity resolution
        InputSource dtdInputSource = loader.resolveEntity("publicId", "dtdResourceName.dtd");
        assertNull(dtdInputSource);
    }

    @Test
    public void testAbstractLoader_WithPublicIdAndDtdResourceName() throws ParserConfigurationException, SAXException, IOException {
        String publicId = "myPublicId";
        String dtdResourceName = "dtdResourceName.dtd";
        AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);

        InputSource inputSource = new InputSource(new StringReader("Test XML data"));
        loader.parseInputSource(inputSource);

        InputSource dtdInputSource = loader.resolveEntity(publicId, null);
        assertTrue(dtdInputSource.getInputStream().available() > 0);
        assertEquals(dtdResourceName, dtdInputSource.getSystemId());
    }

    @Test(expected = SAXException.class)
    public void testError() throws ParserConfigurationException, SAXException, IOException {
        AbstractLoader loader = new AbstractLoader(Maps.newHashMap());
        InputSource inputSource = new InputSource(new StringReader("<xml><error/></xml>"));
        loader.parseInputSource(inputSource);
        loader.error(new SAXParseException("Test error", null, null, null, null, 0, null));
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws ParserConfigurationException, SAXException, IOException {
        AbstractLoader loader = new AbstractLoader(Maps.newHashMap());
        InputSource inputSource = new InputSource(new StringReader("<xml><fatalError/></xml>"));
        loader.parseInputSource(inputSource);
        loader.fatalError(new SAXParseException("Test fatal error", null, null, null, null, 0, null));
    }
}