package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;

public class SAXDirParserTest {

    private SAXDirParser parser;
    private HashMap<String, String> directory;
    private List<String> localPackages;
    private String localBrowser;
    private String editor;
    private boolean acceptsLineNumber;
    private String lineNumberParam;

    @Before
    public void setUp() {
        parser = new SAXDirParser();
        directory = new HashMap<>();
        localPackages = new ArrayList<>();
        parser.directory = directory;
        parser.localPackages = localPackages;
    }

    @After
    public void tearDown() {
        parser = null;
        directory = null;
        localPackages = null;
    }

    @Test
    public void testStartElementBrowserLoc() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/browser_loc.xml");
        assertEquals("Firefox", parser.getWebData());
    }

    @Test
    public void testStartElementEditor() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/editor.xml");
        assertEquals("Notepad++", parser.getWebData());
    }

    @Test
    public void testStartElementAcceptsLineNumber() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/accepts_line_number.xml");
        assertTrue(parser.acceptsLineNumber());
    }

    @Test
    public void testStartElementLineNumberParameter() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/line_number_parameter.xml");
        assertEquals("123", parser.lineNumberParameter());
    }

    @Test
    public void testStartElementWeb() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/web.xml");
        assertEquals("/home/user/webapps", parser.getWebData());
    }

    @Test
    public void testStartElementLocal() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/local.xml");
        assertTrue(parser.isLocal());
    }

    @Test
    public void testStartElementNames() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/names.xml");
        assertEquals("Java", parser.getWebData());
    }

    @Test
    public void testStartElementPkg() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/pkg.xml");
        assertEquals("com.jstevh.viewer", parser.getWebData());
        assertEquals("java.lang", directory.get("java.lang"));
        assertEquals("/home/user/webapps/java.lang", parser.location);
        assertTrue(localPackages.contains("com.jstevh.viewer"));
    }

    @Test
    public void testEndElementGroup() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/group.xml");
        assertFalse(parser.check);
    }

    @Test
    public void testGetDirectory() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/dir.xml");
        directory = parser.getDirectory();
        assertEquals("Java", directory.get("java.lang").substring(0, 4));
        assertEquals("/home/user/webapps/java.lang", directory.get("java.lang"));
        assertEquals("javax.swing.border", directory.get("javax.swing.border").substring(0, 16));
        assertEquals("/home/user/webapps/javax.swing.border", directory.get("javax.swing.border"));
    }

    @Test
    public void testGetLocalPackages() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/local_packages.xml");
        List<String> localPackagesTest = parser.getLocalPackages();
        assertSame(localPackages, localPackagesTest);
    }

    @Test
    public void testGetLocalBrowser() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/local_browser.xml");
        assertEquals("Firefox", parser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/editor.xml");
        assertEquals("Notepad++", parser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/accepts_line_number.xml");
        assertTrue(parser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
        myReader.setContentHandler(parser);
        myReader.parse("test_cases/line_number_parameter.xml");
        assertEquals("123", parser.lineNumberParameter());
    }
}