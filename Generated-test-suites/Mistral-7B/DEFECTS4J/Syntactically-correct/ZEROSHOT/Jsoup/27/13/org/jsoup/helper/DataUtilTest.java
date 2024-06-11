package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DataUtilTest class that tests all methods in the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        // Initialize test file and content
        String testFilePath = "test.html";
        File testFile = new File(testFilePath);
        String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";
        // Write test content to file
        writeTestFile(testFilePath, testContent);

        // Test method call
        Document doc = DataUtil.load(testFile, "UTF-8", "");

        // Assert test content
        assertEquals("Test content", doc.text());

        // Clean up test file
        testFile.delete();
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        // Initialize test content
        String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";

        // Create test input stream
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());

        // Test method call
        Document doc = DataUtil.load(testInputStream, "UTF-8", "");

        // Assert test content
        assertEquals("Test content", doc.text());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        // Initialize test content
        String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test content</p>\n" +
                "</body>\n" +
                "</html>";

        // Create test input stream
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());

        // Test method call
        Document doc = DataUtil.load(testInputStream, null);

        // Assert test content
        assertEquals("Test content", doc.text());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithCustomParser() throws IOException {
        // Initialize test content
        String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<p>Test content</p>\n" +
                "</xhtml:div>\n" +
                "</body>\n" +
                "</html>";

        // Create test input stream
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());

        // Test method call
        Document doc = DataUtil.load(testInputStream, "UTF-8", "", Parser.xmlParser());

        // Assert test content
        Element root = doc.select("xhtml:div").first();
        assertEquals("Test content", root.text());
    }

    @Test
    public void testDetectCharsetFromContentType() {
        // Test null content type
        String contentTypeNull = null;
        String charset = DataUtil.getCharsetFromContentType(contentTypeNull);
        assertTrue(charset == null);

        // Test empty content type
        String contentTypeEmpty = "";
        charset = DataUtil.getCharsetFromContentType(contentTypeEmpty);
        assertTrue(charset == null);

        // Test content type with charset
        String contentType = "text/html; charset=UTF-8";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        // Test content type without charset
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset == null);
    }

    private void writeTestFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
    }
}