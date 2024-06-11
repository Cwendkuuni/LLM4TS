import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

 // Test case for load(File) method
 @Test
 public void testLoadFile() throws IOException {
 Document expectedDocument = Parser.htmlParser().parseInput(
 "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>", null);
 File testFile = new File("test.html");
 FileWriter fileWriter = new FileWriter(testFile);
 fileWriter.write(expectedDocument.html().toString());
 fileWriter.close();

 Document actualDocument = DataUtil.load(testFile, "UTF-8", null);
 assertEquals(expectedDocument.html(), actualDocument.html());

 // Clean up
 testFile.delete();
 }

 // Test case for load(InputStream) method with default charset
 @Test
 public void testLoadInputStreamDefaultCharset() throws IOException {
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
 Document actualDocument = DataUtil.load(inputStream, null, null);
 Document expectedDocument = Parser.htmlParser().parseInput(testData, null);
 assertEquals(expectedDocument.html(), actualDocument.html());
 }

 // Test case for load(InputStream) method with specified charset
 @Test
 public void testLoadInputStreamSpecifiedCharset() throws IOException {
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
 Document actualDocument = DataUtil.load(inputStream, "UTF-8", null);
 Document expectedDocument = Parser.htmlParser().parseInput(testData, null);
 assertEquals(expectedDocument.html(), actualDocument.html());
 }

 // Test case for load(InputStream, String, String, Parser) method
 @Test
 public void testLoadInputStreamParser() throws IOException {
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
 Document actualDocument = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
 Document expectedDocument = Parser.xmlParser().parseInput(testData, null);
 assertEquals(expectedDocument.html(), actualDocument.html());
 }

 // Test case for crossStreams() method
 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
 DataUtil.crossStreams(inputStream, outputStream);
 assertEquals("Test Data", outputStream.toString());
 }

 // Test case for parseByteData() method with default charset
 @Test
 public void testParseByteDataDefaultCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document actualDocument = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 Document expectedDocument = Parser.htmlParser().parseInput("<html>", null);
 assertEquals(expectedDocument.html(), actualDocument.html());
 }

 // Test case for parseByteData() method with specified charset
 @Test
 public void testParseByteDataSpecifiedCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document actualDocument = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 Document expectedDocument = Parser.htmlParser().parseInput("<html>", null);
 assertEquals(expectedDocument.html(), actualDocument.html());
 }

 // Test case for parseByteData() method with invalid charset name
 @Test(expected = IllegalCharsetNameException.class)
 public void testParseByteDataInvalidCharsetName() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document actualDocument = DataUtil.parseByteData(byteData, "XYZ", null, Parser.htmlParser());
 }

 // Test case for getCharsetFromContentType() method with valid content type
 @Test
 public void testGetCharsetFromContentTypeValid() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 // Test case for getCharsetFromContentType() method with invalid content type
 @Test
 public void testGetCharsetFromContentTypeInvalid() {
 String contentType = "text/html; charset=invalid_charset";
 String expectedCharset = null;
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 // Test case for mimeBoundary() method
 @Test
 public void testMimeBoundary() {
 String expectedBoundary = "-----test-_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
 String actualBoundary = DataUtil.mimeBoundary();
 assertEquals(expectedBoundary, actualBoundary);
 }
}