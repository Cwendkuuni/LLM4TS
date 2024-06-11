package org.jsoup.helper;

import org.jsoup.nodes.Document;
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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {
    private static final String FILENAME = "DataUtilTestFile.html";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String BASE_URI = "http://example.com";
    private static final String CHARSET_NAME = "UTF-8";

    private Document document;
    private File file;
    private InputStream inputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws Exception {
        file = new File(System.getProperty("user.dir") + File.separator + FILENAME);
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testLoadFile() throws IOException {
        document = DataUtil.load(file, CHARSET_NAME, BASE_URI);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        document = DataUtil.load(file, null, BASE_URI);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        document = DataUtil.load(file, "invalidCharset", BASE_URI);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byteArrayOutputStream.write(html.getBytes(Charset.forName(CHARSET_NAME)));
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byteArrayOutputStream.write(html.getBytes(Charset.forName(CHARSET_NAME)));
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        document = DataUtil.load(inputStream, null, BASE_URI);
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byteArrayOutputStream.write(html.getBytes(Charset.forName(CHARSET_NAME)));
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        document = DataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(document);

        html = "<html><head><meta charset=\"GBK\"></head><body></body></html>";
        byteArrayOutputStream.reset();
        byteArrayOutputStream.write(html.getBytes(Charset.forName(CHARSET_NAME)));
        byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        String foundCharset = DataUtil.getCharsetFromContentType(byteArrayOutputStream.toString());
        document = DataUtil.parseByteData(byteData, foundCharset, BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("GBK", foundCharset);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inStream = new FileInputStream(file);
        OutputStream outStream = new FileOutputStream("outputFile.html");

        DataUtil.crossStreams(inStream, outStream);
        inStream.close();
        outStream.close();

        File outputFile = new File("outputFile.html");
        Document document = DataUtil.load(outputFile, CHARSET_NAME, BASE_URI);
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byteArrayOutputStream.write(html.getBytes(Charset.forName(CHARSET_NAME)));
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Assert.assertNotNull(byteData);

        html = "<html><head><meta charset=\"GBK\"></head><body></body></html>";
        byteArrayOutputStream.reset();
        byteArrayOutputStream.write(html.getBytes(Charset.forName(CHARSET_NAME)));
        byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        String foundCharset = DataUtil.getCharsetFromContentType(byteArrayOutputStream.toString());
        Assert.assertNotNull(byteData);
        Assert.assertEquals("GBK", foundCharset);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        File file = new File(System.getProperty("user.dir") + File.separator + FILENAME);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(html);
        fileWriter.close();

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteData);

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);

        contentType = "text/html; charset=GBK";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("GBK", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), boundaryLength);
    }
}