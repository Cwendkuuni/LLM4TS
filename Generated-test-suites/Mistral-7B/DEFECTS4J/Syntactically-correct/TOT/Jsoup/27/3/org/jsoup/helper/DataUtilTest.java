package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>Test</div>\n" +
                "</body>\n" +
                "</html>";

        File file = new File("test.html");
        FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(file, null, "/");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.select("div").text());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        String inputStreamContent = "<html><body><div>Test</div></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, contentType, "/");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.select("div").text());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        String contentType = "text/html; charset=gb2312";
        String inputStreamContent = "<html><body><div>测试</div></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, contentType, "/");
        Assert.assertNotNull(doc);
        Assert.assertEquals("测试", doc.select("div").text());

        // The charset was detected as gb2312, so we need to rewind the ByteBuffer and re-decode it with gb2312.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        int read;

        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        doc = DataUtil.parseByteData(byteData, "gb2312", "/", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("测试", doc.select("div").text());

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testLoadInputStreamWithGivenCharset() throws IOException {
        String charsetName = "gb2312";
        String inputStreamContent = "<html><body><div>测试</div></body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, charsetName, "/", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("测试", doc.select("div").text());

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        DataUtil.load(file, null, "/");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "<html><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        DataUtil.load(inputStream, null, "/");
        inputStream.close();
    }
}