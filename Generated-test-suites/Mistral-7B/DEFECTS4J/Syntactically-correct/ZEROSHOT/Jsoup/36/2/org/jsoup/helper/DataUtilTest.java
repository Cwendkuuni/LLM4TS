package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(inputFile, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromFileWithInvalidFile() throws IOException {
        File inputFile = new File("non-existent-file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDetectedCharset() throws IOException {
        String htmlContent = "<html lang='ja' xml:lang='ja' xmlns='http://www.w3.org/1999/xhtml' xml:base='http://example.com/'><head><meta http-equiv='Content-Type' content='text/html; charset=EUC-JP'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, null);

        assertEquals("EUC-JP", document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromInputStreamWithInvalidContentType() throws IOException {
        String htmlContent = "invalid-content-type-header";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertEquals(htmlContent.length(), byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";

        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}