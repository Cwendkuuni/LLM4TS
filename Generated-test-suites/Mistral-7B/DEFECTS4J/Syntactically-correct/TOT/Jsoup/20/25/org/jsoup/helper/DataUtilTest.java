import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class DataUtilTest {

 private static Document doc;
 private static File testFile;

 @BeforeClass
 public static void setUpClass() throws Exception {
 testFile = new File("path/to/your/test.html");
 }

 @Test
 public void testLoadFile() throws Exception {
 Document doc = DataUtil.load(testFile, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws Exception {
 Document doc = DataUtil.load(testFile, null, null);
 assertEquals("UTF-8", DataUtil.getDefaultCharset());
 }

 @Test
 public void testLoadInputStream() throws Exception {
 InputStream input = new FileInputStream(testFile);
 Document doc = DataUtil.load(input, "UTF-8", null);
 input.close();
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws Exception {
 InputStream input = new FileInputStream(testFile);
 Document doc = DataUtil.load(input, null, null);
 input.close();
 assertEquals("UTF-8", DataUtil.getDefaultCharset());
 }

 @Test
 public void testLoadInputStreamWithCustomParser() throws Exception {
 InputStream input = new FileInputStream(testFile);
 Document doc = DataUtil.load(input, "UTF-8", null, Parser.xmlParser());
 input.close();
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithValidCharset() throws Exception {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(testFile));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws Exception {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(testFile));
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertEquals("UTF-8", DataUtil.getDefaultCharset());
 }

 @Test
 public void testParseByteDataWithCustomParser() throws Exception {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(testFile));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithException() throws Exception {
 File invalidFile = new File("path/to/invalid/file");
 Document doc = DataUtil.load(invalidFile, "UTF-8", null);
 fail("IOException expected");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithException() throws Exception {
 FileInputStream input = new FileInputStream(testFile);
 Document doc = DataUtil.load(input, "UTF-8", null);
 input.close();
 assertNotNull(doc);
 InputStream invalidInput = new FileInputStream(new File("path/to/invalid/file"));
 Document doc2 = DataUtil.load(invalidInput, "UTF-8", null);
 fail("IOException expected");
 }
}