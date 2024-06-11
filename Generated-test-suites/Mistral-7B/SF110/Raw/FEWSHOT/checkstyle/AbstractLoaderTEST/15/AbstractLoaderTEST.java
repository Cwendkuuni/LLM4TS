package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class AbstractLoaderTest {

    private AbstractLoader loader;
    private SAXParserFactory saxParserFactory;

    @Before
    public void setup() throws ParserConfigurationException {
        saxParserFactory = SAXParserFactory.newInstance();
        loader = new AbstractLoader(new HashMap<>());
    }

    @Test
    public void testParseInputSource() throws SAXException, IOException {
        final InputSource inputSource = mock(InputSource.class);
        when(inputSource.getSystemId()).thenReturn("test.xml");

        loader.parseInputSource(inputSource);

        verify(loader.parser).parse(inputSource);
    }

    @Test
    public void testResolveEntityWithPublicId() throws SAXException, IOException, ParserConfigurationException {
        final String publicId = "foo.dtd";
        final String expectedResourceName = "bar.dtd";
        loader = new AbstractLoader(new HashMap<String, String>() {{
            put(publicId, expectedResourceName);
        }});

        final InputSource resolvedInputSource = loader.resolveEntity(publicId, null);

        assertTrue(resolvedInputSource instanceof InputSource);
        final InputStream inputStream = ((InputSource) resolvedInputSource).getInputStream();
        final ClassLoader classLoader = loader.getClass().getClassLoader();
        final InputStream expectedInputStream = classLoader.getResourceAsStream(expectedResourceName);
        assertTrue(inputStream.equals(expectedInputStream));
    }

    @Test
    public void testResolveEntityWithoutPublicId() throws SAXException, IOException {
        final InputSource resolvedInputSource = loader.resolveEntity(null, null);

        assertNull(resolvedInputSource);
    }
}