package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://example.com";
 private static final String TEST_HTML = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";

 @Test
 public void testLoadFile() throws IOException {
  File file = new File("src/test/resources/test.html");
  Document doc = DataUtil.load(file, TEST_CHARSET, TEST_BASE_URI);
  assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileIOException() throws IOException {
  File file = new File("src/test/resources/nonexistentfile.html");
  DataUtil.load(file, TEST_CHARSET, TEST_BASE_URI);
 }

 @Test
 public void testLoadInputStream() throws IOException {
  ByteArrayOutputStream os = new ByteArrayOutputStream();
  os.write(TEST_HTML.getBytes(TEST_CHARSET));
  InputStream is = new ByteArrayInputStream(os.toByteArray());
  Document doc = DataUtil.load(is, TEST_CHARSET, TEST_BASE_URI);
  assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
  ByteArrayOutputStream os = new ByteArrayOutputStream();
  os.write(TEST_HTML.getBytes(TEST_CHARSET));
  InputStream is = new ByteArrayInputStream(os.toByteArray());
  Document doc = DataUtil.load(is, null, TEST_BASE_URI);
  assertNotNull(doc);
 }

 @Test
 public void testParseByteData() throws IOException {
  ByteArrayOutputStream os = new ByteArrayOutputStream();
  os.write(TEST_HTML.getBytes(TEST_CHARSET));
  ByteBuffer byteData = ByteBuffer.wrap(os.toByteArray());
  Document doc = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
  assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithNullCharset() throws IOException {
  ByteArrayOutputStream os = new ByteArrayOutputStream();
  os.write(TEST_HTML.getBytes(TEST_CHARSET));
  ByteBuffer byteData = ByteBuffer.wrap(os.toByteArray());
  Document doc = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
  assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
  ByteArrayOutputStream os = new ByteArrayOutputStream();
  os.write(TEST_HTML.getBytes("GBK"));
  ByteBuffer byteData = ByteBuffer.wrap(os.toByteArray());
  Document doc = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
  assertNotNull(doc);
 }

 @Test
 public void testGetCharsetFromContentType() {
  String contentType = "text/html; charset=UTF-8";
  String charset = DataUtil.getCharsetFromContentType(contentType);
  assertEquals(TEST_CHARSET, charset);

  contentType = "text/html";
  charset = DataUtil.getCharsetFromContentType(contentType);
  assertNull(charset);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
  ByteArrayOutputStream os = new ByteArrayOutputStream();
  os.write(TEST_HTML.getBytes(TEST_CHARSET));
  InputStream is = new ByteArrayInputStream(os.toByteArray());
  ByteBuffer byteData = DataUtil.readToByteBuffer(is);
  assertNotNull(byteData);
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
  ByteArrayOutputStream os = new ByteArrayOutputStream();
  os.write(TEST_HTML.getBytes(TEST_CHARSET));
  InputStream is = new ByteArrayInputStream(os.toByteArray());
  ByteBuffer byteData = DataUtil.readToByteBuffer(is, 10);
  assertNotNull(byteData);
  assertEquals(10, byteData.limit());
 }

}