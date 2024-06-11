import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 String fileContent = "This is a test file.";
 File testFile = new File("test.html");
 testFile.createNewFile();
 try (InputStream is = new FileInputStream(testFile);
 ByteArrayOutputStream os = new ByteArrayOutputStream()) {
 os.write(fileContent.getBytes());
 }

 Document doc = dataUtil.load(testFile, null, null);
 assertEquals(fileContent, parser.parseInput(new InputStreamReader(new ByteArrayInputStream(testFile.getBytes()))).html(), doc.html());
 testFile.delete();
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 String fileContent = "This is a test file.";
 File testFile = new File("test.html");
 testFile.createNewFile();
 try (InputStream is = new FileInputStream(testFile);
 ByteArrayOutputStream os = new ByteArrayOutputStream()) {
 os.write(fileContent.getBytes("UTF-16LE"));
 }

 Document doc = dataUtil.load(testFile, "UTF-16LE", null);
 assertEquals(fileContent, parser.parseInput(new InputStreamReader(new ByteArrayInputStream(testFile.getBytes()), "UTF-16LE")).html(), doc.html());
 testFile.delete();
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 String fileContent = "This is a test file.";
 byte[] fileContentBytes = fileContent.getBytes();
 InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
 Document doc = dataUtil.load(inputStream, null, null);
 assertEquals(fileContent, parser.parseInput(inputStream).html(), doc.html());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 String fileContent = "This is a test file.";
 byte[] fileContentBytes = fileContent.getBytes("UTF-16LE");
 InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
 Document doc = dataUtil.load(inputStream, "UTF-16LE", null);
 assertEquals(fileContent, parser.parseInput(inputStream, "UTF-16LE").html(), doc.html());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 String fileContent = "This is a test file.";
 byte[] fileContentBytes = fileContent.getBytes();
 InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
 Document doc = dataUtil.load(inputStream, null, null, parser);
 assertEquals(fileContent, parser.parseInput(inputStream).html(), doc.html());
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
 OutputStream outputStream = new ByteArrayOutputStream();
 dataUtil.crossStreams(inputStream, outputStream);
 String output = outputStream.toString();
 assertEquals("Hello World", output);
 }

 @Test
 public void testParseInputStreamWithDefaultCharset() throws IOException {
 String inputStreamContent = "This is a test file.";
 InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
 Document doc = dataUtil.parseInputStream(inputStream, null, null, parser);
 assertEquals(inputStreamContent, parser.parseInput(inputStream).html(), doc.html());
 }

 @Test
 public void testParseInputStreamWithSpecifiedCharset() throws IOException {
 String inputStreamContent = "This is a test file.";
 byte[] inputStreamContentBytes = inputStreamContent.getBytes("UTF-16LE");
 InputStream inputStream = new ByteArrayInputStream(inputStreamContentBytes);
 Document doc = dataUtil.parseInputStream(inputStream, "UTF-16LE", null, parser);
 assertEquals(inputStreamContent, parser.parseInput(inputStream, "UTF-16LE").html(), doc.html());
 }

 @Test
 public void testParseInputStreamWithEmptyFile() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 Document doc = dataUtil.parseInputStream(inputStream, null, null, parser);
 assertEquals("<html></html>", doc.html());
 }

 @Test
 public void testParseInputStreamWithNullInput() {
 assertNull(dataUtil.parseInputStream(null, null, null, parser));
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0, emptyByteBuffer.limit());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 byte[] fileContentBytes = "This is a test file.".getBytes();
 InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 assertEquals(fileContentBytes.length, byteBuffer.limit());
 }

 @Test
 public void testReadToByteBufferWithInterruptedThread() throws IOException, InterruptedException {
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream("This is a test file.".getBytes()), 100);
 Thread.currentThread().interrupt();
 assertTrue(byteBuffer.hasRemaining());
 }

 @Test
 public void testReadFileToByteBuffer() throws IOException {
 byte[] fileContentBytes = "This is a test file.".getBytes();
 File tempFile = File.createTempFile("testFile", null);
 try (RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "rw");
 OutputStream outputStream = randomAccessFile.getOutputStream()) {
 outputStream.write(fileContentBytes);
 }
 ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(tempFile);
 assertEquals(fileContentBytes.length, byteBuffer.limit());
 }

 @Test
 public void testValidateCharset() {
 assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
 assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
 assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
 assertEquals(null, DataUtil.validateCharset(""));
 assertEquals(null, DataUtil.validateCharset(null));
 assertEquals("GB18030", DataUtil.validateCharset("text/html; charset=GB18030"));
 assertEquals("GB18030", DataUtil.validateCharset("text/html;charset=GB18030"));
 assertEquals("GB18030", DataUtil.validateCharset("text/html; charset=gb18030"));
 assertEquals("GB18030", DataUtil.validateCharset("text/html;charset=gb_18030"));
 assertEquals("UTF-16", DataUtil.validateCharset("text/html; charset=UTF-16"));
 assertEquals("UTF-16", DataUtil.validateCharset("text/html;charset=utf-16"));
 assertEquals("UTF-16", DataUtil.validateCharset("text/html; charset=utf-16be"));
 assertEquals("UTF-16", DataUtil.validateCharset("text/html;charset=utf-16le"));
 assertEquals("UTF-16BE", DataUtil.validateCharset("text/html; charset=UTF-16BE"));
 assertEquals("UTF-16LE", DataUtil.validateCharset("text/html; charset=UTF-16LE"));
 assertEquals("GB2312", DataUtil.validateCharset("text/html; charset=gb2312"));
 assertEquals("GB2312", DataUtil.validateCharset("text/html;charset=gb2312"));
 assertEquals("GB2312", DataUtil.validateCharset("text/html; charset=gb2312"));
 assertEquals("GB2312", DataUtil.validateCharset("text/html;charset=gb_2312"));
 assertEquals("ISO-8859-1", DataUtil.validateCharset("text/html; charset=ISO-8859-1"));
 assertEquals("ISO-8859-1", DataUtil.validateCharset("text/html;charset=iso-8859-1"));
 assertEquals("ISO-8859-1", DataUtil.validateCharset("text/html; charset=ISO-8859-1"));
 assertEquals("ISO-8859-1", DataUtil.validateCharset("text/html;charset=ISO_8859-1"));
 assertEquals("ISO-8859-1", DataUtil.validateCharset("text/html; charset=ISO_8859-1"));
 assertEquals("ISO-8859-1", DataUtil.validateCharset("text/html;charset=ISO-8859-1"));
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(32, boundary.length());
 }

 @Test
 public void testGetCharsetFromContentType() {
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html;charset=utf-8"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8\n"));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset="));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset=invalid"));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset=GB18030, GB2312"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=\"UTF-8\""));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset='UTF-8'"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8\r\n"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8\r"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8\n"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
 }
}