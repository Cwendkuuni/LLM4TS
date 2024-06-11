import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

class DataUtilTest {

 private static Parser parser;

 @BeforeAll
 static void setup() {
 parser = DataUtil.parser();
 }

 @Test
 void testLoadFile() throws IOException {
 File testFile = new File("test.html");
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
 FileWriter fileWriter = new FileWriter(testFile);
 fileWriter.write(htmlContent);
 fileWriter.close();

 Document doc = DataUtil.load(testFile, "UTF-8", null);
 Assertions.assertNotNull(doc);
 Assertions.assertEquals("Test", doc.title());

 FileUtils.deleteQuietly(testFile);
 }

 @Test
 void testLoadInputStream() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 Assertions.assertNotNull(doc);
 Assertions.assertEquals("Test", doc.title());

 inputStream.close();
 }

 @Test
 void testDetectCharset() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, null, null);
 Assertions.assertEquals("UTF-8", doc.outputSettings().charset());

 String htmlContentGB2312 = "<html><head><meta charset=\"gb2312\"></head><body></body></html>";
 InputStream inputStreamGB2312 = new ByteArrayInputStream(htmlContentGB2312.getBytes(StandardCharsets.UTF_8));
 Document docGB2312 = DataUtil.load(inputStreamGB2312, null, null);
 Assertions.assertEquals("gb2312", docGB2312.outputSettings().charset());

 inputStream.close();
 inputStreamGB2312.close();
 }

 @Test
 void testLoadInvalidCharset() throws IOException {
 String htmlContent = "<html><head><meta charset=\"InvalidCharset\"></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, "InvalidCharset", null);
 Assertions.assertNull(doc);

 inputStream.close();
 }

 @Test
 void testParseByteData() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, parser);
 Assertions.assertNotNull(doc);
 Assertions.assertEquals("Test", doc.title());
 }

 @Test
 void testParseByteDataWithDetectedCharset() throws IOException {
 String htmlContentGB2312 = "<html><head><meta charset=\"gb2312\"></head><body><h1>Test</h1></body></html>";
 ByteBuffer byteDataGB2312 = ByteBuffer.wrap(htmlContentGB2312.getBytes(StandardCharsets.UTF_8));
 Document docGB2312 = DataUtil.parseByteData(byteDataGB2312, null, null, parser);
 Assertions.assertEquals("gb2312", docGB2312.outputSettings().charset());
 Assertions.assertNotNull(docGB2312);
 Assertions.assertEquals("Test", docGB2312.title());
 }

 @Test
 void testParseByteDataWithDefaultCharset() throws IOException {
 String htmlContent = "<html><head><meta charset=\"\"></head><body><h1>Test</h1></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 Assertions.assertEquals("UTF-8", doc.outputSettings().charset());
 Assertions.assertNotNull(doc);
 Assertions.assertEquals("Test", doc.title());
 }

 @Test
 void testReadToByteBuffer() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Assertions.assertNotNull(byteData);
 }
}