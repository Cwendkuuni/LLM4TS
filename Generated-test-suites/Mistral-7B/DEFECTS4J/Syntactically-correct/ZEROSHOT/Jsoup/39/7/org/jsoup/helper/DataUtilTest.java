package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {
    private static final String TEST_FILE_PATH = "testData.html";
    private static final String TEST_UTF8_FILE_PATH = "testDataUtf8.html";
    private static final String TEST_GB2312_FILE_PATH = "testDataGb2312.html";
    private static final String TEST_INVALID_CHARSET_FILE_PATH = "testDataInvalidCharset.html";
    private static final String TEST_UTF8_DATA = "Test data for UTF-8.";
    private static final String TEST_GB2312_DATA = "Test data for GB2312.";
    private static final String TEST_UTF8_META_CHARSET = "Content-Type: text/html; charset=UTF-8";
    private static final String TEST_GB2312_META_CHARSET = "Content-Type: text/html; charset=GB2312";
    private static final String TEST_INVALID_CHARSET_META_CHARSET = "Content-Type: text/html; charset=InvalidCharset";
    private static final String TEST_EMPTY_META_CHARSET = "Content-Type: text/html";
    private static final String TEST_HTML5_CHARSET_META_TAG = "<meta charset=\"UTF-8\">";
    private static final String TEST_HTML5_INVALID_CHARSET_META_TAG = "<meta charset=\"InvalidCharset\">";

    private InputStream testFileInputStream;
    private InputStream testUtf8InputStream;
    private InputStream testGb2312InputStream;
    private InputStream testInvalidCharsetInputStream;

    @Before
    public void setUp() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        File testUtf8File = new File(TEST_UTF8_FILE_PATH);
        File testGb2312File = new File(TEST_GB2312_FILE_PATH);
        File testInvalidCharsetFile = new File(TEST_INVALID_CHARSET_FILE_PATH);

        testFileOutputStream(testFile, TEST_UTF8_DATA);
        testFileOutputStream(testUtf8File, TEST_UTF8_DATA);
        testFileOutputStream(testGb2312File, TEST_GB2312_DATA);
        testFileOutputStream(testInvalidCharsetFile, TEST_UTF8_DATA);

        testFileInputStream = new FileInputStream(testFile);
        testUtf8InputStream = new FileInputStream(testUtf8File);
        testGb2312InputStream = new FileInputStream(testGb2312File);
        testInvalidCharsetInputStream = new FileInputStream(testInvalidCharsetFile);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        Document doc = DataUtil.load(new File("testData.html"), null, null);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        Document doc = DataUtil.load(new File("testData.html"), "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        try {
            Document doc = DataUtil.load(new File("testData.html"), "InvalidCharset", null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadFileWithMetaCharset() throws IOException {
        Document doc = DataUtil.load(new File("testData.html"), null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());

        Element meta = doc.selectFirst("meta[http-equiv=content-type], meta[charset]");
        Assert.assertNotNull(meta);
        Assert.assertEquals(TEST_UTF8_META_CHARSET, meta.attr("content"));
    }

    @Test
    public void testLoadFileWithHtml5MetaCharset() throws IOException {
        Document doc = DataUtil.load(new File("testData.html"), null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());

        Element meta = doc.selectFirst("meta[charset]");
        Assert.assertNotNull(meta);
        Assert.assertEquals(TEST_HTML5_CHARSET_META_TAG, meta.html());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        Document doc = DataUtil.load(testFileInputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        Document doc = DataUtil.load(testUtf8InputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        try {
            Document doc = DataUtil.load(testUtf8InputStream, "InvalidCharset", null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithMetaCharset() throws IOException {
        Document doc = DataUtil.load(testUtf8InputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());

        Element meta = doc.selectFirst("meta[http-equiv=content-type], meta[charset]");
        Assert.assertNotNull(meta);
        Assert.assertEquals(TEST_UTF8_META_CHARSET, meta.attr("content"));
    }

    @Test
    public void testLoadInputStreamWithHtml5MetaCharset() throws IOException {
        Document doc = DataUtil.load(testUtf8InputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());

        Element meta = doc.selectFirst("meta[charset]");
        Assert.assertNotNull(meta);
        Assert.assertEquals(TEST_HTML5_CHARSET_META_TAG, meta.html());
    }

    @Test
    public void testLoadInputStreamWithGb2312Charset() throws IOException {
        Document doc = DataUtil.load(testGb2312InputStream, "GB2312", null);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_GB2312_DATA, doc.body().text());

        Element meta = doc.selectFirst("meta[http-equiv=content-type], meta[charset]");
        Assert.assertNull(meta);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetMetaTag() throws IOException {
        Document doc = DataUtil.load(testInvalidCharsetInputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());

        Element meta = doc.selectFirst("meta[http-equiv=content-type], meta[charset]");
        Assert.assertNull(meta);
    }

    @Test
    public void testLoadInputStreamWithEmptyMetaCharset() throws IOException {
        Document doc = DataUtil.load(testFileInputStream, null, null);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());

        Element meta = doc.selectFirst("meta[http-equiv=content-type], meta[charset]");
        Assert.assertNull(meta);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_UTF8_DATA.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_UTF8_DATA.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(TEST_UTF8_DATA, doc.body().text());
    }

    @Test
    public void testParseByteDataWithMetaCharset() throws IOException {
        String charset = "GB2312";
        String documentData = TEST_GB2312_DATA + "\n" + TEST_HTML5_CHARSET_META_TAG;
        InputStream inputStream = new ByteArrayInputStream(documentData.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = ByteBuffer.wrap(inputStream.readAllBytes());

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(TEST_GB2312_DATA, doc.body().text());

        Element meta = doc.selectFirst("meta[charset]");
        Assert.assertNotNull(meta);
        Assert.assertEquals(TEST_HTML5_CHARSET_META_TAG, meta.html());

        doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(TEST_GB2312_DATA, doc.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_UTF8_DATA.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = TEST_UTF8_DATA.getBytes(Charset.forName("UTF-8"));
        byte[] actualBytes = new byte[byteData.limit()];
        System.arraycopy(byteData.array(), 0, actualBytes, 0, byteData.limit());
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        Assert.assertEquals("UTF-8", DataUtil.getCharsetFromContentType(TEST_UTF8_META_CHARSET));
        Assert.assertEquals("GB2312", DataUtil.getCharsetFromContentType(TEST_GB2312_META_CHARSET));
        Assert.assertNull(DataUtil.getCharsetFromContentType(TEST_EMPTY_META_CHARSET));
    }

    private void testFileOutputStream(File file, String data) throws IOException {
        byte[] bytes = data.getBytes();
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }
}