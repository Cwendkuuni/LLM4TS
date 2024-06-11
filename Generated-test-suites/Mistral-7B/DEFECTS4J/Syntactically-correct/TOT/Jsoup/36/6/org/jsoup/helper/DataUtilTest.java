package org.jsoup.helper;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private InputStream inputStreamMock;
 private ByteArrayOutputStream outputStreamMock;

 @Before
 public void setUp() {
 inputStreamMock = Mockito.mock(InputStream.class);
 outputStreamMock = new ByteArrayOutputStream();
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException, HelperException {
 File file = new File("test.html");
 Document document = DataUtil.load(file, null, "");
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException, HelperException {
 File file = new File("test.html");
 Document document = DataUtil.load(file, "UTF-8", "");
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException, HelperException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 Document document = DataUtil.load(inputStream, null, "");
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException, HelperException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes("UTF-8"));
 Document document = DataUtil.load(inputStream, "UTF-8", "");
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException, HelperException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 Document document = DataUtil.load(inputStream, null, "", Parser.xmlParser());
 Assert.assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteBuffer byteData = ByteBuffer.wrap("test.html".getBytes());
 Charset charset = Charset.forName(DataUtil.defaultCharset);
 Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
 Assert.assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteBuffer byteData = ByteBuffer.wrap("test.html".getBytes("UTF-8"));
 String charsetName = "UTF-8";
 Document document = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
 Assert.assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteBuffer byteData = ByteBuffer.wrap("test.html".getBytes());
 String charsetName = "GBK"; // unsupported charset
 Document document = null;
 try {
 document = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
 } catch (HelperException e) {
 // expected
 }
 Assert.assertNull(document);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Assert.assertNotNull(byteData);
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 5);
 Assert.assertEquals(5, byteData.limit());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidCharset() {
 String contentType = "text/html; charset=GBK"; // unsupported charset
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals(null, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNoCharset() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals(null, charset);
 }
}