import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 private static final String TEST_FILE_PATH = "test.html";
 private static final String DEFAULT_CHARSET = DataUtil.defaultCharset;
 private static final String BASE_URI = "http://example.com";

 @BeforeClass
 public static void setup() throws IOException {
 File testFile = new File(TEST_FILE_PATH);
 DataUtil.saveFile(testFile, "<html><head>\n" +
         "<meta charset=\"UTF-8\">\n" +
         "</head>\n" +
         "<body>\n" +
         "</body>\n</html>", StandardCharsets.UTF_8);
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 Document doc = DataUtil.load(new File(TEST_FILE_PATH), null, BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 Document doc = DataUtil.load(new File(TEST_FILE_PATH), "UTF-8", BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 File testFile = new File(TEST_FILE_PATH);
 FileInputStream inputStream = new FileInputStream(testFile);
 Document doc = DataUtil.load(inputStream, null, BASE_URI);
 assertNotNull(doc);
 inputStream.close();
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 File testFile = new File(TEST_FILE_PATH);
 FileInputStream inputStream = new FileInputStream(testFile);
 Document doc = DataUtil.load(inputStream, "UTF-8", BASE_URI);
 assertNotNull(doc);
 inputStream.close();
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 File testFile = new File(TEST_FILE_PATH);
 FileInputStream inputStream = new FileInputStream(testFile);
 try {
 Document doc = DataUtil.load(inputStream, "ISO-8859-1", BASE_URI);
 fail("Should have thrown an exception.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 inputStream.close();
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_FILE_PATH)));
 Document doc = DataUtil.parseByteData(byteData, null, BASE_URI, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_FILE_PATH)));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", BASE_URI, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_FILE_PATH)));
 try {
 Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", BASE_URI, Parser.htmlParser());
 fail("Should have thrown an exception.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}