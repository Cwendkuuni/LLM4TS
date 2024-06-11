import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

 // Test for load(File, String, String) method
 @Test
 void testLoadFile() throws IOException {
 File inputFile = new File("input.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document expectedDocument = Parser.htmlParser().parseInput("<html><body><h1>Hello</h1></body></html>", baseUri);
 Document actualDocument = DataUtil.load(inputFile, charsetName, baseUri);
 Assertions.assertEquals(expectedDocument, actualDocument);
 }

 // Test for load(InputStream, String, String) method
 @Test
 void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document expectedDocument = Parser.htmlParser().parseInput("<html><body><h1>Hello</h1></body></html>", baseUri);
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
 Assertions.assertEquals(expectedDocument, actualDocument);
 }

 // Test for load(InputStream, String, String, Parser) method
 @Test
 void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Parser.xmlParser();
 Document expectedDocument = parser.parseInput("<html><body><h1>Hello</h1></body></html>", baseUri);
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
 Assertions.assertEquals(expectedDocument, actualDocument);
 }

 // Test for parseByteData() method with a valid charset
 @Test
 void testParseByteDataValidCharset() throws IOException {
 byte[] inputData = "<html><body><h1>Hello</h1></body></html>".getBytes(StandardCharsets.UTF_8);
 ByteBuffer byteData = ByteBuffer.wrap(inputData);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document expectedDocument = Parser.htmlParser().parseInput("<html><body><h1>Hello</h1></body></html>", baseUri);
 Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
 Assertions.assertEquals(expectedDocument, actualDocument);
 }

 // Test for parseByteData() method with an invalid charset
 @Test
 void testParseByteDataInvalidCharset() throws IOException {
 byte[] inputData = "<html><body><h1>Hello</h1></body></html>".getBytes(StandardCharsets.UTF_8);
 ByteBuffer byteData = ByteBuffer.wrap(inputData);
 String charsetName = "INVALID_CHARSET";
 String baseUri = "http://example.com";
 Document expectedDocument = null; // should fail
 Document actualDocument = null;
 try {
 actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
 } catch (Exception e) {
 // expected
 }
 Assertions.assertNull(actualDocument);
 }

 // Test for readToByteBuffer() method
 @Test
 void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8.name()));
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap("Hello World".getBytes(StandardCharsets.UTF_8.name()));
 ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
 Assertions.assertArrayEquals(expectedByteBuffer.array(), actualByteBuffer.array());
 }

 // Test for mimeBoundary() method
 @Test
 void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 Assertions.assertFalse(boundary.isEmpty());
 }
}