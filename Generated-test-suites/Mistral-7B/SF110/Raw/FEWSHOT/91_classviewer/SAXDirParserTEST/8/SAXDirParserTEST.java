package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;
import com.jstevh.viewer.*;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(new SAXDirParser());
        reader.parse(new File("path/to/your/packagedirectory.xml"));

        HashMap<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "/path/to/your/java.lang");
        expectedDirectory.put("javax.swing.border", "/path/to/your/javax.swing.border");

        HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();

        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(new SAXDirParser());
        reader.parse(new File("path/to/your/packagedirectory.xml"));

        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("com.jstevh.viewer.test");

        StringList actualLocalPackages = SAXDirParser.getLocalPackages();

        assertEquals(expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(new SAXDirParser());
        reader.parse(new File("path/to/your/packagedirectory.xml"));

        String expectedLocalBrowser = "Firefox";
        String actualLocalBrowser = SAXDirParser.getLocalBrowser();

        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(new SAXDirParser());
        reader.parse(new File("path/to/your/packagedirectory.xml"));

        String expectedEditor = "Eclipse";
        String actualEditor = SAXDirParser.getEditor();

        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(new SAXDirParser());
        reader.parse(new File("path/to/your/packagedirectory.xml"));

        boolean expectedAcceptsLineNumber = true;
        boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();

        assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(new SAXDirParser());
        reader.parse(new File("path/to/your/packagedirectory.xml"));

        String expectedLineNumberParameter = "34";
        String actualLineNumberParameter = SAXDirParser.lineNumberParameter();

        assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
    }

    @Test
    public void testSAXDirParser() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(new SAXDirParser());
        reader.parse(new File("path/to/your/packagedirectory.xml"));

        // Test other fields if needed
    }
}