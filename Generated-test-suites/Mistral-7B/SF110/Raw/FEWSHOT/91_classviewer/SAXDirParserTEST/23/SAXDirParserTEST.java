package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() {
        SAXDirParser parser = new SAXDirParser();
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(parser);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("packagedirectory.xml");
            xmlReader.parse(inputStream);
            Map<String, String> expectedDirectory = new HashMap<>();
            expectedDirectory.put("java.lang", "/java/lang/");
            expectedDirectory.put("javax.swing.border", "/javax/swing/border/");
            Map<String, String> actualDirectory = parser.getDirectory();
            assertEquals(expectedDirectory, actualDirectory);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser parser = new SAXDirParser();
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(parser);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("packagedirectory.xml");
            xmlReader.parse(inputStream);
            List<String> expectedLocalPackages = new ArrayList<>();
            expectedLocalPackages.add("java.lang");
            expectedLocalPackages.add("javax.swing.border");
            List<String> actualLocalPackages = parser.getLocalPackages();
            assertEquals(expectedLocalPackages, actualLocalPackages);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser parser = new SAXDirParser();
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(parser);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("packagedirectory.xml");
            xmlReader.parse(inputStream);
            String expectedLocalBrowser = "Firefox";
            String actualLocalBrowser = parser.getLocalBrowser();
            assertEquals(expectedLocalBrowser, actualLocalBrowser);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetEditor() {
        SAXDirParser parser = new SAXDirParser();
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(parser);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("packagedirectory.xml");
            xmlReader.parse(inputStream);
            String expectedEditor = "Eclipse";
            String actualEditor = parser.getEditor();
            assertEquals(expectedEditor, actualEditor);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(parser);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("packagedirectory.xml");
            xmlReader.parse(inputStream);
            boolean expectedAcceptLineNumber = true;
            boolean actualAcceptLineNumber = parser.acceptsLineNumber();
            assertEquals(expectedAcceptLineNumber, actualAcceptLineNumber);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser parser = new SAXDirParser();
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(parser);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("packagedirectory.xml");
            xmlReader.parse(inputStream);
            String expectedLineNumberParameter = "67";
            String actualLineNumberParameter = parser.lineNumberParameter();
            assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidXmlFile() {
        SAXDirParser parser = new SAXDirParser();
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(parser);
            File inputFile = new File("invalidfile.xml");
            xmlReader.parse(inputFile);
            fail("Expected SAXException to be thrown");
        } catch (SAXException e) {
            // expected
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyXmlFile() {
        SAXDirParser parser = new SAXDirParser();
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(parser);
            InputStream inputStream = new FileInputStream("emptyfile.xml");
            xmlReader.parse(inputStream);
            assertTrue(parser.getDirectory().isEmpty());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}