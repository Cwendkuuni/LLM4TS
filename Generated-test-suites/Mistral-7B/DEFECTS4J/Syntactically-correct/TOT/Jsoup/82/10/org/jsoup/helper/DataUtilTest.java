import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    private static final String HTML_FILE_PATH = "testData/test.html";
    private static final String XML_FILE_PATH = "testData/test.xml";
    private Document htmlDocument;
    private Document xmlDocument;

    @Before
    public void setUp() throws IOException {
        htmlDocument = DataUtil.load(new File(HTML_FILE_PATH), null, null);
        xmlDocument = DataUtil.load(new File(XML_FILE_PATH), null, null);
    }

    @Test
    public void testLoadHtmlFile() throws IOException {
        Document document = DataUtil.load(new File(HTML_FILE_PATH), null, null);
        assertNotNull(document);
        assertTrue(document.outputSettings().contentType().startsWith("text/html"));
    }

    @Test
    public void testLoadXmlFile() throws IOException {
        Document document = DataUtil.load(new File(XML_FILE_PATH), null, null);
        assertNotNull(document);
        assertTrue(document.outputSettings().contentType().startsWith("application/xml"));
    }

    @Test
    public void testLoadInvalidFile() throws IOException {
        File invalidFile = new File("testData/invalidFile.txt");
        try {
            DataUtil.load(invalidFile, null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<?xml version='1.0' encoding='x-invalid'?>".getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, "x-invalid", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithValidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(new File(HTML_FILE_PATH));
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.parseInputStream(inputStream, "x-invalid", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithValidCharsetAndParser() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.xml().asXml());
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expected = "Input data".getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[expected.length];
        int bytesRead = outputStream.read(actual);

        assertEquals(expected.length, bytesRead);
        assertArrayEquals(expected, actual);

        outputStream.close();
        inputStream.close();
    }
}