package com.jstevh.viewer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.junit.Test;
import org.junit.Before;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

public class SAXDirParserTest {

    private SAXDirParser parser;
    private HashMap<String, String> expectedDirectory;
    private List<String> expectedLocalPackages;
    private String expectedLocalBrowser;
    private String expectedEditor;
    private boolean expectedAcceptsLineNumber;
    private String expectedLineNumberParameter;

    @Before
    public void setup() {
        parser = new SAXDirParser();
        expectedDirectory = new HashMap<String, String>();
        expectedDirectory.put("java.lang", "testJavaLang");
        expectedDirectory.put("javax.swing.border", "testJSwingBorder");

        expectedLocalPackages = new ArrayList<String>();
        expectedLocalPackages.add("localPackage1");
        expectedLocalPackages.add("localPackage2");

        expectedLocalBrowser = "testLocalBrowser";
        expectedEditor = "testEditor";
        expectedAcceptsLineNumber = true;
        expectedLineNumberParameter = "lineNumberParameter";
    }

    @Test
    public void testParseDirectory() throws Exception {
        XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(parser);
        InputSource inputSource = new InputSource(getClass().getResourceAsStream("/packagedirectory.xml"));
        xmlReader.parse(inputSource);

        Map<String, String> actualDirectory = parser.getDirectory();
        assertSame(expectedDirectory, actualDirectory);
    }

    @Test
    public void testParseLocalPackages() throws Exception {
        XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(parser);
        InputSource inputSource = new InputSource(getClass().getResourceAsStream("/packagedirectory.xml"));
        xmlReader.parse(inputSource);

        List<String> actualLocalPackages = parser.getLocalPackages();
        assertSame(expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testParseLocalBrowser() throws Exception {
        XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(parser);
        InputSource inputSource = new InputSource(getClass().getResourceAsStream("/packagedirectory.xml"));
        xmlReader.parse(inputSource);

        String actualLocalBrowser = parser.getLocalBrowser();
        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testParseEditor() throws Exception {
        XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(parser);
        InputSource inputSource = new InputSource(getClass().getResourceAsstream("/packagedirectory.xml"));
        xmlReader.parse(inputSource);

        String actualEditor = parser.getEditor();
        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testParseAcceptsLineNumber() throws Exception {
        XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(parser);
        InputSource inputSource = new InputSource(getClass().getResourceAsStream("/packagedirectory.xml"));
        xmlReader.parse(inputSource);

        boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
        assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
    }

    @Test
    public void testParseLineNumberParameter() throws Exception {
        XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(parser);
        InputSource inputSource = new InputSource(getClass().getResourceAsStream("/packagedirectory.xml"));
        xmlReader.parse(inputSource);

        String actualLineNumberParameter = parser.lineNumberParameter();
        assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
    }

    @Test
    public void testParseEmptyDirectory() throws Exception {
        XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(parser);
        InputSource inputSource = new InputSource(new StringReader(""));
        xmlReader.parse(inputSource);

        Map<String, String> actualDirectory = parser.getDirectory();
        assertNull(actualDirectory);
    }

    @Test
    public void testParseNullDirectory() throws Exception {
        XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(parser);
        InputSource inputSource = new InputSource(null);
        xmlReader.parse(inputSource);

        Map<String, String> actualDirectory = parser.getDirectory();
        assertNull(actualDirectory);
    }

    @Test
    public void testParseDirectoryWithInvalidPackage() throws Exception {
        XMLReader xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(parser);
        InputSource inputSource = new InputSource(new StringReader("<invalidpackage>test</invalidpackage>"));
        xmlReader.parse(inputSource);

        Map<String, String> actualDirectory = parser.getDirectory();
        assertNotNull(actualDirectory);
        assertTrue(actualDirectory.isEmpty());
    }
}