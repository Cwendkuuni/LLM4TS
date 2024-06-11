package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.junit.Test;
import com.jstevh.viewer.SAXDirParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement_BrowserLoc() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<BrowserLoc>test</BrowserLoc>";
        parser.parseString(xml);
        assertTrue(parser.check);
        assertEquals(0, parser.level);
        assertEquals("test", parser.localBrowser);
        assertFalse(parser.check);
    }

    @Test
    public void testStartElement_Editor() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<Editor>test</Editor>";
        parser.parseString(xml);
        assertTrue(parser.check);
        assertEquals(10, parser.level);
        assertEquals("test", parser.file_editor);
        assertFalse(parser.check);
    }

    @Test
    public void testStartElement_acceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<acceptsLineNumber>Yes</acceptsLineNumber>";
        parser.parseString(xml);
        assertTrue(parser.check);
        assertEquals(11, parser.level);
        assertTrue(parser.lineNumAccepted);
        assertFalse(parser.check);
    }

    @Test
    public void testStartElement_parameter() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<parameter>test</parameter>";
        parser.parseString(xml);
        assertTrue(parser.check);
        assertEquals(12, parser.level);
        assertEquals("test", parser.lineNumParam);
        assertFalse(parser.check);
    }

    @Test
    public void testStartElement_Group() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<Group><Web><Local><pkg>java.lang</pkg></Local></Web></Group>";
        parser.parseString(xml);
        assertFalse(parser.check);
        assertEquals(-1, parser.level);
    }

    @Test
    public void testStartElement_Web() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<Web><Local><pkg>java.lang</pkg></Local></Web>";
        parser.parseString(xml);
        assertFalse(parser.check);
        assertEquals(30, parser.level);
        assertEquals("", parser.location);
    }

    @Test
    public void testStartElement_Local() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<Web><Local><pkg>java.lang</pkg></Local></Web>";
        parser.parseString(xml);
        assertFalse(parser.check);
        assertEquals(31, parser.level);
        assertFalse(parser.local);
        assertFalse(parser.check);
    }

    @Test
    public void testStartElement_pkg() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<Web><Local><pkg>java.lang</pkg></Local></Web>";
        parser.parseString(xml);
        assertFalse(parser.check);
        assertEquals(4, parser.level);
        assertEquals("java.lang", parser.location);
        assertTrue(parser.check);
        parser.parseString("<pkg>javax.swing.border</pkg>");
        assertEquals("javax.swing.border", parser.location);
        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<Web><Local><pkg>java.lang</pkg><pkg>javax.swing.border</pkg></Local></Web>";
        parser.parseString(xml);
        HashMap<String, String> directory = SAXDirParser.getDirectory();
        List<String> expected = new ArrayList<>();
        expected.add("java.lang");
        expected.add("javax.swing.border");
        assertEquals(expected, directory.keySet());
        List<String> actual = new ArrayList<>();
        for (Object o : directory.values()) {
            actual.add((String) o);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalPackages() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<Web><Local><pkg>java.lang</pkg><pkg>javax.swing.border</pkg></Local></Web>";
        parser.parseString(xml);
        StringList localPackages = SAXDirParser.getLocalPackages();
        List<String> expected = new ArrayList<>();
        expected.add("java.lang");
        expected.add("javax.swing.border");
        assertEquals(expected, localPackages);
    }

    @Test
    public void testGetLocalBrowser() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<BrowserLoc>test</BrowserLoc>";
        parser.parseString(xml);
        assertEquals("test", SAXDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<Editor>test</Editor>";
        parser.parseString(xml);
        assertEquals("test", SAXDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<acceptsLineNumber>Yes</acceptsLineNumber>";
        parser.parseString(xml);
        assertTrue(SAXDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<parameter>test</parameter>";
        parser.parseString(xml);
        assertEquals("test", SAXDirParser.lineNumberParameter());
    }
}