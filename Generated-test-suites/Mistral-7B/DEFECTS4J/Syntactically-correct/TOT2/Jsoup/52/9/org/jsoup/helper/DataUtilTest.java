import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

 // Test case for load(File) method
 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-8", null);
 Assert.assertNotNull(doc);
 }

 // Test case for load(InputStream) method with default charset
 @Test
 public void testLoadInputStreamDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head></head><body></body></html>";
 byte[] testData = htmlContent.getBytes();
 OutputStream output = outputStream;
 output.write(testData, 0, testData.length);
 output.flush();
 InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(input, null);
 Assert.assertNotNull(doc);
 }

 // Test case for load(InputStream) method with specified charset
 @Test
 public void testLoadInputStreamSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head></head><body></body></html>";
 byte[] testData = htmlContent.getBytes("UTF-16LE"); // Use different charset than the default one
 OutputStream output = outputStream;
 output.write(testData, 0, testData.length);
 output.flush();
 InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(input, "UTF-16LE");
 Assert.assertNotNull(doc);
 }

 // Test case for parseByteData method with default charset
 @Test
 public void testParseByteDataDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head></head><body></body></html>";
 byte[] testData = htmlContent.getBytes();
 OutputStream output = outputStream;
 output.write(testData, 0, testData.length);
 output.flush();
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test case for parseByteData method with specified charset
 @Test
 public void testParseByteDataSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head></head><body></body></html>";
 byte[] testData = htmlContent.getBytes("UTF-16LE"); // Use different charset than the default one
 OutputStream output = outputStream;
 output.write(testData, 0, testData.length);
 output.flush();
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
 Document doc = DataUtil.parseByteData(byteData, "UTF-16LE", null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test case for parseByteData method with XML Parser
 @Test
 public void testParseByteDataXMLParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String xmlContent = "<xml><data/></xml>";
 byte[] testData = xmlContent.getBytes();
 OutputStream output = outputStream;
 output.write(testData, 0, testData.length);
 output.flush();
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
 Assert.assertNotNull(doc);
 Assert.assertTrue(doc.childNode(0) instanceof XmlDeclaration);
 }

 // Test case for crossStreams method
 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
 ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
 InputStream input = new ByteArrayInputStream("Test data".getBytes());
 DataUtil.crossStreams(input, outputStream1);
 DataUtil.crossStreams(outputStream1, outputStream2);
 String output = outputStream2.toString();
 Assert.assertEquals("Test data", output);
 }
}