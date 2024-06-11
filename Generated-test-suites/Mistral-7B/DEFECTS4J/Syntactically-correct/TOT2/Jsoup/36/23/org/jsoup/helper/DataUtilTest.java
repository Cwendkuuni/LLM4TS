import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
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
 File inputFile = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document expectedDocument = Parser.htmlParser().parseInput("Test content", baseUri);
 Document actualDocument = DataUtil.load(inputFile, charsetName, baseUri);
 Assert.assertEquals(expectedDocument, actualDocument);
 }

 // Test case for load(InputStream) method with default charset
 @Test
 public void testLoadInputStreamDefaultCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
 Document expectedDocument = Parser.htmlParser().parseInput("Test content", null);
 Document actualDocument = DataUtil.load(inputStream, null, null);
 Assert.assertEquals(expectedDocument, actualDocument);
 }

 // Test case for load(InputStream) method with specified charset
 @Test
 public void testLoadInputStreamSpecifiedCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test content".getBytes("UTF-8"));
 Document expectedDocument = Parser.htmlParser().parseInput("Test content", null);
 Document actualDocument = DataUtil.load(inputStream, "UTF-8", null);
 Assert.assertEquals(expectedDocument, actualDocument);
 }

 // Test case for load(InputStream, String, String, Parser) method
 @Test
 public void testLoadInputStreamParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
 Document expectedDocument = Parser.xmlParser().parseInput("Test content", null);
 Document actualDocument = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 Assert.assertEquals(expectedDocument, actualDocument);
 }

 // Test case for parseByteData method with default charset
 @Test
 public void testParseByteDataDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document expectedDocument = Parser.htmlParser().parseInput("Test content", null);
 Document actualDocument = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, Parser.htmlParser());
 Assert.assertEquals(expectedDocument, actualDocument);
 }

 // Test case for parseByteData method with specified charset
 @Test
 public void testParseByteDataSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document expectedDocument = Parser.htmlParser().parseInput("Test content", null);
 Document actualDocument = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", null, Parser.htmlParser());
 Assert.assertEquals(expectedDocument, actualDocument);
 }

 // Test case for readToByteBuffer method
 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] fileContent = "Test content".getBytes();
 outputStream.write(fileContent);
 File inputFile = new File("test.html");
 FileOutputStream fileOutputStream = new FileOutputStream(inputFile);
 fileOutputStream.write(fileContent);
 fileOutputStream.close();
 InputStream actualInputstream = DataUtil.load(inputFile, null, null);
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap(fileContent);
 ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(actualInputstream);
 Assert.assertArrayEquals(expectedByteBuffer.array(), actualByteBuffer.array());
 }

 // Test case for getCharsetFromContentType method
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals(expectedCharset, actualCharset);
 }

 // Test case for load(File) with charset not found in header or meta tag
 @Test (expected = IOException.class)
 public void testLoadFileNoCharset() throws IOException {
 File inputFile = new File("test.html");
 String baseUri = "http://example.com";
 Document expectedDocument = Parser.htmlParser().parseInput("Test content", baseUri);
 DataUtil.load(inputFile, null, baseUri);
 }

 // Test case for parseByteData method with invalid input
 @Test (expected = IOException.class)
 public void testParseByteDataInvalidInput() throws IOException {
 ByteBuffer byteData = ByteBuffer.allocate(4);
 DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 }
}