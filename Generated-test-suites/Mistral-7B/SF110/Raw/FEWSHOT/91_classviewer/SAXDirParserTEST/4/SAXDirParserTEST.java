package com.jstevh.viewer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.contains;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.parseable.DefaultHandler;
import org.xml.sax.helpers.StringList;

public class SAXDirParserTest {

    private SAXDirParser parser;

    @Before
    public void setUp() {
        parser = new SAXDirParser();
    }

    @Test
    public void testStartElement_BrowserLoc() throws SAXException {
        String xml = "<BrowserLoc>Mozilla/5.0</BrowserLoc>";
        parser.startElement("", "", "BrowserLoc", new Attributes());
        assertEquals("Mozilla/5.0", parser.localBrowser);
    }

    @Test
    public void testStartElement_Editor() throws SAXException {
        String xml = "<Editor>JEdit</Editor>";
        parser.startElement("", "", "Editor", new Attributes());
        assertEquals("JEdit", parser.file_editor);
    }

    @Test
    public void testStartElement_AcceptsLineNumber() throws SAXException {
        String xml = "<acceptsLineNumber>Yes</acceptsLineNumber>";
        parser.startElement("", "", "acceptsLineNumber", new Attributes());
        assertTrue(parser.lineNumAccepted);
    }

    @Test
    public void testStartElement_AcceptsLineNumber_No() throws SAXException {
        String xml = "<acceptsLineNumber>No</acceptsLineNumber>";
        parser.startElement("", "", "acceptsLineNumber", new Attributes());
        assertFalse(parser.lineNumAccepted);
    }

    @Test
    public void testStartElement_LineNumberParameter() throws SAXException {
        String xml = "<parameter name=\"lineNumberParameter\">123</parameter>";
        parser.startElement("", "", "parameter", new Attributes());
        assertEquals("123", parser.lineNumParam);
    }

    @Test
    public void testStartElement_Web() throws SAXException {
        String xml = "<Web>http://example.com</Web>";
        parser.startElement("", "", "Web", new Attributes());
        assertEquals("http://example.com", parser.location);
    }

    @Test
    public void testStartElement_Local() throws SAXException {
        String xml = "<Local>Yes</Local>";
        parser.startElement("", "", "Local", new Attributes());
        assertTrue(parser.local);
    }

    @Test
    public void testStartElement_Local_No() throws SAXException {
        String xml = "<Local>No</Local>";
        parser.startElement("", "", "Local", new Attributes());
        assertFalse(parser.local);
    }

    @Test
    public void testStartElement_pkg() throws SAXException {
        String xml = "<pkg>java.lang</pkg>";
        parser.startElement("", "", "pkg", new Attributes());
        assertEquals("java.lang", parser.location);
        assertTrue(parser.local);
    }

    @Test
    public void testStartElement_pkg_NoLocal() throws SAXException {
        String xml = "<pkg>javax.swing.border</pkg>";
        parser.startElement("", "", "pkg", new Attributes());
        assertEquals("javax.swing.border", parser.location);
        assertFalse(parser.local);
    }

    @Test
    public void testGetDirectory() {
        Map<String, String> expected = new HashMap<>();
        expected.put("java.lang", "location1");
        expected.put("javax.swing.border", "location2");
        parser.directory = expected;

        Map<String, String> result = SAXDirParser.getDirectory();
        assertEquals(expected, result);
    }

    @Test
    public void testGetLocalPackages() {
        List<String> expected = Arrays.asList("pkg1", "pkg2");
        parser.localPackages = new StringList(expected.toArray(new String[0]));

        List<String> result = SAXDirParser.getLocalPackages();
        assertEquals(expected, result);
    }

    @Test
    public void testGetLocalBrowser() {
        String expected = "Mozilla/5.0";
        parser.localBrowser = expected;

        String result = SAXDirParser.getLocalBrowser();
        assertEquals(expected, result);
    }

    @Test
    public void testGetEditor() {
        String expected = "JEdit";
        parser.file_editor = expected;

        String result = SAXDirParser.getEditor();
        assertEquals(expected, result);
    }

    @Test
    public void testAcceptsLineNumber() {
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        assertEquals("", SAXDirParser.lineNumberParameter());
    }

    @Test
    public void testMain() {
        try {
            SAXDirParser.main(new String[0]);
            fail("Exception expected");
        } catch (Exception e) {
            //expected
        }
    }
}