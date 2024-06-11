package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithCharsetAndBaseUri() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(testFile, charsetName, baseUri);

        assertNotNull(doc);
        assertSame(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithDefaultCharsetAndBaseUri() throws IOException {
        File testFile = new File("test.html");
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(testFile, null, baseUri);

        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithCharsetAndBaseUri() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());

        assertNotNull(doc);
        assertSame(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharsetAndBaseUri() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, null, baseUri, Parser.htmlParser());

        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());

        assertNotNull(doc);
        assertSame(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("output.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(new FileInputStream(inputFile), outputStream);

        ByteArrayInputStream outputInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(outputInputStream, charsetName, baseUri, Parser.htmlParser());

        assertNotNull(doc);
        assertSame(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithCharsetAndBaseUri() {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        assertNotNull(doc);
        assertSame(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharsetAndBaseUri() {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";

        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File testFile = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(testFile);

        assertEquals(testFile.length(), byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File testFile = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(testFile, 10);

        assertEquals(10, byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithInputStream() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertEquals(testData.length(), byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSizeAndInputStream() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);

        assertEquals(10, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testContentType = "text/html; charset=UTF-8";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(testContentType));
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String testContentType = "text/html";

        assertNull(DataUtil.getCharsetFromContentType(testContentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8".toUpperCase(Locale.ENGLISH)));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(null));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteDataUtf32 = ByteBuffer.wrap(new byte[] { 0x00, 0x00, 0xFE, 0xFF });
        ByteBuffer byteDataUtf16LE = ByteBuffer.wrap(new byte[] { 0xFF, 0xFE, 0x00, 0x00 });
        ByteBuffer byteDataUtf16BE = ByteBuffer.wrap(new byte[] { 0xFE, 0xFF, 0x00, 0x00 });
        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });

        String charsetUtf32 = DataUtil.detectCharsetFromBom(byteDataUtf32, null);
        String charsetUtf16LE = DataUtil.detectCharsetFromBom(byteDataUtf16LE, null);
        String charsetUtf16BE = DataUtil.detectCharsetFromBom(byteDataUtf16BE, null);
        String charsetUtf8 = DataUtil.detectCharsetFromBom(byteDataUtf8, null);

        assertEquals("UTF-32", charsetUtf32);
        assertEquals("UTF-16", charsetUtf16LE);
        assertEquals("UTF-16", charsetUtf16BE);
        assertEquals("UTF-8", charsetUtf8);

        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[] { 0x00, 0x00 }), null));
    }

    @Test(expected = IllegalStateException.class)
    public void testDetectCharsetFromBomWithNullByteData() {
        DataUtil.detectCharsetFromBom(null, null);
    }
}