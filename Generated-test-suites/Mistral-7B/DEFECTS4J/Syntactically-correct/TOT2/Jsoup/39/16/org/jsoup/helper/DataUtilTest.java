import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File testFile = new File("testFile.html");
 Document doc = DataUtil.load(testFile, null, null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File testFile = new File("testFile.html");
 Document doc = DataUtil.load(testFile, "UTF-16LE", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes());
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(testInputStream, null, null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes("UTF-16LE"));
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(testInputStream, "UTF-16LE", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes());
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(testInputStream, null, null, Parser.xmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 String htmlContent = "<html><head><meta charset=\"UTF-16LE\"></head><body>Test Content</body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(htmlContent.getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 String htmlContent = "<html><head><meta charset=\"UTF-16LE\"></head><body>Test Content</body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(htmlContent.getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, "UTF-16LE", null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes());
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream, 10);
 Assert.assertEquals(10, byteData.limit());
 }

 @Test
 public void testReadToByteBufferWithoutMaxSize() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes());
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
 Assert.assertNotNull(byteData);
 }

 @Test
 public void testGetCharsetFromContentTypeWithSupportedCharset() {
 String contentType = "text/html; charset=UTF-16LE";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-16LE", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
 String contentType = "text/html; charset=X-UNKNOWN-CHARSET";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithIOException() throws IOException {
 File testFile = new File("nonExistingFile.html");
 Document doc = DataUtil.load(testFile, null, null);
 Assert.fail();
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithIOException() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(testInputStream, null, null);
 testInputStream.close();
 testInputStream = new ByteArrayInputStream(new byte[0]);
 Assert.fail();
 }

 @Test(expected = IOException.class)
 public void testParseByteDataWithIOException() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 outputStream.write("Invalid Charset".getBytes());
 Assert.fail();
 }
}