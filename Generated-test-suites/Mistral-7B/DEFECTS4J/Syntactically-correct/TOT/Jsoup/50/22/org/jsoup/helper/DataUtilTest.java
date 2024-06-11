package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, "UTF-16LE", "");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, "InvalidCharset", "");
        fail("Expected IOException but got: " + doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_16LE));
        Document doc = DataUtil.load(in, "UTF-16LE", "");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, "InvalidCharset", "");
        fail("Expected IOException but got: " + doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<xml><root><child></child></root></xml>";
        InputStream in = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, "", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("test_output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        assertFalse(inputStream.available() > 0);
        assertFalse(outputStream.available() > 0);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testData = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">"
                + "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String testData = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=InvalidCharset\">"
                + "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        try {
            Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
            fail("Expected IllegalArgumentException but got: " + doc);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals(testData.length(), byteBuffer.limit());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidMaxSize() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, -1);
        fail("Expected IOException but got: " + byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }
}