package com.example.junit;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AbstractLoaderTest {
    private AbstractLoader abstractLoader;
    private InputSource inputSource;

    @Before
    public void setup() throws SAXException, ParserConfigurationException {
        abstractLoader = new AbstractLoader(new HashMap<>());
        inputSource = mock(InputSource.class);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        abstractLoader.parseInputSource(inputSource);
        verify(abstractLoader.parser, times(1)).parse(inputSource);
    }

    @Test(expected = SAXException.class)
    public void testExceptionHandling() throws IOException, SAXException {
        final AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>());
        abstractLoader.parseInputSource(null);

        final AbstractLoader abstractLoaderWithNullMap = new AbstractLoader(null);

        abstractLoader.resolveEntity(null, null);
        abstractLoader.resolveEntity("nonExistentPublicId", null);
        abstractLoader.resolveEntity("existingPublicId", null);
        abstractLoader.resolveEntity("existingPublicId", "nonExistentResourceName");
    }
}