package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFromFileWithValidCharset() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFromFileWithInvalidCharset() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "GBK";
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(inputFile, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("charset GBK not supported", e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"GBK\"/></head><body></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("charset GBK not supported", e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithValidCharsetDetected() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, null, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"GBK\"/></head><body></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        try {
            Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("charset GBK not supported", e.getMessage());
        }
    }

    @Test
    public void testParseInputStreamWithValidCharsetDetected() throws IOException {
        String htmlContent = "<html lang=\"en\"><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBufferWithValidData() throws IOException {
        String inputString = "This is a test string!";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(inputString, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBufferWithInvalidData() throws IOException {
        byte[] invalidData = new byte[1];
        InputStream inputStream = new ByteArrayInputStream(invalidData);

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("Error reading data", e.getMessage());
        }
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(mimeBoundary.length(), 32);
        for (int i = 0; i < mimeBoundary.length(); i++) {
            Assert.assertTrue(Character.isAlphabetic(mimeBoundary.charAt(i)) || Character.isDigit(mimeBoundary.charAt(i)));
        }
    }
}