package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Test;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);

        InputSource inputSource = new InputSource("src/test/resources/packagedirectory.xml");
        saxParser.parse(inputSource);

        HashMap<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "path/to/java.lang");
        expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

        HashMap<String, String> actualDirectory = parser.getDirectory();

        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);

        InputSource inputSource = new InputSource("src/test/resources/packagedirectory.xml");
        saxParser.parse(inputSource);

        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("localPackage1");
        expectedLocalPackages.add("localPackage2");

        StringList actualLocalPackages = parser.getLocalPackages();

        assertEquals(expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);

        InputSource inputSource = new InputSource("src/test/resources/packagedirectory.xml");
        saxParser.parse(inputSource);

        String expectedLocalBrowser = "Google Chrome";

        String actualLocalBrowser = parser.getLocalBrowser();

        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);

        InputSource inputSource = new InputSource("src/test/resources/packagedirectory.xml");
        saxParser.parse(inputSource);

        String expectedEditor = "Notepad++";

        String actualEditor = parser.getEditor();

        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);

        InputSource inputSource = new InputSource("src/test/resources/packagedirectory.xml");
        saxParser.parse(inputSource);

        boolean expectedAcceptsLineNumber = true;

        boolean actualAcceptsLineNumber = parser.acceptsLineNumber();

        assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);

        InputSource inputSource = new InputSource("src/test/resources/packagedirectory.xml");
        saxParser.parse(inputSource);

        String expectedLineNumberParameter = "56";

        String actualLineNumberParameter = parser.lineNumberParameter();

        assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
    }

    @Test
    public void testParseXmlFile() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);

        InputSource inputSource = new InputSource("src/test/resources/packagedirectory.xml");
        saxParser.parse(inputSource);

        assertEquals("path/to/java.lang", parser.getDirectory().get("java.lang"));
        assertEquals("path/to/javax.swing.border", parser.getDirectory().get("javax.swing.border"));
        assertEquals("Google Chrome", parser.getLocalBrowser());
        assertEquals("Notepad++", parser.getEditor());
        assertTrue(parser.acceptsLineNumber());
        assertEquals("56", parser.lineNumberParameter());
    }

    @Test
    public void testHandleEmptyFile() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);

        InputSource inputSource = new InputSource("src/test/resources/empty.xml");
        saxParser.parse(inputSource);

        // The test will fail if an exception is not thrown.
        // The SAXDirParser should not parse empty XML files.
        try {
            assertEquals("", parser.getDirectory().get(""));
        } catch (NullPointerException e) {
            // Expected
        }

        assertNull(parser.getLocalBrowser());
        assertNull(parser.getEditor());
        assertFalse(parser.acceptsLineNumber());
        assertNull(parser.lineNumberParameter());
    }
}