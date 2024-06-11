package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        // Set up input and output streams for test
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        // Test loading a file with a known character set
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());

        // Test loading a file with an unknown character set
        File unknownFile = new File("test_unknown.html");
        Document unknownDocument = DataUtil.load(unknownFile, null, "");
        assertNotNull(unknownDocument);
        assertEquals("UTF-8", unknownDocument.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Test loading a known character set from an input stream
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());

        // Test loading an unknown character set from an input stream
        String unknownHtml = "<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>";
        InputStream unknownInputStream = new ByteArrayInputStream(unknownHtml.getBytes(StandardCharsets.UTF_8));
        Document unknownDocument = DataUtil.load(unknownInputStream, null, "");
        assertNotNull(unknownDocument);
        assertEquals("UTF-8", unknownDocument.outputSettings().charset().name());
    }

    @Test
    public void testParseInputStream() throws IOException {
        // Test parsing a known character set from an input stream
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());

        // Test parsing an unknown character set from an input stream
        String unknownHtml = "<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>";
        InputStream unknownInputStream = new ByteArrayInputStream(unknownHtml.getBytes(StandardCharsets.UTF_8));
        Document unknownDocument = DataUtil.parseInputStream(unknownInputStream, null, "", Parser.htmlParser());
        assertNotNull(unknownDocument);
        assertEquals("UTF-8", unknownDocument.outputSettings().charset().name());

        // Test parsing with a specified Parser
        String xmlHtml = "<html xmlns='http://www.w3.org/1999/xhtml'><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream xmlInputStream = new ByteArrayInputStream(xmlHtml.getBytes(StandardCharsets.UTF_8));
        Document xmlDocument = DataUtil.parseInputStream(xmlInputStream, "UTF-8", "", Parser.xmlParser());
        assertNotNull(xmlDocument);
        assertEquals("UTF-8", xmlDocument.outputSettings().charset().name());
        assertEquals("xml", xmlDocument.rootElement().tagName());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Test crossing input and output streams
        String inputString = "Test Input";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(inputString, new String(outputBytes, StandardCharsets.UTF_8));
    }
}