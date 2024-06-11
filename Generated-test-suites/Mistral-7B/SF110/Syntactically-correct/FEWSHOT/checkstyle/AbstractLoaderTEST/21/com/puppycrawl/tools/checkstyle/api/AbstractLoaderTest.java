package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.xml.sax.*;
import java.io.*;
import java.util.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;

public class AbstractLoaderTest {

    @Test
    public void testAbstractLoader_ValidConstruction() {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1.dtd");

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);

        assertEquals(publicIdToResourceNameMap, loader.publicIdToResourceNameMap);
        assertNotNull(loader.parser);
    }

    @Test
    public void testParseInputSource_ValidInputSource() throws Exception {
        AbstractLoader loader = new AbstractLoader(new HashMap<>());
        InputSource inputSource = new InputSource(new ByteArrayInputStream("Test data".getBytes()));

        loader.parseInputSource(inputSource);

        verify(loader.parser, times(1)).parse(inputSource);
    }

    @Test(expected = SAXParseException.class)
    public void testResolveEntity_UnknownPublicId() throws Exception {
        AbstractLoader loader = new AbstractLoader(new HashMap<>());
        String publicId = "unknownPublicId";

        loader.resolveEntity(publicId, null);
    }

    @Test
    public void testResolveEntity_KnownPublicId() throws Exception {
        AbstractLoader loader = new AbstractLoader(new HashMap<>());
        String publicId = "publicId1";
        String dtdResourceName = "dtdResourceName1.dtd";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        loader = new AbstractLoader(publicIdToResourceNameMap);
        InputStream dtdIs = new ByteArrayInputStream("Test data".getBytes());
        when(loader.getClass().getClassLoader().getResourceAsStream(dtdResourceName)).thenReturn(dtdIs);

        InputSource actualInputSource = loader.resolveEntity(publicId, null);
        InputSource expectedInputSource = new InputSource(dtdIs);

        assertEquals(expectedInputSource, actualInputSource);
    }
}