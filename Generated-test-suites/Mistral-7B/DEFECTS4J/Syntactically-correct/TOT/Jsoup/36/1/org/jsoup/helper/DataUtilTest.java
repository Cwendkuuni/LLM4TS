package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "This is a sample document.";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        File testFile = new File("test.html");
        Files.write(testFile.getAbsoluteFile().toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(fileContent, doc.body().text());

        testFile.delete(); // clean up
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        String baseUri = "http://example.com/";

        String inputStreamContent = "This is a sample document.";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(contentType.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write('\r');
        byteArrayOutputStream.write('\n');
        byteArrayOutputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, null, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharset() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        String baseUri = "http://example.com/";
        String charsetName = "UTF-8";

        String inputStreamContent = "This is a sample document.";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(contentType.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write('\r');
        byteArrayOutputStream.write('\n');
        byteArrayOutputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String contentType = "text/html; charset=GB2312";
        String baseUri = "http://example.com/";
        String charsetName = "UTF-8";

        String inputStreamContent = "This is a sample document.";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(contentType.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write('\r');
        byteArrayOutputStream.write('\n');
        byteArrayOutputStream.write(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException but got Document.");
        } catch (IOException ex) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String testData = "This is a sample document.";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(testData, doc.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String testData = "This is a sample document.";
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(testData, doc.body().text());
    }

    @Test
    public void testParseByteDataWithUnsupportedCharset() throws IOException {
        String testData = "This is a sample document.";
        String charsetName = "GB2312";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
            Assert.fail("Expected IOException but got Document.");
        } catch (IOException ex) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "This is a sample document.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
        Assert.assertEquals(testData.getBytes(StandardCharsets.UTF_8).length, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
}