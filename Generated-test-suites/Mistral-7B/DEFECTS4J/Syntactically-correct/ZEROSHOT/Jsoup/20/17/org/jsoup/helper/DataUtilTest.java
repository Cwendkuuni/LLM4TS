package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.java
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("src/test/resources/testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document expectedDocument = parser().parse("src/test/resources/testFile.html");

        Document document = DataUtil.load(inputFile, charsetName, baseUri);

        assertEquals(expectedDocument, document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File inputFile = new File("src/test/resources/testFile.html");
        String baseUri = "http://example.com";

        DataUtil.load(inputFile, null, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String htmlContent = "test content";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(Parser.htmlParser().parseInput(htmlContent, baseUri), document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";

        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser());

        assertEquals(parser().parseInput(htmlContent, baseUri), document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";

        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser());

        assertEquals(parser().parseInput(htmlContent, baseUri), document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        String htmlContent = "<html><head><meta charset=\"GB2312\"></head><body><p>Test</p></body></html>";

        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser());

        assertEquals(parser().parseInput(htmlContent.substring(5), baseUri), document); // assuming detection skips the "charset=\"GB2312\" header
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "test content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteArray = htmlContent.getBytes();
        byte[] readByteArray = new byte[expectedByteArray.length];
        System.arraycopy(expectedByteArray, 0, readByteArray, 0, expectedByteArray.length);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(readByteArray);

        assertEquals(expectedByteBuffer, byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testParseByteDataWithNullByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser());

        assertNull(document);
    }

    private Parser parser() {
        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), anyString())).thenCallRealMethod();
        return parser;
    }
}