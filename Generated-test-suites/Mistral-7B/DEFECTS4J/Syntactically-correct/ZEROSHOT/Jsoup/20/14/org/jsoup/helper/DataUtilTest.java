package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private Document document;

 @Before
 public void setup() {
 dataUtil = new DataUtil();
 parser = Mockito.mock(Parser.class);
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("testFile.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 InputStream inputStream = new FileInputStream(file);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[bufferSize];
 int read;
 while ((read = inputStream.read(buffer)) != -1) {
 outputStream.write(buffer, 0, read);
 }
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document expectedDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 document = dataUtil.load(file, charsetName, baseUri);
 assertEquals(expectedDocument, document);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document expectedDocument = dataUtil.parseByteData(ByteBuffer.wrap(inputStream), charsetName, baseUri, parser);
 document = dataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(expectedDocument, document);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Parser parser = Mockito.mock(Parser.class);
 Document expectedDocument = dataUtil.parseByteData(ByteBuffer.wrap(inputStream), charsetName, baseUri, parser);
 Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);
 document = dataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, document);
 }

 @Test
 public void testParseByteData() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 String docData = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
 Document expectedDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 Document document = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDocument, document);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 String charsetName = null;
 String baseUri = "http://test.com";
 String docData = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body</html>";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
 Document expectedDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 Document document = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDocument, document);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 String charsetName = "invalidCharset";
 String baseUri = "http://test.com";
 String docData = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body</html>";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
 Document expectedDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 Document document = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDocument, document);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes("UTF-8"));
 ByteBuffer expectedByteData = ByteBuffer.wrap("testData".getBytes("UTF-8"));
 ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
 assertEquals(expectedByteData, byteData);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = dataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 String charset = dataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }
}