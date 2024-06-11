package org.jsoup.helper;

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
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStream;

 @Before
 public void setUp() {
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 outputStream = new ByteArrayOutputStream();
 inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com/";
 Document expectedDocument = Mockito.mock(Document.class);
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

 Document actualDocument = dataUtil.load(file, charsetName, baseUri);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadInputStreamWithUtf8Charset() throws IOException {
 String htmlData = "<html><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes());
 Document expectedDocument = Mockito.mock(Document.class);
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

 Document actualDocument = dataUtil.load(inputStream, "UTF-8", "http://test.com/");
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadInputStreamWithDetectedCharset() throws IOException {
 String htmlData = "<html><head><meta charset='gb2312'></head><body></body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(htmlData.getBytes("gb2312"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document expectedDocument = Mockito.mock(Document.class);
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

 Document actualDocument = dataUtil.load(inputStream, null, "http://test.com/");
 assertEquals(expectedDocument, actualDocument);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFileName() throws IOException {
 File file = new File("test:\\test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com/";

 dataUtil.load(file, charsetName, baseUri);
 }

 @Test
 public void testParseByteDataWithUtf8Charset() throws IOException {
 String htmlData = "<html><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes("UTF-8"));
 Document expectedDocument = Mockito.mock(Document.class);
 when(parser.parseByteData(Mockito.any(ByteBuffer.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Parser.class)))
 .thenReturn(expectedDocument);

 Document actualDocument = dataUtil.parseByteData(byteData, "UTF-8", "http://test.com/", parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 String htmlData = "<html><head><meta charset='gb2312'></head><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(htmlData.getBytes("gb2312"));
 Document expectedDocument = Mockito.mock(Document.class);
 when(parser.parseByteData(Mockito.any(ByteBuffer.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Parser.class)))
 .thenReturn(expectedDocument);

 Document actualDocument = dataUtil.parseByteData(byteData, null, "http://test.com/", parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 String htmlData = "<html><body></body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(htmlData.getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer expectedByteData = ByteBuffer.wrap(htmlData.getBytes());
 ByteBuffer actualByteData = dataUtil.readToByteBuffer(inputStream);
 assertEquals(expectedByteData, actualByteData);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 String expectedCharset = "EUC-JP";
 String actualCharset = dataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNoCharset() {
 String contentType = "text/html";
 String expectedCharset = null;
 String actualCharset = dataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }
}