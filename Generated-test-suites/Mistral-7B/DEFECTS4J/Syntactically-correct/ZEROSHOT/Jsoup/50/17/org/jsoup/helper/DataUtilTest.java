package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

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

    /**
     * Test load method with file input, default charset and baseUri.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFileDefault() throws IOException {
        String testFile = "test_file.html";
        File file = new File(testFile);
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);
    }

    /**
     * Test load method with file input, specified charset and baseUri.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFileSpecifiedCharset() throws IOException {
        String testFile = "test_file_utf16.html";
        File file = new File(testFile);
        Document doc = DataUtil.load(file, "UTF-16", null);
        assertNotNull(doc);
    }

    /**
     * Test load method with file input, specified charset and baseUri, and parser.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFileSpecifiedCharsetParser() throws IOException {
        String testFile = "test_file_utf16.html";
        File file = new File(testFile);
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(file, "UTF-16", baseUri, Parser.xmlParser());
        assertNotNull(doc);
    }

    /**
     * Test load method with input stream, default charset and baseUri.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamDefault() throws IOException {
        String testContent = "Test input stream.";
        InputStream in = new ByteArrayInputStream(testContent.getBytes());
        Document doc = DataUtil.load(in, null, null);
        assertNotNull(doc);
    }

    /**
     * Test load method with input stream, specified charset and baseUri.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamSpecifiedCharset() throws IOException {
        String testContent = "Test input stream UTF-16.";
        InputStream in = new ByteArrayInputStream(testContent.getBytes("UTF-16"));
        Document doc = DataUtil.load(in, "UTF-16", null);
        assertNotNull(doc);
    }

    /**
     * Test load method with input stream, specified charset, baseUri, and parser.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamSpecifiedCharsetParser() throws IOException {
        String testContent = "Test input stream UTF-16.";
        InputStream in = new ByteArrayInputStream(testContent.getBytes("UTF-16"));
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(in, "UTF-16", baseUri, Parser.xmlParser());
        assertNotNull(doc);
    }

    /**
     * Test crossStreams method.
     * @throws IOException on IO error
     */
    @Test
    public void testCrossStreams() throws IOException {
        String testContent = "Test cross streams.";
        InputStream in = new ByteArrayInputStream(testContent.getBytes());
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        String result = new String(out.toByteArray());
        assertEquals(testContent, result);
    }

    /**
     * Test parseByteData method with empty byte buffer.
     */
    @Test
    public void testParseByteDataEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNull(doc);
    }

    /**
     * Test parseByteData method with valid byte buffer.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataValidByteBuffer() throws IOException {
        String testContent = "<html><body>Test parse byte data.</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testContent.getBytes());
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    /**
     * Test parseByteData method with byte buffer containing BOM.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataWithBOM() throws IOException {
        String testContent = "Test parse byte data with BOM.";
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] { UNICODE_BOM }
                , new byte[] { (byte) 0x41, (byte) 0x42, (byte) 0x43 });
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    /**
     * Test getCharsetFromContentType method.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    /**
     * Test getCharsetFromContentType method with unsupported charset.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetCharsetFromContentTypeUnsupportedCharset() {
        String contentType = "text/html; charset=X-1234";
        String charset = DataUtil.getCharsetFromContentType(contentType);
    }

    /**
     * Test mimeBoundary method.
     */
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == boundaryLength);
    }
}