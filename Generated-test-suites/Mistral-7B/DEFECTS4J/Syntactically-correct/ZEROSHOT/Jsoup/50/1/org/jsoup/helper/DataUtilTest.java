package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        Document document = DataUtil.load(file, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-16";
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        Document document = DataUtil.load(file, charsetName);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = "INVALID_CHARSET";
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        DataUtil.load(file, charsetName);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String content = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        Document document = DataUtil.load(inputStream, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-16";
        String content = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        Document document = DataUtil.load(inputStream, charsetName);
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "INVALID_CHARSET";
        String content = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        DataUtil.load(inputStream, charsetName);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String content = "Your HTML content here";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String charsetName = "UTF-16";
        String content = "Your HTML content here";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharsetAndBOM() {
        String content = "\uFEFFYour HTML content here";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Input content";
        String outputContent = "Output content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(inputContent, new String(outputBytes, "UTF-8"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        String expectedBoundary = new String(new Random().Ints.generateSequence((int) Math.ceil(Math.log(mimeBoundaryChars.length * Math.log(2)) / Math.log(2)).limit(32).toArray())).map(i -> String.valueOf(mimeBoundaryChars[i % mimeBoundaryChars.length])).collect(Collectors.joining());
        assertEquals(expectedBoundary, boundary);
    }
}