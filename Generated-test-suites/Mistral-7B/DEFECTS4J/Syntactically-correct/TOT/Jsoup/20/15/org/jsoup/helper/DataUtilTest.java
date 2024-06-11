import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("test_file.html");
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputFile, null, baseUri);
        Assert.assertNotNull(doc);
        // Add assertions to check the loaded document.
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("test_file_with_charset.html");
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
        // Add assertions to check the loaded document.
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputData = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, null, baseUri);
        Assert.assertNotNull(doc);
        // Add assertions to check the loaded document.
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-16";
        String inputData = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes(Charset.forName(charsetName).name()));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        // Add assertions to check the loaded document.
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String inputData = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException when loading with an invalid charset.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String inputData = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // Add assertions to check the loaded document.
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-16";
        String inputData = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes(Charset.forName(charsetName).name()));
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // Add assertions to check the loaded document.
    }
}