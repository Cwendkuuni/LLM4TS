package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.After;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class DataUtilTest {

    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:\"|')?([^\\s,;\"']*)");
    private static final String defaultCharset = "UTF-8";
    private static final int bufferSize = 0x20000;

    private DataUtil dataUtil;
    private Parser htmlParser;
    private Parser xmlParser;

    @Before
    public void setUp() {
        htmlParser = mock(Parser.class);
        xmlParser = mock(Parser.class);
        dataUtil = new DataUtil();
    }

    @After
    public void tearDown() {
        dataUtil = null;
        htmlParser = null;
        xmlParser = null;
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Some content here";
        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = dataUtil.load(testFile, defaultCharset, null);
        Assert.assertThat(doc, is(notNullValue()));
        Assert.assertEquals(fileContent, doc.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "Some content here";
        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-16");

        dataUtil.load(testFile, "UTF-16BE", null);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "Some content here";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));

        Document doc = dataUtil.load(testStream, defaultCharset, null);
        Assert.assertThat(doc, is(notNullValue()));
        Assert.assertEquals(testData, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "Some content here";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes("ISO-8859-1"));

        Document doc = dataUtil.load(testStream, "ISO-8859-1", null);
        Assert.assertThat(doc, is(notNullValue()));
        Assert.assertEquals(testData, doc.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "Some content here";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes("UTF-16"));

        dataUtil.load(testStream, "UTF-16", null);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><body><p>Some content here</p></body></html>";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));

        Document doc = dataUtil.load(testStream, defaultCharset, null, htmlParser);
        Assert.assertThat(doc, is(notNullValue()));
        Assert.assertEquals(1, doc.body().children().size());
        Assert.assertEquals("Some content here", doc.body().select("p").text());
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html><body><p>Some content here</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("UTF-8"));

        Document doc = dataUtil.parseByteData(byteData, defaultCharset, null, htmlParser);
        Assert.assertThat(doc, is(notNullValue()));
        Assert.assertEquals(1, doc.body().children().size());
        Assert.assertEquals("Some content here", doc.body().select("p").text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String testData = "Some content here";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes("ISO-8859-1"));

        Document doc = dataUtil.parseByteData(byteData, "ISO-8859-1", null, htmlParser);
        Assert.assertThat(doc, is(notNullValue()));
        Assert.assertEquals(testData, doc.body().text());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        Assert.assertThat(charset, is(notNullValue()));
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html; charset=gb2312";
        charset = dataUtil.getCharsetFromContentType(contentType);
        Assert.assertThat(charset, is(notNullValue()));
        Assert.assertEquals("gb2312", charset);

        contentType = "text/html";
        charset = dataUtil.getCharsetFromContentType(contentType);
        Assert.assertThat(charset, is(nullValue()));
    }
}