import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class DataUtilTest {

 private static final String TEST_FILE = "testFile.html";
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://test.com";
 private static final String TEST_XML_PARSER = "xmlParser";

 @BeforeClass
 public static void setup() {
 // Set up any static variables or initialize any static methods here
 }

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File(TEST_FILE);
 Document document = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamUtf8() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamCustomCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.ISO_8859_1));
 Document document = DataUtil.load(inputStream, "ISO-8859-1", TEST_BASE_URI);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamCustomParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataUtf8() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test data".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataCustomCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test data".getBytes(StandardCharsets.ISO_8859_1));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "ISO-8859-1", TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadFileInvalidCharset() throws IOException {
 File testFile = new File(TEST_FILE);
 Document document = DataUtil.load(testFile, "InvalidCharset", TEST_BASE_URI);
 fail("Should have thrown an IOException");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamNullCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
 Document document = null;
 try {
 document = DataUtil.load(inputStream, null, TEST_BASE_URI);
 fail("Should have thrown an IOException");
 } catch (IOException e) {
 // Pass
 }
 }

 @Test(expected = IOException.class)
 public void testParseByteDataNullCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test data".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document document = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
 fail("Should have thrown an IOException");
 }
}