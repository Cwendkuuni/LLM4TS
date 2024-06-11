import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-16\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        try {
            DataUtil.load(inputStream, "UTF-16", null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-16\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        try {
            DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test string".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String output = outputStream.toString();
        Assert.assertEquals("test string", output);
    }
}