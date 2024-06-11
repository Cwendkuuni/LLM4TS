import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

 // Test for load method with File, valid charset and baseUri
 @Test
 public void testLoadFileValid() throws IOException {
 Document expectedDocument = parseExampleHtmlFile("example.html");
 Document actualDocument = DataUtil.load(new File("src/test/resources/example.html"), "UTF-8", "/");
 Assert.assertNotNull(actualDocument);
 Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());
 }

 // Test for load method with File, invalid charset and baseUri
 @Test(expected = IOException.class)
 public void testLoadFileInvalidCharset() throws IOException {
 DataUtil.load(new File("src/test/resources/example.html"), "UTF-16", "/");
 }

 // Test for load method with InputStream, valid charset and baseUri
 @Test
 public void testLoadInputStreamValid() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 this.writeExampleHtmlToStream(byteArrayOutputStream);
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Document expectedDocument = parseExampleHtmlFile("example.html");
 Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/");
 Assert.assertNotNull(actualDocument);
 Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());
 }

 // Test for load method with InputStream, invalid charset and baseUri
 @Test(expected = IOException.class)
 public void testLoadInputStreamInvalidCharset() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 this.writeExampleHtmlToStream(byteArrayOutputStream);
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 DataUtil.load(inputStream, "UTF-16", "/");
 }

 // Test for load method with InputStream, valid charset, baseUri and custom parser
 @Test
 public void testLoadInputStreamCustomParser() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 this.writeExampleHtmlToStream(byteArrayOutputStream);
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Document expectedDocument = parseExampleHtmlFile("example.html", Parser.xmlParser());
 Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/", Parser.xmlParser());
 Assert.assertNotNull(actualDocument);
 Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());
 }

 // Test for crossStreams method
 @Test
 public void testCrossStreams() throws IOException {
 final InputStream in = new FileInputStream("src/test/resources/example.html");
 final OutputStream out = new ByteArrayOutputStream();
 DataUtil.crossStreams(in, out);
 Assert.assertNotNull(out.toByteArray());
 }

 // Test for parseByteData method with valid charset and baseUri
 @Test
 public void testParseByteDataValid() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 this.writeExampleHtmlToStream(byteArrayOutputStream);
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Document expectedDocument = parseExampleHtmlFile("example.html");
 Document actualDocument = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", "/", Parser.htmlParser());
 Assert.assertNotNull(actualDocument);
 Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());
 }

 // Test for parseByteData method with invalid charset and baseUri
 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataInvalidCharset() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 DataUtil.parseByteData(byteData, null, "/", Parser.htmlParser());
 }

 // Test for parseByteData method with valid charset, baseUri and custom parser
 @Test
 public void testParseByteDataCustomParser() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 this.writeExampleHtmlToStream(byteArrayOutputStream);
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Document expectedDocument = parseExampleHtmlFile("example.html", Parser.xmlParser());
 Document actualDocument = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", "/", Parser.xmlParser());
 Assert.assertNotNull(actualDocument);
 Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());
 }

 // Test for readToByteBuffer method with InputStream and maxSize
 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 this.writeExampleHtmlToStream(byteArrayOutputStream);
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 ByteBuffer expectedByteBuffer = DataUtil.readToByteBuffer(inputStream, bufferSize);
 ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, bufferSize * 2);
 Assert.assertArrayEquals(expectedByteBuffer.array(), actualByteBuffer.array());
 }

 // Test for readToByteBuffer method with InputStream and maxSize = 0
 @Test
 public void testReadToByteBufferUnlimited() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 this.writeExampleHtmlToStream(byteArrayOutputStream);
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 ByteBuffer expectedByteBuffer = DataUtil.readToByteBuffer(inputStream);
 ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
 Assert.assertArrayEquals(expectedByteBuffer.array(), actualByteBuffer.array());
 }

 // Test for readFileToByteBuffer method
 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File file = new File("src/test/resources/example.html");
 ByteBuffer expectedByteBuffer = DataUtil.readFileToByteBuffer(file);
 ByteBuffer actualByteBuffer = DataUtil.readFileToByteBuffer(file);
 Assert.assertArrayEquals(expectedByteBuffer.array(), actualByteBuffer.array());
 }

 // Test for emptyByteBuffer method
 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer expectedByteBuffer = DataUtil.emptyByteBuffer();
 ByteBuffer actualByteBuffer = DataUtil.emptyByteBuffer();
 Assert.assertArrayEquals(expectedByteBuffer.array(), actualByteBuffer.array());
 }

 // Test for getCharsetFromContentType method
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals(expectedCharset, actualCharset);
 }

 // Test for getCharsetFromContentType method with invalid charset
 @Test(expected = IllegalArgumentException.class)
 public void testGetCharsetFromContentTypeInvalid() {
 String contentType = "text/html; charset=UTF-16";
 DataUtil.getCharsetFromContentType(contentType);
 }

 // Test for mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String expectedMimeBoundary = "JvLq+Fg4QrNm9bQ5";
 String actualMimeBoundary = DataUtil.mimeBoundary();
 Assert.assertEquals(expectedMimeBoundary, actualMimeBoundary);
 }
}