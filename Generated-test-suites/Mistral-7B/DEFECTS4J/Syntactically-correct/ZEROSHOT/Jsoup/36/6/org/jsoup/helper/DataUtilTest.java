package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser.HtmlParser;
import org.junit.Test;

/**
 * Test class for DataUtil
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("src/test/resources/test.html");
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        File file = new File("src/test/resources/test.html");
        Document document = null;
        try {
            document = DataUtil.load(file, charsetName, baseUri);
            assertNull(document);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream(new File("src/test/resources/test.html"));
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream(new File("src/test/resources/test.html"));
        Document document = null;
        try {
            document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNull(document);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream(new File("src/test/resources/test.html"));
        Parser parser = mock(HtmlParser.class);
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<!--\n<html>\n<head>\n<meta charset=\"EUC-JP\">\n</head>\n<body>\n<p>Test</p>\n</body>\n-->".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, new HtmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<!--\n<html>\n<head>\n<meta charset=\"EUC-JP\">\n</head>\n<body>\n<p>Test</p>\n-->".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, new HtmlParser());
        assertNull(document);
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String charsetName = "EUC-JP";
        String baseUri = "http://example.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<!--\n<html>\n<head>\n<meta charset=\"EUC-JP\">\n</head>\n<body>\n<p>Test</p>\n-->".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, new HtmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileName = "src/test/resources/test.html";
        InputStream inputStream = new FileInputStream(new File(fileName));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String fileName = "src/test/resources/test.html";
        InputStream inputStream = new FileInputStream(new File(fileName));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}