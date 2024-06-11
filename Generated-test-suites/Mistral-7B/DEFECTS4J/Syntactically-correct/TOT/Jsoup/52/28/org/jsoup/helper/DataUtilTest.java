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
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-16", null);
        Assert.assertNotNull(doc);

        // Validate that the document text is decoded correctly with the specified charset
        String content = doc.text();
        Assert.assertEquals("Test content in UTF-16", content, readFileAsUtf16String(file));
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test content</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body>Test content</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        Assert.assertNotNull(doc);

        // Validate that the document text is decoded correctly with the specified charset
        String content = doc.text();
        Assert.assertEquals("Test content in UTF-16", content, readFileAsUtf16String(new File("testFile.html")));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test content</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("testOutput.html");

        DataUtil.load(inputFile, null, null);
        DataUtil.crossStreams(new FileInputStream(inputFile), new FileOutputStream(outputFile));

        // Validate that the output file is the same as the input file
        Document inputDoc = DataUtil.load(inputFile, null, null);
        Document outputDoc = DataUtil.load(outputFile, null, null);
        Assert.assertEquals(inputDoc.html(), outputDoc.html());

        outputFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "InvalidCharset", null);
        Assert.fail("Expected IOException");
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test content</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body>Test content</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-16", null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);

        // Validate that the document text is decoded correctly with the specified charset
        String content = doc.text();
        Assert.assertEquals("Test content in UTF-16", content, readFileAsUtf16String(new File("testFile.html")));
    }

    private String readFileAsUtf16String(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(bytes);
        inputStream.close();
        return new String(bytes, Charset.forName("UTF-16"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-16";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-16", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-16";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(charset, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertFalse(boundary.isEmpty());
    }
}