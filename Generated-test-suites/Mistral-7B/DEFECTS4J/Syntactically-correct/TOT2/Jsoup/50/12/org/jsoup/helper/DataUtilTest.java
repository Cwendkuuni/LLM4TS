import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 // Test for load(File, String, String) method
 @Test
 public void testLoadFile() throws IOException {
 Document expectedDoc = Parser.htmlParser().parseInput("<html><body></body></html>", null);
 File testFile = new File("testFile.html");
 testFile.createNewFile(); // create test file for testing
 try (InputStream outputStream = new FileOutputStream(testFile);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
 byteStream.write("<html><body></body></html>".getBytes());
 outputStream.write(byteStream.toByteArray());
 }

 Document actualDoc = DataUtil.load(testFile, "UTF-8", "/baseUri");

 Assert.assertEquals(expectedDoc, actualDoc);

 // Clean up test file
 testFile.delete();
 }

 // Test for load(InputStream, String, String) method
 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes());
 Document actualDoc = DataUtil.load(inputStream, "UTF-8", "/baseUri");

 Document expectedDoc = Parser.htmlParser().parseInput("<html><body></body></html>", null);

 Assert.assertEquals(expectedDoc, actualDoc);
 }

 // Test for load(InputStream, String, String, Parser) method
 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes());
 Document actualDoc = DataUtil.load(inputStream, "UTF-8", "/baseUri", Parser.xmlParser());

 Document expectedDoc = Parser.xmlParser().parseInput("<html><body></body></html>", null);

 Assert.assertEquals(expectedDoc, actualDoc);
 }

 // Test for parseByteData() method (default charset)
 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><body></body></html>".getBytes());
 Document actualDoc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());

 Document expectedDoc = Parser.htmlParser().parseInput("<html><body></body></html>", null);

 Assert.assertEquals(expectedDoc, actualDoc);
 }

 // Test for parseByteData() method (specified charset)
 @Test
 public void testParseByteDataWithCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><body></body></html>".getBytes());
 Document actualDoc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());

 Document expectedDoc = Parser.htmlParser().parseInput("<html><body></body></html>", null);

 Assert.assertEquals(expectedDoc, actualDoc);
 }

 // Test for crossStreams() method
 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 InputStream inStream = new ByteArrayInputStream("Test Data".getBytes());
 DataUtil.crossStreams(inStream, outStream);

 String actualData = outStream.toString();
 String expectedData = "Test Data";

 Assert.assertEquals(expectedData, actualData);
 }

 // Test for getCharsetFromContentType() method
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";

 String actualCharset = DataUtil.getCharsetFromContentType(contentType);

 Assert.assertEquals(expectedCharset, actualCharset);
 }

 // Test for mimeBoundary() method
 @Test
 public void testMimeBoundary() {
 String expectedMimeBoundary = "-----TestBoundary";
 String actualMimeBoundary = DataUtil.mimeBoundary();

 Assert.assertEquals(expectedMimeBoundary, actualMimeBoundary);
 }

 // Test for readToByteBuffer() method with a limited size
 @Test
 public void testReadToByteBufferWithLimitedSize() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream(10);
 InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
 DataUtil.readToByteBuffer(inputStream, 5);

 String actualData = new String(byteStream.toByteArray());
 String expectedData = "Test";

 Assert.assertEquals(expectedData, actualData);
 }

 // Test for readFileToByteBuffer() method
 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File testFile = new File("testFile.txt");
 testFile.createNewFile(); // create test file for testing
 try (InputStream outputStream = new FileOutputStream(testFile);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
 byteStream.write("Test Data".getBytes());
 outputStream.write(byteStream.toByteArray());
 }

 ByteBuffer actualByteBuffer = DataUtil.readFileToByteBuffer(testFile);
 String actualData = Charset.forName("UTF-8").decode(actualByteBuffer).toString();
 String expectedData = "Test Data";

 Assert.assertEquals(expectedData, actualData);

 // Clean up test file
 testFile.delete();
 }

 // Test for readToByteBuffer() method with an empty file
 @Test
 public void testReadToByteBufferWithEmptyFile() throws IOException {
 File testFile = new File("testFile.txt");
 testFile.createNewFile(); // create test file for testing

 ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(testFile);

 Assert.assertEquals(DataUtil.emptyByteBuffer(), actualByteBuffer);

 // Clean up test file
 testFile.delete();
 }
}