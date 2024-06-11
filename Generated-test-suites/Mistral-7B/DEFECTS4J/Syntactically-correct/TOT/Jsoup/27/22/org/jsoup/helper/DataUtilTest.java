import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private static Parser parser;

 @BeforeClass
 static public void setup() {
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("src/test/resources/test.html"); // replace with actual path to test file
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(testFile, charsetName, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamNullCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream("test data".getBytes());
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(testStream, null, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamValidCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream("test data".getBytes("UTF-8"));
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(testStream, "UTF-8", baseUri);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamInvalidCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream("test data".getBytes());
 String baseUri = "http://example.com";
 Document doc = null;
 try {
 doc = DataUtil.load(testStream, "invalidCharset", baseUri);
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testParseByteDataDefaultCharset() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 outStream.write("<html lang=\"en\">".getBytes("UTF-8"));
 outStream.write("<head><meta charset=\"UTF-8\"></head>");
 outStream.write("<body>Test</body>");
 byte[] testBytes = outStream.toByteArray();
 ByteBuffer byteData = ByteBuffer.wrap(testBytes);
 String charsetName = null;
 String baseUri = "http://example.com";
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 outStream.write("<html lang=\"en\">".getBytes("UTF-8"));
 outStream.write("<head><meta charset=\"GBK\"></head>"); // change charset to test
 outStream.write("<body>Test</body>");
 byte[] testBytes = outStream.toByteArray();
 ByteBuffer byteData = ByteBuffer.wrap(testBytes);
 String charsetName = "GBK";
 String baseUri = "http://example.com";
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testParseByteDataInvalidCharset() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 outStream.write("<html lang=\"en\">".getBytes("UTF-8"));
 outStream.write("<head><meta charset=\"invalidCharset\"></head>"); // change charset to test
 byte[] testBytes = outStream.toByteArray();
 ByteBuffer byteData = ByteBuffer.wrap(testBytes);
 String charsetName = null; // let defaultCharset be used
 String baseUri = "http://example.com";
 try {
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }
}