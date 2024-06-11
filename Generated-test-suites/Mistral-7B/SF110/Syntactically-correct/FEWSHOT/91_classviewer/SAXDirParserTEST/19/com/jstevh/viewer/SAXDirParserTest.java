package com.jstevh.viewer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);

        try {
            reader.parse("src/test/resources/packagedirectory.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "java.lang/SomeClass.class");
        expectedDirectory.put("javax.swing.border", "javax.swing/border/TitledBorder.class");

        Map<String, String> actualDirectory = parser.getDirectory();
        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);

        try {
            reader.parse("src/test/resources/packagedirectory.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> expectedLocalPackages = new ArrayList<>();
        expectedLocalPackages.add("java.lang");

        List<String> actualLocalPackages = parser.getLocalPackages();
        assertSame(expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);

        try {
            reader.parse("src/test/resources/packagedirectory.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String expectedLocalBrowser = "Firefox";
        String actualLocalBrowser = parser.getLocalBrowser();
        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);

        try {
            reader.parse("src/test/resources/packagedirectory.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String expectedEditor = "Eclipse";
        String actualEditor = parser.getEditor();
        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);

        try {
            reader.parse("src/test/resources/packagedirectory.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean expectedAcceptsLineNumber = true;
        boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
        assertTrue(expectedAcceptsLineNumber == actualAcceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);

        try {
            reader.parse("src/test/resources/packagedirectory.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String expectedLineNumberParameter = "23";
        String actualLineNumberParameter = parser.lineNumberParameter();
        assertNull(actualLineNumberParameter);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDirectoryBeforeParsing() {
        SAXDirParser parser = new SAXDirParser();
        parser.getDirectory();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLocalPackagesBeforeParsing() {
        SAXDirParser parser = new SAXDirParser();
        parser.getLocalPackages();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLocalBrowserBeforeParsing() {
        SAXDirParser parser = new SAXDirParser();
        parser.getLocalBrowser();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetEditorBeforeParsing() {
        SAXDirParser parser = new SAXDirParser();
        parser.getEditor();
    }

    @Test(expected = IllegalStateException.class)
    public void testAcceptsLineNumberBeforeParsing() {
        SAXDirParser parser = new SAXDirParser();
        parser.acceptsLineNumber();
    }

    @Test(expected = NullPointerException.class)
    public void testLineNumberParameterBeforeParsing() {
        SAXDirParser parser = new SAXDirParser();
        parser.lineNumberParameter();
    }
}