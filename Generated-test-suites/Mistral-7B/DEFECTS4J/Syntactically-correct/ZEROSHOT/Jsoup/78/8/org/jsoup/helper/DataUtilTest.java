package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private static final int BUFFER_SIZE = 1024 * 32;
 private static final int FIRST_READ_BUFFER_SIZE = 1024 * 5;

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("testFile.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document document = DataUtil.load(file, charsetName, baseUri);
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 File file = new File("invalidFile.txt");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 DataUtil.load(file, charsetName, baseUri);
 fail("IOException expected");
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document document = DataUtil.load(inputStream, charsetName, baseUri);
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 DataUtil.load(inputStream, charsetName, baseUri);
 fail("IOException expected");
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Mockito.mock(Parser.class);
 Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertNotNull(document);
 }

 @Test
 public void testParseInputStreamWithInvalidInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Mockito.mock(Parser.class);
 Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertNull(document);
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes("UTF-8"));
 DataUtil.crossStreams(inputStream, outputStream);
 assertEquals("Hello World", outputStream.toString("UTF-8"));
 }

 @Test
 public void testParseInputStreamWithBomUtf32() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0x00, 0x00, 0xFE, 0xFF });
 Document document = DataUtil.parseInputStream(mock(InputStream.class), null, null, Mockito.mock(Parser.class));
 assertNull(document);
 verify(parser, never()).parseInput(anyString(), eq(null));
 }

 @Test
 public void testParseInputStreamWithBomUtf16() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0xFF, 0xFE, 0x00, 0x00 });
 Document document = DataUtil.parseInputStream(mock(InputStream.class), null, null, Mockito.mock(Parser.class));
 assertNull(document);
 verify(parser, never()).parseInput(anyString(), eq(null));
 }

 @Test
 public void testParseInputStreamWithBomUtf8() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF, 0x68, 0x65, 0x6C, 0x6C, 0x6F });
 Document document = DataUtil.parseInputStream(mock(InputStream.class), null, null, Mockito.mock(Parser.class));
 assertNull(document);
 verify(parser, never()).parseInput(anyString(), eq(null));
 }

 @Test
 public void testParseInputStreamWithValidCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF, 0x68, 0x65, 0x6C, 0x6C, 0x6F });
 String charsetName = "UTF-8";
 Document document = DataUtil.parseInputStream(mock(InputStream.class), charsetName, null, Mockito.mock(Parser.class));
 assertEquals("<html><body><h1>Hello World</h1></body></html>", document.body().html());
 verify(parser, times(1)).parseInput(eq("<html><body><h1>Hello World</h1></body></html>"), anyString());
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 byte[] data = "Hello World".getBytes("UTF-8");
 InputStream inputStream = new ByteArrayInputStream(data);
 String invalidCharsetName = "invalidCharset";
 Document document = DataUtil.parseInputStream(inputStream, invalidCharsetName, null, Mockito.mock(Parser.class));
 assertEquals("<html><body><h1>Hello World</h1></body></html>", document.body().html());
 verify(parser, times(1)).parseInput(eq("<html><body><h1>Hello World</h1></body></html>"), anyString());
 assertNotNull(document.outputSettings().charset());
 assertEquals(DataUtil.defaultCharset, document.outputSettings().charset().name());
 }

 @Test
 public void testParseInputStreamWithNoCharset() throws IOException {
 byte[] data = "Hello World".getBytes("UTF-8");
 InputStream inputStream = new ByteArrayInputStream(data);
 Document document = DataUtil.parseInputStream(inputStream, null, null, Mockito.mock(Parser.class));
 assertEquals("<html><body><h1>Hello World</h1></body></html>", document.body().html());
 verify(parser, times(1)).parseInput(eq("<html><body><h1>Hello World</h1></body></html>"), anyString());
 assertNotNull(document.outputSettings().charset());
 assertEquals(DataUtil.defaultCharset, document.outputSettings().charset().name());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes("UTF-8"));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 assertEquals(13L, byteBuffer.limit());
 byte[] expectedData = new byte[13];
 inputStream.read(expectedData);
 byteBuffer.get(expectedData);
 assertArrayEquals(expectedData, byteBuffer.array());
 }

 @Test
 public void testReadToByteBufferWithInvalidInputStream() throws IOException {
 InputStream inputStream = mock(InputStream.class);
 when(inputStream.read()).thenThrow(new IOException());
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 assertNull(byteBuffer);
 }

 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File file = new File("testFile.html");
 ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
 assertEquals(DataUtil.firstReadBufferSize, byteBuffer.limit());
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0L, byteBuffer.limit());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testValidateCharset() {
 String cs = "UTF-8";
 String validatedCharset = DataUtil.validateCharset(cs);
 assertEquals("UTF-8", validatedCharset);
 }

 @Test
 public void testValidateCharsetWithInvalidCharsetName() {
 String cs = "invalidCharset";
 String validatedCharset = DataUtil.validateCharset(cs);
 assertNull(validatedCharset);
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(DataUtil.boundaryLength, boundary.length());
 }
}