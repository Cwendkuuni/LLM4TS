package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DataUtilTest {

 private static final String CHARSET_UTF8 = "UTF-8";
 private static final String CHARSET_GB2312 = "GB2312";
 private static final String CHARSET_INVALID = "InvalidCharset";

 @Test
 public void testLoadFromFile() throws IOException {
 File testFile = new File("path/to/your/testfile.html");
 Document doc = DataUtil.load(testFile, CHARSET_UTF8, null);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFromFileIOException() throws IOException {
 File testFile = new File("path/to/non_existent_file.html");
 assertThrows(IOException.class, () -> DataUtil.load(testFile, CHARSET_UTF8, null));
 }

 @Test
 public void testLoadFromInputStream() throws IOException {
 InputStream testStream = new ByteArrayInputStream("test data".getBytes(CHARSET_UTF8));
 Document doc = DataUtil.load(testStream, CHARSET_UTF8, null);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFromInputStreamIOException() throws IOException {
 InputStream testStream = new ByteArrayInputStream("test data".getBytes(CHARSET_UTF8));
 assertThrows(IOException.class, () -> DataUtil.load(testStream, null, null));
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/your/testfile.html"));
 Document doc = DataUtil.parseByteData(byteData, CHARSET_UTF8, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/your/testfile_gb2312.html"));
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(doc);
 assertEquals(CHARSET_GB2312, doc.outputSettings().charset());
 }

 @Test(expected = IllegalCharsetNameException.class)
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/your/testfile.html"));
 assertThrows(IllegalCharsetNameException.class, () -> DataUtil.parseByteData(byteData, CHARSET_INVALID, null, Parser.htmlParser()));
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream input = new ByteArrayInputStream("test data".getBytes(CHARSET_UTF8));
 OutputStream output = new ByteArrayOutputStream();
 DataUtil.crossStreams(input, output);
 ByteBuffer byteData = ByteBuffer.wrap(output.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, CHARSET_UTF8, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 assertEquals(CHARSET_UTF8, DataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html; charset=GB2312";
 assertEquals(CHARSET_GB2312, DataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertNotNull(boundary);
 assertEquals(boundary.length(), 32);
 }
}