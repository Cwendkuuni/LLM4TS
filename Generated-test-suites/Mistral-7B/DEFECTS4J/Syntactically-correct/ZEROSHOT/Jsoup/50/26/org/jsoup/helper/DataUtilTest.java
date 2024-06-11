package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * Test file for the DataUtil class.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("testFile.html");

        Document document = DataUtil.load(inputFile, charset, baseUri);

        assertTrue(document != null);
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromNullFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = null;

        Document document = DataUtil.load(inputFile, charset, baseUri);

        assertNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Test Document".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, charset, baseUri);

        assertTrue(document != null);
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromNullInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = null;

        Document document = DataUtil.load(inputStream, charset, baseUri);

        assertNull(document);
    }

    @Test
    public void testLoadDocumentWithAlternateParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Test Document".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, charset, baseUri, Parser.xmlParser());

        assertTrue(document != null);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Input Stream".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String result = outputStream.toString();
        assertTrue(result.equals("Test Input Stream"));
    }

    @Test
    public void testParseByteData() {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put("Test Byte Data".getBytes());
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());

        assertTrue(document != null);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertTrue(charset != null);
        assertTrue(charset.equals("UTF-8"));

        contentType = "text/html";

        charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertTrue(boundary.length() == 32);
    }
}