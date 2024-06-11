import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 private static final String TEST_FILE = "test.html";
 private Document doc;
 private String baseUri = "http://localhost:8080/";

 @Before
 public void setup() throws Exception {
 File file = new File(TEST_FILE);
 doc = DataUtil.load(file, null, baseUri);
 }

 @Test
 public void testLoadFile() throws Exception {
 File file = new File(TEST_FILE);
 Document expectedDoc = Jsoup.parse(file, null, baseUri);
 Document actualDoc = DataUtil.load(file, null, baseUri);
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testLoadInputStream() throws Exception {
 InputStream inputStream = FileUtils.openInputStream(new File(TEST_FILE));
 Document expectedDoc = Jsoup.parse(inputStream, null, baseUri);
 Document actualDoc = DataUtil.load(inputStream, null, baseUri);
 assertEquals(expectedDoc.html(), actualDoc.html());
 IOUtils.closeQuietly(inputStream);
 }

 @Test
 public void testLoadInputStreamWithCharset() throws Exception {
 InputStream inputStream = FileUtils.openInputStream(new File(TEST_FILE));
 Document expectedDoc = Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), baseUri);
 Document actualDoc = DataUtil.load(inputStream, "UTF-8", baseUri);
 assertEquals(expectedDoc.html(), actualDoc.html());
 IOUtils.closeQuietly(inputStream);
 }

 @Test(expected = IOException.class)
 public void testLoadNullFile() {
 DataUtil.load(null, null, baseUri);
 }

 @Test(expected = IOException.class)
 public void testLoadNullInputStream() {
 InputStream inputStream = null;
 DataUtil.load(inputStream, null, baseUri);
 }

 @Test
 public void testParseByteData() {
 byte[] testData = "test data".getBytes("UTF-8");
 ByteBuffer byteData = ByteBuffer.wrap(testData);
 String expectedCharset = "UTF-8";
 Document expectedDoc = Jsoup.parse(new String(testData, StandardCharsets.UTF_8), baseUri);
 Document actualDoc = DataUtil.parseByteData(byteData, expectedCharset, baseUri, Parser.htmlParser());
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testParseByteDataWithUnknownCharset() {
 byte[] testData = "test data".getBytes("GBK");
 ByteBuffer byteData = ByteBuffer.wrap(testData);
 String expectedCharset = "GBK";
 Document expectedDoc = Jsoup.parse(new String(testData, StandardCharsets.UTF_8), baseUri); // decode with UTF-8 to get the original data
 Document actualDoc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testParseByteDataWithDetectedCharset() {
 byte[] testData = "test data".getBytes("UTF-8");
 ByteBuffer byteData = ByteBuffer.wrap(testData);
 Document expectedDoc = Jsoup.parse(new String(testData, StandardCharsets.UTF_8), baseUri);
 Document actualDoc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
 assertNotNull(actualDoc);
 assertEquals(expectedDoc.outputSettings().charset(), actualDoc.outputSettings().charset());
 }
}