package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class)

public class DataUtilTest {

 @BeforeClass
 public static void setup() {
 MockitoAnnotations.initMocks(DataUtilTest.class);
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("testfile.html");
 String charsetName = "UTF-8";
 String baseUri = "https://www.example.com";
 Document expectedDocument = mock(Document.class);
 when(expectedDocument.outputSettings().charset()).thenReturn(charsetName);
 PowerMockito.mockStatic(Parser.class);
 when(Parser.htmlParser()).thenReturn(mock(Parser.class));
 when(Parser.htmlParser().parseInput(anyString(), eq(baseUri))).thenReturn(expectedDocument);

 InputStream inputStream = PowerMockito.mock(InputStream.class);
 when(file.exists()).thenReturn(true);
 when(file.isFile()).thenReturn(true);
 when(file.length()).thenReturn(1024);
 when(inputStream.available()).thenReturn((int) file.length());
 when(file.getInputStream()).thenReturn(inputStream);

 Document actualDocument = DataUtil.load(file, charsetName, baseUri);
 assertNotNull(actualDocument);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 String charsetName = "UTF-8";
 String baseUri = "https://www.example.com";
 Document expectedDocument = mock(Document.class);
 when(expectedDocument.outputSettings().charset()).thenReturn(charsetName);
 PowerMockito.mockStatic(Parser.class);
 when(Parser.htmlParser()).thenReturn(mock(Parser.class));
 when(Parser.htmlParser().parseInput(anyString(), eq(baseUri))).thenReturn(expectedDocument);

 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
 assertNotNull(actualDocument);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 String charsetName = "InvalidCharset";
 String baseUri = "https://www.example.com";

 DataUtil.load(inputStream, charsetName, baseUri);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expectedCharset = "UTF-8";
 String expectedBaseUri = "https://www.example.com";
 outputStream.write("Test content".getBytes(Charset.forName(expectedCharset)));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document expectedDocument = mock(Document.class);
 when(expectedDocument.outputSettings().charset()).thenReturn(expectedCharset);
 PowerMockito.mockStatic(Parser.class);
 when(Parser.htmlParser()).thenReturn(mock(Parser.class));
 when(Parser.htmlParser().parseInput(anyString(), eq(expectedBaseUri))).thenReturn(expectedDocument);

 Document actualDocument = DataUtil.parseByteData(byteData, null, expectedBaseUri, Parser.htmlParser());
 assertNotNull(actualDocument);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expectedCharset = "UTF-8";
 String expectedBaseUri = "https://www.example.com";
 String charsetName = "UTF-8";
 outputStream.write("Test content".getBytes(Charset.forName(expectedCharset)));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document expectedDocument = mock(Document.class);
 when(expectedDocument.outputSettings().charset()).thenReturn(charsetName);
 PowerMockito.mockStatic(Parser.class);
 when(Parser.htmlParser()).thenReturn(mock(Parser.class));
 when(Parser.htmlParser().parseInput(anyString(), eq(expectedBaseUri))).thenReturn(expectedDocument);

 Document actualDocument = DataUtil.parseByteData(byteData, charsetName, expectedBaseUri, Parser.htmlParser());
 assertNotNull(actualDocument);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String expectedBaseUri = "https://www.example.com";
 outputStream.write("Test content".getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document expectedDocument = mock(Document.class);
 PowerMockito.mockStatic(Parser.class);
 when(Parser.htmlParser()).thenReturn(mock(Parser.class));
 when(Parser.htmlParser().parseInput(anyString(), eq(expectedBaseUri))).thenReturn(expectedDocument);

 Document actualDocument = DataUtil.parseByteData(byteData, null, expectedBaseUri, Parser.htmlParser());
 assertNotNull(actualDocument);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNullContentType() {
 String actualCharset = DataUtil.getCharsetFromContentType(null);
 assertNull(actualCharset);
 }
}