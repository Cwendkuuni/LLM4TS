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
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charset = "UTF-8";
        File file = new File("test.html"); // assume file exists with valid html content
        Document doc = DataUtil.load(file, charset, "");
        Assert.assertNotNull(doc);

        // ensure the loaded document contains the expected content
        String expectedTitle = "Test Title";
        Element title = doc.select("title").first();
        Assert.assertNotNull(title);
        Assert.assertEquals(expectedTitle, title.text());
    }

    @Test
    public void testLoadInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        File file = new File("test.html"); // assume file exists with valid html content
        try {
            Document doc = DataUtil.load(file, charset, "");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        String charset = "UTF-8";
        File file = new File("empty.html"); // assume file exists with no content
        Document doc = DataUtil.load(file, charset, "");
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadStreamWithCorrectCharset() throws IOException {
        String charset = "UTF-8";
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName(charset)));
        Document doc = DataUtil.load(inputStream, charset, "");
        Assert.assertNotNull(doc);

        // ensure the loaded document contains the expected content
        String expectedTitle = "Test";
        Element title = doc.select("h1").first();
        Assert.assertNotNull(title);
        Assert.assertEquals(expectedTitle, title.text());
    }

    @Test
    public void testLoadStreamWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName(charset)));
        try {
            Document doc = DataUtil.load(inputStream, charset, "");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Test Input";
        String outputContent = "Test Output";

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(Charset.forName("UTF-8")));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = outputStream.toByteArray();
        String outputString = new String(outputBytes, Charset.forName("UTF-8"));
        Assert.assertEquals(outputContent, outputString);
    }

    @Test
    public void testParseByteDataWithCorrectCharset() throws IOException {
        String charset = "UTF-8";
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(Charset.forName(charset)));
        Document doc = DataUtil.parseByteData(byteData, charset, "", Parser.htmlParser());
        Assert.assertNotNull(doc);

        // ensure the loaded document contains the expected content
        String expectedTitle = "Test";
        Element title = doc.select("h1").first();
        Assert.assertNotNull(title);
        Assert.assertEquals(expectedTitle, title.text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String charset = "InvalidCharset";
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(Charset.forName("UTF-8")));
        try {
            Document doc = DataUtil.parseByteData(byteData, charset, "", Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
}