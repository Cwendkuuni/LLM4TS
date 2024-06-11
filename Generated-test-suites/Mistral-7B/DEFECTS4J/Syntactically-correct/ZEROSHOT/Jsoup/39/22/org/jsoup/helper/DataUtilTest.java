package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "invalid-charset";
        String baseUri = "http://example.com/";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "invalid-charset";
        String baseUri = "http://example.com/";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromInputStreamWithEmptyInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        DataUtil.load(new ByteArrayInputStream(new byte[0]), charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(document);

        // Test charset detection
        String htmlWithInvalidCharset = "<html><head><meta charset=\"invalid-charset\"></head><body></body></html>";
        byteData = ByteBuffer.wrap(htmlWithInvalidCharset.getBytes(Charset.forName("UTF-8")));
        document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("invalid-charset", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(html.length(), byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(10, byteData.limit());
    }
}