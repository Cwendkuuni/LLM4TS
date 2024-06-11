package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 private InputStream inputStream;
 private Document document;
 private Parser parser;

 @Before
 public void setUp() {
 inputStream = Mockito.mock(InputStream.class);
 parser = Mockito.mock(Parser.class);
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File file = new File("test.html");
 String charsetName = null;
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(file, charsetName, baseUri);
 assertNotNull(doc);
 // Verify that parseByteData method is called with UTF-8 charset
 ArgumentCaptor<ByteBuffer> byteDataCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
 Mockito.verify(parser).parseByteData(byteDataCaptor.capture(), eq(charsetName), eq(baseUri), eq(Parser.htmlParser()));
 assertEquals(Charset.forName("UTF-8").encode("Test content"), byteDataCaptor.getValue().asIntBuffer().array());
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File file = new File("test.html");
 String charsetName = "UTF-16";
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(file, charsetName, baseUri);
 assertNotNull(doc);
 // Verify that parseByteData method is called with UTF-16 charset
 ArgumentCaptor<ByteBuffer> byteDataCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
 Mockito.verify(parser).parseByteData(byteDataCaptor.capture(), eq(charsetName), eq(baseUri), eq(Parser.htmlParser()));
 assertEquals(Charset.forName("UTF-16").encode("Test content"), byteDataCaptor.getValue().asIntBuffer().array());
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes());
 InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
 String charsetName = null;
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(in, charsetName, baseUri);
 assertNotNull(doc);
 // Verify that parseByteData method is called with UTF-8 charset (default)
 ArgumentCaptor<ByteBuffer> byteDataCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
 Mockito.verify(parser).parseByteData(byteDataCaptor.capture(), eq(charsetName), eq(baseUri), eq(Parser.htmlParser()));
 assertEquals(Charset.forName("UTF-8").encode("Test content"), byteDataCaptor.getValue().asIntBuffer().array());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes("UTF-16"));
 InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
 String charsetName = "UTF-16";
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(in, charsetName, baseUi);
 assertNotNull(doc);
 // Verify that parseByteData method is called with UTF-16 charset
 ArgumentCaptor<ByteBuffer> byteDataCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
 Mockito.verify(parser).parseByteData(byteDataCaptor.capture(), eq(charsetName), eq(baseUri), eq(Parser.htmlParser()));
 assertEquals(Charset.forName("UTF-16").encode("Test content"), byteDataCaptor.getValue().asIntBuffer().array());
 }

 @Test
 public void testDetectCharsetFromContentType() {
 String contentType = "text/html; charset=gb2312";
 String charsetName = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("gb2312", charsetName);
 }

 @Test
 public void testDetectCharsetFromContentTypeWithUnknownCharset() {
 String contentType = "text/html; charset=x-unknown";
 String charsetName = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charsetName);
 }

 @Test
 public void testDetectCharsetFromContentTypeWithNoCharset() {
 String contentType = "text/html";
 String charsetName = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charsetName);
 }
}