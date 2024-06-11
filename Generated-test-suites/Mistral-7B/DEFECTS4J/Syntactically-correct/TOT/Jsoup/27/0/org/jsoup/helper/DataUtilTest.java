import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

 private File testFile;
 private InputStream testStream;

 @Before
 public void setup() {
 testFile = new File("path/to/test/file.html");
 testStream = getClass().getResourceAsStream("/test-file.html");
 }

 @Test
 public void testLoadFromFile() throws IOException {
 Document doc = DataUtil.load(testFile, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFromFileWithNullCharset() throws IOException {
 Document doc = DataUtil.load(testFile, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFromFileWithDefaultCharset() throws IOException {
 Document doc = DataUtil.load(testFile, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFromInputStream() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFromInputStreamWithNullCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
 Document doc = DataUtil.load(inputStream, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>".getBytes());
 Document doc = DataUtil.load(inputStream, null, null);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFromInvalidFile() throws IOException {
 File invalidFile = new File("path/to/non-existent-file.html");
 Document doc = DataUtil.load(invalidFile, null, null);
 fail("Expected IOException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testLoadFromNullInputStream() throws IOException {
 Document doc = DataUtil.load(null, null, null);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testParseByteDataWithUTF8Charset() {
 String utf8Data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(utf8Data.getBytes());
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 String defaultData = "<html><body><h1>Hello World</h1></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(defaultData.getBytes("UTF-8"));
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 String invalidData = "<html><body><h1>Hello World</h1></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(invalidData.getBytes("ISO-8859-1"));
 Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", null, Parser.htmlParser());
 assertNotNull(doc);
 }
}