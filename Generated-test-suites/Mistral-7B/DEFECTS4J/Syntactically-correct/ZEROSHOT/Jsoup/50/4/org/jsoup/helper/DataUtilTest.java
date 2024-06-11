package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";
    private static final String FILE_PATH = "src/test/resources/testFile.html";
    private DataUtil dataUtil;
    private Parser parser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File(FILE_PATH);
        Document document = dataUtil.load(file, CHARSET_NAME, BASE_URI);
        assertNotNull(document);
        assertTrue(document.titleText().length() > 0);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(CHARSET_NAME));
        Document document = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
        assertNotNull(document);
        assertTrue(document.titleText().length() == 0);
        assertEquals(htmlContent, document.html());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(CHARSET_NAME));
        Document document = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI, parser);
        assertNotNull(document);
        assertTrue(document.titleText().length() == 0);
        assertEquals(htmlContent, document.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Test data";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(CHARSET_NAME));
        OutputStream outputStream = new ByteArrayOutputStream();

        dataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(inputContent, new String(outputBytes, CHARSET_NAME));
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(CHARSET_NAME));
        String charsetName = CHARSET_NAME;
        String baseUri = BASE_URI;
        Document document = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertTrue(document.titleText().length() == 0);
        assertEquals(htmlContent, document.html());

        // Test parsing with a different charset
        byteData.rewind();
        charsetName = "ISO-8859-1";
        document = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertTrue(document.titleText().length() == 0);
        assertEquals(htmlContent, document.html());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals(CHARSET_NAME, charset);

        contentType = "text/html";
        charset = dataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = dataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }
}