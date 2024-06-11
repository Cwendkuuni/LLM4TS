package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * Tests for {@link DataUtil} class
 */
public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://example.com";
    private static final String XML_VALID_CONTENT_TYPE = "text/xml; charset=UTF-8";
    private static final String HTML_VALID_CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String XML_INVALID_CONTENT_TYPE = "text/plain";
    private static final String HTML_INVALID_CONTENT_TYPE = "text/plain; charset=UTF-8";
    private static final String XML_MIME_DECLARATION = "<?xml encoding='UTF-8'?>";
    private static final String HTML_META_CHARSET = "<meta charset='UTF-8'>";

    @Test
    public void testLoadFileWithUTF8Charset() throws IOException {
        File file = new File("testData/testUTF8.xml");
        Document document = DataUtil.load(file, CHARSET_NAME, BASE_URI);
        assertSame("Document should not be null.", document, new Document(BASE_URI));
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("testData/testUTF8.xml");
        Document document = DataUtil.load(file, null, BASE_URI);
        assertNull("Document should be null when charset arg is null.", document);
    }

    @Test
    public void testLoadInputStreamWithUTF8Charset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("testData/testUTF8.xml"));
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
        assertSame("Document should not be null.", document, new Document(BASE_URI));
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("testData/testUTF8.xml"));
        Document document = DataUtil.load(inputStream, null, BASE_URI);
        assertNull("Document should be null when charset arg is null.", document);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithXmlParser() throws IOException {
        InputStream inputStream = new FileInputStream(new File("testData/testUTF8.xml"));
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.xmlParser());
        assertSame("Document should not be null.", document, new Document(BASE_URI));
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidContentTypeXml() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_INVALID_CONTENT_TYPE.getBytes(StandardCharsets.UTF_8));
        Parser parser = mock(Parser.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.eq(BASE_URI))).thenThrow(new IOException("Parse error"));
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, parser);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidContentTypeHtml() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(XML_INVALID_CONTENT_TYPE.getBytes(StandardCharsets.UTF_8));
        Parser parser = mock(Parser.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.eq(BASE_URI))).thenThrow(new IOException("Parse error"));
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, parser);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithXmlParserAndValidContentType() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(XML_VALID_CONTENT_TYPE.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.xmlParser());
        assertSame("Document should not be null.", document, new Document(BASE_URI));
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithHtmlParserAndValidContentType() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_VALID_CONTENT_TYPE.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertSame("Document should not be null.", document, new Document(BASE_URI));
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File file = new File("testData/testUTF8.xml");
        File tempFile = new File("testData/temp.xml");
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = new FileOutputStream(tempFile);
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
        assert file.length() > 0;
        assert tempFile.length() > 0;
        assert file.length() == tempFile.length();
    }

    @Test
    public void testParseInputStreamWithUTF8Charset() throws IOException {
        File file = new File("testData/testUTF8.xml");
        InputStream inputStream = new FileInputStream(file);
        Document document = DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertSame("Document should not be null.", document, new Document(BASE_URI));
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.parseInputStream(inputStream, null, BASE_URI, Parser.htmlParser());
        assertNull("Document should be null when charset arg is null.", document);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithXmlParser() throws IOException {
        InputStream inputStream = new FileInputStream(new File("testData/testUTF8.xml"));
        Document document = DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.xmlParser());
        assertSame("Document should not be null.", document, new Document(BASE_URI));
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("testData/testUTF8.xml");
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        inputStream.close();
        assert byteBuffer.limit() > 0;
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assert byteBuffer.limit() == 0;
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = XML_VALID_CONTENT_TYPE;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertSame("UTF-8", charset);

        contentType = HTML_VALID_CONTENT_TYPE;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertSame("UTF-8", charset);

        contentType = XML_INVALID_CONTENT_TYPE;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull("charset should be null.", charset);

        contentType = HTML_INVALID_CONTENT_TYPE;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull("charset should be null.", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validCharset = DataUtil.validateCharset(charset);
        assertSame("UTF-8", validCharset);

        charset = "UTF-8 ";
        validCharset = DataUtil.validateCharset(charset);
        assertSame("UTF-8", validCharset);

        charset = "UTF-16";
        validCharset = DataUtil.validateCharset(charset);
        assertSame("UTF-16", validCharset);

        charset = "UTF-32";
        validCharset = DataUtil.validateCharset(charset);
        assertSame("UTF-32", validCharset);

        charset = null;
        validCharset = DataUtil.validateCharset(charset);
        assertNull("charset should be null.", validCharset);

        charset = "GB2312";
        validCharset = DataUtil.validateCharset(charset);
        assertSame("GB2312", validCharset);

        charset = "GB2312 ";
        validCharset = DataUtil.validateCharset(charset);
        assertSame("GB2312", validCharset);

        charset = "GB2312UTF-8";
        validCharset = DataUtil.validateCharset(charset);
        assertNull("charset should be null.", validCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull("Boundary should not be null.", boundary);
        assertFalse("Boundary should be larger than an empty string.", boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] { 0x00, 0x00, 0xFE, 0xFF });
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull("BomCharset should not be null.", bomCharset);
        assertEquals("UTF-32", bomCharset.charset);
        assertFalse("offset should be false.", bomCharset.offset);

        byteBuffer = ByteBuffer.wrap(new byte[] { 0xFF, 0xFE, 0x00, 0x00 });
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull("BomCharset should not be null.", bomCharset);
        assertEquals("UTF-16", bomCharset.charset);
        assertFalse("offset should be false.", bomCharset.offset);

        byteBuffer = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull("BomCharset should not be null.", bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue("offset should be true.", bomCharset.offset);

        byteBuffer = ByteBuffer.wrap(new byte[0]);
        bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull("BomCharset should be null.", bomCharset);
    }
}