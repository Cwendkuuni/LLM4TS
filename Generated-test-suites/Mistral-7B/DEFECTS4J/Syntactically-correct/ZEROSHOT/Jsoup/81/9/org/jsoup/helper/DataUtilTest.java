package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        String filePath = "src/test/resources/test.html";
        Document doc = DataUtil.load(new File(filePath), null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamDocument() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamDocumentWithSpecifiedParser() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
        assertTrue(doc instanceof org.jsoup.nodes.XmlDocument);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source";
        String targetContent = "Target";

        InputStream sourceStream = new ByteArrayInputStream(sourceContent.getBytes(StandardCharsets.UTF_8));
        OutputStream targetStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(sourceStream, targetStream);
        byte[] targetBytes = targetStream.toByteArray();
        String targetContentFromStream = new String(targetBytes, StandardCharsets.UTF_8);

        assertEquals(sourceContent, targetContentFromStream);
    }

    @Test
    public void testLoadDocumentWithInvalidCharset() throws IOException {
        String invalidHtmlContent = "<html><head><meta charset=\"invalidCharset\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(invalidHtmlContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testLoadDocumentWithMissingCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        inputStream.close();
    }

    @Test
    public void testLoadDocumentWithValidCharset() throws IOException {
        String htmlContent = "<html xmlns:mb=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xml:lang=\"ru\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:msxsl=\"urn:schemas-microsoft-com:xslt\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" mc:Ignorable=\"d\" xml version=\"1.0\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1251\"><title>Test</title></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "windows-1251", null, Parser.htmlParser());
        assertEquals("windows-1251", doc.outputSettings().charset());
        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] utf8Bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] utf16Lebom = {(byte) 0xFF, (byte) 0xFE};
        byte[] utf16Bebom = {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF};
        byte[] utf32Bebom = {(byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};

        ByteBuffer utf8BomBuffer = ByteBuffer.wrap(utf8Bom);
        ByteBuffer utf16LebomBuffer = ByteBuffer.wrap(utf16Lebom);
        ByteBuffer utf16BebomBuffer = ByteBuffer.wrap(utf16Bebom);
        ByteBuffer utf32BebomBuffer = ByteBuffer.wrap(utf32Bebom);

        BomCharset utf8BomDetected = DataUtil.detectCharsetFromBom(utf8BomBuffer);
        assertEquals("UTF-8", utf8BomDetected.charset);
        assertTrue(utf8BomDetected.offset);

        BomCharset utf16LebomDetected = DataUtil.detectCharsetFromBom(utf16LebomBuffer);
        assertEquals("UTF-16", utf16LebomDetected.charset);
        assertFalse(utf16LebomDetected.offset);

        BomCharset utf16BebomDetected = DataUtil.detectCharsetFromBom(utf16BebomBuffer);
        assertEquals("UTF-16", utf16BebomDetected.charset);
        assertFalse(utf16BebomDetected.offset);

        BomCharset utf32BebomDetected = DataUtil.detectCharsetFromBom(utf32BebomBuffer);
        assertEquals("UTF-32", utf32BebomDetected.charset);
        assertFalse(utf32BebomDetected.offset);
    }
}