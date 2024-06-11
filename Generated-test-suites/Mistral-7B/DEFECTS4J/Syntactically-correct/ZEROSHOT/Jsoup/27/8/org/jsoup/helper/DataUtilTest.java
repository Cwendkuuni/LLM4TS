package org.jsoup.helper;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private Charset utf8;
 private Charset gb2312;
 private Document doc;

 @Before
 public void setUp() {
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 utf8 = Charset.forName("UTF-8");
 gb2312 = Charset.forName("GB2312");
 }

 @Test
 public void testLoadFile() throws IOException, HelperException {
 File file = new File("src/test/resources/test.html");
 Document expectedDoc = DataUtil.load(file, "UTF-8", "");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(file));
 Document actualDoc = DataUtil.parseByteData(byteData, "UTF-8", "", parser);
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testLoadFileWithDetectedCharset() throws IOException, HelperException {
 File file = new File("src/test/resources/test_gb2312.html");
 Document expectedDoc = DataUtil.load(file, null, "");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(file));
 Document actualDoc = DataUtil.parseByteData(byteData, null, "", parser);
 assertEquals(expectedDoc.html(), actualDoc.html());
 // verify that the charset was detected correctly
 assertEquals("GB2312", actualDoc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStream() throws IOException, HelperException {
 InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(utf8));
 Document expectedDoc = DataUtil.load(inputStream, "UTF-8", "");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Document actualDoc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", "", parser);
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testLoadInputStreamWithDetectedCharset() throws IOException, HelperException {
 InputStream inputStream = new ByteArrayInputStream("<html><body><meta charset=\"GB2312\"></body></html>".getBytes(utf8));
 Document expectedDoc = DataUtil.load(inputStream, null, "");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Document actualDoc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, "", parser);
 assertEquals(expectedDoc.html(), actualDoc.html());
 // verify that the charset was detected correctly
 assertEquals("GB2312", actualDoc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException, HelperException {
 InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(utf8));
 Document expectedDoc = DataUtil.load(inputStream, "UTF-8", "", DataUtil.parser("htmlParser"));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Document actualDoc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", "", DataUtil.parser("htmlParser"));
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testParseByteData() throws IOException, HelperException {
 String html = "<html><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(utf8));
 Document expectedDoc = DataUtil.parseByteData(byteData, "UTF-8", "", parser);
 Document actualDoc = DataUtil.parseByteData(byteData, "UTF-8", "", parser);
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException, HelperException {
 String html = "<html><body><meta charset=\"GB2312\"></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(utf8));
 Document expectedDoc = DataUtil.parseByteData(byteData, null, "", parser);
 Document actualDoc = DataUtil.parseByteData(byteData, null, "", parser);
 assertEquals(expectedDoc.html(), actualDoc.html());
 // verify that the charset was detected correctly
 assertEquals("GB2312", actualDoc.outputSettings().charset());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 String expectedCharset = "EUC-JP";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }
}