package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File inputFile = new File("test.html");
        String inputCharset = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputFile, inputCharset, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("test.html");
        String inputCharset = "InvalidCharset";
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(inputFile, inputCharset, baseUri);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String inputData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputData = "<html><head><meta charset=\"InvalidCharset\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(inputStream, "InvalidCharset", baseUri);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();

        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri, parser);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        String readData = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        Assert.assertEquals(inputData, readData);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String inputData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8)));

        Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithProvidedCharset() throws IOException {
        String inputData = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8)));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String inputData = "<html><head><meta charset=\"InvalidCharset\"></head><body>Test</body></html>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8)));

        try {
            Document doc = DataUtil.parseByteData(byteData, "InvalidCharset", null, DataUtil.Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}