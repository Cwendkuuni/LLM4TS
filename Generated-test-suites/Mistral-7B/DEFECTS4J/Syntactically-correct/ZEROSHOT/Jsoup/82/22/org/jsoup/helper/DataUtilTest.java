package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://example.com";
 private static final String TEST_XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 private static final String TEST_XML_CONTENT = "<html><body>Test</body></html>";
 private static final String TEST_CONTENT_TYPE = "text/html; charset=UTF-8";
 private static final String TEST_CONTENT_TYPE_NO_CHARSET = "text/html";

 @Mock
 private Parser parser;

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("test.html");
 Document document = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
 assertSame(testFile, document.inputEncoding().file());
 }

 @Test(expected = IOException.class)
 public void testLoadFileNullFile() throws IOException {
 DataUtil.load(null, TEST_CHARSET, TEST_BASE_URI);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);
 assertEquals(TEST_XML_CONTENT, document.html());
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamNullInput() throws IOException {
 DataUtil.load(null, TEST_CHARSET, TEST_BASE_URI);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI, parser);
 assertEquals(TEST_XML_CONTENT, document.html());
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(testInputStream, testOutputStream);
 assertEquals(TEST_XML_CONTENT, testOutputStream.toString(StandardCharsets.UTF_8.name()));
 }

 @Test
 public void testParseInputStream() throws IOException {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(testInputStream, testOutputStream);
 testInputStream.close();

 Document document = DataUtil.parseInputStream(testOutputStream, TEST_CHARSET, TEST_BASE_URI, parser);
 assertEquals(TEST_XML_CONTENT, document.html());
 }

 @Test
 public void testParseInputStreamWithNoCharset() throws IOException {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(testInputStream, testOutputStream);
 testInputStream.close();

 Document document = DataUtil.parseInputStream(testOutputStream, null, TEST_BASE_URI, parser);
 assertEquals(TEST_XML_CONTENT, document.html());
 }

 @Test
 public void testParseInputStreamWithContentType() throws IOException {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(testInputStream, testOutputStream);
 testInputStream.close();

 when(parser.parseInput(TEST_XML_CONTENT, TEST_BASE_URI)).thenReturn(mock(Document.class));
 Document document = DataUtil.parseInputStream(testOutputStream, TEST_CONTENT_TYPE, TEST_BASE_URI, parser);
 assertSame(document, parser.parseInput(TEST_XML_CONTENT, TEST_BASE_URI));
 }

 @Test
 public void testParseInputStreamWithContentTypeNoCharset() throws IOException {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(testInputStream, testOutputStream);
 testInputStream.close();

 when(parser.parseInput(TEST_XML_CONTENT, TEST_BASE_URI)).thenReturn(mock(Document.class));
 Document document = DataUtil.parseInputStream(testOutputStream, TEST_CONTENT_TYPE_NO_CHARSET, TEST_BASE_URI, parser);
 assertSame(document, parser.parseInput(TEST_XML_CONTENT, TEST_BASE_URI));
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0, emptyByteBuffer.limit());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, TEST_XML_CONTENT.length());
 assertEquals(TEST_XML_CONTENT.length(), byteBuffer.limit());
 }

 @Test
 public void testReadToByteBufferNoMaxSize() throws IOException {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 0);
 assertEquals(TEST_XML_CONTENT.length(), byteBuffer.limit());
 }

 @Test
 public void testReadToByteBufferInterrupted() throws IOException, InterruptedException {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_XML_CONTENT.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 10);
 Thread thread = new Thread(() -> {
 try {
 byteBuffer.get(10);
 } catch (IOException e) {
 // ignore
 }
 });
 thread.start();
 thread.join(100);
 testInputStream.close();
 assertEquals(10, byteBuffer.limit());
 }

 @Test
 public void testgetCharsetFromContentType() {
 assertEquals(TEST_CHARSET, DataUtil.getCharsetFromContentType(TEST_CONTENT_TYPE));
 assertNull(DataUtil.getCharsetFromContentType(TEST_CONTENT_TYPE_NO_CHARSET));
 }

 @Test
 public void testValidateCharset() {
 assertEquals(TEST_CHARSET, DataUtil.validateCharset(TEST_CHARSET));
 assertNull(DataUtil.validateCharset(""));
 assertNull(DataUtil.validateCharset("InvalidCharset"));
 }

 @Test
 public void testMimeBoundary() {
 assertEquals(32, DataUtil.mimeBoundary().length());
 }

 @Test
 public void testDetectCharsetFromBom() {
 ByteBuffer utf32Bom = ByteBuffer.wrap(new byte[] { 0, 0, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF });
 BomCharset utf32BomCharset = DataUtil.detectCharsetFromBom(utf32Bom);
 assertEquals("UTF-32", utf32BomCharset.charset);
 assertEquals(false, utf32BomCharset.offset);

 ByteBuffer utf8Bom = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
 BomCharset utf8BomCharset = DataUtil.detectCharsetFromBom(utf8Bom);
 assertEquals("UTF-8", utf8BomCharset.charset);
 assertEquals(true, utf8BomCharset.offset);
 }
}