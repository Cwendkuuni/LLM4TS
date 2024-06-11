package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Tests for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // assuming you have a test.html file available
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        // add some assertions based on the structure of the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        // add some assertions based on the structure of the loaded document
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        // add some assertions based on the structure of the loaded document
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String output = outputStream.toString();
        assertEquals("test data", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"ABCDEFGH\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        DataUtil.parseInputStream(inputStream, "ABCDEFGH", "", Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(html.length(), byteBuffer.limit());
        // add some assertions based on the structure of the byte buffer
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        String contentType2 = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType2));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("Utf-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));

        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(" "));
        assertNull(DataUtil.validateCharset("ABC"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(boundary.length(), 32);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String html = "\uFEFF<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(false, bomCharset.offset);
    }
}