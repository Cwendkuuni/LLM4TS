package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class DataUtilTest {

    private DataUtil dataUtil;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        File inputFile = new File("input.html");
        Document document = dataUtil.load(inputFile, charsetName, "");
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithoutCharset() throws IOException {
        File inputFile = new File("input.html");
        Document document = dataUtil.load(inputFile, null, "");
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset()); // Default charset is UTF-8
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = "gb2312";
        File inputFile = new File("input.html");
        dataUtil.load(inputFile, charsetName, "");
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        File inputFile = new File("input.html");
        InputStream inputStream = new FileInputStream(inputFile);
        Document document = dataUtil.load(inputStream, charsetName, "");
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        File inputFile = new File("input.html");
        InputStream inputStream = new FileInputStream(inputFile);
        Document document = dataUtil.load(inputStream, null, "");
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset()); // Default charset is UTF-8
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "gb2312";
        File inputFile = new File("input.html");
        InputStream inputStream = new FileInputStream(inputFile);
        dataUtil.load(inputStream, charsetName, "");
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamAndFileWithSameContent() throws IOException {
        String charsetName = "UTF-8";
        File inputFile = new File("input.html");
        InputStream inputStream = new FileInputStream(inputFile);
        Document documentFromFile = dataUtil.load(inputFile, charsetName, "");
        Document documentFromInputStream = dataUtil.load(inputStream, charsetName, "");
        Assert.assertEquals(documentFromFile, documentFromInputStream);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "https://example.com/";
        InputStream inputStream = new FileInputStream(new File("input.html"));
        Document document = dataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String baseUri = "https://example.com/";
        InputStream inputStream = new FileInputStream(new File("input.html"));
        Document document = dataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset()); // Default charset is UTF-8
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "gb2312";
        String baseUri = "https://example.com/";
        InputStream inputStream = new FileInputStream(new File("input.html"));
        dataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        dataUtil.crossStreams(inputStream, outputStream);
        Assert.assertTrue(outputFile.length() > 0);
        inputStream.close();
        outputStream.close();
    }
}