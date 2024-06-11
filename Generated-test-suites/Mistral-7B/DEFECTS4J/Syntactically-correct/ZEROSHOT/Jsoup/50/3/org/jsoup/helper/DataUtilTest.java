package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

 private static final String TEST_FILE_PATH = "test.html";
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://test.com";
 private static final String TEST_MIME_BOUNDARY = "test-boundary";
 private static final int TEST_BUFFER_SIZE = 1024;

 @BeforeClass
 public static void setUp() throws IOException {
 File file = new File(TEST_FILE_PATH);
 if (!file.exists()) {
 FileUtils.copyFile(new File("test.html"), file);
 }
 }

 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(new File(TEST_FILE_PATH), TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new FileInputStream(TEST_FILE_PATH), outputStream);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File(TEST_FILE_PATH));
 Document doc = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);

 contentType = "text/html";
 charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(boundary.length(), DataUtil.boundaryLength);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 File file = new File(TEST_FILE_PATH);
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
 assertNotNull(byteData);
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(byteBuffer.limit(), 0);
 }
}