package com.jstevh.viewer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.helpers.StringList;

import com.jstevh.viewer.SAXDirParser;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse("path/to/your/packagedirectory.xml");

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("java.lang", "test1");
        expectedMap.put("javax.swing.border", "test2");

        Map<String, String> actualMap = SAXDirParser.getDirectory();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse("path/to/your/packagedirectory.xml");

        List<String> expectedList = new ArrayList<>();
        expectedList.add("com.jstevh.viewer");
        expectedList.add("java.lang");

        List<String> actualList = SAXDirParser.getLocalPackages();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse("path/to/your/packagedirectory.xml");

        String expectedString = "Firefox";
        String actualString = SAXDirParser.getLocalBrowser();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetEditor() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse("path/to/your/packagedirectory.xml");

        String expectedString = "Notepad++";
        String actualString = SAXDirParser.getEditor();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse("path/to/your/packagedirectory.xml");

        boolean expectedBoolean = true;
        boolean actualBoolean = SAXDirParser.acceptsLineNumber();
        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse("path/to/your/packagedirectory.xml");

        String expectedString = "param1";
        String actualString = SAXDirParser.lineNumberParameter();
        assertEquals(expectedString, actualString);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDirectoryWhenXMLIsEmpty() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);

        reader.parse("");
        Map<String, String> directory = SAXDirParser.getDirectory();
        assertNull(directory);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocalPackagesWhenXMLIsEmpty() {
        SAXDirParser parser = new SAXDirParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);

        reader.parse("");
        List<String> localPackages = SAXDirParser.getLocalPackages();
        assertNull(localPackages);
    }
}