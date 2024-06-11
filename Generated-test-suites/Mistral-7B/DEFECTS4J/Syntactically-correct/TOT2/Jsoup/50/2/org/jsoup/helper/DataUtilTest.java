import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

 // Test for load method with valid file and charset
 @Test
 public void testLoadValidFile() throws IOException {
 Document document = DataUtil.load(new File("test.html"), "UTF-8", null);
 Assert.assertNotNull(document);
 }

 // Test for load method with invalid file path
 @Test(expected = FileNotFoundException.class)
 public void testLoadInvalidFile() throws IOException {
 DataUtil.load(new File("non_existent_file.html"), "UTF-8", null);
 }

 // Test for load method with null charset
 @Test(expected = NullPointerException.class)
 public void testLoadNullCharset() {
 DataUtil.load(new File("test.html"), null, null);
 }

 // Test for load method with empty byte data
 @Test
 public void testLoadEmptyByteData() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 Assert.assertNull(document);
 }

 // Test for parseByteData method with valid byte data and charset
 @Test
 public void testParseByteDataValidByteData() throws IOException {
 ByteArrayOutputStream byteDataOutputStream = new ByteArrayOutputStream();
 byteDataOutputStream.write("Test content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(byteDataOutputStream.toByteArray());
 Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", null, Parser.htmlParser());
 Assert.assertNotNull(document);
 }

 // Test for parseByteData method with invalid charset
 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataInvalidCharset() {
 ByteArrayOutputStream byteDataOutputStream = new ByteArrayOutputStream();
 byteDataOutputStream.write("Test content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(byteDataOutputStream.toByteArray());
 Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "invalid_charset", null, Parser.htmlParser());
 }

 // Test for crossStreams method with valid input and output streams
 @Test
 public void testCrossStreamsValidStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("Test input".getBytes(StandardCharsets.UTF_8));
 DataUtil.crossStreams(inputStream, outputStream);
 Assert.assertEquals("Test input", outputStream.toString(StandardCharsets.UTF_8));
 }

 // Test for getCharsetFromContentType method with valid content type
 @Test
 public void testGetCharsetFromContentTypeValidContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 // Test for getCharsetFromContentType method with invalid content type
 @Test
 public void testGetCharsetFromContentTypeInvalidContentType() {
 String contentType = "text/plain";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 // Test for mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String mimeBoundary = DataUtil.mimeBoundary();
 Assert.assertNotNull(mimeBoundary);
 Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
 }
}