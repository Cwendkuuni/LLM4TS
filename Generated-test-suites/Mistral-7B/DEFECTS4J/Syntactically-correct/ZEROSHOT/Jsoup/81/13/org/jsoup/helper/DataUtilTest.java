package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import org.junit.Test;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        Document doc = DataUtil.load(new File(filePath), null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String filePath = "src/test/resources/testFileGBK.html";
        Document doc = DataUtil.load(new File(filePath), "GBK");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        Document doc = DataUtil.load(new File(filePath), "invalidCharset");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String filePath = "src/test/resources/testFileGBK.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, "GBK");
        assertNotNull(doc);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, "invalidCharset");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String filePath = "src/test/resources/testFile.xml";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        assertEquals(filePath, outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String inputStreamData = "test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("test data", doc.body().text());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamData = "测试数据";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.GBK));
        Document doc = DataUtil.parseInputStream(inputStream, "GBK", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("测试数据", doc.body().text());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputStreamData = "test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.htmlParser());
    }

    @Test
    public void testParseInputStreamWithParserAndBaseUri() throws IOException {
        String inputStreamData = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><div>test data</div></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        Element titleElement = doc.selectFirst("title");
        assertEquals("Test", titleElement.text());
        Element bodyElement = doc.body();
        assertEquals("test data", bodyElement.text());
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String filePath = "src/test/resources/testFile.html";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        inputStream.close();
        // TODO: add assertions here
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("gb2312", DataUtil.validateCharset("gb2312"));
        assertEquals("ISO-8859-1", DataUtil.validateCharset("ISO-8859-1"));
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
        assertEquals("UTF-32", DataUtil.validateCharset("UTF-32"));

        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(" invalidCharset "));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() > 0);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer utf8Bom = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
        ByteBuffer utf16Bom = ByteBuffer.wrap(new byte[] { (byte) 0xFE, (byte) 0xFF });
        ByteBuffer utf32Bom = ByteBuffer.wrap(new byte[] { 0x00, 0x00, (byte) 0xFE, (byte) 0xFF });

        BomCharset utf8Charset = DataUtil.detectCharsetFromBom(utf8Bom);
        assertEquals("UTF-8", utf8Charset.charset);
        assertTrue(utf8Charset.offset);

        BomCharset utf16Charset = DataUtil.detectCharsetFromBom(utf16Bom);
        assertEquals("UTF-16", utf16Charset.charset);
        assertFalse(utf16Charset.offset);

        BomCharset utf32Charset = DataUtil.detectCharsetFromBom(utf32Bom);
        assertEquals("UTF-32", utf32Charset.charset);
        assertFalse(utf32Charset.offset);

        ByteBuffer noBom = ByteBuffer.wrap(new byte[] { 0x00, 0x01, 0x02 });
        assertNull(DataUtil.detectCharsetFromBom(noBom));
    }
}