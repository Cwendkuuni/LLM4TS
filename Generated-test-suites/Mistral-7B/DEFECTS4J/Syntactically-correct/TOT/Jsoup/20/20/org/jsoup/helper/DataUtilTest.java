import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document expectedDocument = DataUtil.parse(inputFile); // parse manually to have expected Document

        Document actualDocument = DataUtil.load(inputFile, charsetName, baseUri);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document expectedDocument = DataUtil.parse(htmlContent); // parse manually to have expected Document
        String defaultCharset = DataUtil.defaultCharset;

        Document actualDocument = DataUtil.load(inputStream, null);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes("UTF-16LE")); // write with unexpected charset
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-16LE";

        Document expectedDocument = DataUtil.parse(htmlContent); // parse manually to have expected Document

        Document actualDocument = DataUtil.load(inputStream, charsetName);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "invalidCharset";

        try {
            DataUtil.load(inputStream, charsetName);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));

        Document expectedDocument = DataUtil.parse(htmlContent); // parse manually to have expected Document

        Document actualDocument = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes("UTF-16LE")); // write with unexpected charset
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
        String charsetName = "UTF-16LE";

        Document expectedDocument = DataUtil.parse(htmlContent); // parse manually to have expected Document

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.html(), actualDocument.html());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
        String charsetName = "invalidCharset";

        try {
            DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String expectedCharset = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals(expectedCharset, actualCharset);
    }
}