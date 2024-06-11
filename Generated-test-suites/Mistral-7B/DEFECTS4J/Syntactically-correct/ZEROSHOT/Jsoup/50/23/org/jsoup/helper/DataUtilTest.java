package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private Document document;
 private Random random;
 private ByteArrayOutputStream outputStream;
 private InputStream inputStream;
 private File file;

 @Before
 public void setup() {
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 document = mock(Document.class);
 random = new Random();
 outputStream = new ByteArrayOutputStream();
 inputStream = new ByteArrayInputStream(new byte[0]);
 file = new File("testfile.html");
 }

 @Test
 public void testLoadFile() throws IOException {
 // given
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 File file = new File("testfile.html");
 String expectedContent = "Test content";
 ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
 byteOutputStream.write(expectedContent.getBytes());
 File testFile = new File(byteOutputStream.toByteArray());
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

 // when
 Document actualDocument = dataUtil.load(testFile, charsetName, baseUri);

 // then
 assertNotNull(actualDocument);
 assertEquals(expectedContent, actualDocument.body().html());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 // given
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
 byteOutputStream.write("Test content".getBytes());
 InputStream inputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

 // when
 Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);

 // then
 assertNotNull(actualDocument);
 assertEquals("Test content", actualDocument.body().html());
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithException() throws IOException {
 // given
 InputStream inputStream = Mockito.mock(InputStream.class);
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenThrow(new IOException());

 // when, then
 dataUtil.load(inputStream, "UTF-8", null);
 }

 @Test
 public void testParseByteData() {
 // given
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 String expectedContent = "Test content";
 ByteBuffer byteData = ByteBuffer.wrap(expectedContent.getBytes());
 when(parser.parseByteData(Mockito.any(ByteBuffer.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Parser.class)))
 .thenReturn(document);

 // when
 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

 // then
 assertNotNull(actualDocument);
 assertEquals(expectedContent, actualDocument.body().html());
 }

 @Test
 public void testGetCharsetFromContentType() {
 // given
 String contentType = "text/html; charset=UTF-8";

 // when
 String actualCharset = dataUtil.getCharsetFromContentType(contentType);

 // then
 assertEquals("UTF-8", actualCharset);
 }

 @Test
 public void testGetCharsetFromContentTypeEmpty() {
 // given
 String contentType = "text/html";

 // when
 String actualCharset = dataUtil.getCharsetFromContentType(contentType);

 // then
 assertNull(actualCharset);
 }

 @Test
 public void testMimeBoundary() {
 // given
 String expectedBoundary = "test-boundary-12345";

 // when
 String actualBoundary = dataUtil.mimeBoundary();
 // then
 assertEquals(expectedBoundary, actualBoundary.substring(0, expectedBoundary.length() - 1));
 }

 @Test
 public void testCrossStreams() throws IOException {
 // given
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 OutputStream outputStream = mock(OutputStream.class);
ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);

 // when
 dataUtil.crossStreams(inputStream, outputStream);
 // then
 Mockito.verify(outputStream).write(Mockito.anyByte());
 }
}