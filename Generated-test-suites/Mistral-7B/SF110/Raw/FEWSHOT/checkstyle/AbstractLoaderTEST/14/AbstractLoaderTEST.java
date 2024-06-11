package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.xml.sax.InputSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXException;
import java.io.IOException;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoader() throws ParserConfigurationException, SAXException, IOException {
        final String publicId = "testPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";
        final AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);
        assertTrue(loader.publicIdToResourceNameMap.containsKey(publicId));
        assertTrue(loader.publicIdToResourceNameMap.get(publicId).equals(dtdResourceName));
    }

    @Test
    public void testResolveEntityWithPublicId() throws ParserConfigurationException, SAXException, IOException {
        final String publicId = "testPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        loader.publicIdToResourceNameMap.put(publicId, dtdResourceName);

        final InputSource inputSource = loader.resolveEntity(publicId, null);
        assert inputSource != null;
        final ClassLoader loader1 = loader.getClass().getClassLoader();
        final InputStream dtdIs = loader1.getResourceAsStream(dtdResourceName);
        assertTrue(dtdIs.available() > 0);
    }

    @Test
    public void testResolveEntityWithoutPublicId() throws ParserConfigurationException, SAXException, IOException {
        final AbstractLoader loader = new AbstractLoader(new HashMap<String, String>(1));
        final InputSource inputSource = loader.resolveEntity(null, null);
        assertNull(inputSource);
    }
}