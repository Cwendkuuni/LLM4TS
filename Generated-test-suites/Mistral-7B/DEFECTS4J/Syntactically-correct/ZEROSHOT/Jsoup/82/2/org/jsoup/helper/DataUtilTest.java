package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithCharset() throws IOException {
        String filePath = "testData/utf8.html";
        Document doc = DataUtil.load(new File(filePath), "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithoutCharset() throws IOException {
        String filePath = "testData/utf8.html";
        Document doc = DataUtil.load(new File(filePath), "", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "testData/utf8.html";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(new File(filePath), "InvalidCharset", ""));
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, "InvalidCharset", ""));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "", "", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceData = "SourceData";
        String targetData = "TargetData";
        ByteArrayInputStream inputStreamSource = new ByteArrayInputStream(sourceData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStreamTarget = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStreamSource, outputStreamTarget);

        assertEquals(sourceData, outputStreamTarget.toString());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String charsetName = "UTF-8";
        String inputData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithoutCharset() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "", "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, "InvalidCharset", "", Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamWithParserXml() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "", "", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "TestInputData";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        byte[] expectedByteArray = inputData.getBytes(StandardCharsets.UTF_8);
        byte[] bufferBytes = byteBuffer.array();

        assertArrayEquals(expectedByteArray, bufferBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String csUtf8 = "UTF-8";
        String csUtf16 = "UTF-16";
        String csUtf32 = "UTF-32";
        String csInvalid = "InvalidCharset";

        assertEquals(csUtf8, DataUtil.validateCharset(csUtf8));
        assertEquals(csUtf16, DataUtil.validateCharset(csUtf16));
        assertEquals(csUtf8, DataUtil.validateCharset("utf-8"));
        assertEquals(csUtf16, DataUtil.validateCharset("utf-16"));
        assertEquals(csUtf8, DataUtil.validateCharset("UTF-8"));
        assertEquals(csUtf16, DataUtil.validateCharset("UTF-16"));
        assertEquals(csUtf8, DataUtil.validateCharset("UTF-8".toUpperCase(Locale.ENGLISH)));
        assertEquals(csUtf16, DataUtil.validateCharset("UTF-16".toUpperCase(Locale.ENGLISH)));
        assertNull(DataUtil.validateCharset(csInvalid));
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "2145abcdefghijk-lmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";

        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(expectedMimeBoundary, mimeBoundary);
    }

    @Test
    public void testDetectCharsetFromBomUtf8() throws IOException {
        String inputData = "\uFEFF<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8)), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUtf16() throws IOException {
        String inputData = "\uFFFE<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_16)), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertEquals("UTF-16", bomCharset.charset);
        assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUtf32() throws IOException {
        String inputData = "\uFFFE\uFFFE<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_32)), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertEquals("UTF-32", bomCharset.charset);
        assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomEmpty() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNull(bomCharset);
    }

    @Test
    public void testDetectCharsetFromBomInvalidBom() throws IOException {
        String inputData = "TestData";
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8)), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNull(bomCharset);
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String xmlData = "<?xml version='1.0' encoding='UTF-8'?><html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "", "");

        XmlDeclaration xmlDeclaration = doc.xmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
    }

    @Test
    public void testParseInputStreamWithComment() throws IOException {
        String commentData = "--><html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(commentData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "", "");

        Element commentElement = doc.body().child(0);
        assertNotNull(commentElement);
        assertTrue(commentElement.tagName().equalsIgnoreCase("html"));
        assertTrue(commentElement.nodeName().equalsIgnoreCase("#comment"));
        assertEquals("-->", commentElement.text());
    }
}