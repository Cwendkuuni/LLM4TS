import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 // Test for load(File) method
 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("test.html"); // Assuming test.html is present in the project directory
 Document document = DataUtil.load(testFile, "UTF-8", null);
 Assert.assertNotNull(document);
 }

 // Test for load(InputStream) method with default charset
 @Test
 public void testLoadInputStreamDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testString = "Test String";
 outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, null, null);
 Assert.assertEquals(testString, document.text());
 }

 // Test for load(InputStream) method with specified charset
 @Test
 public void testLoadInputStreamSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testString = "Test String";
 outputStream.write(testString.getBytes(StandardCharsets.ISO_8859_1));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, "ISO-8859-1", null);
 Assert.assertEquals(testString, document.text());
 }

 // Test for load(InputStream, String, String, Parser) method
 @Test
 public void testLoadInputStreamParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testString = "<html><body><h1>Test String</h1></body></html>";
 outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
 Assert.assertEquals(testString, document.html());
 }

 // Test for crossStreams() method
 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 DataUtil.crossStreams(inputStream, outputStream);
 String testString = "Test String";
 outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
 Assert.assertEquals(testString, new String(outputStream.toByteArray()));
 }

 // Test for parseByteData() method
 @Test
 public void testParseByteData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testString = "Test String";
 outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 Assert.assertEquals(testString, document.text());
 }

 // Test for readToByteBuffer() method with cap
 @Test
 public void testReadToByteBufferCap() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testString = "Test String";
 outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
 Assert.assertEquals(10, byteBuffer.limit());
 }

 // Test for readToByteBuffer() method without cap
 @Test
 public void testReadToByteBufferNoCap() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testString = "Test String";
 outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 Assert.assertFalse(byteBuffer.hasRemaining());
 }

 // Test for readFileToByteBuffer() method
 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File testFile = new File("test.html"); // Assuming test.html is present in the project directory
 ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
 Assert.assertFalse(byteBuffer.hasRemaining());
 }

 // Test for getCharsetFromContentType() method
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 // Test for mimeBoundary() method
 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 Assert.assertNotNull(boundary);
 }
}