package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // Replace with an actual test file
        Document document = DataUtil.load(file, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // Replace with an actual test file
        Document document = DataUtil.load(file, "UTF-16");
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInvalidFile() throws IOException {
        File file = new File("nonexistent.file");
        Document document = DataUtil.load(file, null);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "Some test data.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.load(inputStream, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "Some test data.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-16"));
        Document document = DataUtil.load(inputStream, "UTF-16");
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "Some test data.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.load(inputStream, "InvalidCharset");
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String testData = "Some test data.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "Some test data.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-16"));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testData = "Some test data.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, "InvalidCharset", null, Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Some test data.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputData = outputStream.toByteArray();
        Assert.assertArrayEquals(testData.getBytes(), outputData);
    }
}