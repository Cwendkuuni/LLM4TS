package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, fileContent, "UTF-8");

        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
        assertNotNull(document.body());
        assertEquals(fileContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String fileContent = "This is a test file.";
        byte[] fileContentBytes = fileContent.getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContentBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertNotNull(document.body());
        assertEquals(fileContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamDocumentWithDifferentParser() throws IOException {
        String fileContent = "This is a test file.";
        byte[] fileContentBytes = fileContent.getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContentBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertNotNull(document.body());
        assertEquals(fileContent, document.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "This is a test.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputData = outputStream.toByteArray();
        assertEquals(inputData, new String(outputData, "UTF-8"));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String inputData = "<html><head><meta charset=\"UTF-8\"></head><body>This is a test file.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes());

        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertNotNull(document.body());
        assertEquals("This is a test file.", document.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String inputData = "<html><head><meta charset=\"UTF-8\"></head><body>This is a test file.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes());

        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertNotNull(document.body());
        assertEquals("This is a test file.", document.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharsetAndBaseUri() throws IOException {
        String inputData = "<html><head><meta charset=\"UTF-8\"></head><body>This is a test file.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes());

        Document document = DataUtil.parseByteData(byteData, "UTF-8", "/test", Parser.htmlParser());
        assertNotNull(document);
        assertNotNull(document.body());
        assertEquals("This is a test file.", document.body().text());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(boundary.length(), 32);
    }
}