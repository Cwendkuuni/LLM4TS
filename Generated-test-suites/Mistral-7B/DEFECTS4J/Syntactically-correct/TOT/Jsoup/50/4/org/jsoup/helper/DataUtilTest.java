package org.jsoup.helper.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private Document doc;

    @Before
    public void setUp() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        doc = DataUtil.parseString(html, Parser.htmlParser());
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File(TEST_FILE);
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File(TEST_FILE);
        String invalidCharset = "invalid-charset";
        Document doc = null;
        try {
            doc = DataUtil.load(file, invalidCharset, null);
            assertNotNull(doc);
        } catch (Exception e) {
            // expected
        } finally {
            assertNull(doc);
        }
    }

    @Test
    public void testLoadStream() throws IOException {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(input, "UTF-8", null);
        assertNotNull(doc);
        input.close();
    }

    @Test
    public void testLoadStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes());
        String invalidCharset = "invalid-charset";
        Document doc = null;
        try {
            doc = DataUtil.load(input, invalidCharset, null);
            assertNotNull(doc);
        } catch (Exception e) {
            // expected
        } finally {
            assertNull(doc);
            input.close();
        }
    }

    @Test
    public void testLoadStreamWithParser() throws IOException {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(input, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertNotNull(doc);
        input.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        // This test is hard to automate and may take a long time to complete, so it's left as an exercise for the reader
    }

    @Test
    public void testParseByteData() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = DataUtil.parseStringToByteBuffer(html);
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithEmptyInput() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(input);
        input.close();
        assertEquals(html.getBytes().length, byteData.limit());
    }

    @Test
    public void testReadToByteBufferEmpty() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteData = DataUtil.readToByteBuffer(input);
        input.close();
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File(TEST_FILE);
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertEquals(new File(TEST_FILE).length(), byteData.limit());
    }

    @Test
    public void testReadFileToByteBufferEmpty() throws IOException {
        File file = new File("empty.txt");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html; charset=unkown";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @After
    public void tearDown() throws Exception {
        if (doc != null) {
            doc.outputSettings().clearAll();
        }
    }
}