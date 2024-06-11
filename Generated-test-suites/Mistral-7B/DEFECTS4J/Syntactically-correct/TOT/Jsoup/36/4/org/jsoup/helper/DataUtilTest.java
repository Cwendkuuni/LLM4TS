package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DataUtilTest {

    private ByteArrayOutputStream outputStream;
    private InputStream inputStream;
    private Parser parser;

    @Before
    public void setUp() {
        parser = Mockito.mock(Parser.class);
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]);
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("test.html", document.title());
    }

    @Test
    public void testLoadFromInvalidFile() {
        File file = new File("test.txt");
        String charsetName = null;
        String baseUri = "http://example.com";
        assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(docData.getBytes(StandardCharsets.UTF_8));
        inputStream = new ByteArrayInputStream(outStream.toByteArray());
        String charsetName = null;
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(docData.getBytes(StandardCharsets.UTF_8));
        inputStream = new ByteArrayInputStream(outStream.toByteArray());
        String charsetName = "GBK";
        String baseUri = "http://example.com";
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("Test", document.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expected = "Test".getBytes(StandardCharsets.UTF_8);
        byteData.position(0);
        byte[] actual = new byte[byteData.remaining()];
        byteData.get(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 1);
        byte[] expected = new byte[0];
        byteData.position(0);
        byte[] actual = new byte[byteData.remaining()];
        byteData.get(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=GBK";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}