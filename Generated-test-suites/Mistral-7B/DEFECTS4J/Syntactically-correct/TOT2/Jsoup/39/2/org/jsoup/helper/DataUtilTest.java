import org.jsoup. helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

 // Test load method with valid arguments
 @Test
 public void testLoadValidArguments() throws IOException {
 File file = new File("testFile.html");
 Document doc = DataUtil.load(file, "UTF-8", "/testBaseUri");
 Assert.assertNotNull(doc);
 }

 // Test load method with null charsetName argument
 @Test(expected = IOException.class)
 public void testLoadNullCharsetName() throws IOException {
 File file = new File("testFile.html");
 Document doc = DataUtil.load(file, null, "/testBaseUri");
 }

 // Test load method with empty charsetName argument
 @Test(expected = IOException.class)
 public void testLoadEmptyCharsetName() throws IOException {
 File file = new File("testFile.html");
 Document doc = DataUtil.load(file, "", "/testBaseUri");
 }

 // Test load method with invalid charsetName argument
 @Test(expected = IOException.class)
 public void testLoadInvalidCharsetName() throws IOException {
 File file = new File("testFile.html");
 Document doc = DataUtil.load(file, "invalidCharset", "/testBaseUri");
 }

 // Test load method from InputStream with valid arguments
 @Test
 public void testLoadInputStreamValidArguments() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String html = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
 outputStream.write(html.getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-8", "/testBaseUri");
 Assert.assertNotNull(doc);
 }

 // Test load method from InputStream with null charsetName argument
 @Test(expected = IOException.class)
 public void testLoadInputStreamNullCharsetName() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String html = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
 outputStream.write(html.getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, "/testBaseUri");
 }

 // Test load method from InputStream with empty charsetName argument
 @Test(expected = IOException.class)
 public void testLoadInputStreamEmptyCharsetName() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String html = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
 outputStream.write(html.getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "", "/testBaseUri");
 }

 // Test load method from InputStream with invalid charsetName argument
 @Test(expected = IOException.class)
 public void testLoadInputStreamInvalidCharsetName() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String html = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
 outputStream.write(html.getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "invalidCharset", "/testBaseUri");
 }

 // Test load method from InputStream with Parser
 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String html = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
 outputStream.write(html.getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-8", "/testBaseUri", Parser.xmlParser());
 Assert.assertNotNull(doc);
 }

 // Test parseByteData method with valid arguments
 @Test
 public void testParseByteDataValidArguments() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 String charsetName = "UTF-8";
 Document doc = DataUtil.parseByteData(byteData, charsetName, "/testBaseUri", Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test parseByteData method with null charsetName argument
 @Test
 public void testParseByteDataNullCharsetName() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document doc = DataUtil.parseByteData(byteData, null, "/testBaseUri", Parser.htmlParser());
 Assert.assertNull(doc);
 }

 // Test parseByteData method with empty charsetName argument
 @Test
 public void testParseByteDataEmptyCharsetName() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document doc = DataUtil.parseByteData(byteData, "", "/testBaseUri", Parser.htmlParser());
 Assert.assertNull(doc);
 }

 // Test parseByteData method with invalid charsetName argument
 @Test
 public void testParseByteDataInvalidCharsetName() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document doc = DataUtil.parseByteData(byteData, "invalidCharset", "/testBaseUri", Parser.htmlParser());
 Assert.assertNull(doc);
 }

 // Test readToByteBuffer method with valid arguments
 @Test
 public void testReadToByteBufferValidArguments() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("testData".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Assert.assertNotNull(byteData);
 }

 // Test readToByteBuffer method with null inputStream argument
 @Test(expected = IOException.class)
 public void testReadToByteBufferNullInputStream() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(null);
 }

 // Test readToByteBuffer method with maxSize > 0
 @Test
 public void testReadToByteBufferMaxSize() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("testData".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 5);
 Assert.assertEquals(5, byteData.limit());
 }

 // Test readToByteBuffer method with maxSize = 0
 @Test
 public void testReadToByteBufferMaxSizeZero() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("testData".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Assert.assertNotNull(byteData);
 }

 // Test getCharsetFromContentType method with valid contentType
 @Test
 public void testGetCharsetFromContentTypeValidContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 // Test getCharsetFromContentType method with invalid contentType
 @Test
 public void testGetCharsetFromContentTypeInvalidContentType() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }
}