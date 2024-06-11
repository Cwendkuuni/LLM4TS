package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        File inputFile = new File("inputFileWithValidCharset.html");
        Document doc = DataUtil.load(inputFile, charsetName, "");
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        File inputFile = new File("inputFileWithInvalidCharset.html");
        try {
            Document doc = DataUtil.load(inputFile, charsetName, "");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("java.io.IOException: Unable to determine charset of document", e.getMessage());
        }
    }

    @Test
    public void testLoadFileWithNoCharset() throws IOException {
        File inputFile = new File("inputFileWithNoCharset.html");
        Document doc = DataUtil.load(inputFile, "", "");
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String htmlContent = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.load(inputStream, charsetName, "");
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String htmlContent = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        try {
            Document doc = DataUtil.load(inputStream, charsetName, "");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("java.io.IOException: Unable to determine charset of document", e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String htmlContent = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, "", "");
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testLoadInputStreamWithBOM() throws IOException {
        String charsetName = "UTF-8";
        String htmlContentWithBom = "\uFEFF" + "<html></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContentWithBom.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.load(inputStream, charsetName, "");
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String htmlContent = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String htmlContent = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        try {
            Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("java.io.IOException: Unable to determine charset of document", e.getMessage());
        }
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String htmlContent = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "", "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String charsetName = "UTF-8";
        String htmlContentWithBom = "\uFEFF" + "<html></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContentWithBom.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source data";
        String target = "";
        InputStream inputStream = new ByteArrayInputStream(source.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String result = outputStream.toString();
        Assert.assertEquals(source, result);
    }
}