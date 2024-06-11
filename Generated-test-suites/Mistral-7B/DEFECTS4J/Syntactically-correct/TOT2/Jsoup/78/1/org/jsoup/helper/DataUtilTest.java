import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 // Test for load method with valid file and charset
 @Test
 public void testLoadValidFileWithCharset() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 String charset = "UTF-8";
 String baseUri = "";
 Document document = DataUtil.load(inputStream, charset, baseUri);
 Assert.assertNotNull(document);
 }

 // Test for load method with valid file and default charset
 @Test
 public void testLoadValidFileWithDefaultCharset() throws IOException {
 String htmlContent = "<html><head></head><body><h1>Test</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
 Document document = DataUtil.load(inputStream, null, "");
 Assert.assertNotNull(document);
 }

 // Test for load method with null input stream
 @Test(expected = IOException.class)
 public void testLoadNullInputStream() {
 Document document = DataUtil.load(null, null, "");
 }

 // Test for load method with invalid charset
 @Test(expected = IllegalArgumentException.class)
 public void testLoadInvalidCharset() throws IOException {
 String htmlContent = "<html><head><meta charset=\"invalidCharset\"></head><body><h1>Test</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document document = null;
 try {
 document = DataUtil.load(inputStream, "invalidCharset", "");
 } catch (IOException e) {
 // Swallow IOException
 }
 Assert.assertNull(document);
 }

 // Test for parseInputStream method with valid input stream
 @Test
 public void testParseInputStreamValidInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><head></head><body><h1>Test</h1></body></html>".getBytes());
 Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
 Assert.assertNotNull(document);
 }

 // Test for parseInputStream method with invalid charset
 @Test(expected = IllegalArgumentException.class)
 public void testParseInputStreamInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><head></head><body><h1>Test</h1></body></html>".getBytes());
 Document document = null;
 try {
 document = DataUtil.parseInputStream(inputStream, "invalidCharset", "", Parser.htmlParser());
 } catch (IOException e) {
 // Swallow IOException
 }
 Assert.assertNull(document);
 }
}