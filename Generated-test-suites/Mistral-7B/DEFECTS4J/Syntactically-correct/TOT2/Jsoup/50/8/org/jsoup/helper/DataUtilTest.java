import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-16LE", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals("Test", outputStream.toString());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("Test".getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("Test".getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-16LE", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithEmptyInput() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharsetFound() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }
}