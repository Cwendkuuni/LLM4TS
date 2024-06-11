import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 private static ByteArrayInputStream inputStream;
 private static File testFile;

 @BeforeClass
 public static void setUp() throws IOException {
 String htmlContent = "<html>\n" +
         "<head>\n" +
         "<meta charset=\"UTF-8\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<h1>Test Page</h1>\n" +
         "</body>\n" +
         "</html>";

 inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 testFile = new File("test.html");
 try (FileWriter fileWriter = new FileWriter(testFile)) {
 fileWriter.write(htmlContent);
 }
 }

 @Test
 public void testLoadFile() throws IOException {
 Document document = DataUtil.load(testFile, "UTF-8", null);
 assertNotNull(document);
 assertEquals("Test Page", document.title());
 }

 @Test
 public void testLoadStreamUTF8() throws IOException {
 Document document = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(document);
 assertEquals("Test Page", document.title());
 }

 @Test
 public void testLoadStreamWithWrongCharset() throws IOException {
 String htmlContentGBK = "\uFEFF<html>\n" +
         "<head>\n" +
         "<meta charset=\"GBK\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<h1>Test Page</h1>\n" +
         "</body>\n" +
         "</html>";

 ByteArrayInputStream inputStreamGBK = new ByteArrayInputStream(htmlContentGBK.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.load(inputStream, null, null);
 assertNotNull(document);
 assertEquals("Test Page", document.title());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithWrongPath() throws IOException {
 Document document = DataUtil.load(new File("nonexistentfile.html"), "UTF-8", null);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullInputStream() throws IOException {
 Document document = DataUtil.load(null, "UTF-8", null);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullCharset() throws IOException {
 Document document = DataUtil.load(testFile, null, null);
 }

 @Test
 public void testParseByteData() throws IOException {
 String htmlContent = "<html>\n" +
         "<head>\n" +
         "<meta charset=\"UTF-8\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<h1>Test Page</h1>\n" +
         "</body>\n" +
         "</html>";

 ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertNotNull(document);
 assertEquals("Test Page", document.title());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNoCharset() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @AfterClass
 public static void tearDown() throws IOException {
 if (testFile.exists()) {
 testFile.delete();
 }
 }

}