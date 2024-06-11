package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;
import java.io.*;
import javax.xml.parsers.*;
import org.junit.*;
import org.xml.sax.*;

public class AbstractLoaderTest {

    private AbstractLoader loader;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException {
        loader = new AbstractLoader(new HashMap<>()) {
            @Override
            public void warning(SAXParseException exception) {
                // Ignore warnings
            }
        };
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        loader.parseInputSource(new StringReader("<xml></xml>"));
        // Add assertions here based on the expected behavior of the method
    }

    @Test
    public void testResolveEntity_publicIdFound() throws IOException, SAXException {
        final String publicId = "myPublicId";
        final String dtdResourceName = "myDtd.dtd";
        when(loader.publicIdToResourceNameMap.get(publicId)).thenReturn(dtdResourceName);

        final InputStream expectedDtd = new ByteArrayInputStream("Expected DTD".getBytes());
        doReturn(new InputSource(expectedDtd)).when(loader).parse(eq(dtdResourceName));

        final InputSource inputSource = new InputSource(new ByteArrayInputStream("Input XML".getBytes()));
        final InputSource result = loader.resolveEntity(publicId, null);

        assertEquals(expectedDtd, result.getByteStream());
        verify(loader, times(1)).parse(eq(dtdResourceName));
    }

    @Test
    public void testResolveEntity_publicIdNotFound() throws IOException, SAXException {
        doThrow(new SAXParseException("Expected exception")).when(loader).parse(anyString());

        final InputSource result = loader.resolveEntity("nonexistentPublicId", null);

        assertNull(result);
        verify(loader, times(1)).parse(anyString());
    }
}