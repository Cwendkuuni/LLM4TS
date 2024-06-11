package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);

        byteData.rewind();
        String charset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charset);

        byteData.rewind();
        byteData.position(3);
        charset = DataUtil.detectCharsetFromBom(byteData, "UTF-8");
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(html.getBytes("UTF-8").length, byteData.limit());

        byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(10, byteData.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteData);
        assertEquals(Files.size(file.toPath()), byteData.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-16";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "invalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }
}