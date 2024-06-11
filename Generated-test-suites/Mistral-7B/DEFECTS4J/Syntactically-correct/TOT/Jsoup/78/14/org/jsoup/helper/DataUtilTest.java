package org.jsoup.helper.test;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Category(IntegrationTests.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, null, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = HelperException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        Document document = null;
        try {
            document = DataUtil.load(file, charsetName, baseUri);
        } catch (IOException e) {
            // expected
        }
        Assert.assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<body>\n<h1>Hello, world!</h1>\n</body>\n</html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, null, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<?xml version='1.0' encoding='UTF-16'?>\n<html>\n<body>\n<h1>Hello, world!</h1>\n</body>\n</html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_16);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = HelperException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<body>\n<h1>Hello, world!</h1>\n</body>\n</html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        Document document = null;
        try {
            document = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        } catch (IOException e) {
            // expected
        }
        Assert.assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidContentType() throws IOException {
        String html = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<body>\n<h1>Hello, world!</h1>\n</body>\n</html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        String baseUri = "http://example.com";
        Document document = null;
        try {
            document = DataUtil.load(inputStream, null, baseUri, DataUtil.Parser.xmlParser());
        } catch (HelperException e) {
            Assert.assertEquals("Content type not supported: text/html", e.getMessage());
        }
        Assert.assertNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<body>\n<h1>Hello, world!</h1>\n</body>\n</html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.xmlParser();
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello, world!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String output = outputStream.toString(StandardCharsets.UTF_8.displayName());
        Assert.assertEquals(input, output);
    }
}