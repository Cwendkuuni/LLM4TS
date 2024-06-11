package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private Parser parser;
    private Document document;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = mock(Parser.class);
        document = mock(Document.class);
    }

    @After
    public void cleanup() {
        parser = null;
        document = null;
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("testFile.html");
        when(document.outputSettings()).thenReturn(new Document.OutputSettings().charset(StandardCharsets.UTF_8.name()));

        Document actualDocument = DataUtil.load(inputFile, null, "");
        assertThat(actualDocument, is(notNullValue()));
    }

    @Test
    public void testLoadFileWithCustomCharset() throws IOException {
        File inputFile = new File("testFile.html");
        String charsetName = "UTF-16";
        when(document.outputSettings()).thenReturn(new Document.OutputSettings().charset(charsetName));

        Document actualDocument = DataUtil.load(inputFile, charsetName, "");
        assertThat(actualDocument, is(notNullValue()));
        assertThat(actualDocument.outputSettings().charset(), is(charsetName));
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("testFile.html");
        String charsetName = "invalidCharset";

        DataUtil.load(inputFile, charsetName, "");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testData.html".getBytes(StandardCharsets.UTF_8));
        when(document.outputSettings()).thenReturn(new Document.OutputSettings().charset(StandardCharsets.UTF_8.name()));

        Document actualDocument = DataUtil.load(inputStream, null, "");
        assertThat(actualDocument, is(notNullValue()));
    }

    @Test
    public void testLoadInputStreamWithCustomCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testData.html".getBytes(StandardCharsets.UTF_16));
        String charsetName = "UTF-16";
        when(document.outputSettings()).thenReturn(new Document.OutputSettings().charset(charsetName));

        Document actualDocument = DataUtil.load(inputStream, charsetName, "");
        assertThat(actualDocument, is(notNullValue()));
        assertThat(actualDocument.outputSettings().charset(), is(charsetName));
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testData.html".getBytes(StandardCharsets.UTF_8));
        String charsetName = "invalidCharset";

        DataUtil.load(inputStream, charsetName, "");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testData.html".getBytes(StandardCharsets.UTF_8));
        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document actualDocument = DataUtil.load(inputStream, null, "", parser);
        assertThat(actualDocument, is(notNullValue()));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("testData.html".getBytes(StandardCharsets.UTF_8));

        Document actualDocument = DataUtil.parseByteData(byteData, null, "", parser);
        assertThat(actualDocument, is(notNullValue()));
    }

    @Test
    public void testParseByteDataWithCustomCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("testData.html".getBytes(StandardCharsets.UTF_16));
        String charsetName = "UTF-16";

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, "", parser);
        assertThat(actualDocument, is(notNullValue()));
        assertThat(actualDocument.outputSettings().charset(), is(charsetName));
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String testData = "testData.html\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        Document actualDocument = DataUtil.parseByteData(byteData, null, "", parser);
        String detectedCharset = actualDocument.outputSettings().charset();

        assertThat(actualDocument, is(notNullValue()));
        assertThat(detectedCharset, is("gb2312"));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("testData.html".getBytes(StandardCharsets.UTF_8));
        ByteBuffer expectedByteData = ByteBuffer.wrap("testData.html".getBytes(StandardCharsets.UTF_8));

        ByteBuffer actualByteData = DataUtil.readToByteBuffer(inputStream);
        assertThat(actualByteData, is(expectedByteData));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";
        String expectedCharset = "gb2312";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        assertThat(actualCharset, is(expectedCharset));
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";
        String expectedCharset = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        assertThat(actualCharset, is(expectedCharset));
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "";
        String expectedCharset = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        assertThat(actualCharset, is(expectedCharset));
    }
}