package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

 @Test
 public void testLoadDocumentFromFile() throws IOException {
 File testFile = new File("src/test/resources/test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";

 Document document = DataUtil.load(testFile, charsetName, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testLoadDocumentFromInputStream() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";

 Document document = DataUtil.load(inputStream, charsetName, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testParseByteData() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
 byte[] bytes = htmlContent.getBytes("UTF-8");
 outputStream.write(bytes, 0, bytes.length);
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 String charsetName = null;
 String baseUri = "http://example.com";

 String htmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"><body><h1>Test</h1></body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] bytes = htmlContent.getBytes("UTF-8");
 outputStream.write(bytes, 0, bytes.length);
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
 assertEquals("ISO-8859-1", document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 String charsetName = "X-PASCAL";
 String baseUri = "http://example.com";

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
 byte[] bytes = htmlContent.getBytes("UTF-8");
 outputStream.write(bytes, 0, bytes.length);
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document document = assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser()));
 assertTrue(document == null);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 String charsetName = "UTF-8";
 InputStream inputStream = new ByteArrayInputStream("Test".getBytes(charsetName));
 byte[] buffer = new byte[bufferSize];
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
 byte[] readBytes = new byte[byteData.remaining()];
 byteData.get(readBytes);
 outputStream.write(readBytes, 0, readBytes.length);
 assertEquals(new String(outputStream.toByteArray(), charsetName), new String("Test"));
 }
}