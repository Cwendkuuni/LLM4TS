package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.StringScanner;
import org.xml.sax.helpers.StringScannerImpl;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Directory>\n" +
                "<Group name=\"java\">\n" +
                "<Web location=\"C:/java\"/>\n" +
                "</Group>\n" +
                "<Group name=\"javax\">\n" +
                "<Web location=\"C:/javax\"/>\n" +
                "</Group>\n" +
                "<BrowserLoc>Mozilla</BrowserLoc>\n" +
                "</Directory>";

        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse(new StringReader(xml));

        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java", "C:/java");
        expectedDirectory.put("javax", "C:/javax");

        Map<String, String> resultDirectory = parser.getDirectory();
        assertEquals(expectedDirectory, resultDirectory);
    }

    @Test
    public void testGetLocalPackages() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Directory>\n" +
                "<Group name=\"java\">\n" +
                "<Web location=\"C:/java\"/>\n" +
                "</Group>\n" +
                "<Group name=\"javax\">\n" +
                "<Web location=\"C:/javax\"/>\n" +
                "</Group>\n" +
                "<Local accept=\"true\">\n" +
                "<pkg>junit</pkg>\n" +
                "</Local>\n" +
                "</Directory>";

        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse(new StringReader(xml));

        List<String> expectedLocalPackages = new ArrayList<>();
        expectedLocalPackages.add("junit");

        List<String> resultLocalPackages = parser.getLocalPackages();
        assertEquals(expectedLocalPackages, resultLocalPackages);

        parser.getDirectory(); // reset parser
        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Directory>\n" +
                "<Group name=\"java\">\n" +
                "<Web location=\"C:/java\"/>\n" +
                "</Group>\n" +
                "<Group name=\"javax\">\n" +
                "<Web location=\"C:/javax\"/>\n" +
                "</Group>\n" +
                "</Directory>";
        reader.parse(new StringReader(xml)); // test empty localPackages
        assertNull(parser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Directory>\n" +
                "<BrowserLoc>Mozilla</BrowserLoc>\n" +
                "</Directory>";

        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse(new StringReader(xml));

        String expectedLocalBrowser = "Mozilla";
        String resultLocalBrowser = parser.getLocalBrowser();
        assertEquals(expectedLocalBrowser, resultLocalBrowser);
    }

    @Test
    public void testGetEditor() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Directory>\n" +
                "<Editor>Eclipse</Editor>\n" +
                "</Directory>";

        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse(new StringReader(xml));

        String expectedEditor = "Eclipse";
        String resultEditor = parser.getEditor();
        assertEquals(expectedEditor, resultEditor);
    }

    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Directory>\n" +
                "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                "</Directory>";

        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse(new StringReader(xml));

        boolean expectedAcceptsLineNumber = true;
        boolean resultAcceptsLineNumber = parser.acceptsLineNumber();
        assertTrue(expectedAcceptsLineNumber == resultAcceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() throws Exception {
        SAXDirParser parser = new SAXDirParser();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Directory>\n" +
                "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                "<parameter>123</parameter>\n" +
                "</Directory>";

        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(parser);
        reader.parse(new StringReader(xml));

        String expectedLineNumberParameter = "123";
        String resultLineNumberParameter = parser.lineNumberParameter();
        assertEquals(expectedLineNumberParameter, resultLineNumberParameter);
    }

    @Test
    public void testSAXDirParserMain() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = factory.newSAXParser().getXMLReader();
        reader.setContentHandler(new SAXDirParser());
        StringReader input = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Directory>\n" +
                "<Web location=\"C:/java\"/>\n" +
                "<Web location=\"C:/javax\"/>\n" +
                "</Directory>");
        reader.parse(input);

        String expectedWebData = "";
        String resultWebData = new SAXDirParser().getWebData();
        assertEquals(expectedWebData, resultWebData);

        System.out.println("test directory, test gives " + new SAXDirParser().getDirectory().get("java.lang"));
        System.out.println("test directory, test gives " + new SAXDirParser().getDirectory().get("javax.swing.border"));
        System.out.println("test local browser, test gives " + new SAXDirParser().getLocalBrowser());
    }

    private static class StringReader implements java.io.Reader {
        private final String data;

        public StringReader(String data) {
            this.data = data;
        }

        public int read() throws IOException {
            StringScanner scanner = new StringScannerImpl(data);
            return scanner.next();
        }
    }
}