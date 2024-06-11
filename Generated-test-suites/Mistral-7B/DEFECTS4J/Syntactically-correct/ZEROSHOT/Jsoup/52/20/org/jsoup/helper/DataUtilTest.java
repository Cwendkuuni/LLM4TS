package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File file = new File("nonexistentfile.html"); // Replace with a non-existent file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(inputData, outputStream.toString());
        outputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String charsetName = "UTF-8";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-16", detectedCharset);

        byteData.clear();
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-32", detectedCharset);

        byteData.clear();
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xBF);
        detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-8", detectedCharset);

        byteData.clear();
        detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertNull(detectedCharset);
    }
}