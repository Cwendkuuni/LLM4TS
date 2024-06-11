package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 *
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("src/test/resources/test.html");
        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File("src/test/resources/test.html");
        Document doc = DataUtil.load(testFile, "ISO-8859-1", "");
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html lang=\"en\"><head><meta charset=\"ISO-8859-1\"><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);

        // Rewind the stream
        inputStream.reset();

        // Set the charset to ISO-8859-1
        Document docWithValidCharset = DataUtil.load(inputStream, "ISO-8859-1", "");
        assertNotNull(docWithValidCharset);
        assertEquals(html, docWithValidCharset.outerHtml());
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<xml lang=\"en\"><root><title>Test</title></root></xml>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("<xml lang=\"en\"><root><title>Test</title></root></xml>", doc.outerHtml());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testParseByteData() throws IOException {
        String html = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>", doc.outerHtml());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String html = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", "", Parser.htmlParser());
        assertNotNull(doc);
        assertFalse(doc.outerHtml().contains("<html")); // The HTML should be decoded from the byte buffer with UTF-8 charset.

        ByteBuffer byteDataWithBOM = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }.concat(html.getBytes("UTF-8")));
        Document docWithBOM = DataUtil.parseByteData(byteDataWithBOM, "UTF-8", "", Parser.htmlParser());
        assertNotNull(docWithBOM);
        assertEquals("<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>", docWithBOM.outerHtml());
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("gb2312", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}