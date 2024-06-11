package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = " <html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<title>Document Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Document Title", doc.title());
        Assert.assertEquals("Hello World!", doc.body().text());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadStreamWithDefaultCharset() throws IOException {
        String fileContent = " <html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"gb2312\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<title>Document Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Document Title", doc.title());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testLoadStreamWithSpecifiedCharset() throws IOException {
        String fileContent = " <html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"gb2312\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<title>Document Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "gb2312", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Document Title", doc.title());
        Assert.assertEquals("Hello World!", doc.body().text());

        // Test with incorrect charset
        Document docIncorrect = DataUtil.load(inputStream, "UTF-16", null);
        Assert.assertNull(docIncorrect);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String fileContent = " <html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<title>Document Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        byte[] fileBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileBytes);

        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Document Title", doc.title());
        Assert.assertEquals("Hello World!", doc.body().text());

        // Test with incorrect charset
        byte[] incorrectFileBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer incorrectByteBuffer = ByteBuffer.wrap(incorrectFileBytes);
        incorrectByteBuffer.put((byte) 0xef); // Add BOM

        Document docIncorrect = DataUtil.parseByteData(incorrectByteBuffer, null, null, Parser.htmlParser());
        Assert.assertNull(docIncorrect);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String fileContent = " <html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"gb2312\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<title>Document Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        byte[] fileBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileBytes);

        Document doc = DataUtil.parseByteData(byteBuffer, "gb2312", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Document Title", doc.title());
        Assert.assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("gb2312", detectedCharset);

        String contentTypeNoCharset = "text/html";
        detectedCharset = DataUtil.getCharsetFromContentType(contentTypeNoCharset);
        Assert.assertNull(detectedCharset);
    }
}