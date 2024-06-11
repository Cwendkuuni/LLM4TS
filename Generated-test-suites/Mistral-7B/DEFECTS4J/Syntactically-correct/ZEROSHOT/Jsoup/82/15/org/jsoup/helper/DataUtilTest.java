package org.jsoup.helper;

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
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("path/to/your/test/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamUTF8() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"ISO-8859-1\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com/";

        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, charsetName, baseUri);
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamDefaultCharset() throws IOException {
        String htmlContent = "<html><head></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document doc1 = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc1);

        String expectedCharset = "UTF-8";
        String actualCharset = doc1.outputSettings().charset().name();
        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String content = "Test content";
        String sourceContent = "Source content";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String expectedContent = sourceContent + content;
        String actualContent = outputStream.toString();
        Assert.assertEquals(expectedContent, actualContent);
    }
}