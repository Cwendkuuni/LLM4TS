package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.junit.Test;

/**
 * Test class for DataUtil.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String filePath = "src/test/resources/test.html";
        File file = new File(filePath);
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String testHtml = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testHtml.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testDetectCharset() throws IOException {
        String filePath = "src/test/resources/test_charset.html";
        File file = new File(filePath);
        Document document = DataUtil.load(file, null, null);
        String detectedCharset = DataUtil.getCharsetFromContentType(document.head().outputSettings().contentType());
        assertTrue(detectedCharset.equals("EUC-JP") || detectedCharset.isEmpty());
    }

    @Test
    public void testParseByteData() throws IOException {
        String testHtml = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testHtml.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testHtml = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertTrue(byteData.capacity() > 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "src/test/resources/test.html";
        File file = new File(filePath);
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertTrue(byteData.capacity() > 0);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
    }
}