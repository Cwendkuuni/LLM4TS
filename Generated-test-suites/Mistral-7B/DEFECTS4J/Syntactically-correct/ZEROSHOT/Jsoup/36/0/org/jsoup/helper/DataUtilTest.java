package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private File testFile;
    private Document doc;

    @Before
    public void setUp() throws IOException {
        testFile = File.createTempFile("testDataUtil", ".html");
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html lang=\"en\" xmlns:html=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test DataUtil</h1>\n" +
                "</body>\n" +
                "</html>";
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();
             InputStream inStream = new ByteArrayInputStream(content.getBytes("UTF-8"))) {
            testFile.createNewFile();
            try (FileWriter fileWriter = new FileWriter(testFile)) {
                IOUtils.copy(inStream, fileWriter);
            }
        }
        this.doc = DataUtil.load(testFile, "UTF-8", null);
    }

    @After
    public void tearDown() throws IOException {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        Document document = null;
        try {
            document = DataUtil.load(testFile, null, null);
        } catch (IOException e) {
            // expected
        }
        assertNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "Test DataUtil\n";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inStream = new ByteArrayInputStream(testData.getBytes("UTF-8"))) {
            doc = DataUtil.load(inStream, "UTF-8", null);
        }
        assertEquals(testData, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "Test DataUtil\n";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inStream = new ByteArrayInputStream(testData.getBytes("UTF-8"))) {
            Document document = DataUtil.load(inStream, null, null);
            assertNull(document);
        }
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String testData = "Test DataUtil\n";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inStream = new ByteArrayInputStream(testData.getBytes("UTF-8"))) {
            doc = DataUtil.load(inStream, "UTF-8", null, Parser.xmlParser());
            assertEquals(testData, doc.body().text());
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "Test DataUtil\n";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals(testData, document.body().text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String testData = "Test DataUtil\n";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test DataUtil\n";
        InputStream inStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        assertEquals(testData.getBytes("UTF-8").length, byteData.limit());
        byteData.rewind();
        byte[] expected = testData.getBytes("UTF-8");
        byte[] actual = new byte[byteData.remaining()];
        byteData.get(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "Test DataUtil\n";
        InputStream inStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, 10);
        assertEquals(10, byteData.limit());
        byteData.rewind();
        byte[] expected = new byte[10];
        byteData.get(expected);
        String actual = new String(expected, "UTF-8");
        assertEquals("Test DataUtil...........", actual);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}