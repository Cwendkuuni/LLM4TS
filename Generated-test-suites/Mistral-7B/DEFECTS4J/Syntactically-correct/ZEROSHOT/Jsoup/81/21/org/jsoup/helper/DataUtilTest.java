package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String content = "Your HTML content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, charsetName, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String content = "Your HTML content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, charsetName, null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String content = "Your HTML content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String content = "Your HTML content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String content = "Your HTML content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String content = "Your HTML content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceFile = "path/to/your/source.html";
        String targetFile = "path/to/your/target.html";
        File source = new File(sourceFile);
        File target = new File(targetFile);

        InputStream inputStream = new FileInputStream(source);
        OutputStream outputStream = new FileOutputStream(target);

        DataUtil.crossStreams(inputStream, outputStream);

        // Add your assertions here to check the target file

        inputStream.close();
        outputStream.close();
    }
}