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
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private Document document;

    @Before
    public void setUp() throws IOException {
        File file = new File(TEST_FILE);
        document = DataUtil.load(file, "UTF-8", null);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File(TEST_FILE);
        Document loadedDocument = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(loadedDocument);
        Assert.assertEquals(document.html(), loadedDocument.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new FileInputStream(TEST_FILE), byteArrayOutputStream);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document loadedDocument = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(loadedDocument);
        Assert.assertEquals(document.html(), loadedDocument.html());
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File(TEST_FILE));
        Document documentFromByteData = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(documentFromByteData);
        Assert.assertEquals(document.html(), documentFromByteData.html());
    }

    @Test
    public void testParseByteDataWithCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File(TEST_FILE));
        String charset = "UTF-8";
        Document documentFromByteData = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        Assert.assertNotNull(documentFromByteData);
        Assert.assertEquals(document.html(), documentFromByteData.html());
        Assert.assertEquals(charset, documentFromByteData.outputSettings().charset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullParser() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File(TEST_FILE));
        Document documentFromByteData = DataUtil.parseByteData(byteData, null, null, null);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0);
        byteData.put((byte) 0);
        byteData.flip();
        String charset = DataUtil.detectCharsetFromBom(byteData, null);
        Assert.assertEquals("UTF-16", charset);

        byteData.clear();
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xBF);
        byteData.flip();
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        Assert.assertEquals("UTF-8", charset);

        byteData.clear();
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0);
        byteData.put((byte) 0);
        byteData.flip();
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        Assert.assertEquals("UTF-32", charset);

        byteData.clear();
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0);
        byteData.put((byte) 0);
        byteData.flip();
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        Assert.assertEquals("UTF-32", charset);

        byteData.clear();
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xBF);
        byteData.flip();
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        Assert.assertEquals("UTF-8", charset);

        byteData.clear();
        byteData.put((byte) 0);
        byteData.put((byte) 0);
        byteData.flip();
        charset = DataUtil.detectCharsetFromBom(byteData, null);
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html; charset=GB2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("GB2312", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("ISO-8859-1", charset);

        contentType = "text/html; charset=Shift_JIS";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("Shift_JIS", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }
}