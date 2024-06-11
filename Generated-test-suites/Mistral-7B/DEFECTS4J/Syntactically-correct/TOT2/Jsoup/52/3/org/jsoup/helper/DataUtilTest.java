import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 // Test load method with valid file, default charset and baseUri
 @Test
 public void testLoadFileWithDefaultCharsetAndBaseUri() throws IOException {
 Document document = DataUtil.load(new File("path/to/valid/file.html"), null, null);
 Assert.assertNotNull(document);
 }

 // Test load method with valid file, specified charset and baseUri
 @Test
 public void testLoadFileWithSpecifiedCharsetAndBaseUri() throws IOException {
 Document document = DataUtil.load(new File("path/to/valid/file.html"), "UTF-16", "https://example.com/");
 Assert.assertNotNull(document);
 }

 // Test load method with an invalid file
 @Test(expected = IOException.class)
 public void testLoadInvalidFile() {
 Document document = null;
 try {
 document = DataUtil.load(new File("path/to/invalid/file"), null, null);
 } catch (IOException e) {
 // Expected exception
 }
 Assert.assertNull(document);
 }

 // Test load method with null file
 @Test(expected = NullPointerException.class)
 public void testLoadNullFile() {
 Document document = DataUtil.load(null, null, null);
 }

 // Test load method with empty InputStream
 @Test
 public void testLoadEmptyInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 Document document = DataUtil.load(inputStream, null, null);
 Assert.assertNull(document);
 }

 // Test load method with valid InputStream, default charset and baseUri
 @Test
 public void testLoadInputStreamWithDefaultCharsetAndBaseUri() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new ByteArrayInputStream(new byte[]{}), outputStream);
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, null, null);
 Assert.assertNotNull(document);
 }

 // Test load method with valid InputStream, specified charset and baseUri
 @Test
 public void testLoadInputStreamWithSpecifiedCharsetAndBaseUri() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new ByteArrayInputStream(new byte[]{}), outputStream);
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, "UTF-16", "https://example.com/");
 Assert.assertNotNull(document);
 }

 // Test load method with null InputStream
 @Test(expected = NullPointerException.class)
 public void testLoadNullInputStream() {
 Document document = DataUtil.load(null, null, null);
 }

 // Test parseByteData method with empty byte buffer
 @Test
 public void testParseByteDataWithEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 Document document = DataUtil.parseByteData(byteBuffer, null, null, null);
 Assert.assertNull(document);
 }

 // Test parseByteData method with valid byte buffer and default charset
 @Test
 public void testParseByteDataWithValidByteBufferAndDefaultCharset() {
 ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x54, 0x79, 0x70, 0x65, 0x3E});
 Document document = DataUtil.parseByteData(byteBuffer, null, null, null);
 Assert.assertNotNull(document);
 }

 // Test parseByteData method with valid byte buffer and specified charset
 @Test
 public void testParseByteDataWithValidByteBufferAndSpecifiedCharset() throws IOException {
 ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x54, 0x79, 0x70, 0x65, 0x3E, 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x54, 0x79, 0x70, 0x65, 0x3E, 0x3C, 0x61, 0x63, 0x63, 0x65, 0x74, 0x20, 0x74, 0x68, 0x69, 0x73, 0x70, 0x61, 0x3D, "UTF-8", 0x22, 0x3E, 0x3C, 0x21, 0x4D, 0x65, 0x73, 0x73, 0x61, 0x72, 0x63, 0x65, 0x54, 0x69, 0x6D, 0x65, 0x3E, 0x3C, 0x2F, 0x73, 0x72, 0x63, 0x3E});
 Document document = DataUtil.parseByteData(byteBuffer, null, null, null);
 Assert.assertNotNull(document);
 }

 // Test parseByteData method with null byte buffer
 @Test(expected = NullPointerException.class)
 public void testParseByteDataNullByteBuffer() {
 Document document = DataUtil.parseByteData(null, null, null, null);
 }

 // Test getCharsetFromContentType method with valid contentType
 @Test
 public void testGetCharsetFromContentTypeWithValidContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 // Test getCharsetFromContentType method with invalid contentType
 @Test
 public void testGetCharsetFromContentTypeWithInvalidContentType() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 // Test validateCharset method with valid charset
 @Test
 public void testValidateCharsetWithValidCharset() {
 String charset = "UTF-8";
 String validCharset = DataUtil.validateCharset(charset);
 Assert.assertEquals("UTF-8", validCharset);
 }

 // Test validateCharset method with invalid charset
 @Test
 public void testValidateCharsetWithInvalidCharset() {
 String charset = "UTF-9";
 String validCharset = DataUtil.validateCharset(charset);
 Assert.assertNull(validCharset);
 }

 // Test mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String mimeBoundary = DataUtil.mimeBoundary();
 Assert.assertNotNull(mimeBoundary);
 }
}