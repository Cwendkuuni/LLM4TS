package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoaderConstructionWithPublicIdAndDtdResourceName() throws Exception {
        final String publicId = "testPublicId";
        final String dtdResourceName = "testDtdResourceName.dtd";
        final AbstractLoader loader = new AbstractLoader(publicId, dtdResourceName);
        assertNotNull(loader.parser);
        assertEquals(Collections.singletonMap(publicId, dtdResourceName), loader.publicIdToResourceNameMap);
    }

    @Test
    public void testAbstractLoaderConstructionWithPublicIdToResourceNameMap() throws Exception {
        final Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("testPublicId1", "testDtdResourceName1.dtd");
        publicIdToResourceNameMap.put("testPublicId2", "testDtdResourceName2.dtd");
        final AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
        assertNotNull(loader.parser);
        assertEquals(publicIdToResourceNameMap, loader.publicIdToResourceNameMap);
    }

    @Test(expected = SAXException.class)
    public void testParseInputSourceThrowsSAXException() throws Exception {
        final AbstractLoader loader = new AbstractLoader();
        loader.parseInputSource(new InputSource(new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testResolveEntityWithKnownPublicId() throws Exception {
        final AbstractLoader loader = new AbstractLoader(Collections.singletonMap("testPublicId", "testDtdResourceName.dtd"));
        final InputSource expectedInputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
        when(loader.parser.getInputSource()).thenReturn(expectedInputSource);
        when(loader.parser.getEntityResolver()).thenReturn(mock(EntityResolver.class));
        final String publicId = "testPublicId";
        final String systemId = "testSystemId";
        final InputSource actualInputSource = loader.resolveEntity(publicId, systemId);
        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test
    public void testResolveEntityWithUnknownPublicId() throws Exception {
        final AbstractLoader loader = new AbstractLoader(Collections.singletonMap("testPublicId", "testDtdResourceName.dtd"));
        final InputSource actualInputSource = loader.resolveEntity("unknownPublicId", "unknownSystemId");
        assertNull(actualInputSource);
    }

    @Test
    public void testError() throws Exception {
        final AbstractLoader loader = new AbstractLoader();
        final SAXParseException expectedException = mock(SAXParseException.class);
        loader.error(expectedException);
        throw expectedException;
    }

    @Test
    public void testFatalError() throws Exception {
        final AbstractLoader loader = new AbstractLoader();
        final SAXParseException expectedException = mock(SAXParseException.class);
        loader.fatalError(expectedException);
        throw expectedException;
    }
}