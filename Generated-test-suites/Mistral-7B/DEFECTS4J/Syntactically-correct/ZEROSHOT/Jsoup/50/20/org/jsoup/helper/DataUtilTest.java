package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = "";
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceFile = "path/to/source/file.html";
        String destinationFile = "path/to/destination/file.html";

        File source = new File(sourceFile);
        File destination = new File(destinationFile);

        InputStream sourceInputStream = new FileInputStream(source);
        OutputStream destinationOutputStream = new FileOutputStream(destination);

        DataUtil.crossStreams(sourceInputStream, destinationOutputStream);

        Assert.assertTrue(destination.length() > 0);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(htmlContent.getBytes().length, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(Files.size(file.toPath()), byteBuffer.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        Assert.assertNotNull(boundary);
        Assert.assertTrue(boundary.length() >= 32);
    }
}