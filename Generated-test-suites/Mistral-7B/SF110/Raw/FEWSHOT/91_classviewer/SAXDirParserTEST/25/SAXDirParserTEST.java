package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        xmlReader.setContentHandler(handler);

        File file = new File("path/to/your/packagedirectory.xml");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        xmlReader.parse(inputSource);

        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "expected_location_for_java.lang");
        expectedDirectory.put("javax.swing.border", "expected_location_for_javax.swing.border");

        Map<String, String> actualDirectory = SAXDirParser.getDirectory();

        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        xmlReader.setContentHandler(handler);

        File file = new File("path/to/your/packagedirectory.xml");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        xmlReader.parse(inputSource);

        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("expected_package1");
        expectedLocalPackages.add("expected_package2");

        StringList actualLocalPackages = SAXDirParser.getLocalPackages();

        assertEquals(expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        xmlReader.setContentHandler(handler);

        File file = new File("path/to/your/packagedirectory.xml");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        xmlReader.parse(inputSource);

        String expectedLocalBrowser = "expected_local_browser";

        String actualLocalBrowser = SAXDirParser.getLocalBrowser();

        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        xmlReader.setContentHandler(handler);

        File file = new File("path/to/your/packagedirectory.xml");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        xmlReader.parse(inputSource);

        String expectedEditor = "expected_editor";

        String actualEditor = SAXDirParser.getEditor();

        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        xmlReader.setContentHandler(handler);

        File file = new File("path/to/your/packagedirectory.xml");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        xmlReader.parse(inputSource);

        boolean expectedAcceptsLineNumber = true;

        boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();

        assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        xmlReader.setContentHandler(handler);

        File file = new File("path/to/your/packagedirectory.xml");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        xmlReader.parse(inputSource);

        String expectedLineNumberParameter = "expected_line_number_parameter";

        String actualLineNumberParameter = SAXDirParser.lineNumberParameter();

        assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
    }

    @Test(expected = SAXException.class)
    public void testExceptionWhenParsingInvalidXML() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        xmlReader.setContentHandler(handler);

        File file = new File("path/to/your/invalid_packagedirectory.xml");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        xmlReader.parse(inputSource);
    }
}