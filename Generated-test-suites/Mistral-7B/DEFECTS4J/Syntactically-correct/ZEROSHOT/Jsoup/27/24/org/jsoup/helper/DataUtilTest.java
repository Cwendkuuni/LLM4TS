package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

 private InputStream inputStreamMock;
 private File fileMock;

 @Before
 public void setUp() {
 inputStreamMock = mock(InputStream.class);
 fileMock = mock(File.class);
 }

 @Test
 public void testLoadWithFileAndValidCharset() throws IOException {
 String charsetName = "UTF-8";
 File file = new File("test.html");
 Document document = DataUtil.load(file, charsetName, "");
 assertNotNull(document);
 }

 @Test
 public void testLoadWithFileAndNullCharset() throws IOException {
 File file = new File("test.html");
 Document document = DataUtil.load(file, null, "");
 assertNotNull(document);
 }

 @Test
 public void testLoadWithInputStreamAndValidCharset() throws IOException {
 String charsetName = "UTF-8";
 InputStream inputStream = new ByteArrayInputStream("test content".getBytes(Charset.forName(charsetName)));
 Document document = DataUtil.load(inputStream, charsetName, "");
 assertNotNull(document);
 }

 @Test
 public void testLoadWithInputStreamAndNullCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
 Document document = DataUtil.load(inputStream, null);
 assertNotNull(document);
 }

 @Test
 public void testLoadWithInputStreamAndSpecifiedParser() throws IOException {
 String charsetName = "UTF-8";
 InputStream inputStream = new ByteArrayInputStream("test content".getBytes(Charset.forName(charsetName)));
 Parser parser = mock(Parser.class);
 when(parser.parseInput("test content", "")).thenReturn(new Document());
 Document document = DataUtil.load(inputStream, charsetName, "", parser);
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithValidCharset() {
 String charsetName = "UTF-8";
 ByteBuffer byteData = ByteBuffer.wrap("test content".getBytes(Charset.forName(charsetName)));
 Document document = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithNullCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("test content".getBytes());
 Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithDetectedCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("text/html; charset=gb2312\r\n\r\ntest content".getBytes());
 Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
 assertNotNull(document);
 assertEquals("gb2312", document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("text/html; charset=invalidCharset\r\n\r\ntest content".getBytes());
 Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
 assertNull(document);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 byte[] buffer = new byte[bufferSize];
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] expectedBuffer = "test readToByteBuffer".getBytes();
 when(inputStreamMock.read(buffer)).thenReturn(expectedBuffer.length);
 when(inputStreamMock.read(buffer, 0, expectedBuffer.length)).thenReturn(expectedBuffer.length, -1);
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStreamMock);
 byte[] actualBuffer = byteData.array();
 assertArrayEquals(expectedBuffer, actualBuffer);
 }

 @Test
 public void testReadToByteBufferWithEOF() throws IOException {
 byte[] buffer = new byte[bufferSize];
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(inputStreamMock.read(buffer)).thenReturn(0);
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStreamMock);
 assertEquals(0, byteData.limit());
 }

 @Test
 public void testGetCharsetFromContentTypeWithValidCharset() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithMissingCharset() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

}