package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.junit.Test;
import com.jstevh.viewer.SAXDirParser;
import org.xml.sax.SAXException;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.XMLInputFactory;
import javax.xml.parsers.XMLParserConfigurationException;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLInputFactory xmlInputFactory = saxParser.getXMLInputFactory();
        File xmlFile = new File("packagedirectory.xml");
        InputSource inputSource = new InputSource(xmlFile.toURI().toURL().openStream());

        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);
        saxParser.parse(inputSource);

        HashMap<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "expected_location_for_java.lang");
        expectedDirectory.put("javax.swing.border", "expected_location_for_javax.swing.border");

        HashMap<String, String> actualDirectory = parser.getDirectory();
        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLInputFactory xmlInputFactory = saxParser.getXMLInputFactory();
        File xmlFile = new File("packagedirectory.xml");
        InputSource inputSource = new InputSource(xmlFile.toURI().toURL().openStream());

        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);
        saxParser.parse(inputSource);

        String[] expectedLocalPackages = new String[]{"local_package1", "local_package2"};
        StringList actualLocalPackages = parser.getLocalPackages();
        assertArrayEquals(expectedLocalPackages, actualLocalPackages.toArray());
    }

    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLInputFactory xmlInputFactory = saxParser.getXMLInputFactory();
        File xmlFile = new File("packagedirectory.xml");
        InputSource inputSource = new InputSource(xmlFile.toURI().toURL().openStream());

        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);
        saxParser.parse(inputSource);

        String expectedLocalBrowser = "expected_local_browser";
        String actualLocalBrowser = parser.getLocalBrowser();
        assertEquals(expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLInputFactory xmlInputFactory = saxParser.getXMLInputFactory();
        File xmlFile = new File("packagedirectory.xml");
        InputSource inputSource = new InputSource(xmlFile.toURI().toURL().openStream());

        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);
        saxParser.parse(inputSource);

        String expectedEditor = "expected_editor";
        String actualEditor = parser.getEditor();
        assertEquals(expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLInputFactory xmlInputFactory = saxParser.getXMLInputFactory();
        File xmlFile = new File("packagedirectory.xml");
        InputSource inputSource = new InputSource(xmlFile.toURI().toURL().openStream());

        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);
        saxParser.parse(inputSource);

        boolean expectedAcceptsLineNumber = true;
        boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
        assertTrue(expectedAcceptsLineNumber == actualAcceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLInputFactory xmlInputFactory = saxParser.getXMLInputFactory();
        File xmlFile = new File("packagedirectory.xml");
        InputSource inputSource = new InputSource(xmlFile.toURI().toURL().openStream());

        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);
        saxParser.parse(inputSource);

        String expectedLineNumberParameter = "expected_line_number_parameter";
        String actualLineNumberParameter = parser.lineNumberParameter();
        assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
    }

    @Test(expected = SAXException.class)
    public void testExceptionWhenParsingXMLFile() throws ParcerConfigurationException, SAXException, IOException, XMLParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        XMLInputFactory xmlInputFactory = saxParser.getXMLInputFactory();
        File xmlFile = new File("non_existing_xml_file.xml");
        InputSource inputSource = new InputSource(xmlFile.toURI().toURL().openStream());

        SAXDirParser parser = new SAXDirParser();
        saxParser.setContentHandler(parser);
        saxParser.parse(inputSource);
    }
}