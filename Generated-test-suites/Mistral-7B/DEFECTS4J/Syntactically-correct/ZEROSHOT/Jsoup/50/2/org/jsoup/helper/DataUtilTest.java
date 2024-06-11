package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

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

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://localhost/";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileInvalidFile() throws IOException {
        File file = new File("non_existent_file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://localhost/";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost/";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("invalid_data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost/";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStreamDifferentCharset() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("ISO-8859-1")));
        String charsetName = "ISO-8859-1";
        String baseUri = "http://localhost/";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test_input.html");
        File outputFile = new File("test_output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

        File testFile = new File("test_output.html");
        String charsetName = "UTF-8";
        String baseUri = "http://localhost/";
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost/";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testData = "<html><head><meta charset=\"ISO-8859-1\"><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = null;
        String baseUri = "http://localhost/";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "test_data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testData.getBytes().length, byteData.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        for (int i = 0; i < boundary.length(); i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_');
        }
    }
}