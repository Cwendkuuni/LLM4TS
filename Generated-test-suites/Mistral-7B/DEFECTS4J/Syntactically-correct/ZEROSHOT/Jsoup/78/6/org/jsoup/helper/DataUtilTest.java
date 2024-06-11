package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static final String TEST_HTML = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><p>Hello World!</p></body></html>";

    @Test
    public void testLoadFile() throws IOException {
        File file = new File(TEST_FILE);
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
        assertEquals(TEST_HTML, doc.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_HTML.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals(TEST_HTML, doc.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_HTML.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(TEST_HTML, doc.html());
    }

    @Test
    public void testParseInputStreamWithXmlParser() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_HTML.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals(TEST_HTML, doc.xml().toString());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_HTML.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(TEST_HTML, outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("invalid charset test".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        try {
            DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
            assert False : "Should not have parsed document with invalid charset";
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_HTML.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(outputStream.size(), byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithInvalidInput() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(null);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testParseInputStreamWithCharsetFromContentType() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_HTML.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.parseInputStream(inputStream, contentType, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(TEST_HTML, doc.html());
    }

    @Test
    public void testParseInputStreamWithCharsetFromXMLDeclaration() throws IOException {
        String xmlDeclaration = "<?xml encoding=\"UTF-8\"?>\n<html><body><p>Hello World!</p></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(xmlDeclaration.getBytes(StandardCharsets.UTF_8));
        outputStream.write(TEST_HTML.substring(1).getBytes(StandardCharsets.UTF_8)); // remove bom
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals(TEST_HTML, doc.xml().toString());
    }
}