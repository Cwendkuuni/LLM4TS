package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("path/to/valid/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        // typical usage: assert document is valid and contains expected elements
        Assert.assertNotNull(document);
        Assert.assertEquals("test.com", document.baseUri());
        Element titleElement = document.title();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("path/to/invalid/file.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            Assert.fail();
        } catch (IOException e) {
            // expected: invalid charset
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<html><head></head><body><title>Test Title</title></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        // typical usage: assert document is valid and contains expected elements
        Assert.assertNotNull(document);
        Assert.assertEquals("test.com", document.baseUri());
        Element titleElement = document.title();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testLoadInputStreamWithMissingCharset() throws IOException {
        String htmlContent = "<html><head></head><body><title>Test Title</title></body></html>";
        String charsetName = null;
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        // typical usage: assert document is valid and contains expected elements
        Assert.assertNotNull(document);
        Assert.assertEquals("test.com", document.baseUri());
        Element titleElement = document.title();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body><title>Test Title</title></body></html>";
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail();
        } catch (IOException e) {
            // expected: invalid charset
        }
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<html><head></head><body><title>Test Title</title></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        // typical usage: assert document is valid and contains expected elements
        Assert.assertNotNull(document);
        Assert.assertEquals("test.com", document.baseUri());
        Element titleElement = document.title();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testParseInputStreamWithMissingCharset() throws IOException {
        String htmlContent = "<html><head></head><body><title>Test Title</title></body></html>";
        String charsetName = null;
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        // typical usage: assert document is valid and contains expected elements
        Assert.assertNotNull(document);
        Assert.assertEquals("test.com", document.baseUri());
        Element titleElement = document.title();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body><title>Test Title</title></body></html>";
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // expected: invalid charset
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Input Content";
        String outputContent = "Output Content";
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();
        String outputString = new String(outputBytes, StandardCharsets.UTF_8);

        // typical usage: assert output string is equal to input string
        Assert.assertEquals(inputContent, outputString);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomUTF8 = "\uFEFF";
        String bomUTF16LE = "\uFFFE\u0000";
        String bomUTF16BE = "\u0000\uFFFE";
        String bomUTF32LE = "\u0000\u0000\u0000\uFFFE";
        String bomUTF32BE = "\uFFFE\u0000\u0000\u0000";

        ByteBuffer byteDataUTF8 = ByteBuffer.wrap(bomUTF8.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteDataUTF16LE = ByteBuffer.wrap(bomUTF16LE.getBytes(StandardCharsets.UTF_16LE));
        ByteBuffer byteDataUTF16BE = ByteBuffer.wrap(bomUTF16BE.getBytes(StandardCharsets.UTF_16BE));
        ByteBuffer byteDataUTF32LE = ByteBuffer.wrap(bomUTF32LE.getBytes(StandardCharsets.UTF_32LE));
        ByteBuffer byteDataUTF32BE = ByteBuffer.wrap(bomUTF32BE.getBytes(StandardCharsets.UTF_32BE));

        BomCharset utf8Bom = DataUtil.detectCharsetFromBom(byteDataUTF8);
        BomCharset utf16LEBom = DataUtil.detectCharsetFromBom(byteDataUTF16LE);
        BomCharset utf16BEBom = DataUtil.detectCharsetFromBom(byteDataUTF16BE);
        BomCharset utf32LEBom = DataUtil.detectCharsetFromBom(byteDataUTF32LE);
        BomCharset utf32BEBom = DataUtil.detectCharsetFromBom(byteDataUTF32BE);

        // typical usage: assert bomCharset objects have the correct charset and offset
        Assert.assertEquals("UTF-8", utf8Bom.charset);
        Assert.assertFalse(utf8Bom.offset);
        Assert.assertEquals("UTF-16", utf16LEBom.charset);
        Assert.assertFalse(utf16LEBom.offset);
        Assert.assertEquals("UTF-16", utf16BEBom.charset);
        Assert.assertTrue(utf16BEBom.offset);
        Assert.assertEquals("UTF-32", utf32LEBom.charset);
        Assert.assertFalse(utf32LEBom.offset);
        Assert.assertEquals("UTF-32", utf32BEBom.charset);
        Assert.assertTrue(utf32BEBom.offset);
    }
}