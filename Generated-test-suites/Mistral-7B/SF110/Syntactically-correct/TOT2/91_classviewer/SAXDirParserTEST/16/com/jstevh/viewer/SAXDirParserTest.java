package com.jstevh.viewer.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testConstructor() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement_BrowserLoc() {
        String expectedBrowser = "Firefox";
        SAXDirParser parser = spy(new SAXDirParser());
        doReturn(true).when(parser).check;

        Attributes atts = mock(Attributes.class);
        when(atts.getValue("name")).thenReturn("BrowserLoc");
        when(atts.getValue("value")).thenReturn(expectedBrowser);

        parser.startElement("", "BrowserLoc", "BrowserLoc", atts);

        assertEquals(expectedBrowser, parser.localBrowser);
    }

    @Test
    public void testCharacters_acceptsLineNumber_Yes() {
        SAXDirParser parser = new SAXDirParser();
        parser.characters(new char[]{'Y', 'e', 's'}, 0, 3);

        assertEquals(true, parser.lineNumAccepted);
    }

    @Test
    public void testGetDirectory() {
        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "/path/to/java.lang.class");
        expectedDirectory.put("javax.swing.border", "/path/to/javax.swing.border.class");

        SAXDirParser parser = new SAXDirParser();
        parser.parse("src/test/resources/packagedirectory.xml");

        Map<String, String> actualDirectory = parser.getDirectory();

        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testMain() {
        try {
            SAXDirParser.main(new String[0]);
            assertTrue(true);
        } catch (Exception e) {
            fail("Main method should not throw an exception");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetDirectory_NullPointerException() {
        SAXDirParser parser = new SAXDirParser();
        parser.getDirectory();
    }

    @Test(expected = SAXException.class)
    public void testStartElement_IncorrectTagName() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "IncorrectTagName", "IncorrectTagName", null);
    }

    @Test(expected = IOException.class)
    public void testParse_NonExistentXMLFile() {
        SAXDirParser parser = new SAXDirParser();
        File xmlFile = new File("src/test/resources/non-existent.xml");
        parser.parse(xmlFile.getAbsolutePath());
    }

    @Test(expected = SAXException.class)
    public void testParse_MalformedXMLFile() {
        SAXDirParser parser = new SAXDirParser();
        File xmlFile = new File("src/test/resources/malformed.xml");
        parser.parse(xmlFile.getAbsolutePath());
    }

    @Test(expected = SAXException.class)
    public void testStartElement_IncorrectTagLevel() {
        SAXDirParser parser = new SAXDirParser();
        parser.startElement("", "Web", "Web", null);
    }

    @Test
    public void testEndElement_Group() {
        SAXDirParser parser = new SAXDirParser();
        parser.endElement("", "Group", "Group");
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser parser = new SAXDirParser();
        parser.parse("src/test/resources/packagedirectory.xml");

        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("java.lang");
        expectedLocalPackages.add("javax.swing.border");

        StringList actualLocalPackages = parser.getLocalPackages();

        assertEquals(expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser parser = new SAXDirParser();
        parser.parse("src/test/resources/packagedirectory.xml");

        assertEquals("", parser.localBrowser);
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        parser.parse("src/test/resources/packagedirectory.xml");

        assertEquals(false, parser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser parser = new SAXDirParser();
        parser.parse("src/test/resources/packagedirectory.xml");

        assertEquals("", parser.lineNumberParameter());
    }
}