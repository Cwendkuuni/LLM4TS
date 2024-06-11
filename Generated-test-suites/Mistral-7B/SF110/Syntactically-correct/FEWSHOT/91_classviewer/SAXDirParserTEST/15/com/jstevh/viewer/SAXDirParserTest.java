package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.junit.Test;
import org.xml.sax.SAXException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            SAXDirParser handler = new SAXDirParser();
            xmlReader.setContentHandler(handler);

            String xmlString = "<Web>\n" +
                               "<Local>\n" +
                               "  <Names>\n" +
                               "    <pkg name=\"java.lang\" location=\"C:/Java/lib/java.lang.jar\"/>\n" +
                               "    <pkg name=\"javax.swing.border\" location=\"C:/Java/lib/javax.swing.border.jar\"/>\n" +
                               "  </Names>\n" +
                               "</Local>\n" +
                               "</Web>";

            InputSource inputSource = new InputSource(new StringReader(xmlString));
            xmlReader.parse(inputSource);

            Map<String, String> actualDirectory = handler.getDirectory();
            Map<String, String> expectedDirectory = new HashMap<>();
            expectedDirectory.put("java.lang", "C:/Java/lib/java.lang.jar");
            expectedDirectory.put("javax.swing.border", "C:/Java/lib/javax.swing.border.jar");

            assertEquals(expectedDirectory, actualDirectory);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLocalPackages() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            SAXDirParser handler = new SAXDirParser();
            xmlReader.setContentHandler(handler);

            String xmlString = "<Web>\n" +
                               "<Local>\n" +
                               "  <Names>\n" +
                               "    <pkg name=\"java.lang\" location=\"C:/Java/lib/java.lang.jar\"/>\n" +
                               "    <pkg name=\"javax.swing.border\" location=\"C:/Java/lib/javax.swing.border.jar\"/>\n" +
                               "    <pkg name=\"com.custom.package\" location=\"C:/Java/lib/custom.jar\"/>\n" +
                               "  </Names>\n" +
                               "</Local>\n" +
                               "</Web>";

            InputSource inputSource = new InputSource(new StringReader(xmlString));
            xmlReader.parse(inputSource);

            StringList actualLocalPackages = handler.getLocalPackages();
            StringList expectedLocalPackages = new StringList();
            expectedLocalPackages.add("com.custom.package");

            assertEquals(expectedLocalPackages, actualLocalPackages);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLocalBrowser() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            SAXDirParser handler = new SAXDirParser();
            xmlReader.setContentHandler(handler);

            String xmlString = "<Web>\n" +
                               "<Local>\n" +
                               "<BrowserLoc>Firefox</BrowserLoc>\n" +
                               "</Local>\n" +
                               "</Web>";

            InputSource inputSource = new InputSource(new StringReader(xmlString));
            xmlReader.parse(inputSource);

            String actualLocalBrowser = handler.getLocalBrowser();
            String expectedLocalBrowser = "Firefox";

            assertEquals(expectedLocalBrowser, actualLocalBrowser);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetEditor() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            SAXDirParser handler = new SAXDirParser();
            xmlReader.setContentHandler(handler);

            String xmlString = "<Web>\n" +
                               "<Editor>Notepad++</Editor>\n" +
                               "</Web>";

            InputSource inputSource = new InputSource(new StringReader(xmlString));
            xmlReader.parse(inputSource);

            String actualEditor = handler.getEditor();
            String expectedEditor = "Notepad++";

            assertEquals(expectedEditor, actualEditor);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptsLineNumber() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            SAXDirParser handler = new SAXDirParser();
            xmlReader.setContentHandler(handler);

            String xmlString = "<Web>\n" +
                               "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                               "</Web>";

            InputSource inputSource = new InputSource(new StringReader(xmlString));
            xmlReader.parse(inputSource);

            boolean actualAcceptsLineNumber = handler.acceptsLineNumber();
            boolean expectedAcceptsLineNumber = true;

            assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLineNumberParameter() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            SAXDirParser handler = new SAXDirParser();
            xmlReader.setContentHandler(handler);

            String xmlString = "<Web>\n" +
                               "<acceptsLineNumber>No</acceptsLineNumber>\n" +
                               "<parameter name=\"lineNumberParameter\">123</parameter>\n" +
                               "</Web>";

            InputSource inputSource = new InputSource(new StringReader(xmlString));
            xmlReader.parse(inputSource);

            String actualLineNumberParameter = handler.lineNumberParameter();
            String expectedLineNumberParameter = "123";

            assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseEmptyXML() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            SAXDirParser handler = new SAXDirParser();
            xmlReader.setContentHandler(handler);

            String xmlString = "";

            InputSource inputSource = new InputSource(new StringReader(xmlString));
            xmlReader.parse(inputSource);

            assertTrue(handler.getDirectory().isEmpty());
            assertTrue(handler.getLocalPackages().isEmpty());
            assertTrue(handler.getLocalBrowser().isEmpty());
            assertTrue(handler.getEditor().isEmpty());
            assertFalse(handler.acceptsLineNumber());
            assertTrue(handler.lineNumberParameter().isEmpty());
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseXMLWithInvalidXMLStructure() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            SAXDirParser handler = new SAXDirParser();
            xmlReader.setContentHandler(handler);

            String xmlString = "<Web>\n" +
                               "<Editor>Notepad++</Editor>\n" +
                               "<Local>\n" +
                               "  <pkg name=\"java.lang\" location=\"C:/Java/lib/java.lang.jar\"/>\n" +
                               "</Local>\n" +
                               "</Web>";

            InputSource inputSource = new InputSource(new StringReader(xmlString));
            xmlReader.parse(inputSource);

            assertTrue(handler.getDirectory().isEmpty());
            assertTrue(handler.getLocalPackages().isEmpty());
            assertTrue(handler.getLocalBrowser().isEmpty());
            assertTrue(handler.getEditor().isEmpty());
            assertFalse(handler.acceptsLineNumber());
            assertTrue(handler.lineNumberParameter().isEmpty());
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}