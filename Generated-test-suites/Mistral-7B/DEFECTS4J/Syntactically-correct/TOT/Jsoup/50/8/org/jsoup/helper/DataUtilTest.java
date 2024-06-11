package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {

    private static final String HTML_FILE_CONTENTS = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"></head><body><p>Test text</p></body></html>";
    private static final String XML_FILE_CONTENTS = "<root><element>Test text</element></root>";
    private Document htmlDoc;
    private Document xmlDoc;

    @Before
    public void setUp() throws IOException {
        htmlDoc = DataUtil.load(new File("test.html"), "UTF-8", null);
        xmlDoc = DataUtil.load(new File("test.xml"), null, null);
    }

    @Test
    public void testLoadHtmlFile() throws IOException {
        Document doc = DataUtil.load(new File("test.html"), "UTF-8", null);
        assertNotNull(doc);
        assertEquals(HTML_FILE_CONTENTS, doc.html());
    }

    @Test
    public void testLoadHtmlFileWithInvalidCharset() throws IOException {
        Document doc = DataUtil.load(new File("test.html"), "InvalidCharset", null);
        assertNull(doc);
    }

    @Test
    public void testLoadHtmlFileWithNoCharset() throws IOException {
        Document doc = DataUtil.load(new File("test.html"), null, null);
        assertNotNull(doc);
        assertEquals(HTML_FILE_CONTENTS, doc.html());
    }

    @Test
    public void testLoadHtmlInputStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, "UTF-8", null);
        assertNotNull(doc);
        assertEquals(HTML_FILE_CONTENTS, doc.html());
    }

    @Test
    public void testLoadHtmlInputStreamWithInvalidCharset() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, "InvalidCharset", null);
        assertNull(doc);
    }

    @Test
    public void testLoadHtmlInputStreamWithNoCharset() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, null, null);
        assertNotNull(doc);
        assertEquals(HTML_FILE_CONTENTS, doc.html());
    }

    @Test
    public void testLoadHtmlInputStreamWithParser() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(HTML_FILE_CONTENTS, doc.html());
    }

    @Test
    public void testLoadXmlFile() throws IOException {
        Document doc = DataUtil.load(new File("test.xml"), null, null);
        assertNotNull(doc);
        assertEquals(XML_FILE_CONTENTS, doc.xml());
    }

    @Test
    public void testLoadXmlFileWithInvalidCharset() throws IOException {
        Document doc = DataUtil.load(new File("test.xml"), "InvalidCharset", null);
        assertNull(doc);
    }

    @Test
    public void testLoadXmlFileWithNoCharset() throws IOException {
        Document doc = DataUtil.load(new File("test.xml"), null, null);
        assertNotNull(doc);
        assertEquals(XML_FILE_CONTENTS, doc.xml());
    }

    @Test
    public void testLoadXmlInputStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(XML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, null, null);
        assertNotNull(doc);
        assertEquals(XML_FILE_CONTENTS, doc.xml());
    }

    @Test
    public void testLoadXmlInputStreamWithInvalidCharset() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(XML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, "InvalidCharset", null);
        assertNull(doc);
    }

    @Test
    public void testLoadXmlInputStreamWithNoCharset() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(XML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, null, null);
        assertNotNull(doc);
        assertEquals(XML_FILE_CONTENTS, doc.xml());
    }

    @Test
    public void testLoadXmlInputStreamWithParser() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(XML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, null, null, DataUtil.Parser.xmlParser());
        assertNotNull(doc);
        assertEquals(XML_FILE_CONTENTS, doc.xml());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream is = new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(is, os);
        assertEquals(HTML_FILE_CONTENTS, os.toString(StandardCharsets.UTF_8.name()));
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = ByteBuffer.wrap(os.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(HTML_FILE_CONTENTS, doc.html());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = ByteBuffer.wrap(os.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "InvalidCharset", null, DataUtil.Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithNoCharset() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = ByteBuffer.wrap(os.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(HTML_FILE_CONTENTS, doc.html());
    }

    @Test
    public void testParseByteDataWithXmlParser() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(XML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = ByteBuffer.wrap(os.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertNotNull(doc);
        assertEquals(XML_FILE_CONTENTS, doc.xml());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(is);
        assertEquals(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8).length, byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(is, 10);
        assertEquals(10, byteData.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertEquals(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8).length, byteData.limit());
    }

    @Test
    public void testReadFileToByteBufferWithEmptyFile() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || boundary.charAt(i) == '-' || boundary.charAt(i) == '_');
        }
    }
}