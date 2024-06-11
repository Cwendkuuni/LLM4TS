package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 @Mock
 private Parser parser;
 @Mock
 private Document document;
 @Mock
 private Element meta;

 @Before
 public void setUp() {
 dataUtil = new DataUtil();
 Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 Document document = dataUtil.load(file, null, null);
 assertEquals("test.html", document.title());
 }

 @Test
 public void testLoadFileWithCharset() throws IOException {
 File file = new File("test.html");
 Document document = dataUtil.load(file, "UTF-8", null);
 assertEquals("test.html", document.title());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes(StandardCharsets.UTF_8));
 Document document = dataUtil.load(inputStream, null, null);
 assertEquals("test.html", document.title());
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes(StandardCharsets.UTF_8));
 Document document = dataUtil.load(inputStream, "UTF-8", null);
 assertEquals("test.html", document.title());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes(StandardCharsets.UTF_8));
 Document document = dataUtil.load(inputStream, null, null, parser);
 assertEquals("test.html", document.title());
 assertSame(parser, document.parser());
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test.html".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = dataUtil.parseByteData(ByteBuffer.wrap(outputStream.toByteArray()), null, null, parser);
 assertEquals("test.html", document.title());
 }

 @Test
 public void testParseByteDataWithCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test.html".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = dataUtil.parseByteData(ByteBuffer.wrap(outputStream.toByteArray()), "UTF-8", null, parser);
 assertEquals("test.html", document.title());
 }

 @Test
 public void testParseByteDataWithMetaCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head><meta charset=\"UTF-16\"><title>test</title></head><body>test</body></html>".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = dataUtil.parseByteData(ByteBuffer.wrap(outputStream.toByteArray()), null, null, parser);
 assertEquals("test", document.title());
 assertNull(document.outputSettings().charset());
 assertEquals("UTF-16", dataUtil.getCharsetFromContentType("<meta charset=\"UTF-16\""));
 document = dataUtil.parseByteData(ByteBuffer.wrap(outputStream.toByteArray()), null, null, parser);
 assertEquals("test", document.title());
 assertSame(StandardCharsets.UTF_16, document.outputSettings().charset());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 assertEquals("EUC-JP", dataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html";
 assertNull(dataUtil.getCharsetFromContentType(contentType));
 }
}