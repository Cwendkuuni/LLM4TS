package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, null, "");
        Assert.assertNotNull(doc);
        // Add assertions based on expected HTML structure or specific tests
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File testFile = new File("test_utf16.html");
        Document doc = DataUtil.load(testFile, "UTF-16", "");
        // Add assertions based on expected HTML structure or specific tests
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File("test.html");
        DataUtil.load(testFile, "INVALID_CHARSET", "");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html lang=\"en\">...</html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null, "");
        Assert.assertNotNull(doc);
        // Add assertions based on expected HTML structure or specific tests
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html lang=\"en\">...</html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-16", "");
        // Add assertions based on expected HTML structure or specific tests
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html lang=\"en\">...</html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "INVALID_CHARSET", "");
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String xmlContent = "<xml lang=\"en\">...</xml>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(xmlContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null, "", DataUtil.parser("xmlParser"));
        Assert.assertNotNull(doc);
        // Add assertions based on expected XML structure or specific tests
    }

    @Test
    public void testCrossStreams() throws IOException {
        File sourceFile = new File("source.txt");
        File targetFile = new File("target.txt");
        InputStream inputStream = new FileInputStream(sourceFile);
        OutputStream outputStream = new FileOutputStream(targetFile);

        DataUtil.crossStreams(inputStream, outputStream);

        // Add assertions based on expected content or specific tests

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String htmlContent = "<html lang=\"en\">...</html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteBuffer, null, "", DataUtil.parser("htmlParser"));
        Assert.assertNotNull(doc);
        // Add assertions based on expected HTML structure or specific tests
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html lang=\"en\">...</html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-16", "", DataUtil.parser("htmlParser"));
        // Add assertions based on expected HTML structure or specific tests
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String htmlContent = "<html lang=\"en\">...</html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteBuffer, "INVALID_CHARSET", "", DataUtil.parser("htmlParser"));
        Assert.assertNull(doc);
    }

    @Test
    public void testParseByteDataWithBOM() throws IOException {
        String htmlContentWithBOM = "\uFEFF<html lang=\"en\">...</html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContentWithBOM.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteBuffer, null, "", DataUtil.parser("htmlParser"));
        Assert.assertNotNull(doc);
        // Add assertions based on expected HTML structure or specific tests
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(32, mimeBoundary.length());
    }
}