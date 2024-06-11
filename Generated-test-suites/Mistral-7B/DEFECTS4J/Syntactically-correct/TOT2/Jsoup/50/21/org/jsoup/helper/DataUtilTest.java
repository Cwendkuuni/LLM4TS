import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {

    // Test for load method with valid arguments
    @Test
    public void testLoadValidFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    // Test for load method with null file argument
    @Test(expected = IOException.class)
    public void testLoadNullFile() {
        Document doc = DataUtil.load(null, "UTF-8", null);
    }

    // Test for load method with null charsetName argument
    @Test
    public void testLoadNullCharsetName() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc);
        // Assume default charset is UTF-8
        String charset = doc.outputSettings().charset();
        Assert.assertEquals(charset, "UTF-8");
    }

    // Test for load method with valid input stream and charsetName
    @Test
    public void testLoadValidInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    // Test for load method with null input stream
    @Test(expected = IOException.class)
    public void testLoadNullInputStream() {
        Document doc = DataUtil.load(null, "UTF-8", null);
    }

    // Test for load method with valid input stream, null charsetName, and Parser.htmlParser()
    @Test
    public void testLoadValidInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    // Test for crossStreams method with valid input and output streams
    @Test
    public void testCrossStreamsValidInputAndOutputStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] actual = outputStream.toByteArray();
        byte[] expected = "test".getBytes(StandardCharsets.UTF_8);
        Assert.assertArrayEquals(expected, actual);
    }

    // Test for parseByteData method with valid byteData, charsetName, baseUri, and Parser.htmlParser()
    @Test
    public void testParseByteDataValidByteDataWithParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    // Test for parseByteData method with valid byteData, null charsetName, and Parser.htmlParser()
    @Test
    public void testParseByteDataValidByteDataWithDefaultCharsetAndParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // Assume default charset is UTF-8
        String charset = doc.outputSettings().charset();
        Assert.assertEquals(charset, "UTF-8");
    }

    // Test for parseByteData method with valid byteData, charsetName, baseUri, and Parser.xmlParser()
    @Test
    public void testParseByteDataValidByteDataWithParserXml() throws IOException {
        String xml = "<xml><root></root></xml>";
        ByteBuffer byteData = ByteBuffer.wrap(xml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    // Test for readToByteBuffer method with valid input stream and maxSize
    @Test
    public void testReadToByteBufferValidInputStreamAndMaxSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 100);
        byte[] actual = byteData.array();
        byte[] expected = html.getBytes(StandardCharsets.UTF_8);

        Assert.assertArrayEquals(expected, actual);
    }

    // Test for readToByteBuffer method with valid input stream and maxSize = 0
    @Test
    public void testReadToByteBufferValidInputStreamAndMaxSizeZero() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] actual = byteData.array();
        byte[] expected = html.getBytes(StandardCharsets.UTF_8);

        Assert.assertArrayEquals(expected, actual);
    }

    // Test for readFileToByteBuffer method with valid file
    @Test
    public void testReadFileToByteBufferValidFile() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        byte[] actual = byteData.array();
        byte[] expected = Files.readAllBytes(file.toPath());

        Assert.assertArrayEquals(expected, actual);
    }

    // Test for getCharsetFromContentType method with valid contentType
    @Test
    public void testGetCharsetFromContentTypeValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(charset, "UTF-8");
    }

    // Test for getCharsetFromContentType method with invalid contentType
    @Test
    public void testGetCharsetFromContentTypeInvalidContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    // Test for mimeBoundary method
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), boundaryLength);
    }
}