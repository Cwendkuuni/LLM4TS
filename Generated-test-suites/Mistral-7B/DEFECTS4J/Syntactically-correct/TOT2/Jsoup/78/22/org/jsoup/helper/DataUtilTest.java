import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class DataUtilTest {

 // Test for load method with valid file and default charset
 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 String xmlFileContent = "<xml><tag>Test</tag></xml>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(xmlFileContent.getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, null);
 Assert.assertEquals("Test", doc.select("tag").text());
 }

 // Test for load method with valid file and specified charset
 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 String xmlFileContent = "<xml><tag>Test</tag></xml>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(xmlFileContent.getBytes("ISO-8859-1"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "ISO-8859-1", null);
 Assert.assertEquals("Test", doc.select("tag").text());
 }

 // Test for load method with null file
 @Test(expected = IOException.class)
 public void testLoadFileWithNullFile() throws IOException {
 Document doc = DataUtil.load(null, null, null);
 }

 // Test for parseInputStream method with valid input stream and default charset
 @Test
 public void testParseInputStreamWithDefaultCharset() throws IOException {
 String xmlString = "<xml><tag>Test</tag></xml>";
 InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
 Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 Assert.assertEquals("Test", doc.select("tag").text());
 }

 // Test for parseInputStream method with valid input stream and specified charset
 @Test
 public void testParseInputStreamWithSpecifiedCharset() throws IOException {
 String xmlString = "<xml><tag>Test</tag></xml>";
 InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes("ISO-8859-1"));
 Document doc = DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.htmlParser());
 Assert.assertEquals("Test", doc.select("tag").text());
 }

 // Test for parseInputStream method with null input stream
 @Test(expected = IOException.class)
 public void testParseInputStreamWithNullInputStream() throws IOException {
 Document doc = DataUtil.parseInputStream(null, null, null, Parser.htmlParser());
 }

 // Test for crossStreams method with valid input and output streams
 @Test
 public void testCrossStreams() throws IOException {
 String testString = "This is a test string.";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));
 DataUtil.crossStreams(inputStream, outputStream);
 Assert.assertEquals(testString, new String(outputStream.toByteArray()));
 }

 // Test for crossStreams method with null input and output streams
 @Test(expected = IOException.class)
 public void testCrossStreamsWithNullInputOrOutputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(null, outputStream);
 }

 // Test for parseInputStream method with invalid BOM and default charset
 @Test
 public void testParseInputStreamWithInvalidBomAndDefaultCharset() throws IOException {
 String invalidBomXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\u00FE\u00FF<xml><tag>Test</tag></xml>";
 InputStream inputStream = new ByteArrayInputStream(invalidBomXml.getBytes("UTF-8"));
 try {
 Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 Assert.fail();
 } catch (IOException e) {
 // Expected
 }
 }

 // Test for parseInputStream method with valid BOM and specified charset
 @Test
 public void testParseInputStreamWithValidBomAndSpecifiedCharset() throws IOException {
 String validBomXmlWithUTF16 = "\uFEFF<xml><tag>Test</tag></xml>";
 InputStream inputStream = new ByteArrayInputStream(validBomXmlWithUTF16.getBytes("UTF-16"));
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
 Assert.assertEquals("Test", doc.select("tag").text());
 }

 // Test for parseInputStream method with invalid BOM and specified charset
 @Test
 public void testParseInputStreamWithInvalidBomAndSpecifiedCharset() throws IOException {
 String invalidBomXmlWithUTF16 = "\uFEFF<xml><tag>Test</tag></xml>";
 InputStream inputStream = new ByteArrayInputStream(invalidBomXmlWithUTF16.getBytes("UTF-16"));
 try {
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
 Assert.fail();
 } catch (IOException e) {
 // Expected
 }
 }
}