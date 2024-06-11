package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html"); // replace with valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com"; // replace with valid base URI
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileThrowsIOException() throws IOException {
        File testFile = new File("invalid_file_path"); // replace with invalid file path
        String charsetName = null;
        String baseUri = "http://example.com"; // replace with valid base URI
        DataUtil.load(testFile, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "Test data for InputStream test.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com"; // replace with valid base URI
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamThrowsIOException() throws IOException {
        String testData = "Test data for InputStream test.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com"; // replace with valid base URI
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "Test data for InputStream test with custom parser.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com"; // replace with valid base URI
        Parser parser = Parser.xmlParser(); // replace with valid parser
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithParserThrowsIOException() throws IOException {
        String testData = "Test data for InputStream test with custom parser.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com"; // replace with valid base URI
        Parser parser = Parser.xmlParser(); // replace with valid parser
        DataUtil.load(inputStream, charsetName, baseUri, parser);
    }

    @Test
    public void testParseByteData() {
        String testData = "Test data for ByteData parse test.";
        byte[] testBytes = testData.getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(testBytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com"; // replace with valid base URI
        Parser parser = Parser.htmlParser(); // replace with valid parser
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataThrowsIOException() {
        byte[] testBytes = new byte[0];
        ByteBuffer byteData = ByteBuffer.wrap(testBytes);
        String charsetName = null;
        String baseUri = "http://example.com"; // replace with valid base URI
        Parser parser = Parser.htmlParser(); // replace with valid parser
        DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data for ByteBuffer read test.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferThrowsIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}