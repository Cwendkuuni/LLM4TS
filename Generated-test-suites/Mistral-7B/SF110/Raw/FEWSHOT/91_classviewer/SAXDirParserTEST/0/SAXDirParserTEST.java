package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import java.util.*;
import javax.xml.parsers.*;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLReader reader = saxParser.getXMLReader();
        reader.setContentHandler(parser);

        reader.parse("path/to/your/packagedirectory.xml");

        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "path/to/your/java.lang");
        expectedDirectory.put("javax.swing.border", "path/to/your/javax.swing.border");

        Map<String, String> actualDirectory = parser.getDirectory();

        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLReader myReader = saxParser.getXMLReader();
        myReader.setContentHandler(parser);

        myReader.parse("path/to/your/packagedirectory.xml");

        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("java.lang");
        expectedLocalPackages.add("javax.swing.border");

        StringList actualLocalPackages = parser.getLocalPackages();

        assertEquals(expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLReader myReader = saxParser.getXMLReader();
        myReader.setContentHandler(parser);

        myReader.parse("path/to/your/packagedirectory.xml");

        String expectedLocalBrowser = "expected_local_browser_value";

        String actualLocalBrowser = parser.getLocalBrowser();

        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLReader myReader = saxParser.getXMLReader();
        myReader.setContentHandler(parser);

        myReader.parse("path/to/your/packagedirectory.xml");

        String expectedEditor = "expected_editor_value";

        String actualEditor = parser.getEditor();

        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLReader myReader = saxParser.getXMLReader();
        myReader.setContentHandler(parser);

        myReader.parse("path/to/your/packagedirectory.xml");

        boolean expectedAcceptLineNumber = true; // or false

        boolean actualAcceptLineNumber = parser.acceptsLineNumber();

        assertEquals(expectedAcceptLineNumber, actualAcceptLineNumber);
    }

    @Test
    public void testLineNumberParameter() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLReader myReader = saxParser.getXMLReader();
        myReader.setContentHandler(parser);

        myReader.parse("path/to/your/packagedirectory.xml");

        String expectedLineNumberParameter = "expected_line_number_parameter_value";

        String actualLineNumberParameter = parser.lineNumberParameter();

        assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
    }

    @Test
    public void testParsingWithEmptyXMLFile() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLReader myReader = saxParser.getXMLReader();
        myReader.setContentHandler(parser);

        myReader.parse("path/to/your/empty_packagedirectory.xml");

        assertTrue(parser.getDirectory().isEmpty());
        assertTrue(parser.getLocalPackages().isEmpty());
        assertNull(parser.getLocalBrowser());
        assertNull(parser.getEditor());
        assertFalse(parser.acceptsLineNumber());
        assertNull(parser.lineNumberParameter());
    }

    @Test
    public void testParsingWithInvalidXMLFile() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLReader myReader = saxParser.getXMLReader();
        myReader.setContentHandler(parser);

        myReader.parse("path/to/your/invalid_packagedirectory.xml");

        assertThrows(SAXException.class, () -> parser.getDirectory());
        assertThrows(SAXException.class, () -> parser.getLocalPackages());
        assertNull(parser.getLocalBrowser());
        assertNull(parser.getEditor());
        assertFalse(parser.acceptsLineNumber());
        assertNull(parser.lineNumberParameter());
    }
}