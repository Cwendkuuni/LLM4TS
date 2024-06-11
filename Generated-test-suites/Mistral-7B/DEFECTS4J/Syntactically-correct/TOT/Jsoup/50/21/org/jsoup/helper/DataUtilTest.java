package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.Parser.MalformedAttrValueException;
import org.jsoup.parser.Parser.MalformedHtmlException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public final class DataUtilTest {

    private static Document doc;
    private static final String TEST_FILE = "testFile.html";
    private static final String TEST_INPUT_STREAM = "testInput.html";
    private static final String TEST_BASE_URI = "http://example.com/";
    private static final String CHARSET_GB2312 = "GB2312";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_INVALID = "INVALID_CHARSET";

    @BeforeClass
    public static void setup() {
        doc = DataUtil.load(new File(TEST_FILE), CHARSET_UTF8, TEST_BASE_URI);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File(TEST_FILE);
        Document result = DataUtil.load(file, CHARSET_UTF8, TEST_BASE_URI);
        assertEquals(doc, result);

        doc = DataUtil.load(file, null, TEST_BASE_URI);
        assertEquals(doc, DataUtil.load(file, CHARSET_UTF8, TEST_BASE_URI));

        file = new File("nonExistentFile");
        assertThrows(FileNotFoundException.class, () -> DataUtil.load(file, CHARSET_UTF8, TEST_BASE_URI));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><head><meta charset='GB2312'></head><body>Test Data</body></html>";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(testData.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        Document result = DataUtil.load(is, CHARSET_GB2312, TEST_BASE_URI);
        Element title = result.title();
        assertEquals("Test Data", title.text());

        result = DataUtil.load(is, null, TEST_BASE_URI);
        title = result.title();
        assertEquals("Test Data", title.text());

        is = new ByteArrayInputStream(new byte[0]);
        assertThrows(MalformedHtmlException.class, () -> DataUtil.load(is, CHARSET_UTF8, TEST_BASE_URI));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><head><meta charset='GB2312'></head><body>Test Data</body></html>";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(testData.getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        Document result = DataUtil.load(is, CHARSET_GB2312, TEST_BASE_URI, Parser.xmlParser());
        Element title = result.title();
        assertEquals("Test Data", title.text());

        is = new ByteArrayInputStream(new byte[0]);
        assertThrows(MalformedHtmlException.class, () -> DataUtil.load(is, CHARSET_UTF8, TEST_BASE_URI, Parser.xmlParser()));
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream(TEST_FILE);
        OutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(in, out);
        byte[] result = out.toByteArray();
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(result), CHARSET_UTF8, TEST_BASE_URI, Parser.htmlParser());
        assertEquals(doc.outerHtml(), document.outerHtml());

        in.close();
        out.close();

        result = new byte[0];
        DataUtil.crossStreams(new FileInputStream(TEST_FILE), new ByteArrayOutputStream());
    }

    @Test
    public void testParseByteData() {
        String testData = "<html><head><meta charset='GB2312'></head><body>Test Data</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        Document result = DataUtil.parseByteData(byteData, CHARSET_GB2312, TEST_BASE_URI, Parser.htmlParser());
        Element title = result.title();
        assertEquals("Test Data", title.text());

        byteData.rewind();
        result = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
        title = result.title();
        assertEquals("Test Data", title.text());

        byteData.rewind();
        byteData = ByteBuffer.wrap(new byte[]{UNICODE_BOM});
        byteData.put(testData.getBytes(StandardCharsets.UTF_8), 0, testData.length());
        byteData.flip();
        result = DataUtil.parseByteData(byteData, CHARSET_UTF8, TEST_BASE_URI, Parser.htmlParser());
        title = result.title();
        assertEquals("Test Data", title.text());

        byteData = ByteBuffer.wrap(new byte[]{UNICODE_BOM});
        assertThrows(MalformedHtmlException.class, () -> DataUtil.parseByteData(byteData, CHARSET_UTF8, TEST_BASE_URI, Parser.htmlParser()));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=GB2312";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(CHARSET_GB2312, result);

        contentType = "text/html; charset=UTF-8";
        result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(CHARSET_UTF8, result);

        contentType = "text/html; charset=INVALID_CHARSET";
        result = DataUtil.getCharsetFromContentType(contentType);
        assertNull(result);

        contentType = "text/html";
        result = DataUtil.getCharsetFromContentType(contentType);
        assertNull(result);
    }

    @Test
    public void testMimeBoundary() {
        String result = DataUtil.mimeBoundary();
        String expected = "-----_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(expected, result);
    }
}