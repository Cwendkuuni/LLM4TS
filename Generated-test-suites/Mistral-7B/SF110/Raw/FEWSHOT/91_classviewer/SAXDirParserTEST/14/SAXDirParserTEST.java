package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.File;
import java.io.IOException;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        reader.setContentHandler(handler);
        File xmlFile = new File("packagedirectory.xml");
        reader.parse(xmlFile);

        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "path/to/java.lang");
        expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

        Map<String, String> actualDirectory = handler.getDirectory();

        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        reader.setContentHandler(handler);
        File xmlFile = new File("packagedirectory.xml");
        reader.parse(xmlFile);

        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("package1");
        expectedLocalPackages.add("package2");

        StringList actualLocalPackages = handler.getLocalPackages();

        assertEquals(expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        reader.setContentHandler(handler);
        File xmlFile = new File("packagedirectory.xml");
        reader.parse(xmlFile);

        String expectedLocalBrowser = "localBrowserValue";

        String actualLocalBrowser = handler.getLocalBrowser();

        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        reader.setContentHandler(handler);
        File xmlFile = new File("packagedirectory.xml");
        reader.parse(xmlFile);

        String expectedEditor = "editorValue";

        String actualEditor = handler.getEditor();

        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        reader.setContentHandler(handler);
        File xmlFile = new File("packagedirectory.xml");
        reader.parse(xmlFile);

        boolean expectedAcceptLineNumber = true;

        boolean actualAcceptLineNumber = handler.acceptsLineNumber();

        assertEquals(expectedAcceptLineNumber, actualAcceptLineNumber);
    }

    @Test
    public void testLineNumberParameter() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        reader.setContentHandler(handler);
        File xmlFile = new File("packagedirectory.xml");
        reader.parse(xmlFile);

        String expectedLineNumberParameter = "lineNumberParamValue";

        String actualLineNumberParameter = handler.lineNumberParameter();

        assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
    }

    @Test
    public void testParsingWithAcceptLineNumberYes() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        reader.setContentHandler(handler);
        File xmlFile = new File("packagedirectory_acceptLineNumberYes.xml");
        reader.parse(xmlFile);

        assertTrue(handler.acceptsLineNumber());
    }

    @Test
    public void testParsingWithAcceptLineNumberNo() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        SAXDirParser handler = new SAXDirParser();
        reader.setContentHandler(handler);
        File xmlFile = new File("packagedirectory_acceptLineNumberNo.xml");
        reader.parse(xmlFile);

        assertFalse(handler.acceptsLineNumber());
    }

}