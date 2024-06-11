import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), null, "");
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-16", "");
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 Document doc = null;
 try {
 doc = DataUtil.load(new File("test.html"), "invalidCharset", "");
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 InputStream input = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.load(input, null, "");
 assertNotNull(doc);
 input.close();
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 InputStream input = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.load(input, "UTF-16", "");
 assertNotNull(doc);
 input.close();
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 InputStream input = getClass().getResourceAsStream("/test.html");
 Document doc = null;
 try {
 doc = DataUtil.load(input, "invalidCharset", "");
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 input.close();
 }

 @Test
 public void testParseInputStreamWithDefaultCharset() throws IOException {
 InputStream input = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.parseInputStream(input, null, "", Parser.htmlParser());
 assertNotNull(doc);
 input.close();
 }

 @Test
 public void testParseInputStreamWithSpecifiedCharset() throws IOException {
 InputStream input = getClass().getResourceAsstream("/test.html");
 Document doc = DataUtil.parseInputStream(input, "UTF-16", "", Parser.htmlParser());
 assertNotNull(doc);
 input.close();
 }

 @Test(expected = IOException.class)
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 InputStream input = getClass().getResourceAsStream("/test.html");
 Document doc = null;
 try {
 doc = DataUtil.parseInputStream(input, "invalidCharset", "", Parser.htmlParser());
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 input.close();
 }

 @Test
 public void testParseInputStreamWithParser() throws IOException {
 InputStream input = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.parseInputStream(input, null, "", Parser.xmlParser());
 assertNotNull(doc);
 input.close();
 }

 @Test(expected = IOException.class)
 public void testCrossStreams() throws IOException {
 InputStream input = new ByteArrayInputStream("test".getBytes());
 OutputStream output = new ByteArrayOutputStream();
 DataUtil.crossStreams(input, output);
 assertThrows(IOException.class, () -> input.read());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeInvalid() {
 String contentType = "text/html";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testValidateCharset() {
 assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
 assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
 assertEquals(null, DataUtil.validateCharset("UTF-17"));
 assertEquals(null, DataUtil.validateCharset("UTF-"));
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertNotNull(boundary);
 }
}