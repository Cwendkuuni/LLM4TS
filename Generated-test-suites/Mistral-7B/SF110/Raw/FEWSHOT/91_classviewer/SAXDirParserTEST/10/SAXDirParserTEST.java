package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.StringList;

import java.util.HashMap;
import java.util.Map;

public class SAXDirParserTest {

    private SAXDirParser parser;
    private SAXParser saxParser;
    private XMLReader xmlReader;

    @Before
    public void setup() {
        parser = new SAXDirParser();
        saxParser = Mockito.mock(SAXParser.class);
        xmlReader = Mockito.mock(XMLReader.class);
        Mockito.when(saxParser.getXMLReader()).thenReturn(xmlReader);
        Mockito.when(xmlReader.getContentHandler()).thenReturn(parser);
    }

    @Test
    public void testGetWebData() {
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement_BrowserLoc() throws SAXException {
        String xmlString = "<BrowserLoc>local browser</BrowserLoc>";
        xmlReader.parse(new StringReader(xmlString));
        assertEquals("local browser", parser.localBrowser);
    }

    @Test
    public void testStartElement_Editor() throws SAXException {
        String xmlString = "<Editor>text editor</Editor>";
        xmlReader.parse(new StringReader(xmlString));
        assertEquals("text editor", parser.file_editor);
    }

    @Test
    public void testStartElement_acceptsLineNumber() throws SAXException {
        String xmlString = "<acceptsLineNumber>Yes</acceptsLineNumber>";
        xmlReader.parse(new StringReader(xmlString));
        assertTrue(parser.lineNumAccepted);
    }

    @Test
    public void testStartElement_acceptsLineNumber_No() throws SAXException {
        String xmlString = "<acceptsLineNumber>No</acceptsLineNumber>";
        xmlReader.parse(new StringReader(xmlString));
        assertFalse(parser.lineNumAccepted);
    }

    @Test
    public void testStartElement_lineNumberParameter() throws SAXException {
        String xmlString = "<parameter>lineNumber</parameter>";
        xmlReader.parse(new StringReader(xmlString));
        assertEquals("lineNumber", parser.lineNumParam);
    }

    @Test
    public void testStartElement_Group() throws SAXException {
        String xmlString = "<Group/>";
        xmlReader.parse(new StringReader(xmlString));
        assertFalse(parser.check);
    }

    @Test
    public void testStartElement_Web() throws SAXException {
        String xmlString = "<Web>http://example.com</Web>";
        xmlReader.parse(new StringReader(xmlString));
        assertEquals("http://example.com", parser.location);
    }

    @Test
    public void testStartElement_Local() throws SAXException {
        String xmlString = "<Local>Java</Local>";
        xmlReader.parse(new StringReader(xmlString));
        assertTrue(parser.local);
    }

    @Test
    public void testStartElement_Local_No() throws SAXException {
        String xmlString = "<Local>No</Local>";
        xmlReader.parse(new StringReader(xmlString));
        assertFalse(parser.local);
    }

    @Test
    public void testStartElement_pkg() throws SAXException {
        String xmlString = "<pkg>javax.swing.border</pkg>";
        xmlReader.parse(new StringReader(xmlString));
        assertEquals("javax.swing.border", parser.location);
        assertEquals(parser.location, SAXDirParser.directory.get("javax.swing.border"));
        assertTrue(parser.local);
        assertEquals(parser.location, SAXDirParser.localPackages.get(0));
    }

    @Test
    public void testEndElement_Group() throws SAXException {
        String xmlString = "<Group><pkg>javax.swing.border</pkg></Group>";
        xmlReader.parse(new StringReader(xmlString));
        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "path/to/java.lang.jar");
        expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border.jar");

        parser.parse("test.xml");
        Map<String, String> actualDirectory = parser.getDirectory();
        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() {
        String[] expectedPackages = {"javax.swing.border"};

        parser.parse("test.xml");
        StringList actualPackages = parser.getLocalPackages();
        assertSame(expectedPackages, actualPackages.toArray());
    }

    @Test
    public void testGetLocalBrowser() {
        assertEquals("", parser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        assertEquals("", parser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        assertTrue(parser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        assertEquals("", parser.lineNumberParameter());
    }

    @Test(expected = NullPointerException.class)
    public void testParse_Null() throws Exception {
        parser.parse(null);
    }
}