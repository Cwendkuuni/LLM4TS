package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;
import org.xml.sax.SAXException;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        SAXDirParser handler = new SAXDirParser();
        InputStream inputStream = new FileInputStream(new File("packagedirectory.xml"));
        saxParser.parse(inputStream, handler);
        inputStream.close();

        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "java.lang");
        expectedDirectory.put("javax.swing.border", "javax.swing.border");

        Map<String, String> actualDirectory = handler.getDirectory();
        assertEquals("test getDirectory, directory is not as expected", expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        SAXDirParser handler = new SAXDirParser();
        InputStream inputStream = new FileInputStream(new File("packagedirectory.xml"));
        saxParser.parse(inputStream, handler);
        inputStream.close();

        List<String> expectedLocalPackages = new ArrayList<>();
        expectedLocalPackages.add("java.lang");
        expectedLocalPackages.add("javax.swing.border");

        List<String> actualLocalPackages = handler.getLocalPackages();
        if (actualLocalPackages != null) {
            assertEquals("test getLocalPackages, local packages are not as expected", expectedLocalPackages, actualLocalPackages);
        } else {
            assertNull("test getLocalPackages, local packages is null");
        }
    }

    @Test
    public void testGetLocalBrowser() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        SAXDirParser handler = new SAXDirParser();
        InputStream inputStream = new FileInputStream(new File("packagedirectory.xml"));
        saxParser.parse(inputStream, handler);
        inputStream.close();

        String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.111 Safari/56.0";
        String actualLocalBrowser = handler.getLocalBrowser();
        assertEquals("test getLocalBrowser, local browser is not as expected", expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        SAXDirParser handler = new SAXDirParser();
        InputStream inputStream = new FileInputStream(new File("packagedirectory.xml"));
        saxParser.parse(inputStream, handler);
        inputStream.close();

        String expectedEditor = "Notepad";
        String actualEditor = handler.getEditor();
        assertEquals("test getEditor, editor is not as expected", expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        SAXDirParser handler = new SAXDirParser();
        InputStream inputStream = new FileInputStream(new File("packagedirectory.xml"));
        saxParser.parse(inputStream, handler);
        inputStream.close();

        boolean expectedAcceptsLineNumber = true;
        boolean actualAcceptsLineNumber = handler.acceptsLineNumber();
        assertEquals("test acceptsLineNumber, accepts line number is not as expected", expectedAcceptsLineNumber, actualAcceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        SAXDirParser handler = new SAXDirParser();
        InputStream inputStream = new FileInputStream(new File("packagedirectory.xml"));
        saxParser.parse(inputStream, handler);
        inputStream.close();

        String expectedLineNumberParameter = "5";
        String actualLineNumberParameter = handler.lineNumberParameter();
        assertEquals("test lineNumberParameter, line number parameter is not as expected", expectedLineNumberParameter, actualLineNumberParameter);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidMethodCall() {
        new SAXDirParser().invalidMethodCall();
    }
}