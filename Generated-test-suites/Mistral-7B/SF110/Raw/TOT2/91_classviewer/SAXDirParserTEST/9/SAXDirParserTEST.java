package com.jstevh.viewer.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.xml.sax.SAXException;

public class SAXDirParserTest {
    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testSAXDirParser() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.webData);
    }

    @Test
    public void testStartElement() throws SAXException {
        SAXDirParser parser = mock(SAXDirParser.class);
        String namespace = "test";
        String local = "testLocal";
        String qname = "testQName";
        Attributes atts = mock(Attributes.class);

        parser.startElement(namespace, local, qname, atts);

        verify(parser, times(1)).check = true;
        verify(parser, times(1)).level = 0;
        verifyNoMoreInteractions(parser);
    }

    @Test(expected = SAXException.class)
    public void testStartElementWithException() throws SAXException {
        SAXDirParser parser = mock(SAXDirParser.class);

        parser.startElement("test", "test", "test", null);

        verify(parser, times(1)).check = true;
        verify(parser, times(1)).level = 0;
        verify(parser, times(1)).throwException(any(SAXException.class));
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser parser = new SAXDirParser();
        HashMap<String, String> expectedHashMap = new HashMap<>();
        expectedHashMap.put("java.lang", "testDirectory");

        parser.parse("test.xml");
        HashMap<String, String> actualHashMap = parser.getDirectory();

        assertEquals(expectedHashMap, actualHashMap);
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser parser = new SAXDirParser();
        StringList expectedStringList = new StringList();
        expectedStringList.add("testPackage");

        parser.parse("test.xml");
        StringList actualStringList = parser.getLocalPackages();

        assertEquals(expectedStringList, actualStringList);
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser parser = new SAXDirParser();
        String expectedLocalBrowser = "testBrowser";

        parser.parse("test.xml");
        String actualLocalBrowser = parser.getLocalBrowser();

        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() {
        SAXDirParser parser = new SAXDirParser();
        String expectedEditor = "testEditor";

        parser.parse("test.xml");
        String actualEditor = parser.getEditor();

        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        boolean expectedLineNumberAccepted = true;

        parser.parse("test.xml");
        boolean actualLineNumberAccepted = parser.acceptsLineNumber();

        assertEquals(expectedLineNumberAccepted, actualLineNumberAccepted);
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser parser = new SAXDirParser();
        String expectedLineNumberParameter = "testParameter";

        parser.parse("test.xml");
        String actualLineNumberParameter = parser.lineNumberParameter();

        assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
    }
}