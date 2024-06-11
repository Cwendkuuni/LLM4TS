package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        File file = new File("test.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document doc = DataUtil.load(file, null, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());

        file.delete(); // clean up
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, null, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, "ISO-8859-1", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testLoadInputStreamWithWrongCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, null, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title()); // the document will be parsed as UTF-8, as it's the default

        doc = DataUtil.load(inputStream, "ISO-8859-1", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title()); // the document will still be parsed as UTF-8, as the charset in the meta tag is ignored when the charset is explicitly provided
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Document doc = DataUtil.parseByteData(byteData, null, "", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", "", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());

        byteData.rewind(); // reset the buffer
        doc = DataUtil.parseByteData(byteData, null, "", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title()); // the document will be parsed as UTF-8, as it's the default
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";
        String contentType3 = "text/html";

        String expectedCharset1 = "UTF-8";
        String expectedCharset2 = "ISO-8859-1";
        String expectedCharset3 = null;

        String charset = DataUtil.getCharsetFromContentType(contentType1);
        Assert.assertEquals(expectedCharset1, charset);

        charset = DataUtil.getCharsetFromContentType(contentType2);
        Assert.assertEquals(expectedCharset2, charset);

        charset = DataUtil.getCharsetFromContentType(contentType3);
        Assert.assertEquals(expectedCharset3, charset);
    }
}