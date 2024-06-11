package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

 private static final String TEST_FILE_PATH = "test.html";

 @org.junit.Test
 public void testLoadFile() throws IOException, HelperException {
 File file = new File(TEST_FILE_PATH);
 Document doc = DataUtil.load(file, "UTF-8", null);
 Assert.assertNotNull(doc);
 }

 @org.junit.Test(expected = FileNotFoundException.class)
 public void testLoadNonExistentFile() throws IOException, HelperException {
 Document doc = DataUtil.load(new File("nonexistentfile.html"), null, null);
 }

 @org.junit.Test
 public void testLoadInputStream() throws IOException, HelperException {
 InputStream input = new FileInputStream(new File(TEST_FILE_PATH));
 Document doc = DataUtil.load(input, "UTF-8", null);
 Assert.assertNotNull(doc);
 }

 @org.junit.Test(expected = IOException.class)
 public void testLoadInputStreamWithNullCharset() throws IOException, HelperException {
 Document doc = DataUtil.load(new ByteArrayInputStream(new byte[0]), null, null);
 }

 @org.junit.Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException, HelperException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Invalid charset test".getBytes("UTF-16LE"));
 InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(input, "UTF-16BE", null);
 Assert.assertNotNull(doc);
 }

 @org.junit.Test
 public void testParseByteData() throws IOException, HelperException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test data".getBytes("UTF-8"));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @org.junit.Test
 public void testParseByteDataWithNullCharset() throws IOException, HelperException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testParseByteDataWithNullParser() throws IOException, HelperException {
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(new byte[0]), null, null, null);
 }

 @org.junit.Test
 public void testDetermineCharset() throws IOException, HelperException {
 String contentType = "text/html; charset=UTF-8";
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap("Test data".getBytes("UTF-8")), null, null, Parser.htmlParser());
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);

 contentType = "text/html";
 doc = DataUtil.parseByteData(ByteBuffer.wrap("Test data".getBytes("UTF-8")), null, null, Parser.htmlParser());
 charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }
}