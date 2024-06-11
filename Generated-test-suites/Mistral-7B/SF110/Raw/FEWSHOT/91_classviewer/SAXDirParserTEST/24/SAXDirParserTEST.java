package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.StringScanner;
import org.xml.sax.helpers.StringScannerWrapper;
import org.xml.sax.*;
import java.io.*;
import java.util.*;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() {
        SAXDirParser parser = new SAXDirParser();
        InputSource inputSource = new InputSource(new StringReader("<Web>\n" +
                "<Local>\n" +
                "  <pkg>java.lang</pkg>\n" +
                "  <pkg>javax.swing.border</pkg>\n" +
                "</Local>\n" +
                "</Web>"));
        parser.parse(inputSource);
        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "test gives java.lang");
        expectedDirectory.put("javax.swing.border", "test gives javax.swing.border");
        Map<String, String> actualDirectory = parser.getDirectory();
        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser parser = new SAXDirParser();
        InputSource inputSource = new InputSource(new StringReader("<Web>\n" +
                "<Local>\n" +
                "<pkg>java.lang</pkg>\n" +
                "<pkg>javax.swing.border</pkg>\n" +
                "</Local>\n" +
                "<Local>\n" +
                "<pkg>com.jstevh.viewer</pkg>\n" +
                "</Local>\n" +
                "</Web>"));
        parser.parse(inputSource);
        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("java.lang");
        expectedLocalPackages.add("javax.swing.border");
        expectedLocalPackages.add("com.jstevh.viewer");
        StringList actualLocalPackages = parser.getLocalPackages();
        assertEquals(expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser parser = new SAXDirParser();
        InputSource inputSource = new InputSource(new StringReader("<Web>\n" +
                "<BrowserLoc>My Browser</BrowserLoc>\n" +
                "</Web>"));
        parser.parse(inputSource);
        String expectedLocalBrowser = "My Browser";
        String actualLocalBrowser = parser.getLocalBrowser();
        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() {
        SAXDirParser parser = new SAXDirParser();
        InputSource inputSource = new InputSource(new StringReader("<Web>\n" +
                "<Editor>JEdit</Editor>\n" +
                "</Web>"));
        parser.parse(inputSource);
        String expectedEditor = "JEdit";
        String actualEditor = parser.getEditor();
        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        InputSource inputSource = new InputSource(new StringReader("<Web>\n" +
                "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                "</Web>"));
        parser.parse(inputSource);
        boolean expectedAcceptsLineNumber = true;
        boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
        assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser parser = new SAXDirParser();
        InputSource inputSource = new InputSource(new StringReader("<Web>\n" +
                "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                "<parameter>123</parameter>\n" +
                "</Web>"));
        parser.parse(inputSource);
        String expectedLineNumberParameter = "123";
        String actualLineNumberParameter = parser.lineNumberParameter();
        assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
    }

    @Test
    public void testParseWithNoWebElement() {
        SAXDirParser parser = new SAXDirParser();
        InputSource inputSource = new InputSource(new StringReader("<Local>\n" +
                "<pkg>java.lang</pkg>\n" +
                "</Local>"));
        try {
            parser.parse(inputSource);
            fail("Expected SAXException to be thrown");
        } catch (SAXException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithEmptyWebElement() {
        SAXDirParser parser = new SAXDirParser();
        InputSource inputSource = new InputSource(new StringReader("<Web></Web>"));
        try {
            parser.parse(inputSource);
            fail("Expected SAXException to be thrown");
        } catch (SAXException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithInvalidWebElement() {
        SAXDirParser parser = new SAXDirParser();
        InputSource inputSource = new InputSource(new StringReader("<Web>Invalid</Web>"));
        try {
            parser.parse(inputSource);
            fail("Expected SAXException to be thrown");
        } catch (SAXException e) {
            // Expected
        }
    }
}