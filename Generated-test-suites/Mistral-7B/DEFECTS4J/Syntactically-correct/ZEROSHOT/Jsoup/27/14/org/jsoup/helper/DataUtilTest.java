package org.jsoup.helper.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 private static final String TEST_FILE_PATH = "test.html"; // Replace with the path to your test file
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "https://example.com/";

 @BeforeClass
 public static void setup() {
 File testFile = new File(TEST_FILE_PATH);
 // You can create a test file with the content you want to test, or use an existing one
 }

 @Test
 public void testLoadFile() throws IOException {
 Document document = DataUtil.load(new File(TEST_FILE_PATH), TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 File testFile = new File(TEST_FILE_PATH);
 FileInputStream inStream = new FileInputStream(testFile);
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[bufferSize];
 int read;
 while ((read = inStream.read(buffer)) != -1) {
 outStream.write(buffer, 0, read);
 }
 ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
 Document document = DataUtil.load(new ByteArrayInputStream(outStream.toByteArray()), TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(document);
 inStream.close();
 }

 @Test
 public void testParseByteData() throws IOException {
 File testFile = new File(TEST_FILE_PATH);
 FileInputStream inStream = new FileInputStream(testFile);
 ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
 Document document = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(document);
 inStream.close();
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNotNull(charset);
 assertNotNull(charset.equals("EUC-JP"));
 }
}