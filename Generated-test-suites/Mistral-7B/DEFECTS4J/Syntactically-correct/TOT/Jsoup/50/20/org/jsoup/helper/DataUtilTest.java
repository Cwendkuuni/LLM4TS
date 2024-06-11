import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

 @Test
 public void testLoadFileWithDefaultCharset() throws Exception {
 File testFile = new File("testFile.html");
 Document doc = DataUtil.load(testFile, null, null);
 assertTrue(doc != null);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws Exception {
 File testFile = new File("testFileWithSpecifiedCharset.html");
 Document doc = DataUtil.load(testFile, "UTF-16", null);
 assertTrue(doc != null);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws Exception {
 InputStream testStream = getClass().getResourceAsStream("/testFile.html");
 Document doc = DataUtil.load(testStream, null, null);
 assertTrue(doc != null);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
 InputStream testStream = getClass().getResourceAsStream("/testFileWithSpecifiedCharset.html");
 Document doc = DataUtil.load(testStream, "UTF-16", null);
 assertTrue(doc != null);
 }

 @Test
 public void testLoadInputStreamWithParser() throws Exception {
 InputStream testStream = getClass().getResourceAsStream("/testFile.xml");
 Document doc = DataUtil.load(testStream, null, null, Parser.xmlParser());
 assertTrue(doc != null);
 }

 @Test
 public void testCrossStreams() throws Exception {
 InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
 OutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 String result = outputStream.toString();
 assertEquals("Test data", result);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullBaseUri() throws Exception {
 File testFile = new File("testFile.html");
 Document doc = DataUtil.load(testFile, null, null);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullBaseUri() throws Exception {
 InputStream testStream = new ByteArrayInputStream("Test data".getBytes());
 Document doc = DataUtil.load(testStream, null, null);
 fail("Expected IOException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFileWithNegativeBufferSize() throws Exception {
 File testFile = new File("testFile.html");
 try {
 DataUtil.load(testFile, null, null, Parser.htmlParser(), -1);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}