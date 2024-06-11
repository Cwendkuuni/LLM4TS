package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;

public class DataUtilTest {

 private InputStream inputStream;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 inputStream = mock(InputStream.class);
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File file = new File("test.html");
 Document doc = DataUtil.load(file, null, "");
 assertSame("Document returned should match expected", expectedDocument("test.html"), doc);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File file = new File("test.html");
 Document doc = DataUtil.load(file, "UTF-16", "");
 assertSame("Document returned should match expected", expectedDocument("test.html_UTF-16"), doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 Document doc = DataUtil.load(inputStream, null, "");
 assertSame("Document returned should match expected", expectedDocument("test.html"), doc);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 Document doc = DataUtil.load(inputStream, "UTF-16", "");
 assertSame("Document returned should match expected", expectedDocument("test.html_UTF-16"), doc);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedParser() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 Parser parser = mock(Parser.class);
 when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument("test.html"));
 Document doc = DataUtil.load(inputStream, null, "", parser);
 assertSame("Document returned should match expected", expectedDocument("test.html"), doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteBuffer byteBuffer = ByteBuffer.wrap(expectedByteData("test.html").getBytes());
 Document doc = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
 assertSame("Document returned should match expected", expectedDocument("test.html"), doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 ByteBuffer byteBuffer = ByteBuffer.wrap(expectedByteData("test.html_UTF-16").getBytes());
 Document doc = DataUtil.parseByteData(byteBuffer, "UTF-16", "", Parser.htmlParser());
 assertSame("Document returned should match expected", expectedDocument("test.html_UTF-16"), doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedParser() throws IOException {
 ByteBuffer byteBuffer = ByteBuffer.wrap(expectedByteData("test.html").getBytes());
 Parser parser = mock(Parser.class);
 when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument("test.html"));
 Document doc = DataUtil.parseByteData(byteBuffer, null, "", parser);
 assertSame("Document returned should match expected", expectedDocument("test.html"), doc);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 byte[] inputData = "test.html".getBytes();
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, inputData.length);
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap(inputData);
 assertSame("ByteBuffers should be equal", expectedByteBuffer, byteBuffer);
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 byte[] inputData = "test.html".getBytes();
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, inputData.length - 5);
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(inputData, 0, inputData.length - 5));
 assertSame("ByteBuffers should be equal", expectedByteBuffer, byteBuffer);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertSame("Charset returned should match expected", expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNullContentType() {
 assertNull("Charset returned should be null", DataUtil.getCharsetFromContentType(null));
 }

 private static Document expectedDocument(String fileName) {
 // ...
 }

 private static ByteBuffer expectedByteData(String fileName) {
 // ...
 }
}