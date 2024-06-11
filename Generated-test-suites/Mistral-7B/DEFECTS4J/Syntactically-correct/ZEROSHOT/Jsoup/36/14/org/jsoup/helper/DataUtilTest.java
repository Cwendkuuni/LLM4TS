package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws Exception {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "XYZ");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "XYZ");
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(html.getBytes("UTF-8").length, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnknownCharset() {
        String contentType = "text/html; charset=XYZ";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}