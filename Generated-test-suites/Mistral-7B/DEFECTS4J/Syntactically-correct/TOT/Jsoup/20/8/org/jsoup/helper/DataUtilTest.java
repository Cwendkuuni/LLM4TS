import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

 private static File testFile;
 private static InputStream testInputStream;
 private static Document doc;

 @BeforeClass
 public static void setup() throws Exception {
 testFile = new File("test.html");
 testInputStream = new FileInputStream(testFile);
 }

 @Test
 public void testLoadFile() throws Exception {
 doc = DataUtil.load(testFile, "UTF-8", "");
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws Exception {
 doc = DataUtil.load(testFile, null, "");
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStream() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[1024];
 for (int i = 0; i < buffer.length; i++) {
 outputStream.write((byte) i);
 }
 testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 doc = DataUtil.load(testInputStream, "UTF-8", "");
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws Exception {
 testInputStream = new ByteArrayInputStream("test data".getBytes());
 doc = DataUtil.load(testInputStream, null, "");
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithUTF8() throws Exception {
 ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes("UTF-8"));
 doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws Exception {
 ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes("UTF-8"));
 doc = DataUtil.parseByteData(byteData, "ISO-8859-1", "", Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFilePath() throws Exception {
 DataUtil.load(new File("nonexistentfile.html"), "UTF-8", "");
 fail("Expected IOException to be thrown, but it wasn't.");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullInputStream() throws Exception {
 DataUtil.load((InputStream) null, "UTF-8", "");
 fail("Expected IOException to be thrown, but it wasn't.");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullData() throws Exception {
 testInputStream = new ByteArrayInputStream(new byte[0]);
 DataUtil.load(testInputStream, "UTF-8", "");
 fail("Expected IOException to be thrown, but it wasn't.");
 }
}