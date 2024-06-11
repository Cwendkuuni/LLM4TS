package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoaderConstructionWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        final AbstractLoader loader = new AbstractLoader("publicId", "dtdResourceName.dtd");
        assertNotNull(loader.getParser());
    }

    @Test
    public void testAbstractLoaderConstructionWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        final Map<String, String> publicIdToResourceNameMap = new HashMap<String, String>() {{
            put("publicId1", "dtdResourceName1.dtd");
            put("publicId2", "dtdResourceName2.dtd");
        }};
        final AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
        assertNotNull(loader.getParser());
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        final AbstractLoader loader = mock(AbstractLoader.class);
        final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        when(loader.getParser()).thenReturn(mock(XMLReader.class));

        loader.parseInputSource(inputSource);

        // Verify that parse method was called
        verify(loader.getParser()).parse(inputSource);
    }

    @Test
    public void testResolveEntityWithKnownPublicId() throws IOException, SAXException {
        final AbstractLoader loader = new AbstractLoader(Collections.singletonMap("publicId", "dtdResourceName.dtd"));

        final InputSource inputSource = loader.resolveEntity("publicId", null);

        assertNotNull(inputSource);
    }

    @Test
    public void testResolveEntityWithUnknownPublicId() throws IOException, SAXException {
        final AbstractLoader loader = new AbstractLoader(Collections.singletonMap("publicId1", "dtdResourceName1.dtd"));
        final InputSource inputSource = loader.resolveEntity("publicId2", null);

        assertNull(inputSource);
    }

    @Test
    public void testResolveEntityWithNullPublicIdAndSystemId() throws SAXException, IOException {
        final AbstractLoader loader = new AbstractLoader(Collections.singletonMap("publicId1", "dtdResourceName1.dtd"));

        final InputSource inputSource = loader.resolveEntity(null, null);

        assertNull(inputSource);
    }

    @Test
    public void testResolveEntityWithNullPublicIdAndNonNullSystemId() throws SAXException, IOException {
        final AbstractLoader loader = new AbstractLoader(Collections.singletonMap("publicId1", "dtdResourceName1.dtd"));

        final InputSource inputSource = loader.resolveEntity("", "systemId");

        assertNull(inputSource);
    }

    @Test
    public void testResolveEntityWithNonEmptySystemId() throws SAXException, IOException {
        final AbstractLoader loader = new AbstractLoader(Collections.singletonMap("publicId1", "dtdResourceName1.dtd"));

        final InputSource inputSource = loader.resolveEntity("", "systemId/dtdResourceName.dtd");

        assertNull(inputSource);
    }
}